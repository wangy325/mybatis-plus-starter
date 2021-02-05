package com.wangy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangy
 * @date 2021-2-5 15:27
 */
@RestController
@RequestMapping("/log")
public class LogController {

    Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    @GetMapping
    public String log(){
        log.trace("A TRACE Message.");
        log.debug("A DEBUG Message.");
        log.info("A INFO Message.");
        log.warn("A WARN Message.");
        log.error("A ERROR Message.");
        return "Check out Console to see the output.";
    }
}
