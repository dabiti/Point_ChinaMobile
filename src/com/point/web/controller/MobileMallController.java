package com.point.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.google.gson.Gson;
import com.point.web.entity.Order;
import com.point.web.entity.VGReturnJson;
import com.point.web.entity.VirtualCode;
import com.point.web.service.OrderService;
import com.point.web.service.VirtualCodeService;
import com.point.web.util.Base64Coder;
import com.point.web.util.BoleTool;
import com.point.web.util.GsonTool;
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
		// Subject subject = SecurityUtils.getSubject();
		// if (subject.isRemembered() == true || subject.isAuthenticated() ==
		// true) {
		return "main";
		// } else
		// return "login";
	}

	// ***正式编码***//
	@RequestMapping("/notifyOrder")
	public void notifyOrder(HttpServletRequest request,
			HttpServletResponse response) {
		/************************************** 接收移动商城请求，保存订单 ******************************************/
		log.info("准备下发通知");
		boolean receiveResult = false;
		VGReturnJson mallRj = new VGReturnJson("9999", "平台内部错误");
		String orderId = null;
		String itemId = null;
		Order order = null;
		String vcode = null;
		try {
			String req = request.getParameter("req");
			log.info("收到报文:" + req);
			if (null != req) {
				log.info("报文为空");
				mallRj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				// Base64解密
				log.info("开始Base64解密");
				// req = req.replace(" ", "+");
				// req = Base64Coder.decodeString(req);
				req = "df882ed62782ddf7cd7afbc73f762411{\"source\":\"2\",\"version\":\"1.0\",\"identity_id\":\"123456\","
						+ "\"data\":{\"orderId\":\"123\",\"phone\":\"13581761989\",\"itemId\":\"123\",\"title\":\"123\",\"price\":\"123\",\"quantity\":\"123\",\"finalFee\":\"123\",\"discount\":\"123\"}}";
				log.info("Base64解密后报文:" + req);
				// 签名
				String sign = req.substring(0, req.indexOf("{"));
				log.info("获得签名sign:" + sign);
				// 参数
				String jsonStr = req.substring(req.indexOf("{"), req.length());
				log.info("参数为:" + jsonStr);
				// 验证签名
				log.info("开始验证签名");
				if (!VirtualGoodsTool.verifySign(jsonStr, sign)) {
					log.error("验证签名未通过");
					mallRj = new VGReturnJson("1001",
							"验签失败，您所传递的数据签名在API服务器端没有验证通过");
				} else if (!VirtualGoodsTool
						.verifyParameterForNotifyOrder(jsonStr)) {
					log.error("缺少参数");
					mallRj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
				} else if (!StringTool.isUTF8(jsonStr)) {
					log.error("错误的编码格式");
					mallRj = new VGReturnJson("1006", "错误的编码格式");
				} else {
					log.info("开始解析公共参数");
					// 公共参数
					JSONObject paramJson = JSONObject.fromObject(jsonStr);
					String version = paramJson.getString("version");
					log.info("version:" + version);
					String identity_id = paramJson.getString("identity_id");
					log.info("identity_id:" + identity_id);
					String source = paramJson.getString("source");
					log.info("source:" + source);
					String data = paramJson.getString("data");
					log.info("data:" + data);
					log.info("开始解析业务参数");
					// 业务参数
					JSONObject dataJson = JSONObject.fromObject(data);
					orderId = dataJson.getString("orderId");
					log.info("orderId:" + orderId);
					String phone = dataJson.getString("phone");
					log.info("phone:" + phone);
					itemId = dataJson.getString("itemId");
					log.info("itemId:" + itemId);
					String title = dataJson.getString("title");
					log.info("title:" + title);
					String price = dataJson.getString("price");
					log.info("price:" + price);
					String quantity = dataJson.getString("quantity");
					log.info("quantity:" + quantity);
					String finalFee = dataJson.getString("finalFee");
					log.info("finalFee:" + finalFee);
					String discount = dataJson.getString("discount");
					log.info("discount:" + discount);
					// 重复
					order = orderService.get(orderId);
					log.info("查询订单是否已存在：" + order);
					if (null != order) {
						log.info("订单已重复");
						mallRj = new VGReturnJson("1009", "重复订单");
						// 插入数据库记录
					} else {
						order = new Order(orderId, phone, itemId, title, price,
								quantity, finalFee, discount);
						// 保存订单
						orderService.save(order);
						mallRj = new VGReturnJson("0", "ok");
						receiveResult = true;
					}
				}
			}
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
			log.error("参数格式错误");
			mallRj = new VGReturnJson("1003", "参数格式错误或不在正确范围内");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			mallRj = new VGReturnJson("9999", e.getMessage());
		} finally {
			try {
				log.error("返回:" + mallRj.getJo().toJSONString());
				response.setContentType("text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(mallRj.getJo().toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/************************************** 请求伯乐，获取虚拟码 ******************************************/
		boolean boleResult = false;
		if (orderId != null && receiveResult) {
			order = orderService.get(orderId);
			String sendStr = GsonTool.objectToJsonDateSerializer(order,
					"yyyy-MM-dd HH:mm:ss");
			JSONObject sendJs = JSONObject.fromObject(sendStr);
			String boleStr = BoleTool.sendRequest(sendJs, "notifyOrder.do");
			// TODO
			boleStr = "{\"code\":\"0\",\"vcode\":\"a1b2c3\",\"msg\":\"\"}";
			// 返回判断
			if (null != boleStr || !"".equals(boleStr)) {
				JSONObject jo = JSONObject.fromObject(boleStr);
				if (jo.containsKey("code") && "0".equals(jo.get("code"))) {
					// 解析出虚拟码
					vcode = (String) jo.get("vcode");
					// 保存
					VirtualCode vc = new VirtualCode();
					vc.setOrderid(orderId);
					vc.setVcode(vcode);
					vc.setStatus("0");
					try {
						virtualCodeService.save(vc);
						boleResult = true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			/************************************** 请求短信网关，给用户发送虚拟码短信 ******************************************/
			boolean MMSResult = false;
			if (vcode != null && boleResult) {
				String MMSReturnStr;
				try {
					MMSReturnStr = MMSTool.sendMMS(order.getPhone(), vcode);
					if (null != MMSReturnStr && MMSReturnStr.contains("1000")) {
						MMSResult = true;
						// 更新状态，发送成功
						virtualCodeService.updateStatus(orderId, "1");
					} else {
						// 更新状态，发送失败
						virtualCodeService.updateStatus(orderId, "2");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/************************************** 请求移动商城，上传虚拟码 ******************************************/
				if (MMSResult) {
					// 通知移动商城发送的虚拟码
					JSONObject notifyCMjo = new JSONObject();
					notifyCMjo.put("orderId", orderId);
					notifyCMjo.put("itemId", itemId);
					// 虚拟商品列表
					JSONObject jcode = new JSONObject();
					jcode.put("vcode", vcode);
					jcode.put("vcodePass", vcode);
					JSONArray codeList = new JSONArray();
					codeList.add(jcode);
					notifyCMjo.put("virtualCodes", codeList.toString());
					log.info("准备调用移动提交虚拟码接口,参数:" + notifyCMjo);
					String returnStr = VirtualGoodsTool.sendRequest(notifyCMjo,
							"setVirtualCode");
					if (null != returnStr && !"".equals(returnStr)) {
						JSONObject returnJson = JSONObject
								.fromObject(returnStr);
						if (returnJson.containsKey("code")
								&& "0".equals(returnJson.get("code"))) {
							log.info("调用成功");
						}
					}
					log.info("调用失败");
				}
			}
		}
	}

	// 重新发送虚拟码(伯乐)
	@RequestMapping("/resendVirtualCodeForBole")
	public void resendVirtualCodeForBole(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("准备重发虚拟码for伯乐");
		VGReturnJson rj = new VGReturnJson("9999", "平台内部错误");
		try {
			String req = request.getParameter("req");
			if (null != req) {
				log.info("报文为空");
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				// Base64解密
				log.info("开始Base64解密");
				// req = Base64Coder.decodeString(req);
				log.info("Base64解密后报文:" + req);
				req = "{\"orderid\":\"123\",\"sign\":\"13581761989\"}";
				if (!StringTool.isUTF8(req)) {
					log.error("错误的编码格式");
					rj = new VGReturnJson("1006", "错误的编码格式");
				} else {
					if (!BoleTool.verifyParameterForResendVirtualCode(req)) {
						log.error("缺少参数");
						rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
					} else {
						// 验证签名
						if (!BoleTool.verifySign(req)) {
							log.error("验证签名未通过");
							rj = new VGReturnJson("1001",
									"验签失败，您所传递的数据签名在API服务器端没有验证通过");
						} else {
							JSONObject reqJson = JSONObject.fromObject(req);
							String orderid = reqJson.getString("orderid");
							Order order = orderService.get(orderid);
							log.info("获得订单:" + order);
							if (null == order) {
								log.info("订单不存在");
								rj = new VGReturnJson("1011",
										"订单有误，订单信息错误或者订单不存在");
							} else {
								log.info("根据订单号查询虚拟码");
								VirtualCode vc = virtualCodeService
										.get(orderid);
								log.info("获得虚拟码:" + vc);
								// 准备发送
								log.info("准备发送短信");
								String MMSReturnStr = MMSTool.sendMMS(
										order.getPhone(), vc.getVcode());
								if (null != MMSReturnStr
										&& MMSReturnStr.contains("1000")) {
									// 发送成功
									rj = new VGReturnJson("0", "ok");
								}
							}
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
				log.info("返回：" + rj.getJo().toJSONString());
				response.setContentType("text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(rj.getJo().toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 重新发送虚拟码(移动商城)
	@RequestMapping("/resendVirtualCode")
	public void resendVirtualCode(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("准备重发虚拟码");
		// 根据请求参数查询到该订单的虚拟码
		VGReturnJson rj = new VGReturnJson("9999", "平台内部错误");
		try {
			String req = request.getParameter("req");
			if (null != req) {
				log.info("报文为空");
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				// Base64解密
				log.info("开始Base64解密");
				// req = req.replace(" ", "+");
				// req = Base64Coder.decodeString(req);
				req = "df882ed62782ddf7cd7afbc73f762411{\"source\":\"2\",\"version\":\"1.0\",\"identity_id\":\"123456\","
						+ "\"data\":{\"orderId\":\"123\",\"phone\":\"13581761989\",\"itemId\":\"123\",\"title\":\"123\",\"price\":\"123\",\"quantity\":\"123\",\"finalFee\":\"123\",\"discount\":\"123\"}}";
				log.info("Base64解密后报文:" + req);
				// 签名
				String sign = req.substring(0, req.indexOf("{"));
				log.info("获得签名sign:" + sign);
				// 参数
				String jsonStr = req.substring(req.indexOf("{"), req.length());
				log.info("参数为:" + jsonStr);
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
					log.info("version:" + version);
					String identity_id = paramJson.getString("identity_id");
					log.info("identity_id:" + identity_id);
					String source = paramJson.getString("source");
					log.info("source:" + source);
					String data = paramJson.getString("data");
					log.info("data:" + data);
					// 业务参数
					log.info("开始解析业务参数");
					JSONObject dataJson = JSONObject.fromObject(data);
					String orderId = dataJson.getString("orderId");
					log.info("orderId:" + orderId);
					Order order = orderService.get(orderId);
					log.info("获得订单:" + order);
					if (null == order) {
						log.info("订单不存在");
						rj = new VGReturnJson("1011", "订单有误，订单信息错误或者订单不存在");
					} else {
						log.info("根据订单号查询虚拟码");
						VirtualCode vc = virtualCodeService.get(orderId);
						log.info("获得虚拟码:" + vc);
						// 准备发送
						log.info("准备发送短信");
						String MMSReturnStr = MMSTool.sendMMS(order.getPhone(),
								vc.getVcode());
						if (null != MMSReturnStr
								&& MMSReturnStr.contains("1000")) {
							// 发送成功
							rj = new VGReturnJson("0", "ok");
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
				log.info("返回：" + rj.getJo().toJSONString());
				response.setContentType("text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(rj.getJo().toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 设置虚拟码失效（移动）
	@RequestMapping("/setCodeInvalid")
	public void setCodeInvalid(HttpServletRequest request,
			HttpServletResponse response) {
		/************************************** 接收移动商城请求，作废虚拟码 ******************************************/
		// 根据请求参数查询到该订单的虚拟码
		VGReturnJson rj = new VGReturnJson("9999", "平台内部错误");
		String orderId = null;
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
				// req =
				// "df882ed62782ddf7cd7afbc73f762411{\"source\":\"2\",\"version\":\"1.0\",\"identity_id\":\"123456\","
				// +
				// "\"data\":{\"orderId\":\"123\",\"phone\":\"13581761989\",\"itemId\":\"123\",\"title\":\"123\",\"price\":\"123\",\"quantity\":\"123\",\"finalFee\":\"123\",\"discount\":\"123\"}}";
				log.info("Base64解密后报文:" + req);
				// 签名
				String sign = req.substring(0, req.indexOf("{"));
				log.info("获得签名sign:" + sign);
				// 参数
				String jsonStr = req.substring(req.indexOf("{"), req.length());
				log.info("参数为:" + jsonStr);
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
					log.info("version:" + version);
					String identity_id = paramJson.getString("identity_id");
					log.info("identity_id:" + identity_id);
					String source = paramJson.getString("source");
					log.info("source:" + source);
					String data = paramJson.getString("data");
					log.info("data:" + data);
					// 业务参数
					log.info("开始解析业务参数");
					JSONObject dataJson = JSONObject.fromObject(data);
					orderId = dataJson.getString("orderId");
					log.info("orderId:" + orderId);
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
				log.info("返回：" + rj.getJo().toJSONString());
				response.setContentType("text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(rj.getJo().toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/************************************** 请求伯乐，作废伯乐虚拟码 ******************************************/
		if (orderId != null) {
			JSONObject sendBoleJs = new JSONObject();
			sendBoleJs.put("orderid", orderId);
			String boleStr = BoleTool.sendRequest(sendBoleJs, "notifyOrder.do");
			// 返回判断
			if (null != boleStr || !"".equals(boleStr)) {
				JSONObject jo = JSONObject.fromObject(boleStr);
				if (jo.containsKey("code") && "0".equals(jo.get("code"))) {
					log.info("请求伯乐设置虚拟码失效接口成功");
				} else {
					log.info("请求伯乐设置虚拟码失效接口失败:" + boleStr);
				}
			}
		}

	}

}
