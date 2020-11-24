package com.mall.tiny04.service;

import com.mall.tiny04.mbg.model.UmsAdmin;
import com.mall.tiny04.mbg.model.UmsPermission;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 *
 */
public interface UmsAdminService {

    //根据用户名获取后台管理员
    UmsAdmin getAdminByUsername(String username);

    //注册功能
    UmsAdmin register(UmsAdmin umsAdminParam);

    /**
     *  登录功能
     * @param username
     * @param password
     * @return 生成的JWT的token
     */
    String login(String username,String password);

    //获取用户所有权限（包括角色权限和+-权限）
    List<UmsPermission> getPermissionList(Long adminId);

}
