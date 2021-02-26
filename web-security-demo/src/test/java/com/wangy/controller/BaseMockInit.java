package com.wangy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.wangy.service.ISpitterService;
import com.wangy.service.ISpittleService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.TestPropertySource;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/2/5 / 22:31
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class BaseMockInit {

//    @Autowired
//    protected ObjectMapper objectMapper;
    protected ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();


    protected @Mock
    ISpitterService spitterService;
    protected @Mock
    ISpittleService spittleService;
    protected SpitterController spitterController;
    protected SpittleController spittleController;

    @BeforeEach
    void initMock() {
        MockitoAnnotations.initMocks(this);
        spitterController = new SpitterController();
        spitterController.setSpitterService(spitterService);
        spittleController = new SpittleController();
        spittleController.setSpittleService(spittleService);
    }


    /**
     * Use json-path, tweaking configuration<br>
     * The config below change default action of json-path<br>
     * Use application-context ObjectMapper config as json and mapper provider<br>
     * <p>
     * Reference: <a href="https://github.com/json-path/JsonPath">
     * https://github.com/json-path/JsonPath</a>
     *
     * @param json standard json string
     * @return {@link DocumentContext}
     */
    protected DocumentContext jsonPathParser(String json) {

        final JsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);
        final MappingProvider mappingProvider = new JacksonMappingProvider(objectMapper);
        Configuration.setDefaults(new Configuration.Defaults() {
            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }
        });
        return JsonPath.parse(json);
    }
}
