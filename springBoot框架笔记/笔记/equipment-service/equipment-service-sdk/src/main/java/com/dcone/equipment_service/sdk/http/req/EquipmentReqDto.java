package com.dcone.equipment_service.sdk.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName EquipmentReqDto
 * @Author CodeDan
 * @Date 2022/7/18 9:49
 * @Version 1.0
 **/

@Data
@ApiModel("电子设备传入参数")
public class EquipmentReqDto {

    @ApiModelProperty("电子设备ID")
    private Integer equipmentId;


    @ApiModelProperty("电子设备名称")
    private String equipmentName;


    @ApiModelProperty("电子设备内存规格")
    private Integer equipmentMemory;


    @ApiModelProperty("电子设备存储规格")
    private Integer equipmentStorage;


    @ApiModelProperty("电子设备芯片信息")
    private String equipmentChip;


    @ApiModelProperty("电子设备简介")
    private String equipmentIntroduce;


    @ApiModelProperty("电子设备最低定价")
    private BigDecimal equipmentMoney;

    @ApiModelProperty("电子设备种类名称")
    private String equipmentTypeName;


}
