
package com.point.web.service;

import java.util.List;
import java.util.Map;

import com.point.web.entity.Order;


/**
 * @Title: OrderService接口
 * @Description:订单
 * @Since: 2015年7月04日
 * @author wangchunlong
 */
public interface OrderService {

	//获得用户
	public Order get(String id);

	//获取系统时间
	public String getSysTime();
	
	//获取下一序列
	public long getNextSysNo();

	//保存
	public void save (Order order) throws Exception;
	
	//模糊搜索查询订单
	public List<Order> findByCreateDate(Map createtime);
}

