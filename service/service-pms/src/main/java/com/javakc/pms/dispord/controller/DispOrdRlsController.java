package com.javakc.pms.dispord.controller;

import com.javakc.commonutils.api.APICODE;
import com.javakc.pms.dispord.entity.DispOrdRls;
import com.javakc.pms.dispord.service.DispOrdRlsService;
import com.javakc.pms.dispord.vo.DispOrdQuery;
import com.javakc.pms.dispord.vo.DispOrdRlsQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用指令管理
 */
@RestController
@RequestMapping("/pms/dispordrls")
@CrossOrigin
public class DispOrdRlsController {

    @Autowired
    private DispOrdRlsService dispOrdRlsService;

    @ApiOperation("带条件的分页查询 - 调度指令管理")
    @PostMapping("{pageNum}/{pageSize}")
    public APICODE findPageDispOrdRls(@RequestBody(required = false) DispOrdRlsQuery dispOrdRlsQuery, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {
        Page<DispOrdRls> page = dispOrdRlsService.findPageDispOrdRls(dispOrdRlsQuery, pageNum, pageSize);
        //获得当前页内容
        List<DispOrdRls> list = page.getContent();
        long totalElements = page.getTotalElements();
        return APICODE.OK().data("total",totalElements).data("items",list);
    }
    @ApiOperation("根据调度指令管理ID进行单条数据获取")
    @GetMapping("{dispOrdRlsId}")
    public APICODE view(@PathVariable("dispOrdRlsId") String dispOrdRlsId) {
        DispOrdRls dispOrdRls = dispOrdRlsService.getById(dispOrdRlsId);
        return APICODE.OK().data("dispOrdRls",dispOrdRls);
    }
}
