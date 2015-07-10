/**   
* @Title: ValidCodeProductTool.java 
* @Package com.point.web.util 
* @Description: TODO
* @author guowanyu   
* @date 2015年7月9日 上午11:36:56 
* @version V1.0   
*/
package com.point.web.util;

import java.util.Random;


/**
 * @title :
 * @author : 
 * @since：2015年7月9日 上午11:36:56 
 * @Description:
 */
public class ValidCodeProductTool {
	public static String productCode(){
		Random random = new Random();
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			res.append(random.nextInt(10));
		}
		return res.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(productCode());
	}
}
