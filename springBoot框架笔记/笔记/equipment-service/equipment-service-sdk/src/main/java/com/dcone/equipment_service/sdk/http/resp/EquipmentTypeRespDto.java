package com.dcone.equipment_service.sdk.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName EquipmentTypeRespDto
 * @Author CodeDan
 * @Date 2022/7/15 13:59
 * @Version 1.0
 **/

@Data
@ApiModel("电子设备种类返回参数")
public class EquipmentTypeRespDto {

    @ApiModelProperty("电子设备种类唯一标识")
    private Integer typeId;

    @ApiModelProperty("电子设备种类名称")
    private String typeName;

    @ApiModelProperty(value = "电子设备种类创建时间")
    private Date typeCreateTime;

    @ApiModelProperty(value = "电子设备种类更新时间")
    private Date typeUpdateTime;

    @ApiModelProperty(value = "电子设备种类是否失效")
    private Integer typeStatus;

}
