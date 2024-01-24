package com.dcone.equipment_service.sdk.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName EquipmentTypeReqDto
 * @Author CodeDan
 * @Date 2022/7/15 15:30
 * @Version 1.0
 **/

@Data
@ApiModel(value = "电子设备种类传入参数")
public class EquipmentTypeReqDto {

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
