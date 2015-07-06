package com.point.web.service;

import com.point.web.entity.User;

public interface UserService {
	/**
	 * @Title:得到用户对象
	 * @Description:根据账号名称得到用户对象
	 * @Since: 2015年7月06日上午10:43:50
	 * @author guowanyu
	 */
	public User getByUsername(String username);
}
