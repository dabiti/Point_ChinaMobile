package com.point.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
