package com.point.web.service.impl;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.point.web.dao.OrderDao;
import com.point.web.entity.Order;
import com.point.web.service.OrderService;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	private OrderDao OrderDao;
	

	@Override
	public Order get(String id) {
		OrderDao = sqlSessionTemplate.getMapper(OrderDao.class);
		Order Order = OrderDao.get(id);
		return Order;
	}
	

	
	
	@Override
	public void save(Order order){
		OrderDao = sqlSessionTemplate.getMapper(OrderDao.class);
		OrderDao.save(order);
	}
	
	
	
	@Override
	public String getSysTime(){
		OrderDao = sqlSessionTemplate.getMapper(OrderDao.class);
		return OrderDao.getSysTime();
	}
	
	
//	public void saveOrderAndSend(Order order){
//		OrderDao = sqlSessionTemplate.getMapper(OrderDao.class);
//		//保存订单
//		OrderDao.save(order);
//		//生成虚拟码
//		
//		//保存虚拟码
//		
//		//发送虚拟码
//	}

	
}
