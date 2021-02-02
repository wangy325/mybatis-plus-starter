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
     * @param spittleDTO required property: {@link SpittleDTO#spitterId}
     * @return IPage
     */
    IPage<SpittleVO> pageQuerySpittleBySpitterId(SpittleDTO spittleDTO);

    /**
     * 分页查询（用户）某个时间段的spittles
     *
     * @param spittleDTO <p> <li>{@link SpittleDTO#spitterId} 为空则查询所有用户</li>
     *                      <li>{@link SpittleDTO#leftTime} 必须</li>
     *                      <li>{@link SpittleDTO#rightTime} 为空则为当前时间</li>
     * @return IPage
     */
    IPage<SpittleVO> pageQuerySpittlesByTimeLine(SpittleDTO spittleDTO);
}
