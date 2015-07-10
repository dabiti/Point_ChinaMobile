package com.point.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.point.web.dao.PassGetbackDao;
import com.point.web.entity.PassGetback;
import com.point.web.service.PassGetbackService;
@Service("passGetbackService")
public class PassGetbackServiceImpl implements PassGetbackService{
	@Resource
	private PassGetbackDao passGetbackDao;

	@Override
	public void insert(PassGetback passGetback) {
		this.passGetbackDao.insert(passGetback);
	}

	@Override
	public PassGetback findById(String id) {
		return this.passGetbackDao.findById(id);
	}

	@Override
	public PassGetback findLastByAccount(String account) {
		return this.passGetbackDao.findLastByAccount(account);
	}

	@Override
	public int updateColoums(PassGetback passGetback) {
		return this.passGetbackDao.updateColoums(passGetback);
	}

	@Override
	public int updateColoumsByCondition(PassGetback passGetback) {
		return this.passGetbackDao.updateColoumsByCondition(passGetback);
	}

	@Override
	public PassGetback findByShaid(String shaid) {
		return this.passGetbackDao.findByShaid(shaid);
	}
}
