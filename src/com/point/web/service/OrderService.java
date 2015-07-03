package com.point.web.service;

import java.util.List;
import java.util.Map;

import com.point.web.entity.Order;

public interface OrderService {

	//获得用户
	public Order get(String id);

	//获取系统时间
	public String getSysTime();
	
	//获取下一序列
	public long getNextSysNo();

	//保存
	public boolean save (Order order,String systime) throws Exception;

	//按时间检索
	public List<Order> findByCreateDate(Map createtime);

}
