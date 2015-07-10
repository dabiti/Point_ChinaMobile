/**   
* @Title: TimeUtils.java 
* @Package com.point.web.util 
* @Description: TODO
* @author guowanyu   
* @date 2015年7月10日 下午2:24:44 
* @version V1.0   
*/
package com.point.web.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @title :
 * @author : 
 * @since：2015年7月10日 下午2:24:44 
 * @Description:
 */
public class TimeUtils {
	public static long differCodeSendTimeMillis = 3 * 60 * 1000;
	public static long differSeconds(Date startDate,Date endDate){
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(endDate);
		long endMillis = cal.getTimeInMillis();
		
		cal.setTime(startDate);
		long startMillis = cal.getTimeInMillis();
		
		return endMillis - startMillis;
	}
	
	public static boolean ValidTime(long enterMillis){
		return enterMillis < differCodeSendTimeMillis;
	}
}
