package com.wangy;

import com.wangy.service.ISpitterService;
import com.wangy.service.ISpittleService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class WebSecurityDemoApplicationTests {

    @Autowired
    protected ISpitterService spitterService;
    @Autowired
    protected ISpittleService spittleService;

    @Test
    void contextLoads() {
    }

    protected <T> void assertSubMap(T vo, T qr) {
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

    protected  <T> void assertContains(List<T> rl, T vo) {
        Field[] fields = vo.getClass().getDeclaredFields();
        int size = rl.size();
        int breaks = 0;

        for (T t : rl) {
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object o = field.get(vo);
                    if (Objects.nonNull(o)) {
                        if (!o.equals(field.get(t))) {
                            breaks++;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // do nothing
            }
        }
        Assert.assertTrue(breaks < size && breaks > 0);
    }

}
