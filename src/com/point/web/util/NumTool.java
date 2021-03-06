package com.point.web.util;

import java.util.Random;


/**
 * @Title:数字工具类
 * @Description:
 * @Since: 2015年7月06日上午11:17:35
 * @author wangchunlong
 */
public class NumTool {
	
    public static String checkCode(int count){
        StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for(int i=0;i<count;i++){
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
            str = str.replace((str.charAt(num)+""), "");
        }
        return sb.toString();
    }
    
  
}

