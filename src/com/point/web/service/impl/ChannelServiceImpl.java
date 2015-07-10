package com.point.web.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.point.web.dao.ChannelDao;
import com.point.web.entity.Channel;
import com.point.web.service.ChannelService;
@Service("channelService")
public class ChannelServiceImpl implements ChannelService{
	@Autowired
	private ChannelDao channelDao;
	
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
	public void update(Channel channel) {
		// TODO Auto-generated method stub
		channelDao.update(channel);
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

}
