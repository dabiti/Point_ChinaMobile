package com.point.web.entity;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * @Title: 
 * @author wcl
 * @Since: 2015年5月20日
 * @Version: 1.1.0
 */

public class VGReturnJson {
	

	private JSONObject jo;
	
	public VGReturnJson(){
		jo = new JSONObject();
		jo.put("code", "");
		jo.put("msg", "");
		jo.put("data", "");
	}
	
	
	public VGReturnJson(String code,String msg){
		jo = new JSONObject();
		jo.put("code", code);
		jo.put("msg", msg);
	}
	
	public VGReturnJson(List dataList){
		jo = new JSONObject();
		jo.put("code", "0000");
		jo.put("msg", "ok");
		Gson gson = new GsonBuilder().create();
		String dataJson = gson.toJson(dataList);
		jo.put("data", dataJson);
	}
	

	public JSONObject getJo() {
		if(null==jo){
			jo = new JSONObject();
			jo.put("code", "");
			jo.put("msg", "");
			jo.put("data", "");
		}
		return jo;
	}


	public void setJo(JSONObject jo) {
		this.jo = jo;
	}

	
	
}

