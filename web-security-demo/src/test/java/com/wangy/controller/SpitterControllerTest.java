package com.wangy.controller;

import com.wangy.common.utils.BeanUtils;
import com.wangy.model.entity.Spitter;
import com.wangy.model.vo.SpitterVO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author wangy
 * @date 2021-1-28 16:26
 */
@SpringBootTest
public class SpitterControllerTest extends BaseMockInit {

    @Test
    public void getSpitterById() throws Exception {
        SpitterVO source = new SpitterVO(1, "alan", "walker", "aw", "xxx");
        Spitter spitter = new Spitter();
        BeanUtils.copyBeanProp(spitter, source);

        Mockito.when(spitterService.getById(1)).thenReturn(spitter);
        spitterController.setSpitterService(spitterService);
        MockMvc mockMvc = standaloneSetup(spitterController).build();
        mockMvc.perform(get("/spitter/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data")
                .value(objectMapper.convertValue(spitter, HashMap.class)));

        Mockito.verify(spitterService, Mockito.times(1)).getById(1);
    }
}
