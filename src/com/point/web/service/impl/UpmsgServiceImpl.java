package com.point.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.point.web.dao.UpmsgDao;
import com.point.web.entity.Upmsg;
import com.point.web.service.UpmsgService;

@Service("UpmsgService")
public class UpmsgServiceImpl implements UpmsgService {

	@Resource
	private UpmsgDao upmsgDao;

	private static Logger log = Logger.getLogger(UpmsgServiceImpl.class);

	@Override
	public void save(Upmsg upmsg) throws Exception {
		// 保存
		upmsgDao.save(upmsg);
		log.info("保存成功");
	}

	
	@Override
	public void updateStatus(String userId,String phone,String upTime,String status) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("phone", phone);
		map.put("upTime", upTime);
		map.put("status", status);
		upmsgDao.updateStatus(map);
	}
}
