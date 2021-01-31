package com.wangy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.entity.Spittle;
import com.wangy.model.vo.SpittleVO;
import com.wangy.service.ISpittleService;
import com.wangy.service.mapper.SpittleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Resource
    private SpittleMapper spittleMapper;

    @Override
    public IPage<SpittleVO> pageQuerySpittleBySpitterId(SpittleDTO spittleDTO) {
        Page<SpittleVO> page = new Page<>(spittleDTO.getCurrentPage(), spittleDTO.getPageSize());
        OrderItem oi = new OrderItem();
        oi.setColumn("id");
        oi.setAsc(true);
        page.addOrder(oi);

        spittleMapper.pageQuerySpittleBySpitterId(page, spittleDTO);
        return page;
    }
}
