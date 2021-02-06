package com.wangy.common.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wangy.common.enums.ReqState;
import lombok.Data;

/**
 * 基础响应
 *
 * @author wangy
 */
@Data
public class ReqResult<T> {

    private int code;

    private String msg;

    private T data;


    public ReqResult() {
    }

    public ReqResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReqResult(int code, String msg, T data) {
        this(code, msg);
        this.data = data;
    }

    ////~~~~~~~~~~~~~~~~~~~~~~~ SUCCESS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~////

    public static <T> ReqResult<T> ok() {
        return ReqResult.ok(ReqState.OK.getCode(), ReqState.OK.getMessage());
    }

    public static <T> ReqResult<T> ok(T data) {
        return ReqResult.ok(ReqState.OK.getCode(), ReqState.OK.getMessage(), data);
    }

    public static <T> ReqResult<T> ok(String msg) {
        return ReqResult.ok(ReqState.OK.getCode(), msg);
    }

    public static <T> ReqResult<T> ok(String msg, T data) {
        return new ReqResult<>(ReqState.OK.getCode(), msg, data);
    }


    private static <T> ReqResult<T> ok(int code, String message) {
        return ReqResult.ok(code, message, null);
    }

    private static <T> ReqResult<T> ok(int code, String msg, T data) {
        return new ReqResult<>(code, msg, data);
    }

    ////~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ FAIL ~~~~~~~~~~~~~~~~~~~~~~~~~~////
    // code
    // message
    // data -> null

    public static <T> ReqResult<T> fail(ReqState state) {
        return ReqResult.fail(state.getCode(), state.getMessage());
    }

    public static <T> ReqResult<T> fail(ReqState state, String message){
        return ReqResult.fail(state.getCode(), message);
    }

    public static <T> ReqResult<T> fail(int code, String msg) {
        return new ReqResult<>(code, msg, null);
    }
}
