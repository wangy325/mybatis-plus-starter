package com.wangy.common.utils;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/2/8 / 23:38
 */
public class MessageUtils {
    static MessageSource messageSource
        = SpringUtils.getBean(ReloadableResourceBundleMessageSource.class);
    static WebMvcProperties webMvcProperties
        = SpringUtils.getBean(WebMvcProperties.class);

    public static String getMvcMessage(String code) {
        return messageSource.getMessage(code, null, webMvcProperties.getLocale());
    }

    public static String getMvcMessageWithArgs(String code, String... args){
        return messageSource.getMessage(code, args, webMvcProperties.getLocale());
    }

    public static MessageUtils build() {
        return SpringUtils.getBean(MessageUtils.class);
    }
}
