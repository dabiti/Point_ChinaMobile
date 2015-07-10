package com.point.web.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.zip.GZIPInputStream;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.point.web.entity.Upmsg;
import com.point.web.service.UpmsgService;
import com.point.web.util.BoleTool;


/**
 * @Title: 文件监听线程类
 * @Description:文件监听线程类
 * @Since: 2015年7月06日上午10:20:20
 * @author wangchunlong
 */
@Component
public class FolderListner implements Runnable {  
    private WatchService service;  
    private String rootPath;  
      
    private ApplicationContext ac;
    
    public FolderListner() {  
        super();
    }  
    
    
    
    public FolderListner(WatchService service,String rootPath,ApplicationContext ac) { 
    	this.ac = ac;
        this.service = service;  
        this.rootPath = rootPath;  
    }  
  
    public void run() {  
        try {  
            while(true){  
                WatchKey watchKey = service.take();  
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();  
                for(WatchEvent<?> event : watchEvents){  
            		BufferedReader in = null;
            		String filePath = rootPath+"/"+event.context();
            		File file = new File(filePath);
            		try {
            			String upMMSStr = "";
						in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(
									file)),"UTF-8"));
						UpmsgService upmsgService = (UpmsgService) ac.getBean("UpmsgService");
						while ((upMMSStr = in.readLine()) != null) {
							System.out.println("有数据："+upMMSStr);
							String[] upEntity = upMMSStr.split(",");
							Upmsg upmsg = new Upmsg(upEntity[0],upEntity[1],upEntity[2],upEntity[3],upEntity[4],"0");
							try {
								//保存实体到数据库
								upmsgService.save(upmsg);
								//通知伯乐
								JSONObject jo = new JSONObject();
								jo.put("userId", upmsg.getUserId());
								jo.put("phone", upmsg.getPhone());
								jo.put("port", upmsg.getPort());
								jo.put("content", upmsg.getContent());
								String boleStr = BoleTool.sendRequest(jo, "notifySms.do");
								// 返回判断
								if (null != boleStr || !"".equals(boleStr)) {
									JSONObject returnJo = JSONObject.fromObject(boleStr);
									if (returnJo.containsKey("code") && "0".equals(returnJo.get("code"))) {
										//更新状态
										upmsgService.updateStatus(upmsg.getUserId(),upmsg.getPhone(),upmsg.getUpTime(),"1");
									}else{
										upmsgService.updateStatus(upmsg.getUserId(),upmsg.getPhone(),upmsg.getUpTime(),"2");
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
                }  
                watchKey.reset();  
            }  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }finally{  
            System.out.println("fdsfsdf");  
            try {  
                service.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
          
    }


	public ApplicationContext getAc() {
		return ac;
	}



	public void setAc(ApplicationContext ac) {
		this.ac = ac;
	}  
    
    
    
}  