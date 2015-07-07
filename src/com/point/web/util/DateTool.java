package com.point.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTool {

	
	
	public static String getCurrentDateTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		return date;
	}
}
