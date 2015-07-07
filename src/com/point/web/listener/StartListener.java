package com.point.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class StartListener implements ServletContextListener {

	private static Logger log = Logger.getLogger(StartListener.class);// ��ʼ����־����
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("off");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("on");
	}

}
