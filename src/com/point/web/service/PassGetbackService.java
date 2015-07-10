package com.point.web.service;

import com.point.web.entity.PassGetback;


public interface PassGetbackService {
	public void insert(PassGetback passGetback);
	
	public PassGetback findById(String id);
	
	public PassGetback findLastByAccount(String account);
	
	public int updateColoums(PassGetback passGetback);
	
	public int updateColoumsByCondition(PassGetback passGetback);
	
	public PassGetback findByShaid(String id);
}
