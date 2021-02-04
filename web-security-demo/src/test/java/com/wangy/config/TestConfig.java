package com.wangy.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wangy
 * @date 2021-2-4 15:37
 */
//@TestConfiguration
public class TestConfig {
    static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Converter<String, LocalDateTime> stringToLocalDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
            }
        };
    }
}
