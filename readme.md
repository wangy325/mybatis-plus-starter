# 关于本项目的简单说明
---

[TOC]

## 1 使用mybatis-plus

### 1.1 代码生成器

[CodeGenerator](./mybatis-plus-generator/src/main/java/com/wangy/generator/CodeGenerator.java)

mybatis-plus的最基本插件，用来生成数据库实体（Entity）、Mapper接口、Service接口及实现、Controller以及Mapper.xml映射文件。

注意，代码生成器生成的Mapper接口，Service接口分别继承了`com.baomidou.mybatisplus.core.mapper.BaseMapper`，
`com.baomidou.mybatisplus.extension.service.IService`，这样可以不需要写sql语句就能实现基本的单表CRUD操作。

### 1.2 逻辑删除

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

### 1.3 使用基本的CRUD/包装器（条件构造器）

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

### 1.4 使用分页插件

在`mybatis-plus`下使用分页插件异常的简单：

#### 1.4.1 配置分页拦截器

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

`mybatis-plus`提供了实用的<span id="page">分页模型</span>`com.baomidou.mybatisplus.extension.plugins.pagination.Page`，用
户可以根据这个类构建自己的分页模型，也可以直接使用之。

在本项目中，直接使用[Page](#page)作为分页查询的入参，而构建了[PageDomain](./common-supplier/src/main/java/com/wangy/common/model/PageDomain.java)
作为分页查询结果的封装（为了简化返回信息）。

#### 1.4.2 返回IPage<T>

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

#### 1.4.3 返回List<T>

还有一种方式，直接返回分页查询的结果集，即上述提及的[records](#records)域中的数据。

```java
List<SpittleVO> pageQuerySpittleBySpitterId(IPage<SpittleVO> page, @Param("dto") SpittleDTO spittleDTO);
```

这种方式本质上和返回IPage并没有什么不同。

## 2 webMvc统一异常处理（@RestControllerAdvice）

本实践使用`@RestControllerAdvice`和`@ExceptionHandler`来进行Spring MVC的统一异常处理[^其他方法]。

[GlobalExceptionHandler](./web-security-demo/src/main/java/com/wangy/advice/GlobalExceptionHandler.java)使用了
`@RestControllerAdvice`注解（相当于`@ControllerAdvice`和`@ResponseBody`），为每个需要处理的异常类声明一个*handler*，就可以处理MVC
控制器抛出的异常：

```java
@ExceptionHandler(SpitterException.class)
    public ReqResult<?> handleException(SpitterException e) {
        log.severe(e.throwableString());
        return ReqResult.fail(e.getReqState(), e.getMessage());
    }
```

值得一提的是，**GlobalExceptionHandler**中的`log`使用的是`java.util.logging.Logger`实例，权当测试用。好像SpringBoot使用jdk日志框架时，
可能会[存在问题](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-custom-log-configuration) ：

> There are known classloading issues with Java Util Logging that cause problems when running from an 'executable jar'.
> We recommend that you avoid it when running from an 'executable jar' if at all possible.

### 2.1 自定义异常

实话讲，有时候真的拿异常没办法，好像遇见异常最好的方式就是在服务器控制台或者日志文件中看到清晰的**堆栈信息**。不过，堆栈信息虽然详细，有时候却让人头痛。
通过自定义异常将业务中的异常尽可能的处理，是良好的编程习惯。特别是在RESTful接口的设计中，你不能将堆栈信息作为接口返回的message推送给调用者。

[BaseException](./web-security-demo/src/main/java/com/wangy/exception/BaseException.java)是一个自定义异常的基类，其定义了4个基本
域：

- throwMethod：抛出异常的方法
- params：方法参数
- reqState：接口响应状态
- message：异常信息

当构造自定义异常时，除了`reqState`之外，其他的信息都是可以缺省的，当使用的信息越多，日志呈现的异常信息就越完善，这使得使用自定义异常非常灵活。

实践中，分别在控制器和服务层中展示了自定义异常的使用：

```java
// 在Controller中抛出自定义异常
@GetMapping("/{id}")
public ReqResult<Spitter> getSpitterById(@PathVariable int id) {
    Spitter spitter = spitterService.getById(id);
    if (Objects.isNull(spitter)){
        throw new SpitterException(ReqState.SATISFIED_RESOURCE_NOT_FOUND, "no.specific.id.resource");
    }
    return ReqResult.ok(spitter);
}

// 在Service中抛出自定义异常
@Override
public boolean updateFromDtoById(SpitterDTO dto) {
    Spitter spitter = new Spitter();
    BeanUtils.copyProperties(dto, spitter);
    if (!updateById(spitter)) {
        // simplest way to throw self-definition exception
//            throw new SpitterException(ReqState.SATISFIED_RESOURCE_NOT_FOUND);
        // recommend way to throw self-definition exception
        throw new SpitterException(
                ReflectionUtils.getAccessibleMethod(this, "updateFromDtoById", SpitterDTO.class),
                ReqState.SATISFIED_RESOURCE_NOT_FOUND);
    }
    return true;
}
```

关于获取**方法**的反射工具类，实际上是调用了`class.getDeclaredMethod(methodName, parameterTypes)`方法。

### 2.2 处理接口的返回数据（ResponseBodyAdvice接口）

通过自定义异常和全局异常处理，控制器的异常大部分都将不能传递到前端，但是，还有一些Servlet异常，并不能被MVC容器处理，这里就出现了接口返回的2重性：

一般地，RESTful接口会自定义一个全局返回的Java Bean
（此实践中为[ReqResult](./common-supplier/src/main/java/com/wangy/common/model/ReqResult.java)），用来统一包装接口的返回数据，并
且自定义了一套接口返回码（此实践中为[ReqState](./common-supplier/src/main/java/com/wangy/common/enums/ReqState.java)），因此，我们
希望所有的返回都是`ReqResult`序列化的结果。

但是，有一些异常信息，如请求了不存在的资源（404）或服务器内部没有处理的异常（500），MVC容器的响应结果实际上封装的是
`org.springframework.http.ResponseEntity`，因此，就存在了一些返回结果不统一的情况。

[GlobalResponseAdvice](./web-security-demo/src/main/java/com/wangy/advice/GlobalResponseAdvice.java)就是用来对RESTful接口的
全局返回结果作统一处理的组件。

`GlobalResponseAdvice`使用了`@RestControllerAdvice`注解并实现了`ResponseBodyAdvice`接口，`ResponseBodyAdvice`接口提供了2个方法：

```java
public interface ResponseBodyAdvice<T> {

	/**
	 * Whether this component supports the given controller method return type
	 * and the selected {@code HttpMessageConverter} type.
	 * @param returnType the return type
	 * @param converterType the selected converter type
	 * @return {@code true} if {@link #beforeBodyWrite} should be invoked;
	 * {@code false} otherwise
	 */
	boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType);

	/**
	 * Invoked after an {@code HttpMessageConverter} is selected and just before
	 * its write method is invoked.
	 * @param body the body to be written
	 * @param returnType the return type of the controller method
	 * @param selectedContentType the content type selected through content negotiation
	 * @param selectedConverterType the converter type selected to write to the response
	 * @param request the current request
	 * @param response the current response
	 * @return the body that was passed in or a modified (possibly new) instance
	 */
	@Nullable
	T beforeBodyWrite(@Nullable T body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response);

}
```

`supports()`方法用来判断是否需要修改接口返回，而`beforeBodyWrite()`方法就是修改接口返回内容的主体了。具体如何实现可以参照
`GlobalResponseAdvice`的逻辑。

> 首次使用此功能，虽然需求满足，但是实现逻辑可能还有纰漏。

`GlobalResponseAdvice`的处理在统一异常处理之后，返回结果渲染之前。

## 3 Spring MessageResource（国际化）

[Use MessageSource with SpringBoot](https://www.baeldung.com/spring-custom-validation-message-source)

这里需要说明的是，接口返回的原始信息是国际化信息的key，需要使用上面提到的`ResponseBodyAdvice`接口将国际化信息转义：

```java
if (body instanceof ReqResult) {
    ReqResult<?> bd = (ReqResult<?>) body;
    String regex = "^([a-z]+\\.)+[a-z]+$";
    if (bd.getMsg().matches(regex)) {
        bd.setMsg(MessageUtils.getMvcMessage(bd.getMsg()));
    }
}
```

## 4 使用Mockito及MockMVC进行单元测试

## 附：使用curl命令

[ruanyifeng.com-curl命令使用指南](http://www.ruanyifeng.com/blog/2019/09/curl-reference.html0)


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