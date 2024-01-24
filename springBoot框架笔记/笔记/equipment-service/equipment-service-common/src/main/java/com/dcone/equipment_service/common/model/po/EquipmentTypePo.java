package com.dcone.equipment_service.common.model.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName EquipmentType
 * @Author CodeDan
 * @Date 2022/7/15 14:15
 * @Version 1.0
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_type")
@ApiModel(description = "tb_type表")
public class EquipmentTypePo implements Serializable {

    private static final long serialVersionUID = 8679160897232225408L;

    @ApiModelProperty(value = "电子设备种类主键id")
    @TableId(type = IdType.AUTO,value = "type_id")
    private Integer typeId;


    @ApiModelProperty(value = "电子设备种类名称")
    @TableField(value = "type_name")
    private String typeName;

    @ApiModelProperty(value = "电子设备种类创建时间")
    @TableField(value = "type_createtime", fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date typeCreateTime;

    @ApiModelProperty(value = "电子设备种类更新时间")
    @TableField(value = "type_updatetime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date typeUpdateTime;

    @ApiModelProperty(value = "电子设备种类是否失效")
    @TableField(value = "type_status")
    private Integer typeStatus;

    @ApiModelProperty(value = "电子设备种类是否被删除")
    @TableField(value = "type_isdelete")
    @TableLogic(value = "0",delval = "1")
    private Integer typeDelete;


}