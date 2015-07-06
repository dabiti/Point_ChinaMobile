package com.point.web.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.point.web.dao.OrderDao;
import com.point.web.entity.Order;
import com.point.web.service.OrderService;


@Service("OrderService")
public class OrderServiceImpl implements OrderService {

	
	
	@Resource
	private OrderDao orderDao;
	
	
	
	private static Logger log = Logger.getLogger(OrderServiceImpl.class);

	@Override
	public Order get(String id) {
		Order Order = orderDao.get(id);
		return Order;
	}
	

	
	
	@Override
	public void save(Order order) throws Exception{
		log.info("准备保存订单");
		//保存订单
		orderDao.save(order);
		log.info("保存成功");
	}
	
	
	
	
	//卓望自身生成虚拟码
//	private String createVCFromZW(){
//		// 查询序列
//		long sysNo = getNextSysNo();
//		log.info("获得系统序列:"+sysNo);
//		String seq = String.format("%08d", sysNo);
//		VirtualCode vc = new VirtualCode(order.getOrderid(),systime, seq);
//		return null;
//	}
//	
	
	
	@Override
	public String getSysTime(){
		return orderDao.getSysTime();
	}




	@Override
	public long getNextSysNo() {
		return orderDao.getNextSysNo();
	}
	
	@Override
	public List<Order> findByCreateDate(Map createtime){
		return orderDao.findByCreateDate(createtime);
	}


	
}
