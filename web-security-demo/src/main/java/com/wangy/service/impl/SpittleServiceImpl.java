package com.wangy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangy.model.entity.Spittle;
import com.wangy.service.mapper.SpittleMapper;
import com.wangy.service.ISpittleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * user spittle table 服务实现类
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
@Service
public class SpittleServiceImpl extends ServiceImpl<SpittleMapper, Spittle> implements ISpittleService {

    public IPage<Spittle> pageQuerySpittleByUsername(IPage<Spittle> page) {
        return null;
    }
}
