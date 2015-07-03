package com.point.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


/*
 * MMS配置类
 */
public class MMSTool {


	private static final String DEFAULT_URI = "MMSConfig";
	
	private static Map<String, String> MMSMap = new HashMap<String,String>();
	
	private static Logger log = Logger.getLogger(MMSTool.class);
	
	static {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(DEFAULT_URI);			
			MMSMap.put("Host", bundle.getString("Host"));
			MMSMap.put("User-Agent", bundle.getString("User-Agent"));
			MMSMap.put("Content-Type", bundle.getString("Content-Type"));
			MMSMap.put("Cmd", bundle.getString("Cmd"));
			MMSMap.put("Connection", bundle.getString("Connection"));
			MMSMap.put("userId", bundle.getString("userId"));
			MMSMap.put("password", bundle.getString("password"));
			MMSMap.put("templateId", bundle.getString("templateId"));
			MMSMap.put("port", bundle.getString("port"));
			MMSMap.put("signature", bundle.getString("signature"));

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
//			log.error(sw.toString());
		}
	}
	
	static public Map<String, String> getMMSMap() {
		return MMSMap;
	}
	
	public static String sendMMS(String phone,String data)throws Exception{
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("phone", phone);
		userMap.put("data", data);
    	log.info("准备组建发送字符串");
		String sendStr = createSendStr(userMap);
		log.info("发送字符串sendStr:"+sendStr);
		log.info("准备发送");
		String returnStr = sendPost(sendStr,userMap);
		log.info("短信网关返回:"+returnStr);
		return returnStr;
	}
	
	private static String createSendStr(Map<String,String> userMap){
		String sendStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    			+ "<request>"
    			+ "<userId>"+MMSMap.get("userId")+"</userId>"
    			+ "<password>"+MMSMap.get("password")+"</password>"
    			+ "<templateId>"+MMSMap.get("templateId")+"</templateId>"
    			+ "<phone>"+userMap.get("phone")+"</phone>"
    			+ "<port>"+MMSMap.get("port")+"</port>"
    			+ "<data><![CDATA["+userMap.get("data")+"]]></data>"
    			+ "<signature>"+MMSMap.get("signature")+"</signature>"
    			+ "</request>";
		return sendStr;
	}
	
	
	
	private static String sendPost(String param,Map<String,String> userMap) throws Exception {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(MMSMap.get("Host"));
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
//	            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
//	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Accept-Charset", "utf-8");
	            conn.setRequestProperty("Charsert", "UTF-8");
	            conn.setUseCaches(false);
	            // 设置通用的请求属性
	            conn.setRequestProperty("Host",MMSMap.get("Host"));
	            conn.setRequestProperty("User-Agent",
	            		"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
	            conn.setRequestProperty("Content-Type","text/xml");
	            conn.setRequestProperty("Cmd",MMSMap.get("Cmd"));
	            String ts = String.valueOf(new Date().getTime());
	            conn.setRequestProperty("TS",ts);

	            conn.setRequestProperty("Authorization", StringTool.MD5_32(param + ts + MMSMap.get("password")));
	            conn.setRequestProperty("Content-Length",String.valueOf(param.length()));
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            
	            conn.setRequestProperty("accept", "*/*");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
//	            param = URLEncoder.encode(param);
//	            param = URIUtil.encodePath(param, "UTF-8");
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
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	            throw new Exception(e);
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }    
	 
	 
	 
//	  public final static String MD5(String s) {
//	        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//	                'A', 'B', 'C', 'D', 'E', 'F' };
//	        try {
//	            byte[] btInput = s.getBytes();
//	            // 获得MD5摘要算法的 MessageDigest 对象
//	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
//	            // 使用指定的字节更新摘要
//	            mdInst.update(btInput);
//	            // 获得密文
//	            byte[] md = mdInst.digest();
//	            // 把密文转换成十六进制的字符串形式
//	            int j = md.length;
//	            char str[] = new char[j * 2];
//	            int k = 0;
//	            for (int i = 0; i < j; i++) {
//	                byte byte0 = md[i];
//	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//	                str[k++] = hexDigits[byte0 & 0xf];
//	            }
//	            return new String(str);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return null;
//	        }
//	    }
}
