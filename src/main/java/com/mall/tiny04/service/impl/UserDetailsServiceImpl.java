package com.mall.tiny04.service.impl;

import com.mall.tiny04.dto.AdminUserDetails;
import com.mall.tiny04.mbg.model.UmsAdmin;
import com.mall.tiny04.mbg.model.UmsPermission;
import com.mall.tiny04.service.UmsAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * UserDetailsService 实现 返回权限查询
 */

public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UmsAdminService umsAdminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UmsAdmin admin = umsAdminService.getAdminByUsername(username);
        if (admin !=null ){
            List<UmsPermission> permissionList = umsAdminService.getPermissionList(admin.getId());
            return new AdminUserDetails(admin,permissionList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");

    }
}
