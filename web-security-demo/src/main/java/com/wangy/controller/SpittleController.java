package com.wangy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangy.common.model.PageDomain;
import com.wangy.common.model.ReqResult;
import com.wangy.model.dto.SpittleDTO;
import com.wangy.model.vo.SpittleVO;
import com.wangy.service.ISpittleService;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * user spittle table 前端控制器
 * </p>
 *
 * @author wangy
 * @since 2021-01-22
 */
@RestController
@RequestMapping("/spittle")
@Setter
public class SpittleController {

    @Resource
    private ISpittleService spittleService;

    @GetMapping("/user/spittles")
    public ReqResult<PageDomain<SpittleVO>> getUserSpittlesPage(SpittleDTO spittleDTO) {
        IPage<SpittleVO> page = spittleService.pageQuerySpittleBySpitterId(spittleDTO);
        return ReqResult.ok(new PageDomain<>(page));
    }

    /**
     * TODO: 参数校验
     *
     * @param spittleDTO
     * @return
     */
    @GetMapping("/range/spittles")
    public ReqResult<PageDomain<SpittleVO>> getSpittlesTimeLinePage(@Valid SpittleDTO spittleDTO) {
        IPage<SpittleVO> page = spittleService.pageQuerySpittlesByTimeLine(spittleDTO);
        return ReqResult.ok(new PageDomain<>(page));
    }
}
