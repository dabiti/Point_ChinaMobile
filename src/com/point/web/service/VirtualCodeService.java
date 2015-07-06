package com.point.web.service;

import com.point.web.entity.VirtualCode;


public interface VirtualCodeService {

	//更新状态
	public void updateStatus(String orderId,String status) ;
	
	//查询
	public VirtualCode get(String orderId);

	//保存虚拟码
	void save(VirtualCode vc) throws Exception;
	
}
