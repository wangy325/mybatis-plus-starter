package com.wangy.common.enums;

import lombok.Getter;

/**
 * @author wangy
 * @date 2021-2-4 10:17
 * @see HttpStatus
 */
@Getter
public enum ReqState {
    // 自定义返回码，前3位对应标准HTTP状态码，后2位为细分码
    OK(200_00, "成功"),
    VALIDATION_BIND_EXCEPTION(400_00, "请求参数校验失败"),
    UNAUTHORIZED(401_00, "未经授权"),
    AUTHENTICATE_FAIL(401_01,"认证失败"),
    AUTHENTICATION_ACCESS_DENIED(403_00, "拒绝访问"),
    NOT_FOUND(404_00, "请求资源未找到"),
    REQUEST_TIMEOUT(408_00, "请求超时"),
    UNSUPPORTED_MEDIA_TYPE(415_00, "不支持的媒体类型"),

    SERVER_INTERNAL_ERROR(500_00,""),
    BAD_SQL_GRAMMAR(500_01, "SQL语法错误"),
    JSON_PARSE_ERROR(500_02, "JSON解析错误"),
    DATABASE_EXCEPTION(500_03, "数据库异常"),
    REDIS_EXCEPTION(500_04, "缓存异常"),
    FILE_UPLOAD_ERROR(500_05, "文件上传错误"),
    FILE_NAME_TOO_LONG(500_06,"文件名过长"),
    FILE_TOO_LARGE(500_07,"文件过大"),

    UNKNOWN_ERROR(600_00, "未知错误");

    private int code;
    private String message;

    ReqState(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
