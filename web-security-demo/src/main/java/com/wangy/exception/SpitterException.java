package com.wangy.exception;


import com.wangy.common.enums.ReqState;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author wangy
 * @date 2021-2-4 11:33
 */
public class SpitterException extends BaseException implements Serializable {


    private static final long serialVersionUID = -6627267233818274355L;

    public SpitterException(Method throwMethod, Object[] params, ReqState reqState, String message) {
        super(throwMethod, params, reqState, message);
    }

    public SpitterException(Method method, Objects[] params, ReqState state) {
        super(method, params, state);
    }

    public SpitterException(Method method, ReqState state) {
        super(method, state);
    }

    public SpitterException(ReqState state) {
        super(state);
    }

    public SpitterException(ReqState state, String message) {
        super(state, message);
    }


}
