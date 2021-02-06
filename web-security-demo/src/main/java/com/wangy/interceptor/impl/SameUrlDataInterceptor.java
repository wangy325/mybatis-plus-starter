package com.wangy.interceptor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wangy.common.utils.HttpHelper;
import com.wangy.interceptor.RepeatSubmitInterceptor;
import com.wangy.interceptor.annotation.RepeatSubmit;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 判断请求url和数据是否和上一次相同，
 * 如果和上次相同，则是重复提交表单。 有效时间为10秒内。
 * <p>
 * 在控制器上引入&#64;{@link RepeatSubmit}注解使用此拦截器
 *
 * @author ruoyi
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {
    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    public final String CACHE_REPEAT_KEY = "repeatData";


    /**
     * 间隔时间，单位:秒 默认10秒
     * <p>
     * 两次相同参数的请求，如果间隔时间大于该参数，系统不会认定为重复提交的数据
     */
    private int intervalTime = 10;

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request) {
        RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
        String nowParams = HttpHelper.getBodyString(repeatedlyRequest);

        // body参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams)) {
            try {
                nowParams = objectMapper.writeValueAsString(request.getParameterMap());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> nowDataMap = new HashMap<String, Object>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();

//        Object sessionObj = redisCache.getCacheObject(CACHE_REPEAT_KEY);
        Object sessionObj = null;
        if (sessionObj != null) {
            Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
            if (sessionMap.containsKey(url)) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);

                boolean bool1 = Comparator
                        .comparing((Function<Map<String, Object>, String>) map -> (String) map.get(REPEAT_PARAMS))
                        .compare(nowDataMap, preDataMap) == 0;

                boolean bool2 = Comparator
                        .comparing((Function<Map<String, Object>, Long>) map -> (Long) map.get(REPEAT_TIME),
                                (o1, o2) -> {
                                    int interval = (int) Math.abs(o1 - o2);
                                    return interval < intervalTime * 1000 ? 1 : (interval == intervalTime ? 0 : -1);
                                })
                        .compare(nowDataMap, preDataMap) > 0;
                return bool1 && bool2;
            }
        }
        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put(url, nowDataMap);
//        redisCache.setCacheObject(CACHE_REPEAT_KEY, cacheMap, intervalTime, TimeUnit.SECONDS);
        return false;
    }
}
