package com.wangy.service.impl;

import com.wangy.model.dto.SpitterDTO;
import com.wangy.model.entity.Spitter;
import com.wangy.service.mapper.SpitterMapper;
import com.wangy.service.ISpitterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * user info table 服务实现类
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
@Service
public class SpitterServiceImpl extends ServiceImpl<SpitterMapper, Spitter> implements ISpitterService {


    @Override
    public boolean updateFromDtoById(SpitterDTO dto) {
        Spitter spitter = new Spitter();
        BeanUtils.copyProperties(dto, spitter);
        return updateById(spitter);
    }
}
