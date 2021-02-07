package com.wangy.exception;

import com.wangy.common.enums.ReqState;

import java.util.Objects;

/**
 * 异常基类
 *
 * @author wangy
 * @date 2021-2-4 11:08
 */
public class BaseException extends RuntimeException {

    /**
     * 异常发生的方法签名
     */
    private String methodSign;

    /**
     * 方法参数，按照形参列表的顺序放入数组中
     */
    private Object[] params;

    /**
     * request status, with system defined message
     */
    private ReqState reqState;

    /**
     * 自定义异常消息，当此消息不为null时，使用此消息，否则使用{@link ReqState}中定义的国际化消息
     */
    private String message;


    public BaseException(String methodSign, Object[] params, ReqState state, String message) {
        this.methodSign = methodSign;
        this.params = params;
        this.reqState = state;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return Objects.isNull(message) ? reqState.getMessage() : message;
    }

    public String getMethodSign() {
        return methodSign;
    }

    public Object[] getParams() {
        return params;
    }

    public ReqState getReqState() {
        return reqState;
    }
}
