package com.dcone.equipment_service.api.controller;

import com.dcone.equipment_service.facade.EquipmentTypeFacade;
import com.dcone.equipment_service.sdk.http.req.EquipmentTypeReqDto;
import com.dcone.equipment_service.sdk.http.resp.EquipmentTypeRespDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bestjoy.cloud.core.bean.Result;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName EquipmentController
 * @Author CodeDan
 * @Date 2022/7/15 14:05
 * @Version 1.0
 **/

@Api(description = "电子设备种类接口")
@RestController
@RequestMapping("/type")
public class EquipmentTypeController {

    @Resource
    private EquipmentTypeFacade equipmentTypeFacade;

    /***
     * 查询全部的电子设备种类
     * @return 电子设备种类集合
     */
    @ApiOperation(httpMethod = "GET", value = "获取全部电子设备种类")
    @GetMapping("/getAllType")
    public Result<List<EquipmentTypeRespDto>> getAllTyp(){
        //去获取全部电子设备种类集合
        List<EquipmentTypeRespDto> allType = equipmentTypeFacade.getAllType();
        //将结果返回
        return Result.success(allType);

    }

    /***
     * 通过id查询指定电子设备种类
     * @param equipmentTypeReqDto 电子设备种类传入参数
     * @return 指定电子设备种类信息
     */
    @ApiOperation(httpMethod = "GET", value = "获取单个电子设备种类信息")
    @GetMapping("/getTypeById")
    public Result<EquipmentTypeRespDto> getTypeById(@ModelAttribute EquipmentTypeReqDto equipmentTypeReqDto){
        //传入参数
        EquipmentTypeRespDto typeDto = equipmentTypeFacade.getTypeById(equipmentTypeReqDto);
        //返回结果
        return Result.success(typeDto);
    }

    /***
     * 通过id删除指定电子设备种类
     * @param reqDto 电子设备种类传入参数
     * @return 删除结果
     */
    @ApiOperation(httpMethod = "GET", value = "根据种类ID删除指定种类")
    @GetMapping("/deleteTypeById")
    public Result deleteTypeById(@ModelAttribute EquipmentTypeReqDto reqDto){
        //传入删除种类Id参数
        equipmentTypeFacade.deleteTypeById(reqDto);
        //返回结果
        return Result.success();
    }

    /***
     * 通过typeName删除指定电子设备种类
     * @param reqDto 电子设备种类传入参数
     * @return
     */
    @ApiOperation(httpMethod = "GET", value = "根据种类Name删除指定种类")
    @GetMapping("/deleteTypeByName")
    public Result deleteTypeByName(@ModelAttribute EquipmentTypeReqDto reqDto){
        //传入删除种类Name参数
        equipmentTypeFacade.deleteTypeByName(reqDto);
        //返回结果
        return Result.success();
    }

    /***
     * 通过多个id批量删除电子设备种类
     * @param reqDtos 电子设备种类传入参数集合
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "通过多个id批量删除电子设备种类")
    @PostMapping("/delteTypeByIdList")
    public Result deleteTypeByIdList(@RequestBody List<EquipmentTypeReqDto> reqDtos){
        //传入删除种类Name集合参数
        equipmentTypeFacade.delteTypeByIdList(reqDtos);
        //返回结果
        return Result.success();
    }

    /***
     * 通过单个或者多个电子设备种类对象进行添加
     * @param reqDtos 电子设备种类传入参数集合
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "通过单个或者多个电子设备种类对象进行添加")
    @PostMapping("/addTypeList")
    public Result addTypeList( @RequestBody List<EquipmentTypeReqDto> reqDtos ){
        //传入增加种类的对象集合
        equipmentTypeFacade.addTypeList(reqDtos);
        //返回结果
        return Result.success();
    }

    /***
     * 通过单个或者多个电子设备种类对象进行修改
     * @param reqDtos 电子设备种类传入参数集合
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "通过单个或者多个电子设备种类对象进行修改")
    @PostMapping("/updateTypeList")
    public Result updateTypeList( @RequestBody List<EquipmentTypeReqDto> reqDtos ){
        //传入增加种类的对象集合
        equipmentTypeFacade.updateTypeList(reqDtos);
        //返回结果
        return Result.success();
    }




}
