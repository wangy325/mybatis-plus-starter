package com.wangy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author wangy
 * @date 2021-1-28 16:32
 */
@Data
@Builder
@AllArgsConstructor
public class SpitterVO {

    private Integer id;

    private String firstname;

    private String lastname;

    private String username;

    private String password;
}
