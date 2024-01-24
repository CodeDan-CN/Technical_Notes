package com.dcone.equipment_service.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dcone.equipment_service.common.model.po.EquipmentTypePo;
import com.dcone.equipment_service.sdk.http.req.EquipmentTypeReqDto;
import com.dcone.equipment_service.sdk.http.resp.EquipmentTypeRespDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName EquipmentTypeService
 * @Author CodeDan
 * @Date 2022/7/15 14:43
 * @Version 1.0
 **/
public interface EquipmentTypeService extends IService<EquipmentTypePo> {


    /***
     * 获取全部电子设备种类信息
     * @return 电子设备种类
     */
    public List<EquipmentTypeRespDto> getAllType();

    /***
     * 将电子设备种类信息DTO转为VO
     * @param reqDto 电子设备种类信息DTO
     * @return 电子设备种类信息VO
     */
    public EquipmentTypePo equipmentTypeDtoToPo(EquipmentTypeReqDto reqDto);

    /***
     * 将电子设备种类信息DTOList转为VoList
     * @param reqDtos
     * @return
     */
    public List<EquipmentTypePo> equipmentTybeDtoToPoList(List<EquipmentTypeReqDto> reqDtos);




}
