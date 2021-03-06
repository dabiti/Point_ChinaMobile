package com.point.web.dao;

import java.util.List;
import java.util.Map;

import com.point.web.entity.Channel;

/**
 * @Title: ChannelDao接口
 * @Description:
 * @Since: 2015年7月01日
 * @author liuhao
 *
 */
public interface ChannelDao {
		/**
		 @Title: 查找全部的方法接口
		 * @Description:
		 * @Since: 2015年7月01日
		 * @author liuhao 
		 */
	public List<Channel> findAll(Map map);
	/**
	 @Title: 查找指定渠道商的方法接口
	 * @Description:
	 * @Since: 2015年7月01日
	 * @author liuhao 
	 */
	public Channel findById(String id);
	
	public Channel findByName(String name);
	
	public int getChannelCountByName(Channel channel);
	/**
	* @Title: 插入渠道商的方法接口
	 * @Description:
	 * @Since: 2015年7月01日
	 * @author liuhao 
	 */
	public void insert(Channel channel);
	/**
	 @Title: 更新渠道商的方法接口
	 * @Description:
	 * @Since: 2015年7月01日
	 * @author liuhao 
	 */
	public int update(Channel channel);
	/**
	 @Title: 删除批量渠道商的方法接口
	 * @Description:
	 * @Since: 2015年7月01日
	 * @author liuhao 
	 */
	public void deleteByParams(String[] params);
	/**
	 @Title: 删除指定渠道商的方法接口
	 * @Description:
	 * @Since: 2015年7月01日
	 * @author liuhao 
	 */
	public void delete(Channel channel);
	/**
	 @Title: 按条件渠道商的方法接口
	 * @Description:
	 * @Since: 2015年7月01日
	 * @author liuhao 
	 */
	public List<Channel> findAllByCondition(Map paramMap);
}
