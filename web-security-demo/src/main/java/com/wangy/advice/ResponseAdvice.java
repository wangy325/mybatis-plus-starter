package com.wangy.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangy.common.enums.ReqState;
import com.wangy.common.model.ReqResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.jsf.el.SpringBeanFacesELResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Locale;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/2/6 / 21:03
 */
@Slf4j
@RestControllerAdvice
public class ResponseAdvice<T extends ReqResult<P>, P> implements ResponseBodyAdvice<T> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WebMvcProperties webMvcProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        String typeName = returnType.getExecutable().getAnnotatedReturnType().getType().getTypeName();
        // if the resultType is ReqResult, then do beforeBodyWrite()
        return typeName.matches("\\S+ReqResult\\S+");
    }

    @Override
    public T beforeBodyWrite(T body,
                             MethodParameter returnType,
                             MediaType selectedContentType,
                             Class<? extends HttpMessageConverter<?>> selectedConverterType,
                             ServerHttpRequest request,
                             ServerHttpResponse response) {
        log.debug("default JVM local setting:" + Locale.getDefault());
        log.debug("current MVC local setting:" + webMvcProperties.getLocale());
        try {
            // the regex match message pattern: http.ok, validation.bind.exception,... in message_*.properties
            if (body.getMsg().matches("^([a-z]+\\.)+[a-z]+$")) {
                body.setMsg(messageSource.getMessage(body.getMsg(), null, webMvcProperties.getLocale()));
            }
        } catch (Exception e) {
            ReqResult<P> error = ReqResult.fail(ReqState.RESPONSE_ADVICE_ERROR,
                    messageSource.getMessage(ReqState.RESPONSE_ADVICE_ERROR.getMessage(), null, webMvcProperties.getLocale()));
            return (T) error;
        }
        return body;
    }
}
