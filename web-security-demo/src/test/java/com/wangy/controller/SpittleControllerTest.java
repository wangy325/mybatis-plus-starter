package com.wangy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wangy.common.model.PageDomain;
import com.wangy.config.DateConfig;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.vo.SpittleVO;
import com.wangy.service.ISpittleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
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

    @Autowired
    @Qualifier("objectMapper")
    ObjectMapper objectMapper;


    static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static ISpittleService spittleService;
    static SpittleController spittleController;
    static SpittleVO sample = SpittleVO.builder().build();
    static SpittleDTO dto = new SpittleDTO();
    static IPage<SpittleVO> page = new Page<>();
    static PageDomain<SpittleVO> pageDomain;


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

        sample.setSpitterId(4);
        sample.setMessage("sixth man");
        sample.setTime(LocalDateTime.parse("2012-06-09 22:20:00", DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        sample.setLatitude(0.0d);
        sample.setLongitude(0.0d);
        sample.setId(214748364L);

        page.setCurrent(dto.getCurrentPage());
        page.setSize(dto.getPageSize());
        page.setTotal(4);
        page.setRecords(new ArrayList<SpittleVO>() {{
            add(sample);
        }});
        pageDomain = new PageDomain<>(page);


    }

    @Test
    public void getUserSpittlesPageTest() throws Exception {

        // for debug
//        String s = objectMapper.writeValueAsString(sample);

        dto.setSpitterId(sample.getSpitterId());
        dto.setCurrentPage(1);
        dto.setPageSize(1);
        HashMap<String, String> dtoMap = (HashMap<String, String>) objectMapper.convertValue(dto, Map.class);

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
                .queryParams(paramMap))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.currentPage")
                        .value(pageDomain.getCurrentPage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize")
                        .value(pageDomain.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pages")
                        .value(pageDomain.getPages()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.total")
                        .value(pageDomain.getTotal()))
                // get element from json array
                // see https://github.com/json-path/JsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[0]")
                        /* 报错原因 ：double和integer的问题
                         * 长整型(long)数据在json序列化之后再反序列化，
                         * Java类型将变为integer(如果数据没有超过int的最大值范围)
                         * 超过范围为没有问题
                         * 不要直接比较对象相等性即可绕过此问题
                         */
                        .value(objectMapper.convertValue(sample, HashMap.class)))
        ;
        Mockito.verify(spittleService, Mockito.times(1));
    }

    /**
     * MockMvc的测试很奇怪，不能直接使用
     * {@link MockHttpServletRequestBuilder#param(String, String...)}
     * /{@link MockHttpServletRequestBuilder#params(MultiValueMap)}或
     * {@link MockHttpServletRequestBuilder#queryParam(String, String...)}
     * /{@link MockHttpServletRequestBuilder#queryParams(MultiValueMap)}
     * 方法，以<b>字符串形式</b>传入LocalDataTime参数，报错信息为类型不匹配。<br>
     * 即使引入{@link com.wangy.config.TestConfig}，配置{@link Converter}，也毫无效果<br>
     * 但是整形参数作为字符串传入是可以的
     * <p>
     * 解决的方式很简单粗暴：<br>
     * 直接调用{@link MockHttpServletRequestBuilder#flashAttr(String, Object)}传入DTO对象即可
     * <p>
     * 参考：https://www.tutorialfor.com/questions-149885.htm
     */
    @Test
    public void getSpittlesTimeLinePageTest() throws Exception {
        dto.setSpitterId(4);
        dto.setCurrentPage(1);
        dto.setPageSize(10);
        dto.setLeftTime(LocalDateTime.parse("2012-06-09T00:00:00.000"));
        dto.setRightTime(LocalDateTime.parse("2012-06-09T23:59:59.999"));

        Mockito.when(spittleService.pageQuerySpittlesByTimeLine(Mockito.any(SpittleDTO.class))).thenReturn(page);

        spittleController.setSpittleService(spittleService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(spittleController).build();
        // /spittle/range/spittles?leftTime=2012-06-09 00:00:00&rightTime=2012-06-09 23:59:59
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/spittle/range/spittles")
                        .flashAttr("spittleDTO", dto))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.currentPage")
                        .value(pageDomain.getCurrentPage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize")
                        .value(pageDomain.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pages")
                        .value(pageDomain.getPages()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.total")
                        .value(pageDomain.getTotal()));

        Mockito.verify(spittleService, Mockito.times(1));
        // 以下用来获取MockMvc返回(Json)
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println(contentAsString);
    }
}
