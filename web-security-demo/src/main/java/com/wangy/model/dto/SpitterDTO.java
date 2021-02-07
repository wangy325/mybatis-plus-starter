package com.wangy.model.dto;

import com.wangy.common.model.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangy
 * @date 2021-1-28 15:31
 */

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SpitterDTO extends BaseEntity {

    private Integer id;

    private String firstname;

    private String lastname;

    private String username;

    private String password;

    @Builder.Default
    private List<SpittleDTO> spittles = new ArrayList<>();
}

