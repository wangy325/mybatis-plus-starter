package com.wangy.controller;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangy.common.utils.BeanUtils;
import com.wangy.model.entity.Spitter;
import com.wangy.model.vo.SpitterVO;
import com.wangy.service.ISpitterService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.mockito.Mockito;
import org.mockito.internal.invocation.MatchersBinder;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author wangy
 * @date 2021-1-28 16:26
 */
@SpringBootTest
public class SpitterControllerTest {

    static ISpitterService spitterService;
    static SpitterController spitterController;

    @BeforeAll
    static void initData(){
        // do nothing
         spitterService = Mockito.mock(ISpitterService.class);
         spitterController = new SpitterController(spitterService);
    }


    @Test
    public void getSpitterById() throws Exception {
        SpitterVO source = new SpitterVO(1,"STEPHEN", "CURRY", "sc30","xxx");
        Spitter spitter = new Spitter();
        BeanUtils.copyBeanProp(spitter, source);

        ObjectMapper mapper = new ObjectMapper();
        Mockito.when(spitterService.getById(1)).thenReturn(spitter);

        MockMvc mockMvc = standaloneSetup(spitterController).build();
        mockMvc.perform(get("/spitter/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(mapper.convertValue(spitter, HashMap.class)));

    }
}
