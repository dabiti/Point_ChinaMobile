package com.point.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/*
 * 虚拟商品工具类
 */
public class VirtualGoodsTool {

	private static final String DEFAULT_URI = "VGConfig";

	private static Map<String, String> PublicMap = new HashMap<String, String>();

	private static Logger log = Logger.getLogger(VirtualGoodsTool.class);
	
	static {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(DEFAULT_URI);
			PublicMap.put("version", bundle.getString("version"));
			PublicMap.put("identity_id", bundle.getString("identity_id"));
			PublicMap.put("source", bundle.getString("source"));
			PublicMap.put("secretkey", bundle.getString("secretkey"));
			PublicMap.put("url", bundle.getString("url"));

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

	public static void sendRequest(JSONObject userJson,String endUrl) {
		// 获得公共参数
		JSONObject pulibcJson = createPublicJson();
//		System.out.println("公共参数：" + pulibcJson.toString());
		// 组建data业务参数,将业务参数添加到公共参数中
		pulibcJson.put("data", userJson.toString());
//		System.out.println("所有参数参数：" + pulibcJson.toString());
		// 生成签名
		// 排序前
		String beforeSort = pulibcJson.toString();

		char[] chars = beforeSort.toCharArray();
		Arrays.sort(chars);
		// 排序后
		String afterSort = new String(chars);
//		System.out.println("排序后：" + afterSort);
		// 签名 = tolower（MD5（secretkey+排序后））
		String sign = MD5(PublicMap.get("secretkey") + afterSort).toLowerCase();
//		System.out.println("签名：" + sign);
		// 最终参数
//		System.out.println("sign:" + sign);
//		System.out.println("beforeSort:" + beforeSort);
		String req = null;
		try {
			req = "req=" + Base64Coder.encodeString(sign + beforeSort);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("最终：" + req);
		log.info("给移动商城发送请求,参数："+req);
		String returnStr = sendPost(PublicMap.get("url")+endUrl, req);
		log.info("移动商城返回："+returnStr);
	}

	// 获得公共参数
	private static JSONObject createPublicJson() {
		JSONObject jo = new JSONObject();
		jo.put("version", PublicMap.get("version"));
		jo.put("identity_id", PublicMap.get("identity_id"));
		jo.put("source", PublicMap.get("source"));
		return jo;
	}

	// 组建data业务参数

	public static String sendPost(String url, String param) {
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

	public final static String MD5(String s) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString(); // md5 32bit
			// result = buf.toString().substring(8, 24))); //md5 16bit
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 参数转化为签名
	public final static String toSign(String msg) {
		char[] chars = msg.toCharArray();
		Arrays.sort(chars);
		// 排序后
		String afterSort = new String(chars);
		System.out.println("排序后：" + afterSort);
		// 签名 = tolower（MD5（secretkey+排序后））
		return MD5(PublicMap.get("secretkey") + afterSort).toLowerCase();
	}

	// 验证签名
	public final static boolean verifySign(String msg, String sign) {
		String sign2 = toSign(msg);
		System.out.println("sign2:"+sign2);
		if (sign.equals(sign2)) {
			return true;
		}
		return false;
	}

	// 验证参数
	public final static boolean verifyParameterForNotifyOrder(String msg) {
		JSONObject jo = JSONObject.fromObject(msg);
		if (null != jo && jo.containsKey("version")
				&& jo.containsKey("identity_id") && jo.containsKey("source")
				&& jo.containsKey("data")) {
			JSONObject data = JSONObject.fromObject(jo.getString("data"));
			if (null != data && data.containsKey("orderId")
					&& data.containsKey("phone") && data.containsKey("itemId")
					&& data.containsKey("title") && data.containsKey("price")
					&& data.containsKey("quantity")
					&& data.containsKey("finalFee")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	
	
	
	// 验证参数
		public final static boolean verifyParameterForResendVirtualCode(String msg) {
			JSONObject jo = JSONObject.fromObject(msg);
			if (null != jo && jo.containsKey("version")
					&& jo.containsKey("identity_id") && jo.containsKey("source")
					&& jo.containsKey("data")) {
				JSONObject data = JSONObject.fromObject(jo.getString("data"));
				if (null != data && data.containsKey("orderId")) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
		
		
		
		
		// 验证参数
			public final static boolean verifyParameterForSetCodeInvalid(String msg) {
				JSONObject jo = JSONObject.fromObject(msg);
				if (null != jo && jo.containsKey("version")
						&& jo.containsKey("identity_id") && jo.containsKey("source")
						&& jo.containsKey("data")) {
					JSONObject data = JSONObject.fromObject(jo.getString("data"));
					if (null != data && data.containsKey("orderId")) {
						return true;
					} else {
						return false;
					}
				}
				return false;
			}


}

