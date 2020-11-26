package com.javakc.pms.dispord.controller;

import com.javakc.commonutils.api.APICODE;
import com.javakc.pms.dispord.entity.DispOrd;
import com.javakc.pms.dispord.service.DispOrdService;
import com.javakc.pms.dispord.vo.DispOrdQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "调度指令库控制器")
@RestController
@RequestMapping("/pms/dispord")
@CrossOrigin
public class DispOrdController {

    @Autowired
    private DispOrdService dispOrdService;

    @ApiOperation("查询所有调度指令库数据")
    @GetMapping
    public APICODE findAll(){
        List<DispOrd> dispOrdList = dispOrdService.findAll();
        //返回自定义的返回格式内容
        return APICODE.OK().data("items",dispOrdList);
    }

    @ApiOperation("带条件的分页查询 - 调度指令库")
    @PostMapping("{pageNum}/{pageSize}")
    public APICODE findPageDispOrd(@RequestBody(required = false) DispOrdQuery dispOrdQuery, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize){

        Page<DispOrd> page = dispOrdService.findPageDispOrd(dispOrdQuery, pageNum, pageSize);
        // ## 当前页的数据集合
        List<DispOrd> list = page.getContent();
        // ## 总条数
        long totalElements = page.getTotalElements();
        return APICODE.OK().data("total",totalElements).data("items",list);
    }

    @ApiOperation("新增 - 调度指令库")
    @PostMapping("createDispOrd")
    public APICODE createDispOrd(@RequestBody DispOrd dispOrd) {
        dispOrdService.saveOrUpdate(dispOrd);
        return APICODE.OK();
    }

    @ApiOperation("根据调度指令ID获取单条数据")
    @GetMapping("{dispOrdId}")
    public APICODE getDispOrdById(@PathVariable("dispOrdId") String dispOrdId){
        DispOrd dispOrd = dispOrdService.getById(dispOrdId);
        return APICODE.OK().data("dispOrd",dispOrd);
    }

    @ApiOperation("修改 - 调度指令库")
    @PutMapping
    public APICODE updateDispOrd(@RequestBody DispOrd dispOrd){
        dispOrdService.saveOrUpdate(dispOrd);
        return APICODE.OK();
    }

    @ApiOperation("删除 - 调度指令库")
    @DeleteMapping("{dispOrdId}")
    public APICODE deleteDispOrd(@PathVariable("dispOrdId") String dispOrdId){
        dispOrdService.removeById(dispOrdId);
        return APICODE.OK();
    }

}
