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
    OK(200_00, "http.ok"),
    VALIDATION_BIND_EXCEPTION(400_01, "validation.bind.exception"),
    REPEAT_SUBMIT_DENIED(400_02,"repeat.submit.denied"),
    UNAUTHORIZED(401_00, "not.authorized"),
    AUTHENTICATE_FAIL(401_01,"authenticate.fail"),
    AUTHENTICATION_ACCESS_DENIED(403_00, "authentication.access.denied"),
    NOT_FOUND(404_00, "not.found"),
    REQUEST_TIMEOUT(408_00, "request.timeout"),
    UNSUPPORTED_MEDIA_TYPE(415_00, "unsupported.media.type"),

    SERVER_INTERNAL_ERROR(500_00,"server.internal.error"),
    BAD_SQL_GRAMMAR(500_01, "bad.sql.grammar"),
    JSON_PARSE_ERROR(500_02, "json.parse.error"),
    DATABASE_EXCEPTION(500_03, "database.exception"),
    REDIS_EXCEPTION(500_04, "redis.exception"),
    FILE_UPLOAD_ERROR(500_05, "file.upload.error"),
    FILE_NAME_TOO_LONG(500_06,"file.name.too.long"),
    FILE_TOO_LARGE(500_07,"file.too.large"),
    RESPONSE_ADVICE_ERROR(500_08, "response.advice.error"),
    SATISFIED_RESOURCE_NOT_FOUND(500_09,"satisfied.resource.not.found"),

    UNKNOWN_ERROR(600_00, "unknown.error");

    private int code;
    private String message;

    ReqState(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
