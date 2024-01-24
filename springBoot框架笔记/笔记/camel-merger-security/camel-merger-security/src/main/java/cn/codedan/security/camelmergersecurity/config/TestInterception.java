package cn.codedan.security.camelmergersecurity.config;

import cn.codedan.security.camelmergersecurity.interception.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: TestInterception
 * @Description: TODO
 * @Author: codedan
 * @Date: 2022/12/13 09:34
 * @Version: 1.0.0
 **/
//@Configuration
public class TestInterception implements WebMvcConfigurer {

    /**
     * 设置拦截路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**")
        ;
    }

    /**
     * 将拦截器注入context
     */
    @Bean
    public Interceptor authenticationInterceptor() {
        return new Interceptor();
    }
}
