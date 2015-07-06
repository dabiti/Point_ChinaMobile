package com.point.web.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
/**
 * @Title:web工具类
 * @Description:对web参数进行处理的类
 * @Since: 2015年7月06日上午11:18:32
 * @author guowanyu
 */
public class WebUtils {
	public static final String SUCCESS_CODE = "901";
	public static final String WARNING_CODE = "909";
	
	public static Map<String,Object> convertRequestParamToMap(HttpServletRequest request){
		Map<String,Object> resMap = new HashMap<String, Object>();
		Enumeration<?> parNames = request.getParameterNames();
		String tempPName = null;
		while(parNames.hasMoreElements()){
			tempPName = (String)parNames.nextElement();
			resMap.put(tempPName, request.getParameter(tempPName));
		}
		return resMap;
	} 
	
	public static String getParameterStr(HttpServletRequest request,String parName){
		Map<String,Object> resMap =  convertRequestParamToMap(request);
		return (String)resMap.get(parName);
	}
	
	public static void writeWarningMsg(HttpServletResponse response,String msg,String data){
		JSONObject object = new JSONObject();
		
		Map<String,Object> resMap = new HashMap<String, Object>();
		resMap.put("code", WARNING_CODE);
		resMap.put("msg", msg);
		resMap.put("data", data);
		object.putAll(resMap);
		
		writeResult(response, object);
	}
	
	
	public static void writeWarningMsg(HttpServletResponse response,String msg){
		JSONObject object = new JSONObject();
		object.put("code", WARNING_CODE);
		object.put("msg", msg);
		
		writeResult(response, object);
	}
	
	public static void writeSuccessMsg(HttpServletResponse response,String msg,String data){
		JSONObject object = new JSONObject();
		object.put("code", SUCCESS_CODE);
		object.put("msg", msg);
		object.put("data", data);
		
		writeResult(response, object);
	}
	
	public static void writeSuccessMsg(HttpServletResponse response,String msg){
		JSONObject object = new JSONObject();
		
		Map<String,Object> resMap = new HashMap<String, Object>();
		resMap.put("code", SUCCESS_CODE);
		resMap.put("msg", msg);
		object.putAll(resMap);
		
		writeResult(response, object);
	}
	
	private static void writeResult(HttpServletResponse response,JSONObject object){
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(object.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
