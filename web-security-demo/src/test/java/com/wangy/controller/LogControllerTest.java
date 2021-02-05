package com.wangy.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author wangy
 * @date 2021-2-5 15:33
 */
@SpringBootTest
public class LogControllerTest extends BaseMockInit {

    @Test
    public void LogTest() throws Exception {
        LogController logController = Mockito.mock(LogController.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(logController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/log"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(logController).log();
    }
}
