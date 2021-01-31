package com.wangy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangy.WebSecurityDemoApplicationTests;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.vo.SpittleVO;
import org.junit.Assert;
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

    @Test
    public void pageQuerySpittleTest() {
        SpittleVO sample = SpittleVO.builder().build();
        sample.setSpitterId(4);
        sample.setMessage("sixth man");
        sample.setTime(LocalDateTime.of(2012, 6, 9, 22, 20, 0));
        sample.setLatitude(0.0d);
        sample.setLongitude(0.0d);

        SpittleDTO dto = new SpittleDTO();
        dto.setSpitterId(4);
        dto.setCurrentPage(1);
        dto.setPageSize(1);

        IPage<SpittleVO> page = spittleService.pageQuerySpittleBySpitterId(dto);

        List<SpittleVO> voList = page.getRecords();
        // items queried by pagination
        Assert.assertEquals(voList.size(), 1);
        assertSubMap(sample, voList.get(0));
    }
}
