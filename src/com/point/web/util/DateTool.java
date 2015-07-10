package com.point.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Title:日期工具类
 * @Description:
 * @Since: 2015年7月06日上午11:17:35
 * @author wangchunlong
 */
public class DateTool {

	
	
	public static String getCurrentDateTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		return date;
	}
}
