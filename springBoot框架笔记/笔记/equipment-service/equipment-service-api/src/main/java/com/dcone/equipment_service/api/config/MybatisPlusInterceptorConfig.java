package com.dcone.equipment_service.api.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MybatisPlusInterceptorConfig
 * @Author CodeDan
 * @Date 2022/7/17 20:53
 * @Version 1.0
 **/
@Configuration
public class MybatisPlusInterceptorConfig {

    /***
     * 配置Mybatis-Plus拦截器中设置分页拦截
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
