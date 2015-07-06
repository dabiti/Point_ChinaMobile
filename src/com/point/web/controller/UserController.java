package com.point.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.point.web.entity.User;
import com.point.web.service.UserService;

@Controller
public class UserController {

	private static Logger log = Logger.getLogger(UserController.class);
	
	@Resource
	private UserService userService;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	/**
	 * @Title: 跳转到userView.jsp
	 * @Description:
	 * @Since: 2015年7月02日
	 * @Version: 1.1.0
	 * @author yanbin_a
	 */
	@RequestMapping("/toUserPage")
	public String toDemoPage(HttpSession session, HttpServletResponse resopnse) {
		return "user/userView";
	}

	@ResponseBody
	@RequestMapping("/user/list")
	public String userList(HttpServletRequest request,
			HttpServletResponse response, String username, String phone, String jobnumber, String enable
			,String page,String rows) {
		
		String top=(Integer.parseInt(page)-1)*Integer.parseInt(rows)+1+"";//top为分页的第一条记录
		String bottom=Integer.parseInt(page)*Integer.parseInt(rows)+"";//bottom为分页的最后一条记录
		
		response.setCharacterEncoding("UTF-8");
		
		Map map = new HashMap<String, String>();// map传入多个参数
		map.put("username", username);
		map.put("jobnumber", jobnumber);
		map.put("enable", enable);
		map.put("phone", phone);
		map.put("top", top);
		map.put("bottom", bottom);
		
		List<User> users = userService.findAllByCondition(map);
		List<User> cha = userService.findAll(map);//此方法是为了取到联合查询后的总记录数
		String total = String.valueOf(cha.size());//total: 总的记录条数；
		
		JSONArray json = new JSONArray();
		for (User user : users) {
			JSONObject jo = new JSONObject();
			jo.put("id", user.getId());
			jo.put("username", user.getUsername());
			jo.put("password", user.getPassword());
			jo.put("realname", user.getRealname());
			jo.put("jobnumber", user.getJobnumber());
			jo.put("email", user.getEmail());
			jo.put("phone", user.getPhone());
			jo.put("enable", user.getEnable());
			jo.put("createtime", user.getCreatetime());
			jo.put("description", user.getDescription());
			json.add(jo);
		}
		String result="{\"total\":\""+total+"\","+"\"rows\":"+json.toString()+"}";//rows:  本页的记录, total :总的记录数
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/user/list/getTypes")
	public String demoListGetTypes() {
		return "[{\"id\":\"1\",\"text\":\"是\"},{\"id\":\"2\",\"text\":\"否\"}]";
	}
	
	/**
	 * 保存用户信息
	 * @author yanbin_a
	 * @since 2015年7月1日
	 * @return ModelAndView
	 */
	@RequestMapping("addUser")
	public String saveUser(HttpServletRequest request,
			HttpServletResponse response, User user){
			userService.insertUser(user);
		return "user/userView";
	}
	
	
	/**
	 * @Title: 批量或单个删除user
	 * @Description:
	 * @Since: 2015年7月04日
	 * @Version: 1.1.0
	 * @author yanbin_a
	 */
	@RequestMapping("deleteUser")
	public String deleteUser(HttpServletRequest request,
			HttpServletResponse response, String id) {
		String[] ids = id.split(",");
		userService.deleteByParams(ids);
		return "user/userView";
	}

	/**
	 * @Title: 修改user
	 * @Description:
	 * @Since: 2015年7月02日
	 * @Version: 1.1.0
	 * @author yanbin_a
	 */
	@RequestMapping("updateUser")
	public String updateChannel(HttpServletRequest request,
			HttpServletResponse response, User user) {
		userService.update(user);
		return "user/userView";
	}
}
