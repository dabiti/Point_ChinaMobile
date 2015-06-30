package com.point.web.dao;

import com.point.web.entity.Order;

public interface OrderDao {

	public Order get(String orderid);
	
	public void save(Order order);
	
	public String getSysTime();
	
}
