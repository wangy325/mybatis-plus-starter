# 关于本项目的简单说明
---


## 使用mybatis-plus

### 代码生成器

[code generator](./mybatis-plus-generator/src/main/java/com/wangy/generator/CodeGenerator.java)

mybatis-plus的最基本插件，用来生成数据库实体（Entity）、Mapper接口、Service接口及实现、Controller以及Mapper.xml
映射文件。

注意，代码生成器生成的Mapper接口，Service接口分别继承了`com.baomidou.mybatisplus.core.mapper.BaseMapper`，
`com.baomidou.mybatisplus.extension.service.IService`，这样可以不需要写sql语句就能实现基本的单表CRUD操作。

## 逻辑删除

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

## 使用基本的CRUD/包装器


## 使用分页插件


 