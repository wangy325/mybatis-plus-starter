package com.wangy.advice;

import com.wangy.common.enums.HttpStatus;
import com.wangy.common.enums.ReqState;
import com.wangy.common.model.ReqResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/2/6 / 21:03
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings({"unchecked"})
public class GlobalResponseAdvice<T> implements ResponseBodyAdvice<T> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WebMvcProperties webMvcProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        String typeName = returnType.getExecutable().getAnnotatedReturnType().getType().getTypeName();
        // if the resultType matches the regex, then do beforeBodyWrite()
        log.debug("{} matches \"\\S+ReqResult\\S+|\\S+ResponseEntity<.+>\" :{}", typeName, typeName.matches("\\S+ReqResult\\S+|\\S+ResponseEntity<.+>"));
        return typeName.matches("\\S+ReqResult\\S+|\\S+ResponseEntity<.+>");
    }

    @Override
    public T beforeBodyWrite(T body,
                             MethodParameter returnType,
                             MediaType selectedContentType,
                             Class<? extends HttpMessageConverter<?>> selectedConverterType,
                             ServerHttpRequest request,
                             ServerHttpResponse response) {
//        log.debug("default JVM local setting: {}" , Locale.getDefault());
//        log.debug("current MVC local setting: {}" , webMvcProperties.getLocale());

        try {
            // the regex match message pattern: http.ok, validation.bind.exception,... in message_*.properties
            if (body instanceof ReqResult) {
                ReqResult<?> bd = (ReqResult<?>) body;
                String regex = "^([a-z]+\\.)+[a-z]+$";
                if (bd.getMsg().matches(regex)) {
                    bd.setMsg(messageSource.getMessage(bd.getMsg(), null, webMvcProperties.getLocale()));
                }
            } else if (body instanceof LinkedHashMap) {
                // status, error, message, timestamp, path
                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) body;
                int status = (int) map.get("status");
                switch (status) {
                    case 404:
                        return (T) ReqResult.fail(ReqState.NOT_FOUND,
                                messageSource.getMessage(ReqState.NOT_FOUND.getMessage(), null, webMvcProperties.getLocale()));
                    case 500:
                        return (T) ReqResult.fail(ReqState.SERVER_INTERNAL_ERROR,
                                messageSource.getMessage(ReqState.SERVER_INTERNAL_ERROR.getMessage(), null, webMvcProperties.getLocale()));
                    default:
                }
                if (status == HttpStatus.NOT_FOUND.getCode()) {
                    return (T) ReqResult.fail(ReqState.NOT_FOUND,
                            messageSource.getMessage(ReqState.NOT_FOUND.getMessage(), null, webMvcProperties.getLocale()));
                }
            }
        } catch (Exception e) {
            ReqResult<?> error = ReqResult.fail(ReqState.RESPONSE_ADVICE_ERROR,
                    messageSource.getMessage(ReqState.RESPONSE_ADVICE_ERROR.getMessage(), null, webMvcProperties.getLocale()));
            return (T) error;
        }
        return body;
    }
}
