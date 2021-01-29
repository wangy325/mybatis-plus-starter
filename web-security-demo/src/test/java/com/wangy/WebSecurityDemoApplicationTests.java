package com.wangy;

import com.wangy.service.ISpitterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class WebSecurityDemoApplicationTests {

	@Autowired
	protected ISpitterService spitterService;

	@Test
	void contextLoads() {
	}

}
