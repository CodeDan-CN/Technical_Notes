package cn.codedan.security.camelmergersecurity.route;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.catalina.connector.ResponseFacade;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class ServletRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

//        restConfiguration().component("servlet").host("0.0.0.0").port(8080);
//        from("rest:get:/hello"+"?matchOnUriPrefix=true&attachmentMultipartBinding=false")
//                .log("input:${body}")
//                .process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        exchange.getIn().setBody(Map.of("success", "servlet get request."));
//                    }
//                })
//                .log("input:${body}");
        from("servlet://hello"+"?matchOnUriPrefix=true&attachmentMultipartBinding=false&async=false")
                .log("input:${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        // 获取请求中的响应消息，并设置到properties中
                        HttpServletResponse keycloakResp = exchange.getIn(HttpServletResponse.class);
                        exchange.setProperty("keycloakResp", keycloakResp);
                    }
                })
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        ResponseFacade facade = (ResponseFacade) exchange.getAllProperties().get("keycloakResp");
                        HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);
                        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, null);

                        exchange.getIn().setBody(Map.of("success", "servlet get request."));
                        TimeUnit.SECONDS.sleep(5);
                    }
                })
                .log("input:${body}");
    }
}