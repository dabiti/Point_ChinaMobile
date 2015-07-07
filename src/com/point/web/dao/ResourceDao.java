package com.point.web.dao;

import java.util.List;

import com.point.web.entity.Resource;

/**
 * @Title: 资源Dao
 * @Description:资源管理Dao
 * @Since: 2015年7月06日上午10:48:35
 * @author guowanyu
 */
public interface ResourceDao {
	/**
	* @Title:得到资源列表
	* @Description:根据角色名得到相关资源列表
	* @Since: 2015年7月6日上午10:48:58
	* @author guowanyu
	 */
	public List<Resource> getResourceByRolename(String rolename);
	/**
	* @Title:得到资源列表
	* @Description:根据角色名称得到顶级菜单项
	* @Since: 2015年7月6日上午10:49:56
	* @author guowanyu
	 */
	public List<Resource> getTopMenuResource(String rolename);
	/**
	* @Title:得到资源列表
	* @Description:根据顶级菜单的Id得到其关联的次级菜单列表
	* @Since: 2015年7月6日上午10:50:15
	* @author guowanyu
	 */
	public List<Resource> getSubMenuResource(String id);
	/**
	* @Title:得到资源列表
	* @Description:根据用户名得到所有其对应的url资源列表
	* @Since: 2015年7月6日上午10:50:20
	* @author guowanyu
	 */
	public List<Resource> getAllUrlResourceByUsername(String username);
}
