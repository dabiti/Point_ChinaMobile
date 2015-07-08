package com.point.web.service;

import com.point.web.entity.VirtualCode;

/**
 * @Title: VirtualCodeService接口
 * @Description:虚拟码
 * @Since: 2015年7月04日
 * @author wangchunlong
 */
public interface VirtualCodeService {

	//更新状态
	public void updateStatus(String orderId,String status) ;
	
	//查询
	public VirtualCode get(String orderId);

	//保存虚拟码
	void save(VirtualCode vc) throws Exception;
	
}
