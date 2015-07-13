package com.point.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.point.web.dao.VirtualCodeDao;
import com.point.web.entity.VirtualCode;
import com.point.web.service.VirtualCodeService;

@Service("VirtualCodeService")
public class VirtualCodeServiceImpl implements VirtualCodeService {


	@Resource
	private VirtualCodeDao virtualCodeDao;
	

	@Override
	public void updateStatus(String orderId,String status) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("status", status);
		virtualCodeDao.updateStatus(map);
	}
	
	@Override
	public VirtualCode get(String orderId) {
		return virtualCodeDao.get(orderId);
	}
	
	
	@Override
	public void save(VirtualCode vc) {
		virtualCodeDao.save(vc);
	}
	
	
	//更新
	public void update(VirtualCode vc) {
		virtualCodeDao.update(vc);
	}
	
}
