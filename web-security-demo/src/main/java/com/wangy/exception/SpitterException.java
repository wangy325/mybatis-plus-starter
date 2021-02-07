package com.wangy.exception;


import com.wangy.common.enums.ReqState;

import java.io.Serializable;

/**
 * @author wangy
 * @date 2021-2-4 11:33
 */
public class SpitterException extends BaseException implements Serializable {


    private static final long serialVersionUID = -6627267233818274355L;

    public SpitterException(String methodSign, Object[] params, ReqState state, String message) {
        super(methodSign, params, state, message);
    }

    public SpitterException(String methodSign, Object[] params, ReqState state) {
        super(methodSign, params, state, null);
    }
}
