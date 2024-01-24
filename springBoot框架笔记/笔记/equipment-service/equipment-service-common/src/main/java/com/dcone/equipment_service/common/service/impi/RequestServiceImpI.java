package com.dcone.equipment_service.common.service.impi;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcone.equipment_service.common.mapper.RequestMapper;
import com.dcone.equipment_service.common.model.po.RequestPo;
import com.dcone.equipment_service.common.service.RequestService;
import org.springframework.stereotype.Service;

/**
 * @ClassName RequestService
 * @Author CodeDan
 * @Date 2022/7/18 16:06
 * @Version 1.0
 **/
@Service
public class RequestServiceImpI extends ServiceImpl<RequestMapper, RequestPo>
        implements RequestService {


    @Override
    public RequestPo getRequestPo(String ip, String method, String url, String parameter) {
        RequestPo requestPo = new RequestPo();
        requestPo.setRequestIp(ip);
        requestPo.setRequestMethod(method);
        requestPo.setRequestUrl(url);
        requestPo.setRequestParameter(parameter);
        return requestPo;
    }
}
