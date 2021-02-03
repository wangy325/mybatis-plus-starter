package com.wangy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wangy.common.model.PageDomain;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.vo.SpittleVO;
import com.wangy.service.ISpittleService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/31 / 17:59
 */
@SpringBootTest
public class SpittleControllerTest {

    static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static ISpittleService spittleService;
    static SpittleController spittleController;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void init() {
        spittleService = Mockito.mock(ISpittleService.class);
        spittleController = new SpittleController();
        // 配置jackson对LocalDateTime的序列化/反序列化规则
        /*objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule);*/
    }

    @Test
    public void getUserSpittlesPageTest() throws Exception {
        SpittleVO sample = SpittleVO.builder().build();
        sample.setSpitterId(4);
        sample.setMessage("sixth man");
        sample.setTime(LocalDateTime.parse("2012-06-09 22:20:00", DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        sample.setLatitude(0.0d);
        sample.setLongitude(0.0d);
        sample.setId(214748364L);
        // for debug
//        String s = objectMapper.writeValueAsString(sample);

        SpittleDTO dto = new SpittleDTO();
        dto.setSpitterId(sample.getSpitterId());
        dto.setCurrentPage(1);
        dto.setPageSize(1);
        HashMap<String, String> dtoMap = (HashMap<String, String>) objectMapper.convertValue(dto, Map.class);

        IPage<SpittleVO> page = new Page<>();
        page.setCurrent(dto.getCurrentPage());
        page.setSize(dto.getPageSize());
        page.setTotal(4);
        page.setRecords(new ArrayList<SpittleVO>() {{
            add(sample);
        }});
        PageDomain<SpittleVO> pageDomain = new PageDomain<>(page);

        // 此处必须使用类类型作为参数
        Mockito.when(spittleService.pageQuerySpittleBySpitterId(Mockito.any(SpittleDTO.class))).thenReturn(page);

        spittleController.setSpittleService(spittleService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(spittleController).build();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : dtoMap.entrySet()) {
            if (Objects.nonNull(entry.getValue())) {
                paramMap.put(entry.getKey(), Collections.singletonList(String.valueOf(entry.getValue())));
            }
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/spittle/user/spittles")
                .queryParams(paramMap)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.currentPage")
                        .value(pageDomain.getCurrentPage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize")
                        .value(pageDomain.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pages")
                        .value(pageDomain.getPages()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.total").
                        value(pageDomain.getTotal()))
                // get element from json array
                // see https://github.com/json-path/JsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[0]")
                        // 报错原因 ：double和integer的问题
                        // 长整型(long)数据在json序列化之后再反序列化，
                        // Java类型将变为integer(如果数据没有超过int的最大值范围)
                        // 超过范围为没有问题
                        // 不要直接比较对象相等性即可绕过此问题
                        .value(objectMapper.convertValue(sample, HashMap.class)))
        ;
        Mockito.verify(spittleService, Mockito.times(1));
    }

}
