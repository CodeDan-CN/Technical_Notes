package com.dcone.equipment_service.api.filter;

import com.dcone.equipment_service.api.util.BodyReaderHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.FileFilter;
import java.io.IOException;

/**
 * @ClassName MyWebFilter
 * @Author CodeDan
 * @Date 2022/7/18 14:32
 * @Version 1.0
 **/
@WebFilter(filterName = "mywebfilter", urlPatterns = "/*")
@Component
@Slf4j
public class MyWebFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("mywebfilter过滤器进行初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("mywebfilter过滤器进行过滤操作");
        //为了防止流读出之后，Controller层无法进行流的读取。所以需要将流继续写出去
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //写入操作
        BodyReaderHttpServletRequestWrapper bodyReaderHttpServletRequestWrapper = new BodyReaderHttpServletRequestWrapper((httpServletRequest));

        //进行替换
        chain.doFilter(bodyReaderHttpServletRequestWrapper,response);

    }

    @Override
    public void destroy() {
        log.info("mywebfilter过滤器销毁完毕");
    }
}
