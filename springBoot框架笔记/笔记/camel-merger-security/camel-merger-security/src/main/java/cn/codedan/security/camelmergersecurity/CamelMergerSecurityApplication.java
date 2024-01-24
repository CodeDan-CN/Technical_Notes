package cn.codedan.security.camelmergersecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync
public class CamelMergerSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamelMergerSecurityApplication.class, args);
    }

}
