package com.wangy.common.utils;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/2/8 / 23:38
 */
public class MessageUtils {

    static MessageSource messageSource
            = SpringUtils.getBean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME);

    public static String getMvcMessage(String code, String... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static MessageUtils build() {
        return SpringUtils.getBean(MessageUtils.class);
    }
}
