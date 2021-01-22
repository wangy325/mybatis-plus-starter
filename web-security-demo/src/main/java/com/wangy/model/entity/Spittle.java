package com.wangy.model.entity;

import com.wangy.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * user spittle table
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Spittle extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * refer to spitter.id
     */
    private Integer spitterId;

    private String message;

    private LocalDateTime time;

    private Double latitude;

    private Double longitude;


}
