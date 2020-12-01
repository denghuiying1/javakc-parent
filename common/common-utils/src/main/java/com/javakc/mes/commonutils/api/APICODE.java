package com.javakc.mes.commonutils.api;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一数据结果返回
 */
@Data
public class APICODE {
    /**
     *状态码
     */
    private Integer code;

    /**
     * 解释消息
     */
    private String message;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 查询数据，数据放入Map中
     */
    private Map<String,Object> data = new HashMap<>();

    /**
     * 返回固定方法的格式
     *
     */
    public static APICODE OK() {
        APICODE apicode = new APICODE();
        apicode.setCode(ResultCode.SUCCESS);
        apicode.setMessage("调用成功");
        apicode.setSuccess(true);
        return apicode;
    }

    public static APICODE ERROR() {
        APICODE apicode = new APICODE();
        apicode.setCode(ResultCode.ERROR);
        apicode.setMessage("调用失败");
        apicode.setSuccess(false);
        return apicode;
    }

    /**
     * 自己设置返回的 code message data 等内容
     */
    public APICODE code(Integer code){
        this.setCode(code);
        return this;
    }

    public APICODE message(String message){
        this.setMessage(message);
        return this;
    }

    public APICODE success(){
        this.setSuccess(success);
        return this;
    }

    public APICODE data(String key,Object value) {
        this.data.put(key,value);
        return this;
    }

    public APICODE data(Map<String,Object> map) {
        this.setData(map);
        return this;
    }

}
