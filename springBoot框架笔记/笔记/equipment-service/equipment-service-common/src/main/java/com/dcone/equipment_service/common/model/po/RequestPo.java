package com.dcone.equipment_service.common.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName RequestPo
 * @Author CodeDan
 * @Date 2022/7/18 15:52
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_request_log")
@ApiModel(description = "tb_request_log表")
public class RequestPo implements Serializable {

    private static final long serialVersionUID = 7734045375941469700L;

    @ApiModelProperty("请求标识")
    @TableField("request_id")
    private Integer requestId;

    @ApiModelProperty("请求IP")
    @TableField("request_ip")
    private String requestIp;

    @ApiModelProperty("请求方式")
    @TableField("request_method")
    private String requestMethod;

    @ApiModelProperty("请求url")
    @TableField("request_url")
    private String requestUrl;

    @ApiModelProperty("请求参数")
    @TableField("request_parameter")
    private String requestParameter;

    @ApiModelProperty("请求时间")
    @TableField(value = "request_createtime", fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requestCreateTime;
}
