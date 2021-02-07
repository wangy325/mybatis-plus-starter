package com.wangy.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangy.common.enums.ReqState;
import com.wangy.common.model.ReqResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Locale;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/2/6 / 21:03
 */
@RestControllerAdvice
public class ResponseAdvice<T extends ReqResult<P>, P> implements ResponseBodyAdvice<T> {

    @Autowired
    MessageSource messageSource;
    @Autowired
    @Qualifier("objectMapper")
    ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        String typeName = returnType.getExecutable().getAnnotatedReturnType().getType().getTypeName();
//        return typeName.contains("ReqResult");
        return typeName.matches("\\S+ReqResult\\S+");
    }

    @Override
    public T beforeBodyWrite(T body,
                             MethodParameter returnType,
                             MediaType selectedContentType,
                             Class<? extends HttpMessageConverter<?>> selectedConverterType,
                             ServerHttpRequest request,
                             ServerHttpResponse response) {
        try {
            if (body.getMsg().matches("^([a-z]+\\.)+[a-z]+$")) {
                String message = messageSource.getMessage(body.getMsg(), null, Locale.getDefault());
                body.setMsg(message);
//                response.getBody().write(objectMapper.writeValueAsBytes(body));
            }
        } catch (Exception e) {
            ReqResult<P> error = ReqResult.fail(ReqState.RESPONSE_ADVICE_ERROR,
                messageSource.getMessage(ReqState.RESPONSE_ADVICE_ERROR.getMessage(), null, Locale.getDefault()));
            return (T) error;
        }
        return body;
    }
}
