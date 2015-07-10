package com.point.web.dao;

import com.point.web.entity.PassGetback;


public interface PassGetbackDao {
	
	public void insert(PassGetback passGetback);
	
	public PassGetback findById(String id);
}
