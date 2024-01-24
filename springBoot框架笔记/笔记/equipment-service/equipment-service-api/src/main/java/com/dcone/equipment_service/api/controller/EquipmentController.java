package com.dcone.equipment_service.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcone.equipment_service.common.model.po.EquipmentPo;
import com.dcone.equipment_service.facade.EquipmentFacade;
import com.dcone.equipment_service.sdk.http.req.EquipmentReqDto;
import com.dcone.equipment_service.sdk.http.req.PageReqDto;
import com.dcone.equipment_service.sdk.http.resp.DMLReultRespDto;
import com.dcone.equipment_service.sdk.http.resp.EquipmentRespDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bestjoy.cloud.core.bean.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName EquipmentController
 * @Author CodeDan
 * @Date 2022/7/18 9:55
 * @Version 1.0
 **/
@RestController
@RequestMapping("/equipment")
@Api(description = "电子设备接口")
public class EquipmentController {

    @Resource
    private EquipmentFacade equipmentFacade;


    /***
     * 使用分页获取当前页码的所有设备信息
     * @param pageReqDto 分页参数
     * @return 当前页码所有设备信息
     */
    @ApiOperation(httpMethod = "GET", value = "使用分页获取当前页码的所有设备信息")
    @GetMapping("/getAllEquipment")
    public Result<IPage<EquipmentRespDto>> getAllEquipment(@ModelAttribute PageReqDto pageReqDto){
        //去获取全部电子设备信息
        IPage<EquipmentRespDto> allEquipment = equipmentFacade.getAllEquipment(pageReqDto);
        //将其返回
        return Result.success(allEquipment);
    }

    /***
     * 使用分页获取当前页码下符合电子设备名称的所有设备信息
     * @param pageReqDto 分页参数
     * @return 当前页码符合电子设备名称的所有设备信息
     */
    @ApiOperation(httpMethod = "GET", value = "获取符合电子设备名称的所有设备信息")
    @GetMapping("/getEquipmentByName")
    public Result<IPage<EquipmentRespDto>> getEquipmentByName(@ModelAttribute PageReqDto pageReqDto){

        IPage<EquipmentRespDto> respDtoIPage = equipmentFacade.getEquipmentByName(pageReqDto);

        return Result.success(respDtoIPage);
    }

    /***
     * 使用id查询单个设备详细信息
     * @param reqDto 设备对象(ID)
     * @return
     */
    @ApiOperation(httpMethod = "GET", value = "使用id查询单个设备详细信息")
    @GetMapping("/getEquipmentById")
    public Result<EquipmentRespDto> getEquipmentById(@ModelAttribute EquipmentReqDto reqDto){

        EquipmentRespDto equipment = equipmentFacade.getEquipmentById(reqDto);
        return Result.success(equipment);

    }

    /***
     * 添加单个或者多个设备
     * @param reqDtos 设备集合请求
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "添加单个或者多个设备")
    @PostMapping("/addEquipment")
    public Result<DMLReultRespDto> addEquipment(@RequestBody List<EquipmentReqDto> reqDtos){

        DMLReultRespDto dmlReultRespDto = equipmentFacade.addEquipment(reqDtos);

        return Result.success(dmlReultRespDto);
    }


    /***
     * 根据单个或者多个id删除设备
     * @param reqDtos 包含id的设备对象集合
     * @return 删除结果
     */
    @ApiOperation(httpMethod = "POST", value = "根据单个或者多个id删除设备")
    @PostMapping("/deleteEquipmentById")
    public Result<DMLReultRespDto> deleteEquipment(@RequestBody List<EquipmentReqDto> reqDtos){
        DMLReultRespDto dmlReultRespDto = equipmentFacade.deleteEquipmentById(reqDtos);

        return Result.success(dmlReultRespDto);
    }

    /***
     * 根据单个或者多个id更新设备
     * @param reqDtos 设备对象集合
     * @return 更新结果
     */
    @ApiOperation(httpMethod = "POST", value = "根据单个或者多个id更新设备")
    @PostMapping("/updateEquipmentById")
    public Result<DMLReultRespDto> updateEquipmentById(@RequestBody List<EquipmentReqDto> reqDtos){
        DMLReultRespDto dmlReultRespDto = equipmentFacade.updateEquipmentById(reqDtos);

        return Result.success(dmlReultRespDto);


    }


}
