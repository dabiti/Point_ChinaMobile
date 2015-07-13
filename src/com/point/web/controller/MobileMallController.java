package com.point.web.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.point.web.entity.Order;
import com.point.web.entity.VGReturnJson;
import com.point.web.entity.VirtualCode;
import com.point.web.service.OrderService;
import com.point.web.service.VirtualCodeService;
import com.point.web.util.Base64Coder;
import com.point.web.util.BoleTool;
import com.point.web.util.CreateMenuTool;
import com.point.web.util.GsonTool;
import com.point.web.util.MMSTool;
import com.point.web.util.StringTool;
import com.point.web.util.VirtualGoodsTool;

/**
 * @Title: 移动商城Controller
 * @Description:虚拟码相关操作控制层、包括移动接口与伯乐接口
 * @Since: 2015年7月06日上午10:20:20
 * @author wangchunlong
 */
@Controller
public class MobileMallController {

	private static Logger log = Logger.getLogger(MobileMallController.class);

	@Resource
	private OrderService orderService;

	@Resource
	private VirtualCodeService virtualCodeService;

	@Resource
	private CreateMenuTool createMenuTool;

	@RequestMapping("/jumpToLoginView")
	public String jumpToLoginView(HttpServletRequest request,
			HttpServletResponse response) {
		// 如果已登录，直接跳到主页面，否则跳转到登陆页面
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated() == true) {
			request.setAttribute("menu", createMenuTool.getMenu(subject));
			return "main";
		} else
			return "login";
	}

	/**
	 * @Title:通知用户下单
	 * @Description: 1.接收移动商城请求 2.请求伯乐获取虚拟码 3.将虚拟码发送至客户 4.将虚拟码上传至移动商城
	 * @Since: 2015年7月6日上午10:30:52
	 * @author wangchunlong
	 */
	@RequestMapping("/notifyOrder")
	public void notifyOrder(HttpServletRequest request,
			HttpServletResponse response) {
		/************************************** 接收移动商城请求，保存订单 ******************************************/
		log.info("从移动商城接收订单");
		boolean receiveResult = false;
		VGReturnJson mallRj = new VGReturnJson("9999", "平台内部错误");
		String orderId = null;
		String itemId = null;
		Order order = null;
		String vcode = null;
		String vpass = null;
		String vcreatetime = null;
		String validityTime = null;
		String deliveryMsg = null;
		String templateId = null;
		try {
			String req = request.getParameter("req");
			log.info("报文req=" + req);
			if (null == req) {
				log.info("报文为空");
				mallRj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
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
					String phone = dataJson.getString("phone");
					itemId = dataJson.getString("itemId");
					String title = dataJson.getString("title");
					String price = dataJson.getString("price");
					String quantity = dataJson.getString("quantity");
					String finalFee = dataJson.getString("finalFee");
					String discount = dataJson.getString("discount");
					log.info("获得参数:" + "orderId=" + orderId + ",phone=" + phone
							+ ",itemId=" + itemId + ",title=" + title
							+ ",price=" + price + ",quantity=" + quantity
							+ ",finalFee=" + finalFee + ",discount=" + discount);
					// 重复
					order = orderService.get(orderId);
					log.info("查询订单是否已存在：" + order != null);
					if (null != order) {
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
			log.info("开始请求伯乐，获取虚拟码");
			order = orderService.get(orderId);
			String sendStr = GsonTool.objectToJsonDateSerializer(order,
					"yyyy-MM-dd HH:mm:ss");
			JSONObject sendJs = JSONObject.fromObject(sendStr);
			String boleStr = BoleTool.sendRequest(sendJs, "notifyOrder.do");
			// boleStr =
			// "{\"code\":0,\"msg\":\"成功\",\"virtualCode\":\"0135781716197897\",\"virtualCodePass\":\"071762\",\"validityTime\":\"2015-08-09\",\"deliveryMsg\":\"您可在m.uqianli.cn查询提货网点等信息\",\"templateId\":\"1\",\"createTime\":\"2015-07-10 10:23:17\"}";
			// 返回判断
			if (null != boleStr && !"".equals(boleStr)) {
				JSONObject jo = JSONObject.fromObject(boleStr);
				System.out.println(jo.containsKey("code"));
				System.out.println("0".equals(jo.getString("code")));
				if (jo.containsKey("code") && "0".equals(jo.getString("code"))) {
					// 解析出虚拟码
					vcode = jo.getString("virtualCode");
					vpass = jo.getString("virtualCodePass");
					vcreatetime = jo.getString("createTime");
					validityTime = jo.getString("validityTime");
					deliveryMsg = jo.getString("deliveryMsg");
					templateId = jo.getString("templateId");
					log.info("解析出参数:" + "virtualCode=" + vcode
							+ ",virtualCodePass=" + vpass + ",vcreatetime="
							+ vcreatetime + ",deliveryMsg=" + deliveryMsg
							+ ",templateId=" + templateId);
					// 保存
					VirtualCode vc = new VirtualCode();
					vc.setOrderId(orderId);
					vc.setVirtualCode(vcode);
					vc.setVirtualCodePass(vpass);
					vc.setCreateTime(vcreatetime);
					vc.setValidityTime(validityTime);
					vc.setDeliveryMsg(deliveryMsg);
					vc.setTemplateId(templateId);
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
					log.info("开始请求短信网关，给用户发送虚拟码短信");
					MMSReturnStr = MMSTool.sendMMS(order.getPhone(),
							order.getPrice() + "|" + vpass + "|" + deliveryMsg
									+ "|" + validityTime);
					if (null != MMSReturnStr && MMSReturnStr.contains("1000")) {
						log.info("发送成功,更新状态至1");
						MMSResult = true;
						// 更新状态，发送成功
						virtualCodeService.updateStatus(orderId, "1");
						log.info("更新完成");
					} else {
						// 更新状态，发送失败
						log.info("发送失败,更新状态至2");
						virtualCodeService.updateStatus(orderId, "2");
						log.info("更新完成");
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
					jcode.put("vcode",
							VirtualGoodsTool.getPublicMap().get("identity_id")
									+ "_" + vcode);
					jcode.put("vcodePass",
							VirtualGoodsTool.getPublicMap().get("identity_id")
									+ "_" + vpass);
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
							log.info("上传成功,更新状态至3");
							virtualCodeService.updateStatus(orderId, "3");
							log.info("更新完成");
						}
					} else {
						log.info("上传失败,更新状态至4");
						virtualCodeService.updateStatus(orderId, "4");
						log.info("更新完成");
					}
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
			String vcode = null;
			String vpass = null;
			String deliveryMsg = null;
			String validityTime = null;
			String req = request.getParameter("req");
			if (null != req) {
				log.info("报文为空");
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				log.info("报文req=" + req);
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
							String orderId = reqJson.getString("orderid");
							Order order = orderService.get(orderId);
							log.info("获得订单:" + order);
							if (null == order) {
								log.info("订单不存在");
								rj = new VGReturnJson("1011",
										"订单有误，订单信息错误或者订单不存在");
							} else {
								log.info("根据订单号查询虚拟码");
								VirtualCode vc = virtualCodeService
										.get(orderId);
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
											vcode = jo.getString("virtualCode");
											vpass = jo
													.getString("virtualCodePass");
											String vcreatetime = jo
													.getString("createTime");
											validityTime = jo
													.getString("validityTime");
											deliveryMsg = jo
													.getString("deliveryMsg");
											String templateId = jo
													.getString("templateId");
											log.info("解析出参数:" + "virtualCode="
													+ vcode
													+ ",virtualCodePass="
													+ vpass + ",vcreatetime="
													+ vcreatetime
													+ ",deliveryMsg="
													+ deliveryMsg
													+ ",templateId="
													+ templateId);
											// 保存
											vc = new VirtualCode();
											vc.setOrderId(orderId);
											vc.setVirtualCode(vcode);
											vc.setVirtualCodePass(vpass);
											vc.setCreateTime(vcreatetime);
											vc.setStatus("0");
											try {
												virtualCodeService.save(vc);
												// 通知移动商城发送的虚拟码
												JSONObject notifyCMjo = new JSONObject();
												notifyCMjo.put("orderId",
														orderId);
												notifyCMjo.put("itemId",
														order.getItemId());
												// 虚拟商品列表
												JSONObject jcode = new JSONObject();
												jcode.put("vcode", vcode);
												jcode.put("vcodePass", vpass);
												JSONArray codeList = new JSONArray();
												codeList.add(jcode);
												notifyCMjo.put("virtualCodes",
														codeList.toString());
												log.info("准备调用移动提交虚拟码接口,参数:"
														+ notifyCMjo);
												String returnStr = VirtualGoodsTool
														.sendRequest(
																notifyCMjo,
																"setVirtualCode");
												if (null != returnStr
														&& !"".equals(returnStr)) {
													JSONObject returnJson = JSONObject
															.fromObject(returnStr);
													if (returnJson
															.containsKey("code")
															&& "0".equals(returnJson
																	.get("code"))) {
														log.info("上传成功,更新状态至3");
														virtualCodeService
																.updateStatus(
																		orderId,
																		"3");
														log.info("更新完成");
													}
												} else {
													log.info("上传失败,更新状态至4");
													virtualCodeService
															.updateStatus(
																	orderId,
																	"4");
													log.info("更新完成");
												}
											} catch (Exception e) {
												log.error(e.getMessage());
												e.printStackTrace();
											}
										}
									}
								}

								log.info("获得虚拟码:" + vc);
								// 准备发送
								log.info("准备发送短信");
								String MMSReturnStr = MMSTool.sendMMS(
										order.getPhone(), order.getPrice()
												+ "|" + vpass + "|"
												+ deliveryMsg + "|"
												+ validityTime);
								if (null != MMSReturnStr
										&& MMSReturnStr.contains("1000")) {
									rj = new VGReturnJson("0", "ok");
									log.info("发送成功");
								} else {
									log.info("发送失败");
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
			String vcode = null;
			String vpass = null;
			String deliveryMsg = null;
			String validityTime = null;
			String req = request.getParameter("req");
			if (null == req) {
				log.info("报文为空");
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				log.info("报文req=" + req);
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
						// 如果虚拟码为空，则向伯乐后台重新获取虚拟码
						if (null == vc) {
							String sendStr = GsonTool
									.objectToJsonDateSerializer(order,
											"yyyy-MM-dd HH:mm:ss");
							JSONObject sendJs = JSONObject.fromObject(sendStr);
							String boleStr = BoleTool.sendRequest(sendJs,
									"notifyOrder.do");
							// 返回判断
							if (null != boleStr || !"".equals(boleStr)) {
								JSONObject jo = JSONObject.fromObject(boleStr);
								if (jo.containsKey("code")
										&& "0".equals(jo.get("code"))) {
									// 解析出虚拟码
									vcode = jo.getString("virtualCode");
									vpass = jo.getString("virtualCodePass");
									String vcreatetime = jo
											.getString("createTime");
									validityTime = jo.getString("validityTime");
									deliveryMsg = jo.getString("deliveryMsg");
									String templateId = jo
											.getString("templateId");
									log.info("解析出参数:" + "virtualCode=" + vcode
											+ ",virtualCodePass=" + vpass
											+ ",vcreatetime=" + vcreatetime
											+ ",deliveryMsg=" + deliveryMsg
											+ ",templateId=" + templateId);
									// 保存
									vc = new VirtualCode();
									vc.setOrderId(orderId);
									vc.setVirtualCode(vcode);
									vc.setVirtualCodePass(vpass);
									vc.setCreateTime(vcreatetime);
									vc.setStatus("0");
									try {
										virtualCodeService.save(vc);
										// 通知移动商城发送的虚拟码
										JSONObject notifyCMjo = new JSONObject();
										notifyCMjo.put("orderId", orderId);
										notifyCMjo.put("itemId",
												order.getItemId());
										// 虚拟商品列表
										JSONObject jcode = new JSONObject();
										jcode.put("vcode", vcode);
										jcode.put("vcodePass", vpass);
										JSONArray codeList = new JSONArray();
										codeList.add(jcode);
										notifyCMjo.put("virtualCodes",
												codeList.toString());
										log.info("准备调用移动提交虚拟码接口,参数:"
												+ notifyCMjo);
										String returnStr = VirtualGoodsTool
												.sendRequest(notifyCMjo,
														"setVirtualCode");
										if (null != returnStr
												&& !"".equals(returnStr)) {
											JSONObject returnJson = JSONObject
													.fromObject(returnStr);
											if (returnJson.containsKey("code")
													&& "0".equals(returnJson
															.get("code"))) {
												log.info("上传成功,更新状态至3");
												virtualCodeService
														.updateStatus(orderId,
																"3");
												log.info("更新完成");
											}
										} else {
											log.info("上传失败,更新状态至4");
											virtualCodeService.updateStatus(
													orderId, "4");
											log.info("更新完成");
										}
									} catch (Exception e) {
										log.error(e.getMessage());
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
								order.getPrice() + "|"
										+ vc.getVirtualCodePass() + "|"
										+ vc.getDeliveryMsg() + "|"
										+ vc.getValidityTime());
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
			if (null == req) {
				log.info("报文为空");
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				log.info("报文req=" + req);
				// Base64解密
				log.info("开始Base64解密");
				req = Base64Coder.decodeString(req);
				log.info("Base64解密后报文:" + req);
				// req = "{\"orderid\":\"123\",\"sign\":\"13581761989\"}";
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
							log.info("获得请求参数:" + req);
							JSONObject reqJson = JSONObject.fromObject(req);
							String orderId = reqJson.getString("orderId");
							String useId = reqJson.getString("useId");
							String useAmount = reqJson.getString("useAmount");
							String useDatetime = reqJson
									.getString("useDatetime");
							String useContent = reqJson.getString("useContent");
							Order order = orderService.get(orderId);
							log.info("获得订单:" + order);
							if (null == order) {
								log.info("订单不存在");
								rj = new VGReturnJson("1011",
										"订单有误，订单信息错误或者订单不存在");
							} else {
								log.info("更新虚拟码状态至5,添加消费内容");

								// virtualCodeService.updateStatus(
								// order.getOrderId(), "4");
								VirtualCode vc = new VirtualCode();
								vc.setOrderId(orderId);
								vc.setUseId(useId);
								vc.setUseAmount(useAmount);
								vc.setUseDatetime(useDatetime);
								vc.setUseContent(useContent);
								vc.setStatus("5");
								virtualCodeService.update(vc);
								vc = virtualCodeService.get(orderId);
								rj = new VGReturnJson("0", "ok");
								// TODO
								// 通知移动商城
								JSONObject jo = new JSONObject();
								// ArrayList<Record> list = new
								// ArrayList<Record>();
								JSONArray ja = new JSONArray();
								JSONObject recordJo = new JSONObject();
								recordJo.put("orderId", orderId);
								recordJo.put("itemId", order.getItemId());
								recordJo.put("useId", VirtualGoodsTool
										.getPublicMap().get("identity_id")
										+ "_" + vc.getUseId());
								recordJo.put("virtualCode", VirtualGoodsTool
										.getPublicMap().get("identity_id")
										+ "_" + vc.getVirtualCode());
								recordJo.put(
										"virtualCodePass",
										VirtualGoodsTool.getPublicMap().get(
												"identity_id")
												+ "_" + vc.getVirtualCodePass());
								recordJo.put("useAmount", vc.getUseAmount());
								recordJo.put("useDatetime", vc.getUseDatetime());
								recordJo.put("useContent", vc.getUseContent());
								recordJo.put("phone", order.getPhone());
								ja.add(recordJo);
								String listJson = ja.toString();
								jo.put("recordList", listJson);
								log.info("准备通知移动消费情况，参数" + jo);
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
		log.info("准备失效虚拟码");
		// 根据请求参数查询到该订单的虚拟码
		VGReturnJson rj = new VGReturnJson("9999", "平台内部错误");
		String orderId = null;
		try {
			String req = request.getParameter("req");
			if (null == req) {
				log.info("报文为空");
				rj = new VGReturnJson("1002", "缺少参数，某个必须传递的参数您没有传递");
			} else {
				log.info("报文req=" + req);
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
					log.info("准备作废订单-6");
					virtualCodeService.updateStatus(orderId, "6");
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
			String boleStr = BoleTool.sendRequest(sendBoleJs,
					"notifyinvalid.do");
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
