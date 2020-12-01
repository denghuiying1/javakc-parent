package com.javakc.mes.servicebase.hanler;

import com.javakc.mes.commonutils.api.APICODE;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public APICODE errorHandler(Exception e) {
        e.printStackTrace();
        return APICODE.ERROR().message("服务调用失败");
    }

    @ExceptionHandler(HctfException.class)
    @ResponseBody
    public APICODE errorHandler(HctfException e) {
        e.printStackTrace();
        return APICODE.ERROR().message(e.getMsg()).code(e.getCode());
    }

}
