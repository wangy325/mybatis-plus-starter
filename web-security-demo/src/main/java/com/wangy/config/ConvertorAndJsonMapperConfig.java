package com.wangy.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * 处理Date类型数据映射（将前端传入的字符串转换为对应的Java type）
 *
 * @author wangy
 * @date 2021-2-3 11:34
 */
@Configuration
public class ConvertorAndJsonMapperConfig {

    /**
     * 默认日期时间格式
     */
    static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";


    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数<br>
     * source pattern : ‘yyyy-MM-dd’
     * <p>
     * 这里使用lambda表达式，启动报错，spring无法推断泛型信息。<br>
     * 参考：https://github.com/spring-projects/spring-framework/issues/22509
     */
    @Bean
    public Converter<String, LocalDate> stringToLocalDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            }
        };
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数<br>
     * source pattern : ‘yyyy-MM-dd HH:mm:ss’
     */
    @Bean
    public Converter<String, LocalDateTime> stringToLocalDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
            }
        };
    }

    /**
     * LocalTime转换器，用于转换RequestParam和PathVariable参数
     * source pattern : ‘HH:mm:ss’
     */
    @Bean
    public Converter<String, LocalTime> stringToLocalTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
            }
        };
    }

    /**
     * Date转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, Date> stringToDateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
                try {
                    return format.parse(source);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }


    /*
     * Json序列化和反序列化转换器，用于转换HTTP 请求体（requestBody）的json以及将我们的对象序列化为返回响应的json<br>
     * 自定义jackson序列化和反序列化的行为，主要针对时间日期的格式
     * <p>
     * 使用此配置之后可以忽略单独的&#64;{@link com.fasterxml.jackson.annotation.JsonFormat}注解<br>
     * <b>!JsonFormat注解只用来序列化</b>
     *
     * @param javaTimeModule {@link #javaTimeModule()}
     * @return {@link ObjectMapper}
     */

    // 使用自动配置替代
//    @Bean("objectMapper")
//    @ConditionalOnClass(JavaTimeModule.class)
//    public ObjectMapper objectMapper(JavaTimeModule javaTimeModule) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        // disable(Feature) = configure(Feature, false)
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
//        //ObjectMapper忽略多余字段
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//
//        //LocalDateTime系列序列化和反序列化模块，继承自jsr310，我们在这里修改了日期格式
//        // 注册新的模块到objectMapper
//        objectMapper.registerModule(javaTimeModule);
//        return objectMapper;
//    }

    /**
     * Module for jackson serialization and deserialization
     *
     * @return {@link JavaTimeModule}
     * @see org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // java.time.* 的序列化和反序列化规则
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));

        //java.util.Date序列化和反序列化
        javaTimeModule.addSerializer(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
                String formattedDate = formatter.format(date);
                jsonGenerator.writeString(formattedDate);
            }
        });
        javaTimeModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
                String date = jsonParser.getText();
                try {
                    return format.parse(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return javaTimeModule;
    }
}
