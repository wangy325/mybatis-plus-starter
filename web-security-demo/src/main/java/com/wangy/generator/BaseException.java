package com.wangy.generator;

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
     * status code
     */
    private int code;

    /**
     * exception brief message
     */
    private String message;


    public BaseException(String methodSign, Object[] params, int code, String message) {
        this.methodSign = methodSign;
        this.params = params;
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return Objects.isNull(message) ? super.getMessage() : message;
    }

    public String getMethodSign() {
        return methodSign;
    }

    public Object[] getParams() {
        return params;
    }

    public int getCode() {
        return code;
    }
}
