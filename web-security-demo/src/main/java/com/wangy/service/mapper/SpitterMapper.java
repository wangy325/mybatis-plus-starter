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
     * 在使用包装器作为参数时，形参列表参数名要么使用
     * <pre>
     *  &#64;Param(Constants.Wrapper)
     * </pre>
     * 注解声明，要么形参的名字只能声明为
     * <pre>
     * queryByWrapper(Wrapper, ew)
     * </pre>
     * <p>
     * Wrapper中实际上是包装的一个“WHERE子句”，因此在使用过程中，直接使用字符串占位符替换的形式即可：
     * <pre>
     *      &lt;select id="getAll" resultType="MysqlData"&gt;
     * 	        SELECT * FROM mysql_data ${ew.customSqlSegment}
     *      &lt;/select&gt;
     * </pre>
     *
     * 使用基于<code>@Select</code>方法注解的原生sql可以替代xml：
     * <pre>
     *     &#64;Select("select * from spitter ${ew.customSqlSegment}")
     * </pre>
     *
     * @param wrapper sql segment wrapped by {@link com.baomidou.mybatisplus.core.conditions.AbstractWrapper}
     * @return List&lt;SpitterVO&gt;
     * @see com.wangy.service.ISpitterService#queryByWrapper(Wrapper)
     * @see Wrapper#getCustomSqlSegment()
     */
    List<SpitterVO> queryByWrapper(@Param(Constants.WRAPPER) Wrapper<Spitter> wrapper);

}
