package com.point.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.point.web.entity.Channel;
import com.point.web.service.ChannelService;
import com.point.web.util.Pager;

@Controller

public class ChannelController {

	@Autowired
	private ChannelService channelService;

	public ChannelService getChannelService() {
		return channelService;
	}

	public void setChannelService(ChannelService channelService) {
		this.channelService = channelService;
	}

	/**
	 * @Title: 查询channel表
	 * @Description:
	 * @Since: 2015年7月02日
	 * @author liuhao
	 */
	@ResponseBody
	@RequestMapping("doChannelview")
	public String doview(HttpServletRequest request,
			HttpServletResponse response, String name, String phone, String cno,String page,String rows) {
	//page 接受客户端的页码，对应的就是用户选择或输入的pageNumber（按照上图的例子，用户点了下一页，传到服务器端就是2）
	//	 rows 接受客户端的每页记录数，对应的就是pageSize  （用户在下拉列表选择每页显示30条记录，传到服务器就是30）
		
/****/
	String top=(Integer.parseInt(page)-1)*Integer.parseInt(rows)+1+"";//top为分页的第一条记录
	String bottom=Integer.parseInt(page)*Integer.parseInt(rows)+"";//bottom为分页的最后一条记录
		
/****/		
		
		response.setCharacterEncoding("UTF-8");
	
		Map map = new HashMap<String, String>();// map传入多个参数•page: 页号，从1计起。 •rows: 每页记录大小。 
		map.put("name", name);
		map.put("phone", phone);
		map.put("cno", cno);
		map.put("top", top);
		map.put("bottom", bottom);
		
		List<Channel> channels = channelService.findAllByCondition(map);
		List<Channel> cha = channelService.findAll(map);//此方法是为了取到联合查询后的总记录数
		
		String total=String.valueOf(cha.size());//total: 总的记录条数；
	System.out.println(total);
//		Gson gson=new Gson();
//		gson.toJson(channels);
//		System.out.println(gson.toJson(channels));
		JSONArray json = new JSONArray();
//gson
		for (Channel channel : channels) {
			JSONObject jo = new JSONObject();
			jo.put("id", channel.getId());
			jo.put("name", channel.getName());
			jo.put("email", channel.getEmail());
			jo.put("phone", channel.getPhone());
			jo.put("cno", channel.getCno());
			jo.put("password", channel.getPassword());
			jo.put("createtime", channel.getCreatetime());
			json.add(jo);

		}
String result="{\"total\":\""+total+"\","+"\"rows\":"+json.toString()+"}";//rows:  本页的记录, total :总的记录数


		return result;
	}

	/**
	 * @Title: 跳转到channelView.jsp
	 * @Description:
	 * @Since: 2015年7月02日
	 * @author liuhao
	 */
	@RequestMapping("toChannelview")//channel/
	public String toview(HttpServletRequest request,
			HttpServletResponse response) {

		return "channel/channelView";
	}

	/**
	 * @Title: 增加channel
	 * @Description:
	 * @Since: 2015年7月02日
	 * @author liuhao
	 */
	@RequestMapping("addChannel")
	public String addChannel(HttpServletRequest request,
			HttpServletResponse response, Channel channel) {
		channelService.insert(channel);

		return "channelView";
	}

	/**
	 * @Title: 批量或单个删除channel
	 * @Description:
	 * @Since: 2015年7月02日
	 * @author liuhao
	 */
	@RequestMapping("deleteChannel")
	public String deleteChannel(HttpServletRequest request,
			HttpServletResponse response, String id) {
		String[] ids = id.split(",");

		channelService.deleteByParams(ids);

		return "channel/channelView";
	}

	/**
	 * @Title: 修改channel
	 * @Description:
	 * @Since: 2015年7月02日
	 * @author liuhao
	 */
	@RequestMapping("updateChannel")
	public String updateChannel(HttpServletRequest request,
			HttpServletResponse response, Channel channel) {

		channelService.update(channel);

		return "channel/channelView";
	}
	
	@RequestMapping("exportChannel")
	public void exportChannel(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
response.setCharacterEncoding("utf-8");

String name=request.getParameter("name");
String phone=request.getParameter("phone");
String cno=request.getParameter("cno");
		String fileName ="渠道商表";//设置导出的文件名称
		Map map = new HashMap<String, String>();// map传入多个参数•page: 页号，从1计起。 •rows: 每页记录大小。 
		map.put("name", name);
		map.put("phone", phone);
		map.put("cno", cno);
		 List<Channel>channels=channelService.findAll(map);
		 
		 
		 
		 String strchannel="";
		 strchannel+="<table border='1'>";
		 strchannel+="<tr><td>商号</td><td>渠道商名称</td><td>密码</td><td>电话</td><td>邮箱</td><td>创建时间</td></tr>";
		 
		 for(Channel channel:channels){
			 strchannel+="<tr><td>"+channel.getCno()+"</td><td>"+channel.getName()+"</td><td>"+channel.getPassword()+"</td><td>"+channel.getPhone()+"</td><td>"+channel.getEmail()+"</td><td>"+channel.getCreatetime()+"</td></tr>";
			 
			 
		 }
		 strchannel+="</table>";
		 System.out.println(strchannel);
	        
	        StringBuffer sb = new StringBuffer(strchannel);//将表格信息放入内存
	        
	        
	        String contentType ="application/vnd.ms-excel";//定义导出文件的格式的字符串
	        String recommendedName = new String(fileName.getBytes(),"ISO-8859-1");//设置文件名称的编码格式
	        response.setContentType(contentType);//设置导出文件格式
	        response.setHeader("Content-Disposition", "attachment; filename=" + recommendedName + ".XLS");//
	        response.resetBuffer();
	        //利用输出输入流导出文件
	        ServletOutputStream sos = response.getOutputStream();
	        sos.write(sb.toString().getBytes());
	        sos.flush();
	        sos.close();
	        
	}
	

}
