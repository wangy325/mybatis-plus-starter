package com.wangy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangy.common.model.PageDomain;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.vo.SpittleVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/31 / 17:59
 */
@SpringBootTest
@Slf4j
@SuppressWarnings("unchecked")
public class SpittleControllerTest extends BaseMockInit {

    SpittleVO sample = SpittleVO.builder().build();
    SpittleDTO dto = new SpittleDTO();
    IPage<SpittleVO> page = new Page<>();
    PageDomain<SpittleVO> pageDomain;
    MockMvc mockMvc;


    @BeforeEach
    void init() {

        sample.setSpitterId(4);
        sample.setMessage("sixth man");
        sample.setTime(LocalDateTime.parse("2012-06-09T22:20:00"));
        sample.setLatitude(0.0d);
        sample.setLongitude(0.0d);
        sample.setId(1L);

        page.setCurrent(dto.getCurrentPage());
        page.setSize(dto.getPageSize());
        page.setTotal(4);
        page.setRecords(new ArrayList<SpittleVO>() {{
            add(sample);
        }});
        pageDomain = new PageDomain<>(page);

        mockMvc = standaloneSetup(spittleController)
            .alwaysExpect(status().isOk())
            .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
            .build();

    }

    @Test
    public void getUserSpittlesPageTest() throws Exception {
        // for debug
        log.debug(objectMapper.writeValueAsString(sample));

        // 此处必须使用类类型作为参数
        when(spittleService.pageQuerySpittleBySpitterId(any(SpittleDTO.class))).thenReturn(page);

        // perform get with request params transferred by pojo
        ResultActions resultActions = mockMvc
            .perform(get("/spittle/user/spittles?spitterId={spitterId}", 4))
            // get element from json
            // see https://github.com/json-path/JsonPath
            .andExpect(jsonPath("$.data.currentPage")
                .value(pageDomain.getCurrentPage()))
            .andExpect(jsonPath("$.data.pageSize")
                .value(pageDomain.getPageSize()))
            .andExpect(jsonPath("$.data.pages")
                .value(pageDomain.getPages()))
            .andExpect(jsonPath("$.data.total")
                .value(pageDomain.getTotal()))
            .andDo(print());
            /* 报错原因 ：long和integer的问题*/
//            .andExpect(jsonPath("$.data.records[0]")
//                .value(objectMapper.convertValue(sample, HashMap.class)));

        String jsonResult = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        log.info(jsonResult);
        // verify is not necessary here
        verify(spittleService).pageQuerySpittleBySpitterId(any(SpittleDTO.class));

        assertEquals((int) jsonPathParser(jsonResult).read("$.data.records.length()"), 1);
        SpittleVO rvo = jsonPathParser(jsonResult).read("$.data.records[0]", SpittleVO.class);
        assertEquals(sample, rvo);
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
     * 参考：<a href = "https://www.tutorialfor.com/questions-149885.htm">
     * https://www.tutorialfor.com/questions-149885.htm</a>
     */
    @Deprecated
    public void getSpittlesTimeLinePageTest() throws Exception {
        dto.setSpitterId(4);
        dto.setCurrentPage(1);
        dto.setPageSize(10);
        dto.setLeftTime(LocalDateTime.parse("2012-06-09T00:00:00.000"));
        dto.setRightTime(LocalDateTime.parse("2012-06-09T23:59:59.999"));

        when(spittleService.pageQuerySpittlesByTimeLine(dto)).thenReturn(page);

        MockMvc mockMvc = standaloneSetup(spittleController).build();
        // /spittle/range/spittles?leftTime=2012-06-09 00:00:00&rightTime=2012-06-09 23:59:59
        ResultActions resultActions = mockMvc
            .perform(get("/spittle/range/spittles")
                .flashAttr("spittleDTO", dto))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // 以下用来获取MockMvc返回(Json)
        MvcResult mvcResult = resultActions.andReturn();
        String jsonResult = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        log.info(jsonResult);

        verify(spittleService).pageQuerySpittlesByTimeLine(dto);

        PageDomain<SpittleVO> rpg = jsonPathParser(jsonResult).read("$.data", PageDomain.class);
        assertEquals((int) jsonPathParser(jsonResult).read("$.data.records.length()"), 1);
        SpittleVO rvo = jsonPathParser(jsonResult).read("$.data.records[0]", SpittleVO.class);
        rpg.setRecords(new ArrayList<SpittleVO>() {{
            add(rvo);
        }});
        assertEquals(rpg, pageDomain);
    }

    /**
     * mock post query to /spittle/range/spittles
     * <b> with post request body like
     * <pre>
     *     {
     *             "leftTime": "2012-06-09 00:00:00",
     *             "rightTime": "2012-06-09 23:59:59",
     *             "currentPage":2,
     *             "pageSize": 1
     *      }
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void postSpittlesTimeLinePageTest() throws Exception {
        dto.setSpitterId(4);
        dto.setCurrentPage(1);
        dto.setPageSize(10);
        dto.setLeftTime(LocalDateTime.parse("2012-06-09T00:00:00.000"));
        dto.setRightTime(LocalDateTime.parse("2012-06-09T23:59:59.999"));

        when(spittleService.pageQuerySpittlesByTimeLine(any(SpittleDTO.class))).thenReturn(page);

        // perform post request
        String s = objectMapper.writeValueAsString(dto);
        log.info("request body: {}", s);
        ResultActions resultActions = mockMvc
            .perform(post("/spittle/range/spittles")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf8")
                .content(s))
            .andDo(print());

        // 以下用来获取MockMvc返回(Json)
        String jsonResult = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        log.info(jsonResult);

        PageDomain<SpittleVO> rpg = jsonPathParser(jsonResult).read("$.data", PageDomain.class);
        assertEquals((int) jsonPathParser(jsonResult).read("$.data.records.length()"), 1);
        SpittleVO rvo = jsonPathParser(jsonResult).read("$.data.records[0]", SpittleVO.class);
        rpg.setRecords(new ArrayList<SpittleVO>() {{
            add(rvo);
        }});
        assertEquals(rpg, pageDomain);
    }

}
