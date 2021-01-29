package com.wangy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangy.model.entity.Spittle;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * user spittle table 服务类
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
public interface ISpittleService extends IService<Spittle> {

    /**
     *
     * @param page
     * @return
     */
    IPage<Spittle> pageQuerySpittleByUsername(IPage<Spittle> page);
}
