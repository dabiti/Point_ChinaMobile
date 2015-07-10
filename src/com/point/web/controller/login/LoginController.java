package com.point.web.controller.login;


import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.point.web.util.ValidateCode;
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
	
		
		String validcode = (String)WebUtils.getParameterStr(request,"validcode");
		
		if(StringUtils.isEmpty(username)){
			WebUtils.writeWarningMsg(response,"用户名不能为空！");
			return;
		}
		
		if(StringUtils.isEmpty(password)){
			WebUtils.writeWarningMsg(response,"密码不能为空！");
			return;
		}
		
		if(StringUtils.isEmpty(validcode)){
			WebUtils.writeWarningMsg(response,"验证码不能为空！");
			return;
		}
        
		HttpSession session = request.getSession();  
        if(!validcode.toUpperCase().equals(session.getAttribute("code"))){
        	WebUtils.writeWarningMsg(response,"验证码错误！");
			return;
        }
		
	    try {
	        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
	        Subject subject = SecurityUtils.getSubject();
	        subject.login(token);
	        
	        if(subject.isAuthenticated()){
	        
	        	WebUtils.writeSuccessMsg(response,"登录成功！");

	        	
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
	
	@ResponseBody
	@RequestMapping("/login/validateCode")
	public void validateCode(HttpServletRequest request,HttpServletResponse response) {
		
		// 设置响应的类型格式为图片格式  
        response.setContentType("image/jpeg");  
        //禁止图像缓存。  
        response.setHeader("Pragma", "no-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
          
        HttpSession session = request.getSession();  
          
        ValidateCode vCode = new ValidateCode(120,40,5,100);  
        session.setAttribute("code", vCode.getCode());  
        try {
			vCode.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
}
