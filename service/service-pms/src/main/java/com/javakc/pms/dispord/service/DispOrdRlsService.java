package com.javakc.pms.dispord.service;

import com.javakc.mes.commonutils.jpa.base.service.BaseService;
import com.javakc.mes.commonutils.jpa.dynamic.SimpleSpecificationBuilder;
import com.javakc.pms.dispord.vo.DispOrdRlsQuery;
import com.javakc.pms.dispord.dao.DispOrdRlsDao;
import com.javakc.pms.dispord.entity.DispOrdRls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DispOrdRlsService extends BaseService<DispOrdRlsDao, DispOrdRls> {

    /**
     * 带条件的分页查询 - 调度指令管理
     * @param dispOrdRlsQuery
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<DispOrdRls> findPageDispOrdRls (DispOrdRlsQuery dispOrdRlsQuery, int pageNum, int pageSize) {
        SimpleSpecificationBuilder<DispOrdRls> simpleSpecificationBuilder = new SimpleSpecificationBuilder();
        Page<DispOrdRls> page = dao.findAll(simpleSpecificationBuilder.getSpecification(), PageRequest.of(pageNum - 1,pageSize));
        return page;
    }
}
