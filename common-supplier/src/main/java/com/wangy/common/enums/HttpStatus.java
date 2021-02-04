package com.wangy.common.enums;

import lombok.Getter;

/**
 * @version 1.0
 * @author wangy
 * @date 2021/1/24 / 23:29
 * @see org.springframework.http.HttpStatus
 */
@Getter
public enum HttpStatus {

    //Informational 1xx  信息
    CONTINUE(100, "继续"),
    SWITCHING_PROTOCOLS(101, "交换协议"),

    //Successful 2xx
    OK(200, "成功"),
    CREATED(201, "创建"),
    ACCEPTED(202, "已接受"),
    NON_AUTHORITATIVE_INFORMATION(203, "非权威信息"),
    NO_CONTENT(204, "没有内容"),
    RESET_CONTENT(205, "重置内容"),
    PARTIAL_CONTENT(206, "部分内容"),

    //Redirection 3xx  重定向
    MULTIPLE_CHOICES(300,"多种选择"),
    MOVED_PERMANENTLY(301,"永久移动"),
    FOUND(302,"找到"),
    SEE_OTHER(303,"参见其他"),
    NOT_MODIFIED(304,"未修改"),
    USE_PROXY(305,"使用代理"),
    UNUSED(306,"未使用"),
    TEMPORARY_REDIRECT(307,"暂时重定向"),

    //Client_Error 4xx  客户端错误
    BAD_REQUEST(400,"错误的请求"),
    UNAUTHORIZED(401,"未经授权"),
    PAYMENT_REQUIRED(402,"付费请求"),
    FORBIDDEN(403,"禁止"),
    NOT_FOUND(404,"请求资源未找到"),
    METHOD_NOT_ALLOWED(405,"方法不允许"),
    NOT_ACCEPTABLE(406,"不可接受"),
    PROXY_AUTHENTICATION_REQUIRED(407,"需要代理身份验证"),
    REQUEST_TIMEOUT(408,"请求超时"),
    CONFLICT(409,"指令冲突"),
    GONE(410,"文档永久地离开了指定的位置"),
    LENGTH_REQUIRED(411,"需要CONTENT-LENGTH头请求"),
    PRECONDITION_FAILED(412,"前提条件失败"),
    REQUEST_ENTITY_TOO_LARGE(413,"请求实体太大"),
    REQUEST_URI_TOO_LONG(414,"请求URI太长"),
    UNSUPPORTED_MEDIA_TYPE(415,"不支持的媒体类型"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416,"请求的范围不可满足"),
    EXPECTATION_FAILED(417,"期望失败"),

    //SERVER_ERROR 5XX  服务器错误
    INTERNAL_SERVER_ERROR(500,"内部服务器错误"),
    NOT_IMPLEMENTED(501,"未实现"),
    BAD_GATEWAY(502,"错误的网关"),
    SERVICE_UNAVAILABLE(503,"服务不可用"),
    GATEWAY_TIMEOUT(504,"网关超时"),
    HTTP_VERSION_NOT_SUPPORTED(505,"HTTP版本不支持");

    int code;
    String detail;

    HttpStatus(int code, String detail) {
        this.code = code;
        this.detail = detail;
    }
}
