package com.point.web.service;

import com.point.web.entity.Upmsg;


/**
 * @Title: UpmsgService接口
 * @Description:上行短信
 * @Since: 2015年7月04日
 * @author wangchunlong
 */
public interface UpmsgService {

	//保存
	public void save (Upmsg upmsg) throws Exception;
	
	//更新状态
	public void updateStatus(String userId,String phone,String upTime,String status);

}
