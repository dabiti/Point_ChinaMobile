package com.point.web.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.point.web.entity.Channel;


/**
 * Servlet implementation class DownloadExcelServlet
 */
@Service("downloadExcelServlet")
public class DownloadExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource
       private ChannelService channelService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadExcelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 doPost(request, response);
		 
	}

	/**
	* @title :处理下载Excel服务的请求
	* @author : haojianyun
	* @since：${date} ${time} 
	* @params：默认
	* @Description:
	*/ 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		request.setCharacterEncoding("UTF-8");//设置request的编码方式，防止中文乱码
       
        this.downLoad(request, response);
		
		
	}
	
	
	public void downLoad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		/* String fileName ="订单表";//设置导出的文件名称
		 
		// List<Channel>channels=channelService.findAll();
		 
		
		 
		 String strchannel="";
		 strchannel+="<table border='1'>";
		 for(Channel channel:channels){
			 strchannel+="<tr><td>"+channel.getCno()+"</td><td>"+channel.getCreatetime()+"</td><td>"+channel.getName()+"</td></tr>";
			 
			 
		 }
		 strchannel+="</table>";
		 
	        
	        StringBuffer sb = new StringBuffer(strchannel);//将表格信息放入内存
	        
	        
	        String contentType ="application/vnd.ms-excel";//定义导出文件的格式的字符串
	        String recommendedName = new String(fileName.getBytes(),"iso_8859_1");//设置文件名称的编码格式
	        response.setContentType(contentType);//设置导出文件格式
	        response.setHeader("Content-Disposition", "attachment; filename=" + recommendedName + ".XLS");//
	        response.resetBuffer();
	        //利用输出输入流导出文件
	        ServletOutputStream sos = response.getOutputStream();
	        sos.write(sb.toString().getBytes());
	        sos.flush();
	        sos.close();
	        */
	}

}
