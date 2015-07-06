package com.point.web.dao;

import com.point.web.entity.User;
/**
 * @Title: 用户Dao
 * @Description:用户管理Dao
 * @Since: 2015年7月06日上午10:46:05
 * @author guowanyu
 */
public interface UserDao {
	/**
	* @Title:得到用户对象
	* @Description:根据用户名得到用户对象
	* @Since: 2015年7月6日上午10:46:35
	* @author guowanyu
	*/
	public User getByUsername(String username);
	
}
