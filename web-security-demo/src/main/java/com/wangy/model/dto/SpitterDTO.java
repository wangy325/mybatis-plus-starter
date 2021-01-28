package com.wangy.model.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author wangy
 * @date 2021-1-28 15:31
 */
@Data
@Builder
public class SpitterDTO {

    private Integer id;

    private String firstname;

    private String lastname;

    private String username;

    private String password;
}
