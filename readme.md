# 关于本项目的简单说明
---


## 使用mybatis-plus

### 代码生成器

[code generator](./mybatis-plus-generator/src/main/java/com/wangy/generator/CodeGenerator.java)

mybatis-plus的最基本插件，用来生成数据库实体（Entity）、Mapper接口、Service接口及实现、Controller以及Mapper.xml
映射文件。

注意，代码生成器生成的Mapper接口，Service接口分别继承了`com.baomidou.mybatisplus.core.mapper.BaseMapper`，
`com.baomidou.mybatisplus.extension.service.IService`，这样可以不需要写sql语句就能实现基本的单表CRUD操作。

### 逻辑删除

mybatis-plus支持逻辑删除，需要在数据库表的字段中新增一个字段（字段名可以任意配置），mybatis-plus用1和0分别代表删除
和非删除的状态。

基础的配置信息如下所示：

```
mybatis-plus.global-config.db-config.logic-delete-field=deleted
mybatis-plus.global-config.db-config.logic-delete-value=1
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```

此外，mybatis-plus还提供了基于注解形式的逻辑删除标注。在实体的`deleted`字段上使用`@TableLogic`注解可以实现上述配置的同等作用。
（亦即可以取消`mybatis-plus.global-config.db-config.logic-delete-field=deleted`这个配置语句）

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

前文提到使用mybatis-plus代码生成器生成的Service层代码继承了框架提供的基础Service，使DAO/SERVICE层代码不需要任何额外的SQL编写就
具备基础的CRUD能力。

除此之外，mybatis-plus提供了丰富包装器（`AbstractWrapper`），用来构建SQL where子句中的条件，借用此功能，在基本的CRUD无法满足需求时，
仍旧可以尝试【偷懒】，不写额外的sql语句来完成操作（这并不是确定的）。

值得一提的是，mybatis-plus针对jdk1.8提供了基于Lambda表达式的`LambdaQueryWrapper`和`LambdaUpdateWrapper`，可以使用Lambda
表达式来【美化】代码。


### 使用分页插件


---

TODOS:

- [x] 使用mybatis-plus提供的分页插件 
- [x] 在mybatis-plus框架中使用自定义查询
- [x] [事务已自动配置](web-security-demo/src/main/java/com/wangy/config/MybatisPlusConfig.java)
- [ ] 完成ibatis分支下的原生mybatis配置
- [ ] 引入日志系统（使用logback）
- [x] 统一异常处理
- [ ] 引入JWT统一认证

- [ ] 引入SpringSecurity

 