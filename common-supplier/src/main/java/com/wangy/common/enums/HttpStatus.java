package com.wangy.common.enums;

import lombok.Getter;

/**
 * @version 1.0
 * @author wangy
 * @date 2021/1/24 / 23:29
 */
@Getter
public enum HttpStatus {

    //Informational 1xx  信息
    Continue(100, "继续"),
    Switching_Protocols(101, "交换协议"),

    //Successful 2xx
    OK(200, "成功"),
    Created(201, "创建"),  
    Accepted(202, "已接受"),  
    Non_Authoritative_Information(203, "非权威信息"),  
    No_Content(204, "没有内容"),  
    Reset_Content(205, "重置内容"),  
    Partial_Content(206, "部分内容"),  

    //Redirection 3xx  重定向
    Multiple_Choices(300,"多种选择"),  
    Moved_Permanently(301,"永久移动"),  
    Found(302,"找到"),  
    See_Other(303,"参见其他"),  
    Not_Modified(304,"未修改"),  
    Use_Proxy(305,"使用代理"),  
    Unused(306,"未使用"),  
    Temporary_Redirect(307,"暂时重定向"),  

    //Client_Error 4xx  客户端错误
    Bad_Request(400,"错误的请求"),  
    Unauthorized(401,"未经授权"),  
    Payment_Required(402,"付费请求"),  
    Forbidden(403,"禁止"),  
    Not_Found(404,"Not_Found"),  
    Method_Not_Allowed(405,"方法不允许"),  
    Not_Acceptable(406,"不可接受"),  
    Proxy_Authentication_Required(407,"需要代理身份验证"),  
    Request_Timeout(408,"请求超时"),  
    Conflict(409,"指令冲突"),  
    Gone(410,"文档永久地离开了指定的位置"),  
    Length_Required(411,"需要Content-Length头请求"),  
    Precondition_Failed(412,"前提条件失败"),  
    Request_Entity_Too_Large(413,"请求实体太大"),  
    Request_URI_Too_Long(414,"请求URI太长"),
    Unsupported_Media_Type(415,"不支持的媒体类型"),
    Requested_Range_Not_Satisfiable(416,"请求的范围不可满足"),
    Expectation_Failed(417,"期望失败"),

    //Server_Error 5xx  服务器错误
    Internal_Server_Error(500,"内部服务器错误"),
    Not_Implemented(501,"未实现"),
    Bad_Gateway(502,"错误的网关"),
    Service_Unavailable(503,"服务不可用"),
    Gateway_Timeout(504,"网关超时"),
    HTTP_Version_Not_Supported(505,"HTTP版本不支持");  

    int code;
    String detail;

    HttpStatus(int code, String detail) {
        this.code = code;
        this.detail = detail;
    }
}
