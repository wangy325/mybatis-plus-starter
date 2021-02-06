package com.wangy.message;

import com.wangy.WebSecurityDemoApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author wangy
 * @date 2021-2-6 15:27
 */
@SpringBootTest
@Slf4j
public class MessageTest extends WebSecurityDemoApplicationTests {

    @Autowired
    private MessageSource messageSource;

    @Test
    public void testMessage() {
        log.info(messageSource.getMessage("test.msg", null, Locale.SIMPLIFIED_CHINESE));
        log.info(messageSource.getMessage("test.msg", null, Locale.ENGLISH));
        log.info(messageSource.getMessage("test.msg", null, Locale.getDefault()));
    }
}
