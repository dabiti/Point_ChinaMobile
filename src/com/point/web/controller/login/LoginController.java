package com.point.web.controller.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.point.web.util.WebUtils;

/**
 * @Title: 登录Controller
 * @Description:用户与渠道商登录控制层
 * @Since: 2015年7月06日上午10:20:20
 * @author guowanyu
 */
@Controller
public class LoginController{
	
	/**
	 * @Title:进入登录页面
	 * @Description:由于没有认证，跳转至登录页面
	 * @Since: 2015年7月06日上午10:20:30
	 * @author guowanyu
	 */
	@RequestMapping("/login/toLogin")
	public String jumpToLoginView(HttpServletRequest request,
			HttpServletResponse response) {
		return "login";
	}
	/**
	* @Title:登出
	* @Description:登录账号进行登出的操作，登出后进入登录页面
	* @Since: 2015年7月6日上午10:30:52
	* @author guowanyu
	*/
	@RequestMapping("/login/logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		return "login";
	}
	
	/**
	* @Title:登录提交数据
	* @Description:用户输入账号相关信息，提交信息到服务端处理方法
	* @Since: 2015年7月6日上午10:34:50
	* @author guowanyu
	*/
	@ResponseBody
	@RequestMapping("/login/login")
	public void login(HttpServletRequest request,HttpServletResponse response) {
		
		String username = (String)WebUtils.getParameterStr(request,"username");
		
		String password = (String)WebUtils.getParameterStr(request,"password");
		
		if(StringUtils.isEmpty(username)){
			WebUtils.writeWarningMsg(response,"用户名不能为空！");
			return;
		}
		
		if(StringUtils.isEmpty(password)){
			WebUtils.writeWarningMsg(response,"密码不能为空！");
			return;
		}
		
	    try {
	        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
	        Subject subject = SecurityUtils.getSubject();
	        subject.login(token);
	        
	        if(subject.isAuthenticated()){
	        	WebUtils.writeSuccessMsg(response,"登录成功！");
	        	return;
	        }
	    } catch (IncorrectCredentialsException e) {
	    	e.printStackTrace();
	    	WebUtils.writeWarningMsg(response,"密码错误！");
        	return;
	    } catch (AuthenticationException e) {
	    	e.printStackTrace();
	    	WebUtils.writeWarningMsg(response,"账号错误！");
        	return;
	    }  catch (Exception e) {
	    	e.printStackTrace();
	    	WebUtils.writeWarningMsg(response,"登录失败！");
        	return;
	    }
	}
}
