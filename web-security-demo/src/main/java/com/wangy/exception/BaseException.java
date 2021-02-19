package com.wangy.exception;

import com.wangy.common.enums.ReqState;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.wangy.common.constant.UniversalConstants.*;

/**
 * 异常基类
 * <p>
 * How to use user definition exception:
 * <li>
 * 1. Initializing with all properties.<br>
 * The {@link Method}(throwMethod) and {@link Object}[](params) consist
 * the full-qualified name of caller which throws exception. Which means this two properties only used to debug.
 * If a user-definition exception is thrown in this case, you will see a <b>ERROR</b> log like this:
 *
 * <pre>
 *     c.w.a.GlobalExceptionHandler(31): className#methodName(paramsList) -> error message
 *     </pre>
 * And the {@link ReqState}(reqState) and {@link String}(message) represent the HTTP response <b>code</b> and
 * <b>message</b>.
 * </li>
 * <p>
 * <li>
 * 2. Initializing with {@link Method}(throwMethod), {@link Object}[](params), and {@link ReqState}(reqState)<br>
 * If a user-definition exception is thrown in this case, you will see a same <b>ERROR</b> log pattern as usage 1.<br>
 * The difference between usage 1 and 2 is that usage 2 use {@link ReqState#getMessage()} as HTTP response
 * <b>message</b>
 * </li>
 * <p>
 * <li>3. Initializing with {@link Method}(throwMethod) and {@link ReqState}(reqState) </li><br>
 * If a user-definition exception is thrown in this case, you will see a <b>ERROR</b> log like this:
 * <pre>
 *     c.w.a.GlobalExceptionHandler(31): className#methodName -> error message
 * </pre>
 * This also use the {@link ReqState#getMessage()} as HTTP response <b>message</b>
 * <p>
 * <li>4. Initializing with {@link ReqState}(reqState) and {@link String}(message)</li><br>
 * If a user-definition exception is thrown in this case, you will see a <b>ERROR</b> log like this:
 * <pre>
 *     c.w.a.GlobalExceptionHandler(31): className#methodName -> error message
 * </pre>
 * <p>
 * <li>5. Initializing with {@link ReqState}(reqState) only</li><br>
 * The simplest way to throw a user definition exception, supplying HTTP response code and message by using a
 * {@link ReqState} instance.
 * If a user-definition exception is thrown in this case, you will see a <b>ERROR</b> log like this:
 * <pre>
 *     c.w.a.GlobalExceptionHandler(31): -> error message
 * </pre>
 * This also use the {@link ReqState#getMessage()} as HTTP response <b>message</b>
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

    public BaseException(Method method, Objects[] params, ReqState state) {
        this(method, params, state, null);
    }

    public BaseException(Method method, ReqState state) {
        this(method, null, state);
    }

    public BaseException(ReqState state, String message) {
        this(null, null, state, message);
    }

    public BaseException(ReqState state) {
        this(state, null);
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
