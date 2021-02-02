package com.wangy.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.entity.Spittle;
import com.wangy.model.vo.SpittleVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * user spittle table Mapper 接口
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
public interface SpittleMapper extends BaseMapper<Spittle> {


    /**
     * 根据用户id分页查询spittles
     * <pre>
     *     &#64;Select("select * from spittle where spittle.spitter_id = #{dto.spitterId}")
     * </pre>
     *
     * @param page       IPage
     * @param spittleDTO spittleDTO
     * @return IPage
     * @see com.wangy.service.ISpittleService#pageQuerySpittleBySpitterId(SpittleDTO)
     */

    IPage<SpittleVO> pageQuerySpittleBySpitterId(IPage<SpittleVO> page, @Param("dto") SpittleDTO spittleDTO);

    /**
     * 分页查询某个时间段的spittles
     *
     * @param page       IPage
     * @param spittleDTO spittleDTO
     * @return IPage page
     * @see com.wangy.service.ISpittleService#pageQuerySpittlesByTimeLine(SpittleDTO)
     */
    IPage<SpittleVO> pageQuerySpittlesByTimeLine(Page<SpittleVO> page, @Param("dto") SpittleDTO spittleDTO);
}
