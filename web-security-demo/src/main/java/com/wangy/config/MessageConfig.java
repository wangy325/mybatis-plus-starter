package com.wangy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

/**
 * Configuration of Spring international message source.
 *
 * @author wangy
 * @date 2021-2-6 14:33
 */
@Configuration
public class MessageConfig {

    @Value("${spring.messages.basename}")
    private String basename;
    @Value("${spring.messages.encoding}")
    private String msgCharset;


    /**
     * Register {@link org.springframework.context.support.ResourceBundleMessageSource}
     * for application context<br>
     *
     * @return MessageSource
     * @see MessageSource#getMessage(String, Object[], Locale)
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(basename);
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
}
