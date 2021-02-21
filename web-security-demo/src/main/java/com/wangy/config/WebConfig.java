package com.wangy.config;

import com.wangy.interceptor.GlobalResponseInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @author wangy
 * @date 2021-2-6 17:53
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private GlobalResponseInterceptor globalResponseInterceptor;
    @Autowired
    private LocaleChangeInterceptor localeChangeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalResponseInterceptor).addPathPatterns("/**");
        registry.addInterceptor(localeChangeInterceptor).addPathPatterns("/**");
    }
}
