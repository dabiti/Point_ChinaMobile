package com.point.web.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    
    
    public final static String MD5_32(String s) {
		  String result = null;
		  try {
		   MessageDigest md = MessageDigest.getInstance("MD5");
		   md.update(s.getBytes());
		   byte b[] = md.digest();
		   int i;
		   StringBuffer buf = new StringBuffer("");
		   for (int offset = 0; offset < b.length; offset++) {
		    i = b[offset];
		    if (i < 0)
		     i += 256;
		    if (i < 16)
		     buf.append("0");
		    buf.append(Integer.toHexString(i));
		   }
		   result = buf.toString();  //md5 32bit
//		   result = buf.toString().substring(8, 24); //md5 16bit
//		   System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
		   System.out.println("md5 32bit: " + buf.toString() );
		  } catch (NoSuchAlgorithmException e) {
		   e.printStackTrace();
		  }
		  return result;
	    }
    
    
    
    public final static String MD5_16(String s) {
		  String result = null;
		  try {
		   MessageDigest md = MessageDigest.getInstance("MD5");
		   md.update(s.getBytes());
		   byte b[] = md.digest();
		   int i;
		   StringBuffer buf = new StringBuffer("");
		   for (int offset = 0; offset < b.length; offset++) {
		    i = b[offset];
		    if (i < 0)
		     i += 256;
		    if (i < 16)
		     buf.append("0");
		    buf.append(Integer.toHexString(i));
		   }
//		   result = buf.toString();  //md5 32bit
		   result = buf.toString().substring(8, 24); //md5 16bit
		   System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
		  } catch (NoSuchAlgorithmException e) {
		   e.printStackTrace();
		  }
		  return result;
	    }
}
