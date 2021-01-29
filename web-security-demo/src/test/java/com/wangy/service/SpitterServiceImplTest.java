package com.wangy.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangy.WebSecurityDemoApplicationTests;
import com.wangy.model.dto.SpitterDTO;
import com.wangy.model.entity.Spitter;
import com.wangy.model.vo.SpitterVO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author wangy
 * @date 2021-1-28 16:10
 */
@SpringBootTest
public class SpitterServiceImplTest extends WebSecurityDemoApplicationTests {


    @Test
    public void updateFromDtoByIdTest() {
        SpitterDTO dto = SpitterDTO.builder().build();
        dto.setId(1);
        dto.setFirstname("STEPHEN");
        dto.setLastname("CURRY");

        Assert.assertTrue(spitterService.updateFromDtoById(dto));
    }

    @Test
    public void queryByUsernameTest() {
        String username = "sc30";
        SpitterVO vo = SpitterVO.builder().build();
        vo.setFirstname("stephen");
        vo.setLastname("curry");
        vo.setUsername(username);

        SpitterVO qr = spitterService.queryByUsername(username);

        assertSubMap(vo, qr);
    }

    @Test
    public void blurQueryByNameTest(){
        String keyWord = "k";
        SpitterVO vo = SpitterVO.builder().build();
        vo.setFirstname("klay");
        vo.setLastname("thompson");
        vo.setUsername("kt11");
        List<SpitterVO> rl = spitterService.queryByWrapper(Wrappers.lambdaQuery(Spitter.class)
                .like(Spitter::getFirstname, keyWord)
                .or()
                .like(Spitter::getLastname, keyWord));
        Assert.assertEquals(rl.size(), 2);
    }

    private <T> void assertSubMap(T vo, T qr) {
        // T -> class -> Fields,
        Field[] fields = vo.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (Objects.nonNull(field.get(vo)))
                    Assert.assertEquals(field.get(vo), field.get(qr));
            }
        } catch (Exception e) {
            // do nothing
        }
    }
}
