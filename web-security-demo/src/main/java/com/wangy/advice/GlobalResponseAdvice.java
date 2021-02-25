package com.wangy.advice;

import com.wangy.common.enums.HttpStatus;
import com.wangy.common.enums.ReqState;
import com.wangy.common.model.ReqResult;
import com.wangy.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;

/**
 * Spring MVC 全局响应处理
 *
 * @author wangy
 * @version 1.0
 * @date 2021/2/6 / 21:03
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings({"unchecked"})

public class GlobalResponseAdvice<T> implements ResponseBodyAdvice<T> {
    @Value("${management.server.port}")
    private int actuatorPort;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        String typeName = returnType.getExecutable().getAnnotatedReturnType().getType().getTypeName();
        // if the resultType matches the regex, then do beforeBodyWrite()
        String regex = "([\\w]+\\.)+([A-z][a-z]+)+(<.*>|\\b)";
        boolean matches = typeName.matches(regex);
        if (!matches) {
            log.warn("returnType {} doesn't match regex '{}'", typeName, regex);
        }
        return matches;
    }

    @Override
    public T beforeBodyWrite(T body,
                             MethodParameter returnType,
                             MediaType selectedContentType,
                             Class<? extends HttpMessageConverter<?>> selectedConverterType,
                             ServerHttpRequest request,
                             ServerHttpResponse response) {
        try {

            int port = request.getURI().getPort();
            if (port == actuatorPort) {
                return body;
            }

            // the regex match message pattern: http.ok, validation.bind.exception,... in messages*.properties
            if (body instanceof ReqResult) {
                ReqResult<?> bd = (ReqResult<?>) body;
                String regex = "^([a-z]+\\.)+[a-z]+$";
                if (bd.getMsg().matches(regex)) {
                    bd.setMsg(MessageUtils.getMvcMessage(bd.getMsg()));
                }
            } else if (body instanceof LinkedHashMap) {
                // status, error, message, timestamp, path
                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) body;
                int status = (int) map.get("status");
                switch (status) {
                    case 404:
                        return (T) ReqResult.fail(ReqState.NOT_FOUND,
                                MessageUtils.getMvcMessage(ReqState.NOT_FOUND.getMessage()));
                    case 500:
                        return (T) ReqResult.fail(ReqState.SERVER_INTERNAL_ERROR,
                                MessageUtils.getMvcMessage(ReqState.SERVER_INTERNAL_ERROR.getMessage()));
                    default:
                }
            }
        } catch (Exception e) {
            // user should never see this response...
            ReqResult<?> error = ReqResult.fail(ReqState.RESPONSE_ADVICE_ERROR,
                    MessageUtils.getMvcMessage(ReqState.RESPONSE_ADVICE_ERROR.getMessage()));
            return (T) error;
        }
        return body;
    }
}
