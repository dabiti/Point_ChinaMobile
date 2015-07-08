package com.point.web.listener;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @Title: 文件监听启动servlet
 * @Description:文件监听启动servlet
 * @Since: 2015年7月06日上午10:20:20
 * @author wangchunlong
 */
public class FolderStartServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext sc  = this.getServletContext();
		final ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		
		new Thread(new Runnable() {
			public void run() {
				try {
					ResourceListener.main(ac);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
}
