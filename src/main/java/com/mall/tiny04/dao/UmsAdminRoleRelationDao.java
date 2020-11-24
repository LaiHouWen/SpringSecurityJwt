package com.mall.tiny04.dao;

import com.mall.tiny04.mbg.model.UmsPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后台用户与角色管理自定义Dao
 *
 */
public interface UmsAdminRoleRelationDao {

    //获取用户所有权限(包括+-权限)
    public List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);

}
