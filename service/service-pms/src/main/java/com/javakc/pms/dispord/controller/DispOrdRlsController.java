package com.javakc.pms.dispord.controller;

import com.javakc.mes.commonutils.api.APICODE;
import com.javakc.mes.servicebase.hanler.HctfException;
import com.javakc.pms.dispord.client.MesClient;
import com.javakc.pms.dispord.entity.DispOrdRls;
import com.javakc.pms.dispord.service.DispOrdRlsService;
import com.javakc.pms.dispord.vo.DispOrdRlsQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 调用指令管理
 */
@RestController
@RequestMapping("/pms/dispordrls")
//@CrossOrigin
public class DispOrdRlsController {

    @Autowired
    private DispOrdRlsService dispOrdRlsService;

    @Autowired
    MesClient mesClient;

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

    @ApiOperation("下达集团调度指令")
    @GetMapping("updateRelease/{dispOrdRlsId}")
    //通过ID查询 数据
    public APICODE updateRelease(@PathVariable("dispOrdRlsId") String dispOrdRlsId) {
        DispOrdRls dispOrdRls = dispOrdRlsService.getById(dispOrdRlsId);
        // 设置下达改变为已下达状态
        dispOrdRls.setIsRelease(1);
        dispOrdRls.setReleaseTime(new Date());

        // ## 调用 MES 服务，来进行数据的下达
        APICODE apicode = mesClient.savePmsDispOrdRls(dispOrdRls);
        if (apicode.getCode() == 20001) {
            throw new HctfException(20001,apicode.getMessage());
        }
        // ## 修改 pms 内容
        dispOrdRlsService.saveOrUpdate(dispOrdRls);
        return APICODE.OK();
    }
}
