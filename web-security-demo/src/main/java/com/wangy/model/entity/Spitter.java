package com.wangy.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.wangy.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * user info table
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Spitter extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String firstname;

    private String lastname;

    private String username;

    private String password;

    @TableLogic
    private Integer deleted;


}
