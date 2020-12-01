package com.javakc.pms.dispord.client;

import com.javakc.mes.commonutils.api.APICODE;
import com.javakc.pms.dispord.entity.DispOrdRls;
import org.springframework.stereotype.Component;

/**
 * 熔断器实现类 无法调用时使用
 */
@Component
public class MesFeignClient implements MesClient{

    @Override
    public APICODE savePmsDispOrdRls(DispOrdRls dispOrdRls) {
        return APICODE.ERROR().message("MES - 服务diao调用失败 - 下达  ");
    }
}
