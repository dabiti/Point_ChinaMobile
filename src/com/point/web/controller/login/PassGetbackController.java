/**   
* @Title: PassController.java 
* @Package com.point.web.controller.login 
* @Description: TODO
* @author guowanyu   
* @date 2015年7月9日 上午11:20:12 
* @version V1.0   
*/
package com.point.web.controller.login;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.point.web.entity.Channel;
import com.point.web.entity.PassGetback;
import com.point.web.service.ChannelService;
import com.point.web.service.PassGetbackService;
import com.point.web.util.ValidCodeProductTool;
import com.point.web.util.WebUtils;

/**
 * @title :
 * @author : 
 * @since：2015年7月9日 上午11:20:12 
 * @Description:
 */
@Controller
public class PassGetbackController {
	@Resource
	private ChannelService channelService;
	@Resource
	private PassGetbackService passGetbackService;
	
	@RequestMapping("/pass/toPassGetback")
	public String passGetback(HttpServletRequest request,
			HttpServletResponse response) {
		return "pass/passGetback";
	}
	
	@ResponseBody
	@RequestMapping("/pass/validAccount")
	public void passValidAccount(HttpServletRequest request,
			HttpServletResponse response) {
		String account = (String)WebUtils.getParameterStr(request,"account");
		
		if(StringUtils.isEmpty(account)){
			WebUtils.writeWarningMsg(response,"用户名不能为空！");
			return;
		}
		
	    try {
	    	Channel channel = channelService.findByName(account);
	    	if(channel == null){
	    		WebUtils.writeWarningMsg(response,"用户名不存在！");
				return;
	    	}
	    	
	    	String phone = channel.getPhone();
	    	
        	WebUtils.writeSuccessMsg(response,"发送成功！",this.replacePhone(phone));
        	return;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	WebUtils.writeWarningMsg(response,"发送失败！");
        	return;
	    }
	}
	
	private String replacePhone(String srcPhone) {
		String start = srcPhone.substring(0, 3);
		String end = srcPhone.substring(srcPhone.length()-1,srcPhone.length());
		String middle = srcPhone.substring(3, srcPhone.length()-1).replaceAll("([\\d])", "*");
		return start + middle + "*************" + end;
	}
	
	@ResponseBody
	@RequestMapping("/pass/sendCode")
	public void passSendCode(HttpServletRequest request,
			HttpServletResponse response) {
		String account = (String)WebUtils.getParameterStr(request,"account");
		
		String phone = (String)WebUtils.getParameterStr(request,"phone");
		
		if(StringUtils.isEmpty(account)){
			WebUtils.writeWarningMsg(response,"用户名不能为空！");
			return;
		}
		
		if(StringUtils.isEmpty(phone)){
			WebUtils.writeWarningMsg(response,"手机号不能为空！");
			return;
		}
		
	    try {
	    	Channel channel = channelService.findByName(account);
	    	if(channel == null){
	    		WebUtils.writeWarningMsg(response,"用户名不存在！");
				return;
	    	}
	    	
	    	//进行手机号的验证,如果验证失败,则返回错误消息，手机号错误
//	    	if(!phone.equals(channel.getPhone())){
//	    		
//	    	}
	    	
	        String validCode = ValidCodeProductTool.productCode();
	        
	        //String result = MMSTool.sendMMS(phone, validCode);
	        //发送验证码，如果验证码发送成功，结果中包含‘1000’，则写入数据库
	        PassGetback passGetback = new PassGetback();
	        passGetback.setAccount(account);
	        passGetback.setPhone(phone);
	        passGetback.setValidcode(validCode);
	        passGetback.setCreatetime(new Date());
	        
	        passGetbackService.insert(passGetback);
	        
        	WebUtils.writeSuccessMsg(response,"发送成功！",passGetback.getId());
        	return;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	WebUtils.writeWarningMsg(response,"发送失败！");
        	return;
	    }
	}
	
	@ResponseBody
	@RequestMapping("/pass/passGetsubmit")
	public void passGetsubmit(HttpServletRequest request,
			HttpServletResponse response) {
		String account = (String)WebUtils.getParameterStr(request,"account");
		
		String phone = (String)WebUtils.getParameterStr(request,"phone");
		
		String validcode = (String)WebUtils.getParameterStr(request,"validcode");
		
		String pid = (String)WebUtils.getParameterStr(request,"pid");
		
		if(StringUtils.isEmpty(account)){
			WebUtils.writeWarningMsg(response,"用户名不能为空！");
			return;
		}
		
		if(StringUtils.isEmpty(phone)){
			WebUtils.writeWarningMsg(response,"手机号不能为空！");
			return;
		}
		
		if(StringUtils.isEmpty(validcode)){
			WebUtils.writeWarningMsg(response,"验证码不能为空！");
			return;
		}
		
		if(StringUtils.isEmpty(pid)){
			WebUtils.writeWarningMsg(response,"请重新发送验证码！");
			return;
		}
		
	    try {
	    	Channel channel = channelService.findByName(account);
	    	if(channel == null){
	    		WebUtils.writeWarningMsg(response,"用户名不存在！");
				return;
	    	}
	    	
	    	//进行手机号的验证,如果验证失败,则返回错误消息
	    	
	    	//根据pid查找发送验证码的表
	    	PassGetback passGetback = this.passGetbackService.findById(pid);
	        
        	WebUtils.writeSuccessMsg(response,"发送成功！");
        	return;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	WebUtils.writeWarningMsg(response,"发送失败！");
        	return;
	    }
	}
}
