package com.dcone.equipment_service.api;

import net.bestjoy.cloud.web.starter.BaseWebBootstraper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName equipmentApplication
 * @Author CodeDan
 * @Date 2022/7/15 13:35
 * @Version 1.0
 **/

@SpringBootApplication
@ComponentScan(basePackages = "com.dcone")
@MapperScan("com.dcone.equipment_service.common.mapper")
public class equipmentApplication extends BaseWebBootstraper {
    public static void main(String[] args) {
        run(equipmentApplication.class);
    }
}
