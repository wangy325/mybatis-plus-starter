package com.wangy.exception;

import com.wangy.common.enums.ReqState;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.wangy.common.constant.UniversalConstants.*;

/**
 * 异常基类
 *
 * @author wangy
 * @date 2021-2-4 11:08
 */
public class BaseException extends RuntimeException {

    /**
     * method throw exception
     */
    private Method throwMethod;

    /**
     * 方法参数，按照形参列表的顺序放入数组中
     */
    private Object[] params;

    /**
     * request status, with system defined message
     */
    private ReqState reqState;

    /**
     * 自定义异常消息，当此消息不为null时，使用此消息，否则使用{@link ReqState}中定义的国际化消息
     */
    private String message;

    public BaseException(Method throwMethod, Object[] params, ReqState reqState, String message) {
        this.throwMethod = throwMethod;
        this.params = params;
        this.reqState = reqState;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return Objects.isNull(message) ? reqState.getMessage() : message;
    }

    public Object[] getParams() {
        return params;
    }

    public ReqState getReqState() {
        return reqState;
    }

    /**
     * generate log message for user-definition exception
     *
     * @return exception log message
     */
    public String throwableString() {
        StringBuffer sb = new StringBuffer();
        if (Objects.nonNull(throwMethod)) {
            sb.append(throwMethod.getDeclaringClass().getName())
                    .append(SHARP)
                    .append(throwMethod.getName());
        }
        if (Objects.nonNull(params)) {
//          int[] ints = {1, 2, 3, 4};
//          System.out.println(Arrays.stream(ints).mapToObj(String::valueOf).collect(Collectors.joining(COMMA)));  // 1,2,3,4
            sb.append(LEFT_BRACKET)
                    .append(Arrays.stream(params).map(Object::toString).collect(Collectors.joining(COMMA)));
        }
        if (sb.length() == 0) {
            sb.append(getMessage());
        } else {
            sb.append(SPACE).append(RIGHT_ARROW).append(SPACE).append(getMessage());
        }
        return sb.toString();
    }
}
