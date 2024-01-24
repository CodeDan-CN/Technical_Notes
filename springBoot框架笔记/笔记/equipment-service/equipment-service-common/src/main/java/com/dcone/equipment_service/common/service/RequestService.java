package com.dcone.equipment_service.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dcone.equipment_service.common.model.po.RequestPo;

/**
 * @ClassName RequestService
 * @Author CodeDan
 * @Date 2022/7/18 16:07
 * @Version 1.0
 **/
public interface RequestService extends IService<RequestPo> {


    /***
     * 将访问参数封装为一个RequestPO对象
     * @param ip 访问Ip
     * @param method 访问方式
     * @param url 访问url
     * @param parameter 访问携带参数
     * @return
     */
    public RequestPo getRequestPo(String ip, String method, String url, String parameter);

}
