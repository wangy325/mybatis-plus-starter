package com.wangy.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;

/**
 * 注意&#64;{@link JsonIgnore}和&#64;{@link JsonProperty}的区别：<br>
 * 当使用&#64;{@link RequestBody}并使用json作为请求参数时，若涉及分页查询，分页参数不能正确传递的问题。<br>
 * <p>
 * 目前测试的情况来看，使用&#64;{@link JsonIgnore}时无法传递分页参数，因为其反序列化时（json->pojo）直接忽略了
 * 分页参数的反序列化，<br>
 * 而使用
 * <pre>
 *   &#64;JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
 * </pre>
 * 则可行。<code>JsonProperty.Access.WRITE_ONLY</code>配置意味着只在反序列化时读取属性，而在序列化时忽略属性，这似乎完美符合
 * 当前的需求。
 *
 * @author wangy
 * @version 1.0
 * @date 2021/1/22 / 17:29
 */
@Setter
@Getter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -1L;

    @TableField(exist = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected int currentPage = 1;

    @TableField(exist = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected int pageSize = 10;

   /* protected String fieldToColumn(){

    }*/
}
