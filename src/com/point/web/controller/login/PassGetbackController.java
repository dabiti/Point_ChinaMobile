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
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.point.web.entity.Channel;
import com.point.web.entity.PassGetback;
import com.point.web.service.ChannelService;
import com.point.web.service.PassGetbackService;
import com.point.web.util.MMSTool;
import com.point.web.util.PassEncodeTool;
import com.point.web.util.TimeUtils;
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
	
//	@RequestMapping("/pass/toPassInfo")
//	public String passInfo(HttpServletRequest request,
//			HttpServletResponse response) {
//		return "pass/passInfo";
//	}
	
	@RequestMapping("/pass/toPassNew")
	public String passNew(HttpServletRequest request,
			HttpServletResponse response) {
		String vpid = (String)WebUtils.getParameterStr(request,"vpid");
		if(StringUtils.isEmpty(vpid)){
			return "redirect:/pass/toPassGetback";
		}
		PassGetback passGetback = this.passGetbackService.findByShaid(vpid);
		if(passGetback == null || 
				"1".equals(passGetback.getIsvalid())|| 
				passGetback.getNewpasstime() != null ||
				0 == this.passGetbackService.updateColoumsByCondition(passGetback)){
			return "redirect:/pass/toPassGetback";
		}
		request.setAttribute("vpid", vpid);
		return "pass/passNew";
	}
	
	@RequestMapping("/pass/passConfirm")
	public String passConfirm(HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String,Object> dataMap = WebUtils.convertRequestParamToMap(request);
		
		String vpid = (String)dataMap.get("vpid");
		
		String pass = (String)dataMap.get("pass");
		
		String rpass = (String)dataMap.get("rpass");
		
		String returnUrl = "pass/passInfo";
		
		if(StringUtils.isEmpty(vpid) || pass == null || rpass == null || !pass.equals(rpass)){
			request.setAttribute("msg", "非法请求！");
			return returnUrl;
		}
		
		String res = this.channelService.updatePassGetBack(vpid, rpass);
		
		if(res != null){
			request.setAttribute("msg", res);
		}else{
			request.setAttribute("msg", "新密码设置成功！");
		}
		return returnUrl;
	}
	
	//用户名输入框移除焦点需要验证用户名的有效性
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
	
	//发送手机验证码
	@ResponseBody
	@RequestMapping("/pass/sendCode")
	public void passSendCode(HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String,Object> dataMap = WebUtils.convertRequestParamToMap(request);
		
		String account = (String)dataMap.get("account");
		
		String phone = (String)dataMap.get("phone");
		
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
	    	
	    	//查找当前用户，最近的一次找回密码时间，如果当前时间与最近一次找回的时间间隔不到120秒，则提示 用户需要等待
	    	PassGetback validPassGetback = this.passGetbackService.findLastByAccount(account);
	    	
	    	if(validPassGetback != null){
		    	Date createTime = validPassGetback.getCreatetime();
		    	
		    	long differMillis = TimeUtils.differSeconds(createTime, new Date());
		    	
		    	if(differMillis - TimeUtils.differCodeSendTimeMillis < 0){
		    		WebUtils.writeWarningMsg(response,"请等待3分钟！");
					return;
		    	}
	    	}
	    	
	        String validCode = ValidCodeProductTool.productCode();
	        String sendTemp = "blql send validcode : "+validCode;
	        
	        String result = MMSTool.sendMMS(channel.getPhone(), sendTemp,MMSTool.getMMSMap().get("pb_templateId"));
	        
	        if(!result.contains("1000")){
	        	WebUtils.writeWarningMsg(response,"发送失败！");
	        	return;
	        }
	        //发送验证码，如果验证码发送成功，结果中包含‘1000’，则写入数据库
	        PassGetback passGetback = new PassGetback();
	        passGetback.setAccount(account);
	        passGetback.setPhone(channel.getPhone());
	        passGetback.setValidcode(validCode);
	        passGetback.setCreatetime(new Date());
	        
	        passGetbackService.insert(passGetback);
	        
        	WebUtils.writeSuccessMsg(response,"发送成功！");
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
		
		Map<String,Object> dataMap = WebUtils.convertRequestParamToMap(request);
		
		String account = (String)dataMap.get("account");
		
		String phone = (String)dataMap.get("phone");
		
		String validcode = (String)dataMap.get("validcode");
		
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
		
	    try {
	    	Channel channel = channelService.findByName(account);
	    	if(channel == null){
	    		WebUtils.writeWarningMsg(response,"用户名不存在！");
				return;
	    	}
	    	//根据账号找到最近一条密码找回数据,其记录了发送短信相关信息
	    	PassGetback validPassGetback = this.passGetbackService.findLastByAccount(account);
	    	
	    	if(validPassGetback == null || validPassGetback.getIsvalid() != null
	    			|| validPassGetback.getShaid() != null){
	    		WebUtils.writeWarningMsg(response,"请发送短信验证码！");
				return;
	    	}
	    	
	    	if(!validcode.equals(validPassGetback.getValidcode())){
	    		WebUtils.writeWarningMsg(response,"验证码错误！");
				return;
	    	}
    		
	    	Date createTime = validPassGetback.getCreatetime();
	    	
	    	long differMillis = TimeUtils.differSeconds(createTime, new Date());
	    	
	    	if(differMillis - TimeUtils.differCodeValidTimeMillis > 0){
	    		WebUtils.writeWarningMsg(response,"验证码已过期，请重新发送验证码！");
				return;
	    	}
	    	
            //更新 shaid 与 isvalid字段
            String shaid = PassEncodeTool.createShaId(account);
            validPassGetback.setShaid(shaid);
            validPassGetback.setValidtime(new Date());
            int count = this.passGetbackService.updateColoums(validPassGetback);
            
            if(count == 0){
            	WebUtils.writeWarningMsg(response,"网页已失效，请刷新网页！");
				return;
            }
	        
        	WebUtils.writeSuccessMsg(response,"操作成功！",shaid);
        	return;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	WebUtils.writeWarningMsg(response,"操作失败！");
        	return;
	    }
	}
}
