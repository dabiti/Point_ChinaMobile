package com.point.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.point.web.dao.OrderDao;
import com.point.web.dao.VirtualCodeDao;
import com.point.web.entity.Order;
import com.point.web.entity.VirtualCode;
import com.point.web.service.OrderService;
import com.point.web.util.MMSTool;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	private OrderDao orderDao;
	
	private VirtualCodeDao virtualCodeDao;
	
	
	private static Logger log = Logger.getLogger(OrderServiceImpl.class);

	@Override
	public Order get(String id) {
		orderDao = sqlSessionTemplate.getMapper(OrderDao.class);
		Order Order = orderDao.get(id);
		return Order;
	}
	

	
	
	@Override
	public boolean save(Order order,String systime) throws Exception{
		log.info("准备保存订单相关操作");
		boolean reslut = false;
		virtualCodeDao = sqlSessionTemplate.getMapper(VirtualCodeDao.class);
		orderDao = sqlSessionTemplate.getMapper(OrderDao.class);
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("phone", order.getPhone());
		// 查询序列
		long sysNo = getNextSysNo();
		log.info("获得系统序列:"+sysNo);
		String seq = String.format("%08d", sysNo);
		VirtualCode vc = new VirtualCode(order.getOrderid(),systime, seq);
		userMap.put("data", vc.getVcodepass());
		// 保存
		//保存订单
		log.info("准备保存订单:"+order);
		orderDao.save(order);
		log.info("保存成功");
		//保存虚拟码
		log.info("准备保存订单:"+order);
		virtualCodeDao.save(vc);
		log.info("保存成功");
		// 发送
		log.info("准备发送短信，参数"+userMap);
		MMSTool.sendMMS(userMap);
		log.info("发送成功");
		reslut = true;
		return reslut;
	}
	
	
	
	@Override
	public String getSysTime(){
		orderDao = sqlSessionTemplate.getMapper(OrderDao.class);
		return orderDao.getSysTime();
	}




	@Override
	public long getNextSysNo() {
		orderDao = sqlSessionTemplate.getMapper(OrderDao.class);
		return orderDao.getNextSysNo();
	}
	
	
	
}
