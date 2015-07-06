package com.point.web.dao;

import java.util.List;

import com.point.web.entity.Role;

/**
 * @Title: 角色Dao
 * @Description:角色管理Dao
 * @Since: 2015年7月06日上午10:47:08
 * @author guowanyu
 */
public interface RoleDao {
	/**
	* @Title:得到角色列表
	* @Description:根据用户名得到相关角色列表
	* @Since: 2015年7月6日上午10:48:08
	* @author guowanyu
	 */
	public List<Role> getRolesByUsername(String username);
}
