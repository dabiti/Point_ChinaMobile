package com.point.web.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.point.web.dao.UserDao;
import com.point.web.entity.User;
import com.point.web.service.UserService;

@Service("UserService")
public class UserServiceImpl implements UserService {
	
	private static Logger log = Logger.getLogger(UserServiceImpl.class);
	
	@Resource
	private UserDao userDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public boolean insertUser(User username) {
		userDao.insertUser(username);
		return true;
	}

	@Override
	public int getUserCountByName(String username) {
		return userDao.getUserCountByName(username);
	}

	@Override
	public List<User> findAll(Map map) {
		return userDao.findAll(map);
	}

	@Override
	public User findById(String id) {
		return userDao.findById(id);
	}

	@Override
	public void update(User user) {
		userDao.update(user);
	}

	@Override
	public void deleteByParams(String[] params) {
		userDao.deleteByParams(params);
	}

	@Override
	public void delete(User user) {
		userDao.delete(user);
	}

	@Override
	public List<User> findAllByCondition(Map paramMap) {
		return userDao.findAllByCondition(paramMap);
	}

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
