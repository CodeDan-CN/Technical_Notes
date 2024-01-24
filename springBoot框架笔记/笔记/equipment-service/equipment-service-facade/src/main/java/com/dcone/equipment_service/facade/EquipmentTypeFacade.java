package com.dcone.equipment_service.facade;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @ClassName EquipmentTypeFacade
 * @Author CodeDan
 * @Date 2022/7/15 14:12
 * @Version 1.0
 **/

@Service
@Slf4j
public class EquipmentTypeFacade {

    @Resource
    private EquipmentTypeService equipmentTypeService;

    /***
     * 获取全部电子设备种类
     * @return 电子设备种类集合
     */
    public List<EquipmentTypeRespDto> getAllType(){
        List<EquipmentTypeRespDto> allTypeDto = equipmentTypeService.getAllType();
        log.info("获取到数据");
        return allTypeDto;
    }

    /***
     * 获取指定电子设备种类信息
     * @param reqDto 电子设备种类传入参数
     * @return 指定指点设备种类信息
     */
    public EquipmentTypeRespDto getTypeById(EquipmentTypeReqDto reqDto){
        EquipmentTypePo equipmentTypePo = equipmentTypeService.equipmentTypeDtoToPo(reqDto);
        Integer typeId = equipmentTypePo.getTypeId();
        log.info("查询id为：{}",typeId);
        EquipmentTypePo type = equipmentTypeService.getById(typeId);
        log.info("获取到结果：{}",type);
        EquipmentTypeRespDto equipmentTypeRespDto = new EquipmentTypeRespDto();
        if( type != null ){
            BeanUtils.copyProperties(type,equipmentTypeRespDto);
        }
        return equipmentTypeRespDto;
    }

    /***
     * 通过id删除指定电子设备种类
     * @param reqDto 电子设备种类传入参数
     */
    public void deleteTypeById(EquipmentTypeReqDto reqDto){
        EquipmentTypePo equipmentTypePo = equipmentTypeService.equipmentTypeDtoToPo(reqDto);
        Integer typeId = equipmentTypePo.getTypeId();
        log.info("删除id为：{}",typeId);
        boolean flag = equipmentTypeService.removeById(typeId);
        if(!flag){
            log.error("删除失败，typeid为:{}",typeId);
        }
    }

    /***
     * 通过Name删除指定电子设备种类
     * @param reqDto
     */
    public void deleteTypeByName(EquipmentTypeReqDto reqDto){
        EquipmentTypePo equipmentTypePo = equipmentTypeService.equipmentTypeDtoToPo(reqDto);
        String typeName = equipmentTypePo.getTypeName();
        log.info("删除name为：{}",typeName);
        QueryWrapper<EquipmentTypePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EquipmentTypePo::getTypeName,typeName);
        boolean flag = equipmentTypeService.remove(queryWrapper);
        if(!flag){
            log.error("删除失败，typeName为:{}",typeName);
        }
    }

    /***
     * 通过多个id批量删除电子设备种类
     * @param reqDtos
     */
    public void delteTypeByIdList(List<EquipmentTypeReqDto> reqDtos){
        List<EquipmentTypePo> typeVos = equipmentTypeService.equipmentTybeDtoToPoList(reqDtos);
        QueryWrapper<EquipmentTypePo> queryWrapper = new QueryWrapper<>();
        List<Integer> ids = new ArrayList<>();
        for( EquipmentTypePo equipmentTypePo : typeVos ){
            ids.add(equipmentTypePo.getTypeId());
        }
        queryWrapper.lambda().in(EquipmentTypePo::getTypeId,ids);
        equipmentTypeService.remove(queryWrapper);
    }

    /***
     * 通过单个/多个电子设备种类对象进行数据库插入
     * @param reqDtos
     */
    public void addTypeList(List<EquipmentTypeReqDto> reqDtos){
        List<EquipmentTypePo> typePos = equipmentTypeService.equipmentTybeDtoToPoList(reqDtos);
        equipmentTypeService.saveBatch(typePos);
    }

    /***
     * 通过单个/多个电子设备种类对象进行数据修改
     * @param reqDtos
     */
    public void updateTypeList(List<EquipmentTypeReqDto> reqDtos){
        List<EquipmentTypePo> typePos = equipmentTypeService.equipmentTybeDtoToPoList(reqDtos);
        equipmentTypeService.updateBatchById(typePos);
    }

}
