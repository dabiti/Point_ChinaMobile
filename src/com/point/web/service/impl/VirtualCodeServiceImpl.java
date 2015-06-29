package com.point.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.point.web.dao.VirtualCodeDao;
import com.point.web.entity.VirtualCode;
import com.point.web.service.VirtualCodeService;

@Service("VirtualCodeService")
public class VirtualCodeServiceImpl implements VirtualCodeService {

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	private VirtualCodeDao virtualCodeDao;
	

	@Override
	public void updateStatus(String orderId,String status) {
		virtualCodeDao = sqlSessionTemplate.getMapper(VirtualCodeDao.class);
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderid", orderId);
		map.put("status", status);
		virtualCodeDao.updateStatus(map);
	}
	
	@Override
	public VirtualCode get(String orderId) {
		virtualCodeDao = sqlSessionTemplate.getMapper(VirtualCodeDao.class);
		return virtualCodeDao.get(orderId);
	}
	
	
	
}
