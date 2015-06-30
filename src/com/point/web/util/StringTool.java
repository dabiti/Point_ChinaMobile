package com.point.web.util;

import java.io.UnsupportedEncodingException;

public class StringTool {
	
    public static boolean isUTF8(String s){
        String utf8Str;
		try {
			utf8Str = new String(s.getBytes(),"UTF-8");
	        if(utf8Str.equals(s)){
	        	return true;
	        }
	        return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
    }
}
