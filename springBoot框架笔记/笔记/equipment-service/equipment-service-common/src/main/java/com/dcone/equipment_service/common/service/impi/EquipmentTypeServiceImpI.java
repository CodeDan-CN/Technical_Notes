package com.dcone.equipment_service.common.service.impi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcone.equipment_service.common.mapper.EquipmentTypeMapper;
import com.dcone.equipment_service.common.model.po.EquipmentTypePo;
import com.dcone.equipment_service.common.service.EquipmentTypeService;
import com.dcone.equipment_service.sdk.http.req.EquipmentTypeReqDto;
import com.dcone.equipment_service.sdk.http.resp.EquipmentTypeRespDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName EuuipmentTypeService
 * @Author CodeDan
 * @Date 2022/7/15 14:36
 * @Version 1.0
 **/
@Service
@Slf4j
public class EquipmentTypeServiceImpI
        extends ServiceImpl<EquipmentTypeMapper, EquipmentTypePo>
        implements EquipmentTypeService {

    @Resource
    private EquipmentTypeMapper equipmentTypeMapper;

    public List<EquipmentTypeRespDto> getAllType(){
        log.info("开始获取数据");
        QueryWrapper<EquipmentTypePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(EquipmentTypePo::getTypeId,EquipmentTypePo::getTypeName);
        List<EquipmentTypePo> equipmentTypePos = equipmentTypeMapper.selectList(queryWrapper);
        List<EquipmentTypeRespDto> respDtos = new ArrayList<>();
        for( EquipmentTypePo equipmentTypePo : equipmentTypePos ){
            EquipmentTypeRespDto equipmentTypeRespDto = new EquipmentTypeRespDto();
            BeanUtils.copyProperties(equipmentTypePo,equipmentTypeRespDto);
            respDtos.add(equipmentTypeRespDto);
        }
        return respDtos;
    }

    @Override
    public EquipmentTypePo equipmentTypeDtoToPo(EquipmentTypeReqDto reqDto) {
        log.info("前端传来数据:{}",reqDto);
        EquipmentTypePo equipmentTypePo = new EquipmentTypePo();
        equipmentTypePo.setTypeId(reqDto.getTypeId());
        equipmentTypePo.setTypeName(reqDto.getTypeName());
        equipmentTypePo.setTypeCreateTime(reqDto.getTypeCreateTime());
        equipmentTypePo.setTypeUpdateTime(reqDto.getTypeUpdateTime());
        equipmentTypePo.setTypeStatus(reqDto.getTypeStatus());
        return equipmentTypePo;
    }

    @Override
    public List<EquipmentTypePo> equipmentTybeDtoToPoList(List<EquipmentTypeReqDto> reqDtos) {
        log.info("前端传来数据:{}",reqDtos);
        List<EquipmentTypePo> typeVos = new ArrayList<>();
        for( EquipmentTypeReqDto reqDto : reqDtos ){
            EquipmentTypePo equipmentTypePo = new EquipmentTypePo();
            BeanUtils.copyProperties(reqDto,equipmentTypePo);
            typeVos.add(equipmentTypePo);
        }
        return typeVos;
    }

}
