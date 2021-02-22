package com.wangy.controller;

import com.wangy.common.model.ReqResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author wangy
 * @date 2021-2-5 15:27
 */
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    @GetMapping
    public ReqResult<?> log() {
        log.trace("A TRACE Message.");
        log.debug("A DEBUG Message.");
        log.info("A INFO Message.");
        log.warn("A WARN Message.");
        log.error("A ERROR Message.");
        return ReqResult.ok("Check out Console to see the output.");
    }

    @GetMapping("/locale")
    public ReqResult<?> locale(Locale locale) {
        return ReqResult.ok(locale);
    }

    @GetMapping("/resolver/locale")
    public ReqResult<?> locale(HttpServletRequest request) {
        RequestContext rc = new RequestContext(request);
        return ReqResult.ok(rc.getMessage("http.ok"), rc.getLocale());
    }

    @GetMapping("/request/locale")
    public ReqResult<?> locale(HttpServletRequest request, HttpServletResponse response){
        // TODO why this method always return default locale?
        return ReqResult.ok(request.getLocale());
    }
}
