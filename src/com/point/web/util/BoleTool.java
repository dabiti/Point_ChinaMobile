package com.point.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/**
 * @Title:伯乐接口工具类
 * @Description:发送请求
 * @Since: 2015年7月06日上午11:17:35
 * @author wangchunlong
 */
public class BoleTool {

	private static final String DEFAULT_URI = "BoleConfig";

	private static Map<String, String> PublicMap = new HashMap<String, String>();

	private static Logger log = Logger.getLogger(BoleTool.class);
	
	static {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(DEFAULT_URI);
			PublicMap.put("url", bundle.getString("url"));
			PublicMap.put("secretKey", bundle.getString("secretKey"));

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			// log.error(sw.toString());
		}
	}

	static public Map<String, String> getPublicMap() {
		return PublicMap;
	}

	public static String sendRequest(JSONObject userJson,String endUrl) {
		String returnStr = null;
		// 生成签名
		String sign = creatSign(userJson);
		//将签名加入参数
		userJson.put("sign", sign);
		log.info("加密前所有参数:"+userJson);
		String req = null;
		try {
			req = "req=" + Base64Coder.encodeString(userJson.toString());
			log.info("给伯乐后台发送请求,参数："+req);
			returnStr = sendPost(PublicMap.get("url")+endUrl, req);
			log.info("伯乐后台返回："+returnStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return returnStr;
	}


	private static String creatSign(JSONObject userJson){
		List<String> keyList = new ArrayList<String>();
			for (Object key : userJson.keySet()) {  
			      keyList.add(key.toString());
			}  
	        Collections.sort(keyList);
	        StringBuffer valueSb = new StringBuffer();
	        for(String i : keyList){
	        	valueSb.append(userJson.get(i));
	        }
	        System.out.println("secretKey:" + PublicMap.get("secretKey"));
	        return StringTool.MD5_32(PublicMap.get("secretKey")+valueSb.toString()).toLowerCase();
	}
	
	
	
	// 组建data业务参数

	private static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	

	// 验证签名
	public final static boolean verifySign(String msg) {
		JSONObject reqJson = JSONObject.fromObject(msg);
		String sign  = (String) reqJson.get("sign");
		reqJson.remove("sign");
		String sign2 = creatSign(reqJson);
		if (sign.equals(sign2)) {
			return true;
		}
		return false;
	}

	// 验证参数
	public final static boolean verifyParameterForConsumeVCfromBole(String msg) {
		JSONObject jo = JSONObject.fromObject(msg);
		if (null != jo && jo.containsKey("orderId")&& jo.containsKey("sign")&& jo.containsKey("useId")&& jo.containsKey("sign")&& jo.containsKey("useAmount")&& jo.containsKey("useDatetime")&& jo.containsKey("useContent")){
			return true;
		}
		return false;
	}
	
	
	// 验证参数
		public final static boolean verifyParameterForResendVirtualCode(String msg) {
			JSONObject jo = JSONObject.fromObject(msg);
			if (null != jo && jo.containsKey("orderid")&& jo.containsKey("sign")){
				return true;
			}
			return false;
		}
	
	

}

