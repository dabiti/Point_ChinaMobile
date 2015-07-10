package com.point.web.dao;

import com.point.web.entity.PassGetback;


public interface PassGetbackDao {
	
	public void insert(PassGetback passGetback);
	
	public PassGetback findById(String id);
	
	public PassGetback findByShaid(String id);
	
	public PassGetback findLastByAccount(String account);
	
	public int updateColoums(PassGetback passGetback);
	
	public int updateColoumsByCondition(PassGetback passGetback);
	
}
