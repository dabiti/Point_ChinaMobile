package com.point.web.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.point.web.entity.Order;
import com.point.web.entity.VGReturnJson;
import com.point.web.service.OrderService;
import com.point.web.util.Base64Coder;
import com.point.web.util.StringTool;
import com.point.web.util.VirtualGoodsTool;

@Controller
public class MobileMallController {

	private static Logger log = Logger.getLogger(MobileMallController.class);
	
	@Resource
	private OrderService orderService;

	@RequestMapping("/test")
	public void getOrgnizationTree(HttpSession session,
			HttpServletResponse resopnse) {
		try {
			System.out.println("1111");
			// System.out.println(orderService.getNextSysNo());
			System.out.println("1111");
			resopnse.getWriter().print("1111111");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/jumpToLoginView")
	public String jumpToLoginView(HttpServletRequest request,
			HttpServletResponse response) {
		// 如果已登录，直接跳到主页面，否则跳转到登陆页面
		Subject subject = SecurityUtils.getSubject();
		if (subject.isRemembered() == true || subject.isAuthenticated() == true) {
			return "main";
		} else
			return "login";
	}

	// ***正式编码***//
	@RequestMapping("/notifyOrder")
	public void notifyOrder(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("下发通知");
		VGReturnJson rj = new VGReturnJson("9999", "平台内部错误");
		try {
			String req = request.getParameter("req");
			if(null==req){
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			}else{
			// Base64解密
			req = Base64Coder.decodeString(req);
//			req = "412e9884911f20516d02d8c0ef33a593{\"source\":\"2\",\"version\":\"1.0\",\"identity_id\":\"123456\","
//					+ "\"data\":{\"orderId\":\"123\",\"phone\":\"123\",\"itemId\":\"123\",\"title\":\"123\",\"price\":\"123\",\"quantity\":\"123\",\"finalFee\":\"123\",\"discount\":\"123\"}}";
			// 签名
			String sign = req.substring(0, req.indexOf("{"));
			System.out.println("sign:"+sign);
			// 参数
			String jsonStr = req.substring(req.indexOf("{"), req.length());
			System.out.println("jsonStr:"+jsonStr);
			// 验证签名
			if (!VirtualGoodsTool.verifySign(jsonStr, sign)) {
				rj = new VGReturnJson("1001", "验签失败，您所传递的数据签名在API服务器端没有验证通过");
			} else if (!VirtualGoodsTool.verifyParameter(jsonStr)) {
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else if (!StringTool.isUTF8(jsonStr)) {
				rj = new VGReturnJson("1006", "错误的编码格式");
			} else {
				// 公共参数
				JSONObject paramJson = JSONObject.fromObject(jsonStr);
				String version = paramJson.getString("version");
				String identity_id = paramJson.getString("identity_id");
				String source = paramJson.getString("source");
				String data = paramJson.getString("data");
				// 业务参数
				JSONObject dataJson = JSONObject.fromObject(data);
				String orderId = dataJson.getString("orderId");
				String phone = dataJson.getString("phone");
				String itemId = dataJson.getString("itemId");
				String title = dataJson.getString("title");
				String price = dataJson.getString("price");
				String quantity = dataJson.getString("quantity");
				String finalFee = dataJson.getString("finalFee");
				String discount = dataJson.getString("discount");
				String systime = orderService.getSysTime();
				String createdate = systime.substring(0, 9);
				String createtime = systime.substring(11);
				// 重复
				Order order = orderService.get(orderId);
				if (null != order) {
					rj = new VGReturnJson("1009", "重复订单");
					// 插入数据库记录
				} else {
					order = new Order(orderId, phone, itemId, title, price,
							quantity, finalFee, discount, createdate,
							createtime);
					orderService.save(order);
					//发送
					rj = new VGReturnJson("0000","ok");
				}

			}
			}
		} catch(IllegalArgumentException iae){
			iae.printStackTrace();
			rj = new VGReturnJson("1003","参数格式错误或不在正确范围内");
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.setContentType("text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(rj.getJo().toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
