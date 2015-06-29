package com.point.web.dao;

import java.util.Map;

import com.point.web.entity.VirtualCode;


public interface VirtualCodeDao {

	public void updateStatus(Map<String,String> map);
	
	public VirtualCode get(String orderId);
	
	public void save(VirtualCode virtualCode);
}
