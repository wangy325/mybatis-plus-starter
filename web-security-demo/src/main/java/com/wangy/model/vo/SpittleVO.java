package com.wangy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/30 / 22:38
 */
@Data
@Builder
@AllArgsConstructor
public class SpittleVO {
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
