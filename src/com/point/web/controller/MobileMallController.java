package com.point.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import com.google.gson.Gson;
import com.point.web.entity.Order;
import com.point.web.entity.VGReturnJson;
import com.point.web.entity.VirtualCode;
import com.point.web.service.OrderService;
import com.point.web.service.VirtualCodeService;
import com.point.web.util.Base64Coder;
import com.point.web.util.MMSTool;
import com.point.web.util.StringTool;
import com.point.web.util.VirtualGoodsTool;

@Controller
public class MobileMallController {

	private static Logger log = Logger.getLogger(MobileMallController.class);

	@Resource
	private OrderService orderService;

	@Resource
	private VirtualCodeService virtualCodeService;

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
		log.info("准备下发通知");
		VGReturnJson rj = new VGReturnJson("9999", "平台内部错误");
		String orderId = null;
		boolean saveResult = false;
		try {
			String req = request.getParameter("req");
			log.info("收到报文:"+req);
			if (null == req) {
				log.info("报文为空");
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				// Base64解密
				log.info("开始Base64解密");
				req = req.replace(" ", "+");
				req = Base64Coder.decodeString(req);
//				req = "df882ed62782ddf7cd7afbc73f762411{\"source\":\"2\",\"version\":\"1.0\",\"identity_id\":\"123456\","
//						+ "\"data\":{\"orderId\":\"123\",\"phone\":\"13581761989\",\"itemId\":\"123\",\"title\":\"123\",\"price\":\"123\",\"quantity\":\"123\",\"finalFee\":\"123\",\"discount\":\"123\"}}";
				log.info("Base64解密后报文:"+req);
				// 签名
				String sign = req.substring(0, req.indexOf("{"));
				log.info("获得签名sign:"+sign);
				// 参数
				String jsonStr = req.substring(req.indexOf("{"), req.length());
				log.info("参数为:"+jsonStr);
				// 验证签名
				log.info("开始验证签名");
				if (!VirtualGoodsTool.verifySign(jsonStr, sign)) {
					log.error("验证签名未通过");
					rj = new VGReturnJson("1001",
							"验签失败，您所传递的数据签名在API服务器端没有验证通过");
				} else if (!VirtualGoodsTool
						.verifyParameterForNotifyOrder(jsonStr)) {
					log.error("缺少参数");
					rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
				} else if (!StringTool.isUTF8(jsonStr)) {
					log.error("错误的编码格式");
					rj = new VGReturnJson("1006", "错误的编码格式");
				} else {
					log.info("开始解析公共参数");
					// 公共参数
					JSONObject paramJson = JSONObject.fromObject(jsonStr);
					String version = paramJson.getString("version");
					log.info("version:"+version);
					String identity_id = paramJson.getString("identity_id");
					log.info("identity_id:"+identity_id);
					String source = paramJson.getString("source");
					log.info("source:"+source);
					String data = paramJson.getString("data");
					log.info("data:"+data);
					log.info("开始解析业务参数");
					// 业务参数
					JSONObject dataJson = JSONObject.fromObject(data);
					orderId = dataJson.getString("orderId");
					log.info("orderId:"+orderId);
					String phone = dataJson.getString("phone");
					log.info("phone:"+phone);
					String itemId = dataJson.getString("itemId");
					log.info("itemId:"+itemId);
					String title = dataJson.getString("title");
					log.info("title:"+title);
					String price = dataJson.getString("price");
					log.info("price:"+price);
					String quantity = dataJson.getString("quantity");
					log.info("quantity:"+quantity);
					String finalFee = dataJson.getString("finalFee");
					log.info("finalFee:"+finalFee);
					String discount = dataJson.getString("discount");
					log.info("discount:"+discount);
					String systime = orderService.getSysTime();
					log.info("获取数据库系统时间："+systime);
					String createdate = systime.substring(0, 9);
					String createtime = systime.substring(11);
					// 重复
					Order order = orderService.get(orderId);
					log.info("查询订单是否已存在："+order);
					if (null != order) {
						log.info("订单已重复");
						rj = new VGReturnJson("1009", "重复订单");
						// 插入数据库记录
					} else {
						order = new Order(orderId, phone, itemId, title, price,
								quantity, finalFee, discount, createdate,
								createtime);
						saveResult = orderService.save(order, systime);
						log.info("保存成功");
						log.info("准备更新订单状态-1");
						virtualCodeService.updateStatus(orderId, "1");
						log.info("更新成功");
						
						//通知移动商城发送的虚拟码
						JSONObject jo = new JSONObject();
						jo.put("orderId", orderId);
						jo.put("itemId", itemId);
						// 虚拟商品列表
						ArrayList<VirtualCode> list = new ArrayList<VirtualCode>();
						VirtualCode vc = virtualCodeService.get(orderId); 
						list.add(vc);
						Gson gg = new Gson();
						String listJson = gg.toJson(list);
						jo.put("virtualCodes", listJson);
						log.info("准备调用移动提交虚拟码接口,参数:"+jo);
						VirtualGoodsTool.sendRequest(jo,"setVirtualCode");
						log.info("调用成功");
						
						rj = new VGReturnJson("0", "ok");
					}
				}
			}
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
			log.error("参数格式错误");
			rj = new VGReturnJson("1003", "参数格式错误或不在正确范围内");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if(saveResult){
				log.info("准备更新订单状态-2");
				virtualCodeService.updateStatus(orderId, "2");
				log.info("更新成功");
			}
			rj = new VGReturnJson("9999", e.getMessage());
		} finally {
			try {
				log.error("返回:"+rj.getJo().toJSONString());
				response.setContentType("text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(rj.getJo().toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 重新发送虚拟码
	@RequestMapping("/resendVirtualCode")
	public void resendVirtualCode(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("准备重发虚拟码");
		// 根据请求参数查询到该订单的虚拟码
		VGReturnJson rj = new VGReturnJson("9999", "平台内部错误");
		try {
			String req = request.getParameter("req");
			if (null == req) {
				log.info("报文为空");
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				// Base64解密
				log.info("开始Base64解密");
				req = req.replace(" ", "+");
				 req = Base64Coder.decodeString(req);
//				req = "df882ed62782ddf7cd7afbc73f762411{\"source\":\"2\",\"version\":\"1.0\",\"identity_id\":\"123456\","
//					+ "\"data\":{\"orderId\":\"123\",\"phone\":\"13581761989\",\"itemId\":\"123\",\"title\":\"123\",\"price\":\"123\",\"quantity\":\"123\",\"finalFee\":\"123\",\"discount\":\"123\"}}";
				log.info("Base64解密后报文:"+req);
				// 签名
				String sign = req.substring(0, req.indexOf("{"));
				log.info("获得签名sign:"+sign);
				// 参数
				String jsonStr = req.substring(req.indexOf("{"), req.length());
				log.info("参数为:"+jsonStr);
				// 验证签名
				log.info("开始验证签名");
				if (!VirtualGoodsTool.verifySign(jsonStr, sign)) {
					log.error("验证签名未通过");
					rj = new VGReturnJson("1001",
							"验签失败，您所传递的数据签名在API服务器端没有验证通过");
				} else if (!VirtualGoodsTool
						.verifyParameterForResendVirtualCode(jsonStr)) {
					log.error("缺少参数");
					rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
				} else if (!StringTool.isUTF8(jsonStr)) {
					log.error("错误的编码格式");
					rj = new VGReturnJson("1006", "错误的编码格式");
				} else {
					log.info("开始解析公共参数");
					// 公共参数
					JSONObject paramJson = JSONObject.fromObject(jsonStr);
					String version = paramJson.getString("version");
					log.info("version:"+version);
					String identity_id = paramJson.getString("identity_id");
					log.info("identity_id:"+identity_id);
					String source = paramJson.getString("source");
					log.info("source:"+source);
					String data = paramJson.getString("data");
					log.info("data:"+data);
					// 业务参数
					log.info("开始解析业务参数");
					JSONObject dataJson = JSONObject.fromObject(data);
					String orderId = dataJson.getString("orderId");
					log.info("orderId:"+orderId);
					Order order = orderService.get(orderId);
					log.info("获得订单:"+order);
					if (null == order) {
						log.info("订单不存在");
						rj = new VGReturnJson("1011", "订单有误，订单信息错误或者订单不存在");
					} else {
						log.info("根据订单号查询虚拟码");
						VirtualCode vc = virtualCodeService.get(orderId);
						log.info("获得虚拟码:"+vc);
						// 准备发送
						Map<String, String> userMap = new HashMap<String, String>();
						userMap.put("phone", order.getPhone());
						userMap.put("data", vc.getVcodepass());
						// 发送
						log.info("准备发送短信,参数:"+userMap);
						
						String MMSReturn = MMSTool.sendMMS(userMap);
						if(MMSReturn.contains("1000")){
							log.info("发送成功");
							rj = new VGReturnJson("0", "ok");
						}else{
							log.info("发送失败");
							rj = new VGReturnJson("9999", "短信发送失败,返回："+MMSReturn);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			rj = new VGReturnJson("9999", e.getMessage());
		} finally {
			try {
				log.info("返回："+rj.getJo().toJSONString());
				response.setContentType("text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(rj.getJo().toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 设置虚拟码失效
	@RequestMapping("/setCodeInvalid")
	public void setCodeInvalid(HttpServletRequest request,
			HttpServletResponse response) {
		// 根据请求参数查询到该订单的虚拟码
		VGReturnJson rj = new VGReturnJson("9999", "平台内部错误");
		try {
			String req = request.getParameter("req");
			if (null == req) {
				log.info("报文为空");
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				// Base64解密
				log.info("开始Base64解密");
				req = req.replace(" ", "+");
				 req = Base64Coder.decodeString(req);
//				req = "df882ed62782ddf7cd7afbc73f762411{\"source\":\"2\",\"version\":\"1.0\",\"identity_id\":\"123456\","
//						+ "\"data\":{\"orderId\":\"123\",\"phone\":\"13581761989\",\"itemId\":\"123\",\"title\":\"123\",\"price\":\"123\",\"quantity\":\"123\",\"finalFee\":\"123\",\"discount\":\"123\"}}";
				log.info("Base64解密后报文:"+req);
				// 签名
				String sign = req.substring(0, req.indexOf("{"));
				log.info("获得签名sign:"+sign);
				// 参数
				String jsonStr = req.substring(req.indexOf("{"), req.length());
				log.info("参数为:"+jsonStr);
				// 验证签名
				log.info("开始验证签名");
				if (!VirtualGoodsTool.verifySign(jsonStr, sign)) {
					log.error("验证签名未通过");
					rj = new VGReturnJson("1001",
							"验签失败，您所传递的数据签名在API服务器端没有验证通过");
				} else if (!VirtualGoodsTool
						.verifyParameterForSetCodeInvalid(jsonStr)) {
					log.error("缺少参数");
					rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
				} else if (!StringTool.isUTF8(jsonStr)) {
					log.error("错误的编码格式");
					rj = new VGReturnJson("1006", "错误的编码格式");
				} else {
					log.info("开始解析公共参数");
					// 公共参数
					JSONObject paramJson = JSONObject.fromObject(jsonStr);
					String version = paramJson.getString("version");
					log.info("version:"+version);
					String identity_id = paramJson.getString("identity_id");
					log.info("identity_id:"+identity_id);
					String source = paramJson.getString("source");
					log.info("source:"+source);
					String data = paramJson.getString("data");
					log.info("data:"+data);
					// 业务参数
					log.info("开始解析业务参数");
					JSONObject dataJson = JSONObject.fromObject(data);
					String orderId = dataJson.getString("orderId");
					log.info("orderId:"+orderId);
					// 作废
					log.info("准备作废订单-3");
					virtualCodeService.updateStatus(orderId, "3");
					log.info("作废成功");
					rj = new VGReturnJson("0", "ok");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			rj = new VGReturnJson("9999", e.getMessage());
		} finally {
			try {
				log.info("返回："+rj.getJo().toJSONString());
				response.setContentType("text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(rj.getJo().toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
