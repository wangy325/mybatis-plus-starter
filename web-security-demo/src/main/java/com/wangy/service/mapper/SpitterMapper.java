package com.wangy.service.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wangy.model.entity.Spitter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangy.model.vo.SpitterVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * user info table Mapper 接口
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
public interface SpitterMapper extends BaseMapper<Spitter> {

    /**
     * @see com.wangy.service.ISpitterService#queryByWrapper(Wrapper)
     */
    @Select("select * from spitter ${ew.customSqlSegment}")
    List<SpitterVO> queryByWrapper(@Param(Constants.WRAPPER)Wrapper<Spitter> wrapper);
}
