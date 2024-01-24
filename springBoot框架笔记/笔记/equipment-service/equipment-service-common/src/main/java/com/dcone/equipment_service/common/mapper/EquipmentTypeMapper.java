package com.dcone.equipment_service.common.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcone.equipment_service.common.model.po.EquipmentTypePo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName EquipmentTypeMapper
 * @Author CodeDan
 * @Date 2022/7/15 14:45
 * @Version 1.0
 **/
@Mapper
@Repository
public interface EquipmentTypeMapper extends BaseMapper<EquipmentTypePo> {

}
