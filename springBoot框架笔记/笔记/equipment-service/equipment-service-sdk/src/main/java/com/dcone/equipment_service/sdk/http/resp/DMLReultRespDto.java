package com.dcone.equipment_service.sdk.http.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName DMLReultRespDto
 * @Author CodeDan
 * @Date 2022/7/18 17:22
 * @Version 1.0
 **/
@Data
@ApiModel("批量DML语句成功失败传出参数")
public class DMLReultRespDto {

    @ApiModelProperty("操作总条数")
    private Integer total;

    @ApiModelProperty("成功次数")
    private Integer successNum;

    @ApiModelProperty("失败次数")
    private Integer failNum;
}
