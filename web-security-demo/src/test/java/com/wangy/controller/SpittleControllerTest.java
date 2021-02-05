package com.wangy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.wangy.common.model.PageDomain;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.vo.SpittleVO;
import com.wangy.service.ISpittleService;
import org.junit.Assert;
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
        spittleController.setSpittleService(spittleService);

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

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(spittleController).build();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : dtoMap.entrySet()) {
            if (Objects.nonNull(entry.getValue())) {
                paramMap.put(entry.getKey(), Collections.singletonList(String.valueOf(entry.getValue())));
            }
        }
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/spittle/user/spittles")
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
                .value(pageDomain.getTotal()));
        // get element from json array
        // see https://github.com/json-path/JsonPath
        /* 报错原因 ：double和integer的问题
         * 长整型(long)数据在json序列化之后再反序列化，
         * Java类型将变为integer(如果数据没有超过int的最大值范围)
         * 超过范围为没有问题
         * <del>不要直接比较对象相等性即可绕过此问题<del>
         * 可以使用json-path进行json判断
         */
                /*.andExpect(MockMvcResultMatchers.jsonPath("$.data.records[0]")
                        .value(objectMapper.convertValue(sample, HashMap.class)))*/

        Mockito.verify(spittleService, Mockito.times(1))
            .pageQuerySpittleBySpitterId(Mockito.any(SpittleDTO.class));
//        Mockito.reset(spittleService);

        String jsonResult = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertEquals((int) jsonPathParser(jsonResult).read("$.data.records.length()"), 1);
        SpittleVO rvo = jsonPathParser(jsonResult).read("$.data.records[0]", SpittleVO.class);
        Assert.assertEquals(sample, rvo);
    }

    /**
     * MockMvc的测试很奇怪，不能直接使用
     * {@link MockHttpServletRequestBuilder#param(String, String...)}
     * /{@link MockHttpServletRequestBuilder#params(MultiValueMap)}或
     * {@link MockHttpServletRequestBuilder#queryParam(String, String...)}
     * /{@link MockHttpServletRequestBuilder#queryParams(MultiValueMap)}
     * 方法，以<b>字符串形式</b>传入LocalDataTime参数，报错信息为类型不匹配(typeMismatch)。<br>
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

        Mockito.when(spittleService.pageQuerySpittlesByTimeLine(dto)).thenReturn(page);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(spittleController).build();
        // /spittle/range/spittles?leftTime=2012-06-09 00:00:00&rightTime=2012-06-09 23:59:59
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/spittle/range/spittles")
                .flashAttr("spittleDTO", dto))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        Mockito.verify(spittleService).pageQuerySpittlesByTimeLine(dto);
        // 以下用来获取MockMvc返回(Json)
        MvcResult mvcResult = resultActions.andReturn();
        String jsonResult = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println(jsonResult);

        PageDomain<SpittleVO> rpg = jsonPathParser(jsonResult).read("$.data", PageDomain.class);
        Assert.assertEquals((int) jsonPathParser(jsonResult).read("$.data.records.length()"), 1);
        SpittleVO rvo = jsonPathParser(jsonResult).read("$.data.records[0]", SpittleVO.class);
        rpg.setRecords(new ArrayList<SpittleVO>() {{
            add(rvo);
        }});
        Assert.assertEquals(rpg, pageDomain);
    }

    /**
     * Use json-path, tweaking configuration<br>
     * The config below change default action of json-path<br>
     * Use application-context ObjectMapper config as json and mapper provider<br>
     * <p>
     * Reference: https://github.com/json-path/JsonPath
     *
     * @param json standard json string
     * @return {@link DocumentContext}
     */
    private DocumentContext jsonPathParser(String json) {

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
