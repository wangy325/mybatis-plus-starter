package com.wangy.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/30 / 22:38
 */
@Data
@Builder
@AllArgsConstructor
public class SpittleVO{

    private Long id;

    /**
     * refer to spitter.id
     */
    private Integer spitterId;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING )
    private LocalDateTime time;

    @NotNull
    private Double latitude;

    private Double longitude;
}
