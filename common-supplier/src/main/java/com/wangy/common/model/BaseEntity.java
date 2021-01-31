package com.wangy.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/22 / 17:29
 */
@Setter
@Getter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -1L;

    @TableField(exist = false)
    protected int currentPage = 1;

    @TableField(exist = false)
    protected int pageSize = 10;

   /* protected String fieldToColumn(){

    }*/


}
