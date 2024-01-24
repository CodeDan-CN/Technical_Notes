package cn.codedan.security.camelmergersecurity.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: MyRoute
 * @Description: TODO
 * @Author: codedan
 * @Date: 2022/12/12 21:43
 * @Version: 1.0.0
 **/
//@Component
public class NettyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
//        restConfiguration()
//                .component("netty-http")
//                .port("0.0.0.0")
//                .port(8080)
//                .bindingMode(RestBindingMode.auto);
//        from("rest:get:/hello"+"?matchOnUriPrefix=true&attachmentMultipartBinding=false")
//                .log("netty consumer.")
//                .process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        exchange.getIn().setBody(Map.of("success", "netty get request."));
//                    }
//                })
//        ;
//        from("rest:post:/hello"+"?matchOnUriPrefix=true&attachmentMultipartBinding=false")
//                .log("netty consumer.")
//                .process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        exchange.getIn().setBody(Map.of("success", "netty post request."));
//                    }
//                })
//        ;

                from("netty-http://0.0.0.0:8080/hello"+"?matchOnUriPrefix=true&attachmentMultipartBinding=false")
                .log("netty consumer.")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(Map.of("success", "netty post request."));
                    }
                });


    }
}