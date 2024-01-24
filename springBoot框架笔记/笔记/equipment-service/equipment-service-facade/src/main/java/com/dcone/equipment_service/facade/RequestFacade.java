package com.dcone.equipment_service.facade;

import com.dcone.equipment_service.common.model.po.RequestPo;
import com.dcone.equipment_service.common.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName RequestFacade
 * @Author CodeDan
 * @Date 2022/7/18 16:13
 * @Version 1.0
 **/

@Service
@Slf4j
public class RequestFacade {

    @Resource
    private RequestService requestService;

    /***
     * 增加一个访问记录
     * @param ip 访问Ip
     * @param method 访问方式
     * @param url 访问url
     * @param parameter 访问携带参数
     */
    public void addGetBody(String ip, String method, String url, String parameter){
        RequestPo requestPo = requestService.getRequestPo(ip, method, url, parameter);

        requestService.save(requestPo);


    }

}
