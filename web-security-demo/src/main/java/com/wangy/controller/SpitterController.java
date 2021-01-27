package com.wangy.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wangy.common.ReqResult;
import com.wangy.model.entity.Spitter;
import com.wangy.service.ISpitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * get spitter info by id
     */
    @GetMapping("/{id}")
    public ReqResult<Spitter> getSpitterById(@PathVariable int id) {
        Spitter spitter = spitterService.getById(id);
        return ReqResult.ok(spitter);
    }

    /**
     * should be logical remove -> update
     */
    @DeleteMapping("/delete/{id}")
    public ReqResult<?> deleteSpitterById(@PathVariable int id) {
        boolean b = spitterService.removeById(id);
        return b ? ReqResult.ok() : ReqResult.fail();
    }

    /**
     * resume a logical deleted spitter by id.
     * <p>
     * <b>[Important]</b>You should NEVER try to access data that has been deleted.<br>
     * <p>
     * In this controller, the {@link UpdateWrapper<Spitter>} always add `deleted = 0` in
     * where clause. This is the philosophy of <b>Logical Delete</b> in mybatis-plus.
     */
    @PostMapping("/resume/{id}")
    public ReqResult<?> resumeSpitterById(@PathVariable int id) {
//        boolean b = spitterService.update(new UpdateWrapper<Spitter>().set("deleted", 1).eq("id", id));
        return ReqResult.ok();
    }
}
