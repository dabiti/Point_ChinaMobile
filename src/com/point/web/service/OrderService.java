package com.point.web.service;

import com.point.web.entity.Order;

public interface OrderService {

	//获得用户
	public Order get(String id);

	//获取系统时间
	public String getSysTime();
	
	//获取下一序列
	public long getNextSysNo();

	//保存
	public void save (Order order) throws Exception;

}
