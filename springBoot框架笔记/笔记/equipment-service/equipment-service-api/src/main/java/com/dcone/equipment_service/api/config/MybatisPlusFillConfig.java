package com.dcone.equipment_service.api.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName MybatisPlusFillConfig
 * @Author CodeDan
 * @Date 2022/7/17 16:56
 * @Version 1.0
 **/

@Slf4j
@Component
public class MybatisPlusFillConfig implements MetaObjectHandler {
    /***
     * 插入时要填充字段值
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入开始填充");
        this.strictInsertFill(metaObject ,"typeCreateTime", Date.class, new Date());
        this.strictInsertFill(metaObject ,"typeUpdateTime", Date.class, new Date());
        this.strictInsertFill(metaObject ,"equipmentCreateTime", Date.class, new Date());
        this.strictInsertFill(metaObject ,"equipmentUpdateTime", Date.class, new Date());
        this.strictInsertFill(metaObject ,"requestCreateTime", Date.class, new Date());

    }

    /***
     * 更新时要填充字段值
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("更新开始填充");
        this.strictInsertFill(metaObject ,"typeUpdateTime", Date.class, new Date());
        this.strictInsertFill(metaObject ,"equipmentUpdateTime", Date.class, new Date());
    }
}
