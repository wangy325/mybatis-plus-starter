package com.wangy.advice;

import com.wangy.common.enums.ReqState;
import com.wangy.common.model.ReqResult;
import com.wangy.exception.SpitterException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
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
    public ReqResult<?> spitterException(SpitterException e) {
        log.severe(e.throwableString());
        return ReqResult.fail(e.getReqState(), e.getMessage());
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ReqResult<?> bindException(Exception e) {
        log.severe(e.getMessage());
        if (e instanceof BindException || e instanceof MethodArgumentNotValidException) {
            FieldError error = e instanceof BindException
                    ? ((BindException) e).getBindingResult().getFieldError()
                    : ((MethodArgumentNotValidException) e).getBindingResult().getFieldError();
            if (Objects.nonNull(error) && Objects.nonNull(error.getDefaultMessage())) {
                return ReqResult.fail(ReqState.VALIDATION_BIND_EXCEPTION, error.getDefaultMessage().split(SEMICOLON)[0]);
            }
        }
        return ReqResult.fail(ReqState.VALIDATION_BIND_EXCEPTION);
    }

    /**
     * 处理时间字符串参数无法解析的异常，例如试图将yyyy-MM-dd解析为LocalDateTime
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ReqResult<?> dateParseException(HttpMessageNotReadableException e) {
        log.severe(e.getMessage());
        Throwable cause = e.getRootCause();
        if (Objects.nonNull(cause)) {
            return ReqResult.fail(ReqState.JSON_PARSE_ERROR, cause.getMessage());
        }
        return ReqResult.fail(ReqState.JSON_PARSE_ERROR);
    }

    @ExceptionHandler
    public ReqResult<?> unknownException(Exception e) {
        e.printStackTrace();
        log.severe(e.getMessage());
        return ReqResult.fail(ReqState.UNKNOWN_ERROR, e.getMessage());
    }
}
