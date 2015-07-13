package com.point.web.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.point.web.dao.ChannelDao;
import com.point.web.dao.PassGetbackDao;
import com.point.web.entity.Channel;
import com.point.web.entity.PassGetback;
import com.point.web.service.ChannelService;
import com.point.web.util.PassEncodeTool;
@Service("channelService")
public class ChannelServiceImpl implements ChannelService{
	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private PassGetbackDao passGetbackDao;
	
	public ChannelDao getChannelDao() {
		return channelDao;
	}

	public void setChannelDao(ChannelDao channelDao) {
		this.channelDao = channelDao;
	}

	private static Logger log = Logger.getLogger(ChannelServiceImpl.class);
	@Override
	public List<Channel> findAll(Map map) {
		// TODO Auto-generated method stub
		return channelDao.findAll(map);
	}

	@Override
	public Channel findById(String id) {
		// TODO Auto-generated method stub
		return channelDao.findById(id);
	}

	@Override
	public void insert(Channel channel) {
		// TODO Auto-generated method stub
		channelDao.insert(channel);
	}

	@Override
	public int update(Channel channel) {
		// TODO Auto-generated method stub
		return channelDao.update(channel);
	}

	@Override
	public void deleteByParams(String[] params) {
		// TODO Auto-generated method stub
		channelDao.deleteByParams(params);
	}

	@Override
	public void delete(Channel channel) {
		// TODO Auto-generated method stub
		channelDao.delete(channel);
	}

	@Override
	public List<Channel> findAllByCondition(Map paramMap) {
		// TODO Auto-generated method stub
		return channelDao.findAllByCondition(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.point.web.service.ChannelService#findByName(java.lang.String)
	 */
	@Override
	public Channel findByName(String name) {
		return this.channelDao.findByName(name);
	}

	/* (non-Javadoc)
	 * @see com.point.web.service.ChannelService#updatePassGetBack(com.point.web.entity.PassGetback)
	 */
	@Override
	public String updatePassGetBack(String vpid,String pass) {
		
		PassGetback passGetback = this.passGetbackDao.findByShaid(vpid);
		if(passGetback == null || passGetback.getNewpasstime() != null){
			return "网页已失效！";
		}
		
		Channel channel = this.findByName(passGetback.getAccount());
		
		if(channel == null ){
			return "用户信息不存在！";
		}
		
		passGetback.setNewpasstime(new Date());
		if(0 == this.passGetbackDao.updateColoumsByCondition2(passGetback)){
			return "网页已失效！";
		}
		
		Map<String,String> passMap = PassEncodeTool.passwordEncode(channel.getName(),pass);
		
		channel.setPassword(passMap.get("password"));
		channel.setSalt(passMap.get("salt"));
		
		if(0 == this.update(channel)){
			throw new RuntimeException("更新密码失败！！");
		}
		
		return null;
	}

}
