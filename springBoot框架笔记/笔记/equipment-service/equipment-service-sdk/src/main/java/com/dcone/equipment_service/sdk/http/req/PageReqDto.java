package com.dcone.equipment_service.sdk.http.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName PageReqDto
 * @Author CodeDan
 * @Date 2022/7/18 11:28
 * @Version 1.0
 **/

@Data
@ApiModel(value = "查询分页传入参数")
public class PageReqDto {

    @ApiModelProperty("电子设备种类唯一标识")
    private Integer typeId;

    @ApiModelProperty("电子设备名称")
    private String equipmentName;

    @ApiModelProperty("当前页面页码")
    private Integer pageNum;

    @ApiModelProperty("页码中记录条数")
    private Integer pageSize;

}
