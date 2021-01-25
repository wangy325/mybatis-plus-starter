package com.wangy.controller;


import com.wangy.common.ReqResult;
import com.wangy.model.entity.Spitter;
import com.wangy.service.ISpitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * user info table 前端控制器
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
@RestController
@RequestMapping("/spitter")
public class SpitterController {

    @Autowired
    ISpitterService spitterService;

    @RequestMapping("/{id}")
    @ResponseBody
    public ReqResult<Spitter> getSpitterById(@PathVariable int id){
        Spitter spitter = spitterService.getById(id);
        return ReqResult.success(spitter);
    }

}
