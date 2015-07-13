/**   
* @Title: TimeUtils.java 
* @Package com.point.web.util 
* @Description: TODO
* @author guowanyu   
* @date 2015年7月10日 下午2:24:44 
* @version V1.0   
*/
package com.point.web.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @title :
 * @author : 
 * @since：2015年7月10日 下午2:24:44 
 * @Description:
 */
public class TimeUtils {
	public static long differCodeSendTimeMillis = 0l; //发送短信验证码的间隔是3分钟
	public static long differCodeValidTimeMillis = 0l;//短信验证码的有效期是30分钟内
	
	private static final String DEFAULT_URI = "passGetBack";
	
	static {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(DEFAULT_URI);	
			differCodeSendTimeMillis = Integer.parseInt(bundle.getString("differCodeSendTimeMillis")) * 60 * 1000;
			differCodeValidTimeMillis = Integer.parseInt(bundle.getString("differCodeValidTimeMillis")) * 60 * 1000;

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
		}
	}
	
	public static long differSeconds(Date startDate,Date endDate){
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(endDate);
		long endMillis = cal.getTimeInMillis();
		
		cal.setTime(startDate);
		long startMillis = cal.getTimeInMillis();
		
		return endMillis - startMillis;
	}
}
