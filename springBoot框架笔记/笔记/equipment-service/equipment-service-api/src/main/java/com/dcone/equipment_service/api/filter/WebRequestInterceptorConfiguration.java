package com.dcone.equipment_service.api.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @ClassName WebRequestInterceptorConfiguration
 * @Author CodeDan
 * @Date 2022/7/18 15:07
 * @Version 1.0
 **/
@Configuration
public class WebRequestInterceptorConfiguration extends WebMvcConfigurerAdapter {

    @Resource
    private RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).addPathPatterns("/**");
    }
}
