package com.wangy.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangy.common.enums.ReqState;
import com.wangy.common.utils.BeanUtils;
import com.wangy.exception.SpitterException;
import com.wangy.model.dto.SpitterDTO;
import com.wangy.model.entity.Spitter;
import com.wangy.model.vo.SpitterVO;
import com.wangy.service.ISpitterService;
import com.wangy.service.mapper.SpitterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.wangy.common.constant.UniversalConstants.SHARP;

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

    @Resource
    private SpitterMapper spitterMapper;

    @Override
    public boolean updateFromDtoById(SpitterDTO dto) {
        Spitter spitter = new Spitter();
        BeanUtils.copyProperties(dto, spitter);
        if (!updateById(spitter)) {
            try {
                throw new SpitterException(
                    getClass().getName() + SHARP + getClass().getDeclaredMethod("updateFromDtoById", SpitterDTO.class).getName(),
                    new Object[]{dto},
                    ReqState.SATISFIED_RESOURCE_NOT_FOUND,
                    "no.specific.id.resource"
                );
            } catch (NoSuchMethodException e) {
                // ignore
            }
        }
        return true;
    }

    @Override
    public SpitterVO queryByUsername(String username) {
        // 使用QueryWrapper列名只能使用硬编码
        Spitter spitter = getOne(Wrappers.query(new Spitter()).eq("username", username));

        // 使用lambdaQuery可以减少硬编码
        spitter = getOne(Wrappers.lambdaQuery(Spitter.class).eq(Spitter::getUsername, username));
        SpitterVO vo = SpitterVO.builder().build();
        BeanUtils.copyProperties(spitter, vo);
        return vo;
    }

    @Override
    public List<SpitterVO> queryByWrapper(Wrapper<Spitter> wrapper) {
        return spitterMapper.queryByWrapper(wrapper);
    }
}
