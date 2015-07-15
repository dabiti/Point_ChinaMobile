package com.point.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;


/**
 * @Title: 应用事件监听类
 * @Description:应用事件监听类
 * @Since: 2015年7月06日上午10:20:20
 * @author wangchunlong
 */
public class StartListener implements ServletContextListener {

	private static Logger log = Logger.getLogger(StartListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		MMSFTPStartServlet.MMSftpButton = false;
		log.info("MMSftp_off");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		MMSFTPStartServlet.MMSftpButton = true;
		log.info("MMSftp_on");
	}

}
