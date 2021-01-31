package com.wangy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangy.common.model.PageDomain;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.vo.SpittleVO;
import com.wangy.service.ISpittleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.*;


/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/31 / 17:59
 */
@SpringBootTest
public class SpittleControllerTest {

    static ISpittleService spittleService;
    static SpittleController spittleController;
    static ObjectMapper objectMapper;

    @BeforeAll
    static void init() {
        spittleService = Mockito.mock(ISpittleService.class);
        spittleController = new SpittleController();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void getUserSpittlesPageTest() throws Exception {
        SpittleVO sample = SpittleVO.builder().build();
        sample.setSpitterId(4);
        sample.setMessage("sixth man");
        sample.setTime(LocalDateTime.of(2012, 6, 9, 22, 20, 0));
        sample.setLatitude(0.0d);
        sample.setLongitude(0.0d);
        sample.setId(1L);

        SpittleDTO dto = new SpittleDTO();
        dto.setSpitterId(sample.getSpitterId());
        dto.setCurrentPage(1);
        dto.setPageSize(1);
        HashMap<String, String> dtoMap = (HashMap<String, String>) objectMapper.convertValue(dto, Map.class);

        IPage<SpittleVO> page = new Page<>();
        page.setCurrent(dto.getCurrentPage());
        page.setSize(dto.getPageSize());
        page.setTotal(4);
        // if pages set by itself ?
        page.setPages(page.getPages());
        page.setRecords(new ArrayList<SpittleVO>() {{
            add(sample);
        }});
        PageDomain<SpittleVO> pageDomain = new PageDomain<>(page);

        Mockito.when(spittleService.pageQuerySpittleBySpitterId(Mockito.any(SpittleDTO.class))).thenReturn(page);

        spittleController.setSpittleService(spittleService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(spittleController).build();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : dtoMap.entrySet()) {
            if (Objects.nonNull(entry.getValue())) {
                paramMap.put(entry.getKey(), Collections.singletonList(String.valueOf(entry.getValue())));
            }
        }
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/spittle/user/spittles")
            .queryParams(paramMap)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.currentPage").value(pageDomain.getCurrentPage()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(pageDomain.getPageSize()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.pages").value(pageDomain.getPages()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.total").value(pageDomain.getTotal()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.records").value(objectMapper.convertValue(sample, HashMap.class)))
            ;


        Mockito.verify(spittleController, Mockito.times(1));

    }

}
