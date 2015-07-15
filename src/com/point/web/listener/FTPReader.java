package com.point.web.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import net.sf.json.JSONObject;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.point.web.entity.Upmsg;
import com.point.web.service.UpmsgService;
import com.point.web.util.BoleTool;

/**
 * @Title: ftp文件读取线程类
 * @Description:ftp文件监听线程类
 * @Since: 2015年7月06日上午10:20:20
 * @author wangchunlong
 */
@Component
public class FTPReader implements Runnable {
	private String fileName;

	private ApplicationContext ac;

	private FTPClient ftpClient;

	private static Logger log = Logger.getLogger(FTPReader.class);
	
	
	public FTPReader() {
		super();
	}

	public FTPReader(String fileName, ApplicationContext ac,FTPClient ftpClient) {
		this.ac = ac;
		this.fileName = fileName;
		this.ftpClient = ftpClient;
	}

	public void run() {
		InputStream ins = null;
		UpmsgService upmsgService = (UpmsgService) ac.getBean("UpmsgService");
		try {
			// 从服务器上读取指定的文件
			ins = ftpClient.retrieveFileStream(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new GZIPInputStream(ins), "GBK"));
			String line;
			while ((line = reader.readLine()) != null) {
				log.info("数据：" + line);
				String[] upEntity = line.split(",");
				Upmsg upmsg = new Upmsg(upEntity[0], upEntity[1], upEntity[2],
						upEntity[3], upEntity[4], "0");
				// 保存实体到数据库
				try {
					log.info("保存上行消息:"+upmsg);
					upmsgService.save(upmsg);
					// 通知伯乐
					JSONObject jo = new JSONObject();
					jo.put("userId", upmsg.getUserId());
					jo.put("phone", upmsg.getPhone());
					jo.put("port", upmsg.getPort());
					jo.put("content", upmsg.getContent());
					log.info("通知伯乐,参数为:"+jo);
					String boleStr = BoleTool.sendRequest(jo, "notifySms.do");
					// 返回判断
					if (null != boleStr || !"".equals(boleStr)) {
						JSONObject returnJo = JSONObject.fromObject(boleStr);
						if (returnJo.containsKey("code")
								&& "0".equals(returnJo.getString("code"))) {
							// 更新状态
							log.info("通知成功，更新上行短信状态至1");
							upmsgService.updateStatus(upmsg.getUserId(),
									upmsg.getPhone(), upmsg.getUpTime(), "1");
						} else {
							log.info("通知失败，更新上行短信状态至2");
							upmsgService.updateStatus(upmsg.getUserId(),
									upmsg.getPhone(), upmsg.getUpTime(), "2");
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			reader.close();
			if (ins != null) {
				ins.close();
			}
			// 主动调用一次getReply()把接下来的226消费掉. 这样做是可以解决这个返回null问题
			ftpClient.getReply();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ApplicationContext getAc() {
		return ac;
	}

	public void setAc(ApplicationContext ac) {
		this.ac = ac;
	}

}