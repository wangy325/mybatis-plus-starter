package com.wangy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuration of Spring international message source.
 *
 * @author wangy
 * @date 2021-2-6 14:33
 */
@Configuration
public class MessageSourceConfig {

    @Value("${spring.messages.basename}")
    private String basename;
    @Value("${spring.messages.encoding}")
    private String msgCharset;
    @Value("${spring.mvc.locale}")
    private Locale locale;


    /**
     * Register {@link org.springframework.context.support.ResourceBundleMessageSource}
     * for application context<br>
     *
     * @return MessageSource
     * @see MessageSource#getMessage(String, Object[], Locale)
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource
                = new ResourceBundleMessageSource();
//        messageSource.setBasename(basename);
        messageSource.setBasenames(basename);
        messageSource.setDefaultEncoding(msgCharset);
        return messageSource;
    }

    /**
     * Config this bean for using <code>message = "{messages.key}"</code> in Bean validation.<br>
     * Reference: <br>
     * <a href="https://www.baeldung.com/spring-custom-validation-message-source#defining-localvalidatorfactorybean">
     * https://www.baeldung.com/spring-custom-validation-message-source#defining-localvalidatorfactorybean</a>
     *
     * @param messageSource messageSource
     * @return {@link LocalValidatorFactoryBean}
     * @see com.wangy.model.dto.SpittleDTO
     */
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    /**
     * use SessionLocaleResolver
     *
     * @return {@link SessionLocaleResolver}
     */
    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(locale);
        return sessionLocaleResolver;
    }

    // it's ok to use CookieLocaleResolver
    /*@Bean
    public CookieLocaleResolver localeResolver(){
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(locale);
        return cookieLocaleResolver;
    }*/

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }
}
