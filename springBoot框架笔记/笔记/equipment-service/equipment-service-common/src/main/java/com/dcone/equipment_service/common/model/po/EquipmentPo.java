package com.dcone.equipment_service.common.model.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName EquipmentPo
 * @Author CodeDan
 * @Date 2022/7/18 9:11
 * @Version 1.0
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_equipment")
@ApiModel(description = "equipment表")
public class EquipmentPo implements Serializable {

    private static final long serialVersionUID = 4028522073092013175L;

    @TableId(type = IdType.AUTO, value = "equipment_id")
    @ApiModelProperty("电子设备ID")
    private Integer equipmentId;

    @TableField("equipment_name")
    @ApiModelProperty("电子设备名称")
    private String equipmentName;

    @TableField("equipment_memory")
    @ApiModelProperty("电子设备内存规格")
    private Integer equipmentMemory;

    @TableField("equipment_storage")
    @ApiModelProperty("电子设备存储规格")
    private Integer equipmentStorage;

    @TableField("equipment_chip")
    @ApiModelProperty("电子设备芯片信息")
    private String equipmentChip;

    @TableField("equipment_introduce")
    @ApiModelProperty("电子设备简介")
    private String equipmentIntroduce;

    @TableField("equipment_money")
    @ApiModelProperty("电子设备最低定价")
    private BigDecimal equipmentMoney;

    @TableField(value = "equipment_createtime", fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("电子设备创建时间")
    private Date equipmentCreateTime;


    @TableField(value = "equipment_updatetime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("电子设备更新时间")
    private Date equipmentUpdateTime;

    @TableField(value = "equipment_status")
    @ApiModelProperty("电子设备失效状态")
    private Integer equipmentStatus;

    @TableField("equipment_isdelete")
    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("电子设备逻辑删除字段")
    private Integer equipmentDelete;

    @ApiModelProperty("电子设备种类")
    @TableField(exist = false)
    private EquipmentTypePo equipmentTypePo;


}
