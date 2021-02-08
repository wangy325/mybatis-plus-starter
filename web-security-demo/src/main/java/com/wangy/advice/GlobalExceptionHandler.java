package com.wangy.advice;

import com.wangy.common.enums.ReqState;
import com.wangy.common.model.ReqResult;
import com.wangy.exception.SpitterException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.wangy.common.constant.UniversalConstants.*;


/**
 * MVC全局异常处理
 *
 * @author wangy
 * @date 2021-2-4 9:08
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = Logger.getLogger("GlobalExceptionHandler");

    @ExceptionHandler(SpitterException.class)
    public ReqResult<?> handleException(SpitterException e) {
        log.severe(e.throwableString());
        return ReqResult.fail(e.getReqState(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ReqResult<?> handleException(BindException e) {
        log.severe(e.getMessage());
        FieldError error = e.getBindingResult().getFieldError();
        if (Objects.nonNull(error) && Objects.nonNull(error.getDefaultMessage())) {
            return ReqResult.fail(ReqState.VALIDATION_BIND_EXCEPTION, error.getDefaultMessage().split(SEMICOLON)[0]);
        }
        return ReqResult.fail(ReqState.VALIDATION_BIND_EXCEPTION);
    }

    @ExceptionHandler(Exception.class)
    public ReqResult<?> handleException(Exception e) {
        e.printStackTrace();
        log.severe(e.getMessage());
        return ReqResult.fail(ReqState.UNKNOWN_ERROR, e.getMessage());
    }
}
