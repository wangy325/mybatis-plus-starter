package com.wangy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.entity.Spittle;
import com.wangy.model.vo.SpittleVO;

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
     * 分页查询用户spittles
     *
     * @param spittleDto required property: {@link SpittleDTO#spitterId}
     * @return IPage
     */
    IPage<SpittleVO> pageQuerySpittleBySpitterId(SpittleDTO spittleDto);
}
