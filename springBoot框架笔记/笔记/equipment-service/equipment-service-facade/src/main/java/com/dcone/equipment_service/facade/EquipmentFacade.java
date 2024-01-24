package com.dcone.equipment_service.facade;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dcone.equipment_service.common.model.po.EquipmentPo;
import com.dcone.equipment_service.common.model.po.EquipmentTypePo;
import com.dcone.equipment_service.common.service.EquipmentService;
import com.dcone.equipment_service.common.service.EquipmentTypeService;
import com.dcone.equipment_service.sdk.http.req.EquipmentReqDto;
import com.dcone.equipment_service.sdk.http.req.PageReqDto;
import com.dcone.equipment_service.sdk.http.resp.DMLReultRespDto;
import com.dcone.equipment_service.sdk.http.resp.EquipmentRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName EquipmentFacade
 * @Author CodeDan
 * @Date 2022/7/18 9:56
 * @Version 1.0
 **/
@Service
@Slf4j
public class EquipmentFacade {

    @Resource
    private EquipmentService equipmentService;

    @Resource
    private EquipmentTypeService equipmentTypeService;


    /***
     * 获取全部电子设备信息
     * @param reqDto 分页传入参数
     * @return 当前页码包括电子设备信息
     */
    public IPage<EquipmentRespDto> getAllEquipment(PageReqDto reqDto){

        Page<EquipmentPo> allEquipment = equipmentService.getAllEquipment(reqDto.getPageNum(), reqDto.getPageSize());
        //最终返回DTO对象
        IPage<EquipmentRespDto> respDtoIPage = allEquipment.convert(result ->{
            EquipmentRespDto equipmentRespDto = new EquipmentRespDto();
            BeanUtils.copyProperties(result, equipmentRespDto);
            Integer typeId = result.getEquipmentTypePo().getTypeId();
            String typeName = result.getEquipmentTypePo().getTypeName();
            equipmentRespDto.setEquipmentTypeId(typeId);
            equipmentRespDto.setEquipmentTypeName(typeName);
            return equipmentRespDto;
        });

        return respDtoIPage;
    }


    /***
     * 通过设备名称匹配对应电子设备信息
     * @param pageReqDto 设备对象
     * @return 匹配的电子设备信息集合
     */
    public IPage<EquipmentRespDto> getEquipmentByName(PageReqDto pageReqDto){
        Page<EquipmentPo> allEquipment = equipmentService.getEquipmentByName(pageReqDto.getPageNum(), pageReqDto.getPageSize(), pageReqDto.getEquipmentName());
        IPage<EquipmentRespDto> respDtoIPage = allEquipment.convert(result ->{
            EquipmentRespDto equipmentRespDto = new EquipmentRespDto();
            BeanUtils.copyProperties(result, equipmentRespDto);
            Integer typeId = result.getEquipmentTypePo().getTypeId();
            String typeName = result.getEquipmentTypePo().getTypeName();
            equipmentRespDto.setEquipmentTypeId(typeId);
            equipmentRespDto.setEquipmentTypeName(typeName);
            return equipmentRespDto;
        });
        return respDtoIPage;
    }

    /***
     * 通过设备id获取指定电子设备的详细信息
     * @param reqDto 包含设备id的设备对象
     * @return 设备详细信息
     */
    public EquipmentRespDto getEquipmentById(EquipmentReqDto reqDto){
        EquipmentPo equipmentPo = equipmentService.equipmentReqDtoToPo(reqDto);
        log.info("转化PO对象:{}",equipmentPo);
        EquipmentPo resultEquipment = equipmentService.getById(equipmentPo.getEquipmentId());
        EquipmentRespDto equipmentRespDto = new EquipmentRespDto();
        BeanUtils.copyProperties(resultEquipment,equipmentRespDto);
        return equipmentRespDto;
    }

    /***
     * 添加单个或者多个电子设备
     * @param reqDtos 电子设备集合
     * @return 插入结果
     */
    public DMLReultRespDto addEquipment(List<EquipmentReqDto> reqDtos){
        Iterator<EquipmentReqDto> iterator = reqDtos.iterator();
        List<EquipmentPo> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createAndUpdateTime = dateFormat.format(new Date());
        Date date = null;
        try {
            date = dateFormat.parse(createAndUpdateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        while(iterator.hasNext()){
            EquipmentPo equipmentPo = new EquipmentPo();
            EquipmentReqDto equipmentReqDto = iterator.next();
            QueryWrapper<EquipmentTypePo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().select(EquipmentTypePo::getTypeId).eq(EquipmentTypePo::getTypeName,equipmentReqDto.getEquipmentTypeName());
            EquipmentTypePo one = equipmentTypeService.getOne(queryWrapper);
            BeanUtils.copyProperties(equipmentReqDto,equipmentPo);
            equipmentPo.setEquipmentTypePo(one);
            equipmentPo.setEquipmentCreateTime(date);
            equipmentPo.setEquipmentUpdateTime(date);
            list.add(equipmentPo);
        }
        //处理完数据，可以开始批量插入了
        log.info("处理之后的数据为{}",list);
        DMLReultRespDto dmlReultRespDto = new DMLReultRespDto();
        int total = list.size();
        dmlReultRespDto.setTotal(total);
        int successNum = equipmentService.addEquipment(list);
        dmlReultRespDto.setSuccessNum(successNum);
        dmlReultRespDto.setFailNum(total - successNum);
        return dmlReultRespDto;
    }

    /***
     * 根据id进行设备的删除
     * @param reqDtos 包含id的设备对象集合
     * @return 删除情况
     */
    public DMLReultRespDto deleteEquipmentById(List<EquipmentReqDto> reqDtos ){
        Iterator<EquipmentReqDto> iterator = reqDtos.iterator();
        List<EquipmentPo> list = new ArrayList<>();
        while(iterator.hasNext()){
            EquipmentReqDto equipmentReqDto = iterator.next();
            EquipmentPo equipmentPo = equipmentService.equipmentReqDtoToPo(equipmentReqDto);
            list.add(equipmentPo);
        }
        int successNum = equipmentService.deleteEquipmentById(list);
        DMLReultRespDto dmlReultRespDto = new DMLReultRespDto();
        int total = list.size();
        dmlReultRespDto.setTotal(total);
        dmlReultRespDto.setSuccessNum(successNum);
        dmlReultRespDto.setFailNum(total - successNum);
        return dmlReultRespDto;
    }

    /***
     * 根据单个或者多个id进行单次或者批量更新
     * @param reqDtos 设备对象集合
     * @return 更新结果
     */
    public DMLReultRespDto updateEquipmentById(List<EquipmentReqDto> reqDtos){
        Iterator<EquipmentReqDto> iterator = reqDtos.iterator();
        List<EquipmentPo> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createAndUpdateTime = dateFormat.format(new Date());
        Date date = null;
        //总更新条数
        int total = reqDtos.size();
        int failNum = 0;
        try {
            date = dateFormat.parse(createAndUpdateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        while(iterator.hasNext()){
            EquipmentTypePo one = null;
            EquipmentPo equipmentPo = new EquipmentPo();
            EquipmentReqDto equipmentReqDto = iterator.next();
            BeanUtils.copyProperties(equipmentReqDto,equipmentPo);
            QueryWrapper<EquipmentTypePo> queryWrapper = new QueryWrapper<>();

            if( equipmentReqDto != null && equipmentReqDto.getEquipmentTypeName() != null && equipmentReqDto.getEquipmentTypeName() != "" ){
                queryWrapper.lambda().eq(EquipmentTypePo::getTypeName,equipmentReqDto.getEquipmentTypeName());
                one = equipmentTypeService.getOne(queryWrapper);
                if( one == null ){
                    log.error("此条设备种类有问题，无法插入:{}",equipmentReqDto);
                    failNum++;
                    continue;
                }
                else{
                    equipmentPo.setEquipmentTypePo(one);
                }
            }else{
                equipmentPo.setEquipmentTypePo(one);
            }
            equipmentPo.setEquipmentUpdateTime(date);
            list.add(equipmentPo);

        }
        //进行修改
        int successNum = 0;
        if( total > 0  ){
            successNum = equipmentService.updateEquipmentById(list);
        }
        DMLReultRespDto dmlReultRespDto = new DMLReultRespDto();
        dmlReultRespDto.setTotal(total);
        //批量查询成功结果为1，0，-1
        if( successNum == 1 ){
            dmlReultRespDto.setSuccessNum(total - failNum);
        }
        dmlReultRespDto.setFailNum(failNum);
        return dmlReultRespDto;

    }


}
