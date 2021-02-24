package com.wangy.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangy.model.dto.SpittleDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author wangy
 * @date 2021-2-24 11:01
 */
@SpringBootTest
@Slf4j
@TestPropertySource("classpath:application-test.properties")
public class ObjectMapperTest {

    @Autowired
    Jackson2ObjectMapperBuilder builder;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MappingJackson2HttpMessageConverter converter;


    @Test
    public void configResultTest() {
        Assertions.assertNotNull(builder);
        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(converter);

        Assertions.assertEquals(objectMapper, converter.getObjectMapper());


        DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig();
        int deserializationFeatures = deserializationConfig.getDeserializationFeatures();
        log.info("deserializationFeatures: {}", deserializationFeatures);

        // TODO: figure out how featureMask works
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        // default feature
        Assertions.assertFalse(deserializationContext.hasDeserializationFeatures(1));

        // self definition features
        Assertions.assertFalse(deserializationContext.hasDeserializationFeatures(1 << 4));
        Assertions.assertFalse(deserializationContext.hasDeserializationFeatures(1 << 26));

    }

    @Test
    public String serializeDeserializeTest() throws JsonProcessingException {
        SpittleDTO spittleDTO = new SpittleDTO();

        spittleDTO.setSpitterId(4);
        spittleDTO.setCurrentPage(1);
        spittleDTO.setPageSize(10);
        spittleDTO.setLeftTime(LocalDateTime.parse("2012-06-09T00:00:00.000"));
        spittleDTO.setRightTime(LocalDateTime.parse("2012-06-09T23:59:59.999"));

        String json = objectMapper.writeValueAsString(spittleDTO);
        log.info("serialization: {}", json);

        SpittleDTO readValue = objectMapper.readValue(json, SpittleDTO.class);

        assertContentEquals(spittleDTO, readValue);
        return json;
    }

    @Test
    public void converterTest() throws IOException {
        String json = serializeDeserializeTest();
        MockHttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(json.getBytes(StandardCharsets.UTF_8));

        SpittleDTO read = (SpittleDTO) converter.read(SpittleDTO.class, mockHttpInputMessage);

        log.info("read from converter: {}", objectMapper.writeValueAsString(read));
    }

    private void assertContentEquals(Object a, Object b) {

        Field[] fields = a.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (Objects.nonNull(field.get(b)))
                    Assert.assertEquals(field.get(b), field.get(b));
            }
        } catch (Exception e) {
            // do nothing
        }
    }
}
