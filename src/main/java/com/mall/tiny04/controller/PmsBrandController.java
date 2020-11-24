package com.mall.tiny04.controller;

import com.mall.tiny04.common.CommonPage;
import com.mall.tiny04.common.CommonResult;
import com.mall.tiny04.mbg.model.PmsBrand;
import com.mall.tiny04.service.PmsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌管理 controller
 * 商品品牌管理
 * PmsBrandController
 */
@Api(tags = "PmsBrandController",description = "品牌管理")
@Controller
@RequestMapping("/brand")
public class PmsBrandController {

    @Autowired
    private PmsBrandService brandService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmsBrandController.class);

    ///
    @ApiOperation("获取所有品牌列表")
    @PreAuthorize("hasAuthority('pms:brand:read')")
    @GetMapping("/listAll")
    @ResponseBody
    public CommonResult<List<PmsBrand>> getBrandList(){
        return CommonResult.success(brandService.listAllBrand());
    }

    //
    @ApiOperation("添加品牌")
    @PreAuthorize("hasAuthority('pms:brand:create')")
    @PostMapping("/create")
    @ResponseBody
    public CommonResult createBrand(@RequestBody PmsBrand pmsBrand){
        CommonResult commonResult;
        int count = brandService.createBrand(pmsBrand);
        if (count == 1) {
            commonResult = CommonResult.success(pmsBrand);
            LOGGER.debug("Create brand seccess:{}"+pmsBrand);
        }else {
            commonResult = CommonResult.failed("操作失败");
            LOGGER.debug("Create brand failed:{}"+pmsBrand);
        }
        return commonResult;
    }

    //
    @ApiOperation("更新指定id品牌信息")
    @PreAuthorize("hasAuthority('pms:brand:update')")
    @PostMapping("/update/{id}")
    @ResponseBody
    public CommonResult updateBrand(@PathVariable("id") Long id,
                                    @RequestBody PmsBrand pmsBrand,
                                    BindingResult result){
        CommonResult commonResult;
        int count = brandService.updateBrand(id,pmsBrand);
        if (count == 1){
            commonResult = CommonResult.success(pmsBrand);
            LOGGER.debug("update brand success:{}"+pmsBrand);
        }else{
            commonResult = CommonResult.failed("操作失败");
            LOGGER.debug("update brand failed:{}"+pmsBrand);
        }
        return commonResult;
    }

    //
    @ApiOperation("删除指定id的品牌")
    @PreAuthorize("hasAuthority('pms:brand:delete')")
    @GetMapping("/delete/{id}")
    @ResponseBody
    public CommonResult deleteBrand(@PathVariable("id") Long id){
        int count = brandService.deleteBrand(id);
        if (count == 1){
            LOGGER.debug("delete brand success:id={}"+id);
            return CommonResult.success(null);
        }else{
            LOGGER.debug("delete brand failed:id={}"+id);
            return CommonResult.failed("操作失败");
        }
    }

    //
    @ApiOperation("分页查询品牌列表")
    @PreAuthorize("hasAuthority('pms:brand:read')")
    @GetMapping("/list")
    @ResponseBody
    public CommonResult<CommonPage<PmsBrand>> listBrand(@RequestParam(value = "pageNum",defaultValue ="1") Integer pageNum,
                                                        @RequestParam(value = "pageSize",defaultValue = "3") Integer pageSize){
        List<PmsBrand> brandList = brandService.listBrand(pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

    //
    @ApiOperation("获取指定id的品牌详情")
    @PreAuthorize("hasAuthority('pms:brand:read')")
    @GetMapping("/{id}")
    @ResponseBody
    public CommonResult<PmsBrand> brand(@PathVariable("id") Long id ){
        return CommonResult.success(brandService.getBrand(id));
    }

}
