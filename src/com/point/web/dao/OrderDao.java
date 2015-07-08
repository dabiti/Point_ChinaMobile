package com.point.web.dao;

import java.util.List;
import java.util.Map;

import com.point.web.entity.Order;

/**
 * @Title: 订单Dao
 * @Description:订单数据库操作
 * @Since: 2015年7月06日上午10:20:20
 * @author wangchunlong
 */
public interface OrderDao {

	public Order get(String orderid);
	
	public void save(Order order);
	
	public String getSysTime();
	
	public long getNextSysNo();
	
	public List<Order> findByCreateDate(Map createtime);
	
}
