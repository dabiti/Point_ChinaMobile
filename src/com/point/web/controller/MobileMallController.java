package com.point.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.misc.BASE64Encoder;

import com.google.gson.Gson;
import com.point.web.entity.Order;
import com.point.web.entity.Record;
import com.point.web.entity.VGReturnJson;
import com.point.web.entity.VirtualCode;
import com.point.web.service.OrderService;
import com.point.web.service.VirtualCodeService;
import com.point.web.util.Base64Coder;
import com.point.web.util.BoleTool;
import com.point.web.util.CookieUtil;
import com.point.web.util.CreateMenuTool;
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

	@Resource
	private CreateMenuTool createMenuTool;

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
			HttpServletResponse response,String username,String password,String rp) throws Exception {
		// 如果已登录，直接跳到主页面，否则跳转到登陆页面
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated() == true) {
			request.setAttribute("menu", createMenuTool.getMenu(subject));
			String ck=username+":"+password;
			String cks=CookieUtil.encrypt(ck);
			
			String base=new BASE64Encoder().encode(cks.getBytes());
			System.err.println(base);
			Cookie cookie = new Cookie("name", base);
			if("ever".equals(rp)){
				cookie.setMaxAge(Integer.MAX_VALUE);
			}else{
				cookie.setMaxAge(0);
			}
			response.addCookie(cookie);
			return "main";
		} else
			return "login";
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
		String vpass = null;
		String vcreatetime = null;
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
						log.info("准备保存订单:" + order);
						orderService.save(order);
						log.info("保存完成");
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
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}

		/************************************** 请求伯乐，获取虚拟码 ******************************************/
		boolean boleResult = false;
		if (receiveResult) {
			order = orderService.get(orderId);
			String sendStr = GsonTool.objectToJsonDateSerializer(order,
					"yyyy-MM-dd HH:mm:ss");
			JSONObject sendJs = JSONObject.fromObject(sendStr);
			log.info("准备请求伯乐，获取虚拟码");
			String boleStr = BoleTool.sendRequest(sendJs, "notifyOrder.do");
			boleStr = "{\"code\":\"0\",\"vcode\":\"a1b2c3\",\"msg\":\"\"}";
			log.info("伯乐返回:" + boleStr);
			// 返回判断
			if (null != boleStr || !"".equals(boleStr)) {
				JSONObject jo = JSONObject.fromObject(boleStr);
				if (jo.containsKey("code") && "0".equals(jo.get("code"))) {
					// 解析出虚拟码
					vcode = jo.getString("virtualCode");
					vpass = jo.getString("virtualCodePass");
					vcreatetime = jo.getString("createTime");
					// 保存
					VirtualCode vc = new VirtualCode();
					vc.setOrderId(orderId);
					vc.setVirtualCode(vcode);
					vc.setVirtualCodePass(vpass);
					vc.setCreateTime(vcreatetime);
					vc.setStatus("0");
					try {
						log.info("准备保存虚拟码:" + vc);
						virtualCodeService.save(vc);
						log.info("保存完成");
						boleResult = true;
					} catch (Exception e) {
						log.error(e.getMessage());
						e.printStackTrace();
					}
				}
			}
			/************************************** 请求短信网关，给用户发送虚拟码短信 ******************************************/
			boolean MMSResult = false;
			if (vcode != null && boleResult) {
				String MMSReturnStr;
				try {
					log.info("准备请求短信网关，给用户发送虚拟码短信");
					MMSReturnStr = MMSTool.sendMMS(order.getPhone(), vcode);
					log.info("短信网关返回：" + MMSReturnStr);
					if (null != MMSReturnStr && MMSReturnStr.contains("1000")) {
						log.info("发送成功");
						MMSResult = true;
						// 更新状态，发送成功
						virtualCodeService.updateStatus(orderId, "1");
					} else {
						// 更新状态，发送失败
						log.info("发送失败");
						virtualCodeService.updateStatus(orderId, "2");
					}
				} catch (Exception e) {
					log.error(e.getMessage());
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
					jcode.put("vcodePass", vpass);
					JSONArray codeList = new JSONArray();
					codeList.add(jcode);
					notifyCMjo.put("virtualCodes", codeList.toString());
					log.info("准备调用移动提交虚拟码接口,参数:" + notifyCMjo);
					String returnStr = VirtualGoodsTool.sendRequest(notifyCMjo,
							"setVirtualCode");
					log.info("移动商城返回：" + returnStr);
					if (null != returnStr && !"".equals(returnStr)) {
						JSONObject returnJson = JSONObject
								.fromObject(returnStr);
						if (returnJson.containsKey("code")
								&& "0".equals(returnJson.get("code"))) {
							log.info("上传成功");
						}
					}
					log.info("上传失败");
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
								// 如果虚拟码为空，则向伯乐后台重新获取虚拟码
								if (null == vc) {
									String sendStr = GsonTool
											.objectToJsonDateSerializer(order,
													"yyyy-MM-dd HH:mm:ss");
									JSONObject sendJs = JSONObject
											.fromObject(sendStr);
									String boleStr = BoleTool.sendRequest(
											sendJs, "notifyOrder.do");
									// 返回判断
									if (null != boleStr || !"".equals(boleStr)) {
										JSONObject jo = JSONObject
												.fromObject(boleStr);
										if (jo.containsKey("code")
												&& "0".equals(jo.get("code"))) {
											// 解析出虚拟码
											String vcode = jo
													.getString("virtualCode");
											String vpass = jo
													.getString("virtualCodePass");
											String vcreatetime = jo
													.getString("createTime");
											// 保存
											vc = new VirtualCode();
											vc.setOrderId(orderid);
											vc.setVirtualCode(vcode);
											vc.setVirtualCodePass(vpass);
											vc.setCreateTime(vcreatetime);
											vc.setStatus("0");
											try {
												virtualCodeService.save(vc);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}

								log.info("获得虚拟码:" + vc);
								// 准备发送
								log.info("准备发送短信");
								String MMSReturnStr = MMSTool.sendMMS(
										order.getPhone(),
										vc.getVirtualCodePass());
								if (null != MMSReturnStr
										&& MMSReturnStr.contains("1000")) {
									rj = new VGReturnJson("0", "ok");
									// 更新状态，发送成功
									virtualCodeService.updateStatus(orderid,
											"1");
								} else {
									// 更新状态，发送失败
									virtualCodeService.updateStatus(orderid,
											"2");
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
								vc.getVirtualCodePass());
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

	// 消费接口（伯乐）
	@RequestMapping("/consumeVC")
	public void consumeVC(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("准备接收消费的虚拟码from伯乐");
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
					if (!BoleTool.verifyParameterForConsumeVCfromBole(req)) {
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
								log.info("更新虚拟码状态至4");
								VirtualCode vc = virtualCodeService
										.get(orderid);
								virtualCodeService.updateStatus(
										order.getOrderId(), "4");
								// TODO
								// 通知移动商城
								JSONObject jo = new JSONObject();
								ArrayList<Record> list = new ArrayList<Record>();
								Record rd = new Record();
								rd.setVirtualCode(VirtualGoodsTool
										.getPublicMap().get("identity_id")
										+ "_" + vc.getVirtualCode());
								rd.setVirtualCodePass(VirtualGoodsTool
										.getPublicMap().get("identity_id")
										+ "_" + vc.getVirtualCodePass());
								rd.setOrderId(orderid);
								rd.setItemId(order.getItemId());
								rd.setUseId(VirtualGoodsTool.getPublicMap()
										.get("identity_id")
										+ "_"
										+ vc.getUseId());
								rd.setUseAmount(order.getFinalFee());
								rd.setUseContent(vc.getUseContent());
								rd.setUseDatetime(vc.getUseDatetime());
								rd.setPhone(order.getPhone());
								list.add(rd);
								Gson gg = new Gson();
								String listJson = gg.toJson(list);
								jo.put("recordList", listJson);
								VirtualGoodsTool.sendRequest(jo, "setRecord");
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
					virtualCodeService.updateStatus(orderId, "4");
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
