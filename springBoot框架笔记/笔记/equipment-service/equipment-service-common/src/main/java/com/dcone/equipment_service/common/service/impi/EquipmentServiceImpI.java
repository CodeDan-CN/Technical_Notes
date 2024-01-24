package com.dcone.equipment_service.common.service.impi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcone.equipment_service.common.mapper.EquipmentMapper;
import com.dcone.equipment_service.common.mapper.EquipmentTypeMapper;
import com.dcone.equipment_service.common.model.po.EquipmentPo;
import com.dcone.equipment_service.common.service.EquipmentService;
import com.dcone.equipment_service.sdk.http.req.EquipmentReqDto;
import com.dcone.equipment_service.sdk.http.resp.EquipmentRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName EquipmentServiceImpI
 * @Author CodeDan
 * @Date 2022/7/18 10:01
 * @Version 1.0
 **/
@Service
@Slf4j
public class EquipmentServiceImpI extends ServiceImpl<EquipmentMapper, EquipmentPo>
        implements EquipmentService  {

    @Resource
    private EquipmentMapper equipmentMapper;

    public Page<EquipmentPo> getAllEquipment(int pageNum, int pageSize){
        Page<EquipmentPo> poPage = new Page<>(pageNum,pageSize);
        Page<EquipmentPo> allByEquipment = equipmentMapper.getAllByEquipment(poPage);
        return allByEquipment;
    }

    @Override
    public Page<EquipmentPo> getEquipmentByName(int pageNum, int pageSize, String equipmentName) {
        Page<EquipmentPo> poPage = new Page<>(pageNum,pageSize);
        Page<EquipmentPo> allByEquipment = equipmentMapper.getEquipmentByName(poPage,equipmentName);
        return allByEquipment;
    }

    @Override
    public EquipmentPo equipmentReqDtoToPo(EquipmentReqDto reqDto) {
        EquipmentPo equipmentPo = new EquipmentPo();
        BeanUtils.copyProperties(reqDto,equipmentPo);
        return equipmentPo;
    }

    @Override
    public int addEquipment(List<EquipmentPo> equipmentPos) {
        log.info("插入总条数：{}", equipmentPos.size());
        int successNum = equipmentMapper.addEquipment(equipmentPos);
        log.info("插入成功条数:{}",successNum);
        return successNum;
    }

    @Override
    public int deleteEquipmentById(List<EquipmentPo> equipmentPos) {
        log.info("删除总条数：{}", equipmentPos.size());
        int successNum = equipmentMapper.deleteByEquipmentId(equipmentPos);
        log.info("删除成功条数:{}",successNum);
        return successNum;
    }

    @Override
    public int updateEquipmentById(List<EquipmentPo> equipmentPos) {
        int successNum = equipmentMapper.updateByEquipmentId(equipmentPos);
        log.info("更新成功条数:{}",successNum);
        return successNum;
    }


}
