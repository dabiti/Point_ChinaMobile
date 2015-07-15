package com.point.web.listener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.point.web.util.MMSTool;


/**
 * @Title: MMSFTP启动servlet
 * @Description:MMSFTP启动servlet
 * @Since: 2015年7月06日上午10:20:20
 * @author wangchunlong
 */
public class MMSFTPStartServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(MMSFTPStartServlet.class);
	
	public static boolean MMSftpButton =  true;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext sc  = this.getServletContext();
		final ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		log.info("启动MMS-FTP监听程序");
					
					new Thread(new Runnable() {
						public void run() {
							try {
								while(MMSFTPStartServlet.MMSftpButton){
									log.info("开始扫描");
									Calendar calendar = Calendar.getInstance();
									calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
									SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
									String dateStr = df.format(calendar.getTime());
//									String dateStr = "20150714";
									log.info("获取前1小时时间:"+dateStr);
						    		MMSFTPListener mfl = new MMSFTPListener();
						    		//获得所有文件
						    		List<String> fileNameList = mfl.getFileList(MMSTool.getMMSMap().get("ftppath")+dateStr.substring(0, dateStr.length()-2)+"/");
						    		log.info(MMSTool.getMMSMap().get("ftppath")+dateStr.substring(0, dateStr.length()-2)+"/"+"总文件列表:"+fileNameList);
						    		ArrayList<String> trueList =  new ArrayList<String>();
						    		  for(String fileName:fileNameList){
						    			  if(MMSFTPListener.compareName(fileName,dateStr)){
						    				  trueList.add(fileName);
						    			  }
						    		  }
						    		  log.info("取得条件文件列表："+trueList);
						    		  ArrayList<Thread> tlist = new ArrayList<Thread>();
						    		  for(String truefileName:trueList){
						    			  //启动读取线程
						    			  log.info("读取文件"+MMSTool.getMMSMap().get("ftppath")+dateStr.substring(0, dateStr.length()-2)+"/"+truefileName);
						    			  FTPReader fr = new FTPReader(MMSTool.getMMSMap().get("ftppath")+dateStr.substring(0, dateStr.length()-2)+"/"+truefileName, ac,mfl.getFtpClient());
						    			  Thread tt = new Thread(fr);
						    			  tt.start();
						    			  tlist.add(tt);
						    		  }
						    		for(Thread tt:tlist){
						    			tt.join();
						    		}
						    		mfl.closeServer();
						    		Thread.sleep(50000);//睡眠1小时360000
						    	}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}).start();
			
		
	}
}
