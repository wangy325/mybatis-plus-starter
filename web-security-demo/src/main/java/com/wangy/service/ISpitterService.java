package com.wangy.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.wangy.model.dto.SpitterDTO;
import com.wangy.model.entity.Spitter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangy.model.vo.SpitterVO;

import java.util.List;

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
     * @param dto{id is required}
     * @return true if affected row(s) > 0, or false
     */
    boolean updateFromDtoById(SpitterDTO dto);

    /**
     * Wrapper Usage Demo 1:<br>
     * <pre>use QueryWrapper in service.</pre>
     *
     * @param username column username
     * @return {@link SpitterVO}
     */
    SpitterVO queryByUsername(String username);

    /**
     * Wrapper Usage Demo 2:<br>
     * <pre>use QueryWrapper in Mapper.XMl</pre>
     *
     * @param wp a Wrapper<Spitter> wrapped self definition sql
     * @return {@link SpitterVO}
     */
    List<SpitterVO> queryByWrapper(Wrapper<Spitter> wp);
}
