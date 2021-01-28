package com.wangy.service;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.wangy.model.dto.SpitterDTO;
import com.wangy.model.entity.Spitter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * user info table 服务类
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
public interface ISpitterService extends IService<Spitter> {

    /**
     * update spitter info by spitterDTO
     *
     * @param dto
     * @return
     */
    boolean updateFromDtoById(SpitterDTO dto);
}
