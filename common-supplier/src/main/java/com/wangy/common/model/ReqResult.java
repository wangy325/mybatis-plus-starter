package com.wangy.common.model;

import com.wangy.common.enums.HttpStatus;
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
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <T> ReqResult<T> ok() {
        return ReqResult.ok("操作成功");
    }

    public static <T> ReqResult<T> ok(T data) {
        return ReqResult.ok("操作成功", data);
    }

    public static <T> ReqResult<T> ok(String msg) {
        return ReqResult.ok(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> ReqResult<T> ok(String msg, T data) {
        return new ReqResult<>(HttpStatus.OK.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static <T> ReqResult<T> fail() {
        return ReqResult.fail("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> ReqResult<T> fail(String msg) {
        return ReqResult.fail(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> ReqResult<T> fail(String msg, T data) {
        return new ReqResult<>(HttpStatus.Internal_Server_Error.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static <T> ReqResult<T> fail(int code, String msg) {
        return new ReqResult<>(code, msg, null);
    }

    private static class NullObject{
        private static final NullObject NULL_OBJECT = new NullObject();

        private NullObject(){};

        static NullObject of(){
            return NULL_OBJECT;
        }
    }
}
