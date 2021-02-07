# 关于本项目的简单说明
---

[TOC]

## 使用mybatis-plus

### 代码生成器

[CodeGenerator](./mybatis-plus-generator/src/main/java/com/wangy/generator/CodeGenerator.java)

mybatis-plus的最基本插件，用来生成数据库实体（Entity）、Mapper接口、Service接口及实现、Controller以及Mapper.xml映射文件。

注意，代码生成器生成的Mapper接口，Service接口分别继承了`com.baomidou.mybatisplus.core.mapper.BaseMapper`，
`com.baomidou.mybatisplus.extension.service.IService`，这样可以不需要写sql语句就能实现基本的单表CRUD操作。

### 逻辑删除

mybatis-plus支持逻辑删除，需要在数据库表的字段中新增一个字段（字段名可以任意配置），mybatis-plus用1和0分别代表删除和非删除的状态。

基础的配置信息如下所示：

```
mybatis-plus.global-config.db-config.logic-delete-field=deleted
mybatis-plus.global-config.db-config.logic-delete-value=1
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```

此外，mybatis-plus还提供了基于注解形式的逻辑删除标注。在实体的`deleted`字段上使用`@TableLogic`注解可以实现上述配置的同等作用。（亦即可以取消
`mybatis-plus.global-config.db-config.logic-delete-field=deleted`这个配置语句）

```java
public class Entity{
    @TableId(value = "id", type = IdType.AUTO)
    private Integer Id;
    
    @TableLogic
    private Integer delete;

}
```

逻辑删除的作用在2个方面：

- 将delete语句转换为update语句，并将`delete`字段的值修改为1
- 在select/update语句的where字句中加入`and delete = 0`

### 使用基本的CRUD/包装器（条件构造器）

前文提到使用mybatis-plus代码生成器生成的Service层代码继承了框架提供的基础Service，使DAO/SERVICE层代码不需要任何额外的SQL编写就具备基础的
CRUD能力。

除此之外，mybatis-plus提供了丰富包装器（`AbstractWrapper`），用来构建SQL where子句中的条件，借用此功能，在基本的CRUD无法满足需求时，仍旧可
以尝试【偷懒】，不写额外的sql语句来完成操作（这并不是确定的）。

值得一提的是，mybatis-plus针对jdk1.8提供了基于Lambda表达式的`LambdaQueryWrapper`和`LambdaUpdateWrapper`，可以使用Lambda表达式来
【美化】代码。



需要注意的是，在使用包装器作为参数时，形参列表参数名要么使用`@Param(Constants.Wrapper)`注解声明，要么形参的名字只能声明为
`queryByWrapper(Wrapper, ew)`。

`mybatis-plus`的包装器本质上是构建一个**WHERE子句**，因此单表简单查询的工作，Wrapper可以很好的胜任。在使用过程中，直接使用字符串占位符替换的
形式即可：

```xml
<select id="getAll" resultType="MysqlData">
   SELECT * FROM mysql_data ${ew.customSqlSegment}
</select>
```

或者使用基于注解的方式：

```
@Select("select * from spitter ${ew.customSqlSegment}")
```

- [ ] 试试mybatis-plus的多表查询包装器？

### 使用分页插件

在`mybatis-plus`下使用分页插件异常的简单：

#### 1. 配置分页拦截器

```java
@Configuration
@MapperScan("com.wangy.service.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor pagination() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }
}
```

如此简单配置之后，既可以`mybatis-plus`提供的分页功能了。

`mybatis-plus`提供了实用的<span id="page">分页模型</span>`**com.baomidou.mybatisplus.extension.plugins.pagination.Page**`，用
户可以根据这个类构建自己的分页模型，也可以直接使用之。

在本项目中，直接使用[Page](#page)作为分页查询的入参，而构建了[PageDomain](./common-supplier/src/main/java/com/wangy/common/model/PageDomain.java)
作为分页查询结果的封装（为了简化返回信息）。

#### 2.1 返回IPage<T>

一个简单的分页查询接口示例：

```java
IPage<SpittleVO> pageQuerySpittleBySpitterId(IPage<SpittleVO> page, @Param("dto") SpittleDTO spittleDTO);
```

当使用这种方式时，记住，入参[`page`](#page)会在**执行SQL之后返回**，并且将查询到的数据封装到分页模型`page`中。如果入参为null，那么不分页。

需要注意的是，在**com.baomidou.mybatisplus.core.metadata.IPage**的泛型声明中，T即为查询结果实际映射的Java Bean，封装在[Page](#page)
的`records`<span id= "records">域</span>中。一般使用“领域模型”进行封装。特别需要注意的是，如果接口的声明类型和SQL语句（XML Mapper文件）中声明的resultType/resultMap不
一致，该接口也不会报错，并且查询结果以Mapper文件中的声明为主。

如上述接口对应如下SQL(xml)：

```xml
<select id="pageQuerySpittleBySpitterId" resultType="com.wangy.model.entity.Spittle">
    select * from ...
</select>
```

在SQL语句没错的情况下，执行此分页查询，最终返回的结果就是`IPage<Spittle>`而不是`IPage<SpittleVO>`。

一般来讲，实用这种封装良好的形式更加方便。

#### 2.2 返回List<T>

还有一种方式，直接返回分页查询的结果集，即上述提及的[records](#records)域中的数据。

```java
List<SpittleVO> pageQuerySpittleBySpitterId(IPage<SpittleVO> page, @Param("dto") SpittleDTO spittleDTO);
```

这种方式本质上和返回IPage并没有什么不同。

## 自定义异常/webMvc统一异常处理（RestControllerAdvice）




## Spring MessageResource（RestControllerAdvice/ResponseBodyAdvice）

---

TODOS:

- [x] 使用mybatis-plus提供的分页插件 
- [x] 在mybatis-plus框架中使用自定义查询
- [x] [事务已自动配置](web-security-demo/src/main/java/com/wangy/config/MybatisPlusConfig.java)
- [ ] 完成ibatis分支下的原生mybatis配置
- [x] 引入日志系统（logback）
- [x] 统一异常处理/引入自定义异常
- [x] i18n（spring message source）
- [ ] 引入JWT统一认证

- [ ] 引入SpringSecurity

 # 参考
 
 - https://mybatis.plus/
 - 