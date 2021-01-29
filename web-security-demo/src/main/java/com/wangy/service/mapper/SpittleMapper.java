package com.wangy.service.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangy.model.entity.Spittle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * user spittle table Mapper 接口
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
public interface SpittleMapper extends BaseMapper<Spittle> {


    IPage<Spittle> pageQuerySpittleByUsername(IPage<Spittle> page);
}
