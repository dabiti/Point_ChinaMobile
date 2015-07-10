package com.point.web.service;

import com.point.web.entity.PassGetback;


public interface PassGetbackService {
	public void insert(PassGetback passGetback);
	
	public PassGetback findById(String id);
}
