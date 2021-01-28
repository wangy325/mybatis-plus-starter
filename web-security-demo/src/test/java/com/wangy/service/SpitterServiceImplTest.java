package com.wangy.service;

import com.wangy.model.dto.SpitterDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author wangy
 * @date 2021-1-28 16:10
 */
@SpringBootTest
@ActiveProfiles("h2")
@PropertySource("classpath:**/application-h2.properties")
public class SpitterServiceImplTest {

    @Autowired
    private ISpitterService spitterService;

    @Test
    public void  UpdateFromDtoByIdTest(){
        SpitterDTO dto = SpitterDTO.builder().build();
        dto.setId(1);
        dto.setFirstname("STEPHEN");
        dto.setLastname("CURRY");

        Assert.assertTrue(spitterService.updateFromDtoById(dto));
    }
}
