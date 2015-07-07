package com.point.web.dao;

import java.util.Map;

import com.point.web.entity.Upmsg;

public interface UpmsgDao {

	public void save(Upmsg upmsg);
	
	public void updateStatus(Map<String,String> map);
}
