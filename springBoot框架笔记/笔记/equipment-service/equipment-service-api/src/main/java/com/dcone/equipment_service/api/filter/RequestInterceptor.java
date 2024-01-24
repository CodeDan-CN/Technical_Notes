package com.dcone.equipment_service.api.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcone.equipment_service.api.util.BodyReaderHttpServletRequestWrapper;
import com.dcone.equipment_service.facade.RequestFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName RequestInterceptor
 * @Author CodeDan
 * @Date 2022/7/18 14:51
 * @Version 1.0
 **/

/**
 * 自定义拦截器
 * 拦截时机 Filter pre -> service -> dispatcher -> preHandle ->controller
 *  ->postHandle - > afterCompletion -> FilterAfter
 */
@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Resource
    private RequestFacade requestFacade;


    /**
     * 在controller处理之前首先对请求参数进行处理，以及对公共参数的保存
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进行preHandle");
        try{
            String method = request.getMethod();
            log.info("请求方式{}",method);
            String remoteAddr = request.getRemoteAddr();
            log.info("请求IP{}",remoteAddr);
            String requestMethord = request.getRequestURI();
            log.info("请求url{}",requestMethord);
            String body = null;
            if("GET".equals(method)){
                body = request.getQueryString();
                log.info("请求参数{}",body);

            }
            if("POST".equals(method)){
                //进行Post的请求获取
                body = new BodyReaderHttpServletRequestWrapper(request).getBody();
                log.info("请求参数{}",body);
            }
            requestFacade.addGetBody(remoteAddr,method,requestMethord,body);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("preHandle拦截成功");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
       log.info("进行postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
