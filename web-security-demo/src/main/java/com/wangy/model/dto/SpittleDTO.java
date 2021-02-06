package com.wangy.model.dto;


import com.wangy.common.model.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/31 / 16:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpittleDTO extends BaseEntity {

    private Long id;

    /**
     * refer to spitter.id
     */
    private Integer spitterId;

    private String message;

    private LocalDateTime time;

    @NotNull(message = "{leftTime.not.null}")
    private LocalDateTime leftTime;
    private LocalDateTime rightTime;
}
