package com.dcone.equipment_service.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dcone.equipment_service.common.model.po.EquipmentPo;
import com.dcone.equipment_service.common.model.po.EquipmentTypePo;
import com.dcone.equipment_service.sdk.http.req.EquipmentReqDto;
import com.dcone.equipment_service.sdk.http.req.EquipmentTypeReqDto;

import java.util.List;

/**
 * @ClassName EquipmentService
 * @Author CodeDan
 * @Date 2022/7/18 10:00
 * @Version 1.0
 **/

public interface EquipmentService extends IService<EquipmentPo> {

    /***
     * 获取全部电子设备信息
     * @return 电子设备信息集合
     */
    public Page<EquipmentPo> getAllEquipment(int pageNum, int pageSize);

    /***
     * 获取匹配设备名称的设备信息集合
     * @param pageNum 当前页码
     * @param pageSize 每页显示记录数量
     * @param equipmentName 设备名称
     * @return 匹配的设备信息集合
     */
    public Page<EquipmentPo> getEquipmentByName(int pageNum, int pageSize, String equipmentName);


    /***
     * 将电子设备信息DTO转为PO
     * @param reqDto 电子设备种类信息DTO
     * @return 电子设备种类信息PO
     */
    public EquipmentPo equipmentReqDtoToPo(EquipmentReqDto reqDto);


    /***
     * 批量添加电子设备
     * @param equipmentPos 电子设备集合
     * @return 返回插入成功条数
     */
    public int addEquipment(List<EquipmentPo> equipmentPos);


    /***
     * 根据id批量删除电子设备
     * @param equipmentPos 包含id的电子设备集合
     * @return 返回删除成功条数
     */
    public int deleteEquipmentById(List<EquipmentPo> equipmentPos);


    /***
     * 根据id批量更新电子设备
     * @param equipmentPos 电子设备集合
     * @return 返回更新成功条数
     */
    public int updateEquipmentById(List<EquipmentPo> equipmentPos);


}
