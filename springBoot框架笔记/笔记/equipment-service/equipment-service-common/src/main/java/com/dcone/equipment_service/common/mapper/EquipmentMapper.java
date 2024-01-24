package com.dcone.equipment_service.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dcone.equipment_service.common.model.po.EquipmentPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName EquipmentMapper
 * @Author CodeDan
 * @Date 2022/7/18 9:45
 * @Version 1.0
 **/

@Mapper
@Repository
public interface EquipmentMapper extends BaseMapper<EquipmentPo> {

    /***
     * 获取全部电子设备信息(包括种类)
     * @param poPage 分页对象
     * @return 电子设备信息集合
     */
    public Page<EquipmentPo> getAllByEquipment(Page<EquipmentPo> poPage);


    /***
     * 根据名称获取对应设备名称的设备集合
     * @param poPage 分页参数
     * @param EquipmentName 设备名称
     * @return 匹配设备集合
     */
    public Page<EquipmentPo> getEquipmentByName(Page<EquipmentPo> poPage, @Param("equipmentName") String EquipmentName);


    /***
     * 添加单个或者多个设备
     * @param equipmentPos 设备对象集合
     * @return 添加成功条数
     */
    public int addEquipment(List<EquipmentPo> equipmentPos);

    /***
     * 根据单个或者多个id删除设备
     * @param equipmentPos 包含id的设备对象集合
     * @return 删除成功条数
     */
    public int deleteByEquipmentId(List<EquipmentPo> equipmentPos);


    /***
     * 根据单个id或者多个id更新单个或者多个设备
     * @param equipmentPos 设备对象集合
     * @return 更新成功条数
     */
    public int updateByEquipmentId(List<EquipmentPo> equipmentPos);



}
