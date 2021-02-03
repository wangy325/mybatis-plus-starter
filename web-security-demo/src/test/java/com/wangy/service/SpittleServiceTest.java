package com.wangy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangy.WebSecurityDemoApplicationTests;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.vo.SpittleVO;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/30 / 21:22
 */
@SpringBootTest
public class SpittleServiceTest extends WebSecurityDemoApplicationTests {

    static SpittleVO sample = SpittleVO.builder().build();
    static SpittleDTO dto = new SpittleDTO();

    @BeforeAll
    static void init(){
        sample.setSpitterId(4);
        sample.setMessage("sixth man");
        sample.setTime(LocalDateTime.parse("2012-06-09T22:20:00"));
        sample.setLatitude(0.0d);
        sample.setLongitude(0.0d);
    }

    @Test
    public void pageQuerySpittleTest() {
        dto.setSpitterId(4);
        dto.setCurrentPage(1);
        dto.setPageSize(1);

        IPage<SpittleVO> page = spittleService.pageQuerySpittleBySpitterId(dto);

        List<SpittleVO> voList = page.getRecords();
        // items queried by pagination
        Assert.assertEquals(voList.size(), 1);
        assertSubMap(sample, voList.get(0));
    }

    @Test
    public void pageQuerySpittlesByTimeLineTest(){
//        dto.setSpitterId(4);
        dto.setLeftTime(LocalDateTime.parse("2012-06-09T00:00:00.000"));
        dto.setRightTime(LocalDateTime.parse("2012-06-09T23:59:59.999"));
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        IPage<SpittleVO> page = spittleService.pageQuerySpittlesByTimeLine(dto);
        List<SpittleVO> spittles = page.getRecords();

        Assert.assertEquals(spittles.size(), 5);
        assertContains(spittles, sample);
    }
}
