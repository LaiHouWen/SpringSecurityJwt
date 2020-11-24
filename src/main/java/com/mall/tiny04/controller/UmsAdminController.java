package com.mall.tiny04.controller;

import com.mall.tiny04.common.CommonResult;
import com.mall.tiny04.dto.UmsAdminLoginParam;
import com.mall.tiny04.mbg.model.UmsAdmin;
import com.mall.tiny04.mbg.model.UmsPermission;
import com.mall.tiny04.service.UmsAdminService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *后台用户管理
 */
@Api(tags = "UmsAdminController")
@Controller
@RequestMapping("/admin")
public class UmsAdminController {


    @Autowired
    private UmsAdminService adminService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    @ResponseBody
    public CommonResult<UmsAdmin> register(@RequestBody  UmsAdmin umsAdminParam,
                                           BindingResult result){
        UmsAdmin umsAdmin = adminService.register(umsAdminParam);
        if (umsAdmin ==null){
            CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }


    @ApiOperation(value = "登录返回token")
    @PostMapping("/login")
    @ResponseBody
    public CommonResult login(@RequestBody UmsAdminLoginParam umsAdminLoginParam,
                              BindingResult result){
        String token =
                adminService.login(umsAdminLoginParam.getUsername(),
                        umsAdminLoginParam.getPassword());
        if (token ==null){
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return CommonResult.success(tokenMap);
    }


    @ApiOperation(value = "获取用户所有权限（包括+-权限）")
    @GetMapping("/permission/{adminId}")
    @ResponseBody
    public CommonResult<List<UmsPermission>> getPermissionList(@PathVariable("adminId") Long id ){
        List<UmsPermission> permissionList = adminService.getPermissionList(id);
        return CommonResult.success(permissionList);
    }

}
