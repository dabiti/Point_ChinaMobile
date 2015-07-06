package com.point.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.point.web.dao.UserDao;
import com.point.web.entity.User;
import com.point.web.service.UserService;
/**
 * @Title: 用户Service
 * @Description:用户管理Service
 * @Since: 2015年7月06日上午10:43:10
 * @author guowanyu
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	/**
	 * @Title:得到用户对象
	 * @Description:根据账号名称得到用户对象
	 * @Since: 2015年7月06日上午10:43:50
	 * @author guowanyu
	 */
	@Override
	public User getByUsername(String username) {
		return userDao.getByUsername(username);
	}
}
