package com.point.web.dao;

import java.util.List;
import java.util.Map;

import com.point.web.entity.Order;

public interface OrderDao {

	public Order get(String orderid);
	
	public void save(Order order);
	
	public String getSysTime();
	
	public long getNextSysNo();
	
	public List<Order> findByCreateDate(Map createtime);
	
}
