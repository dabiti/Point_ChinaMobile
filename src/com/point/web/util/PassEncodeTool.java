package com.point.web.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
/**
 * @Title:密码加密与验证类
 * @Description:对密码加密的策略与验证
 * @Since: 2015年7月06日上午11:19:05
 * @author guowanyu
 */
public class PassEncodeTool {
	public static final String PRIVATE_SALT = "BLQL";//私盐
	public static final int HASH_ITERATIONS = 3;//hash次数
	public static final String ALGORITHM_NAME = "SHA-256";//算法名称

	public static final String PRIVATE_SALT_CODE = "PASSGETBACK";//密码找回用的私盐
	public static final String ALGORITHM_NAME_CODE = "SHA-512";//密码找回用的算法名称
	public static void main(String[] args) {
		Map<String,String> resMap = passwordEncode("it","123");
		System.out.println("salt: "+resMap.get("salt"));
		System.out.println("password: "+resMap.get("password"));
		//salt: 46f7eddf919679b370fc0cf9c593fdc8
		//password: 25097fca72c65dce871eb686ee08e8e814c9b4e36cb97587081a5434f09ded3e
		System.out.println(getEncodedPassword("it","123","6ec8e5589a53f9d1a71b0192dcfbe0d3"));
	}
	
	//密码找回生成shaid
	public static String createShaId(String account) {
		
		String orginSalt = createOriginSalt();
		
		String salt = Base64.encodeToString(((orginSalt + PRIVATE_SALT_CODE + account + System.currentTimeMillis())).getBytes());
		
		String encodedCode = new SimpleHash(ALGORITHM_NAME_CODE, 
				PRIVATE_SALT_CODE + account + System.currentTimeMillis(), salt, HASH_ITERATIONS).toHex();
		
		return encodedCode;
	}
	
	//加密调用方法
	public static Map<String,String> passwordEncode(String username,String password) {
		Map<String,String> resMap = new HashMap<String, String>();
		
		String orginSalt = createOriginSalt();
		resMap.put("salt", orginSalt);//数据库存放的是原始的salt
		
		//进行拼接salt防止暴力破解
		String salt = constructSalt(username,orginSalt);
        
		String encodedPass = new SimpleHash(ALGORITHM_NAME, password, salt, HASH_ITERATIONS).toHex();
		resMap.put("password", encodedPass);
		
		return resMap;
	}
	//随机数产生原始salt
	private static String createOriginSalt(){
		SecureRandomNumberGenerator secureRandomNumberGenerator=new SecureRandomNumberGenerator(); 
		return secureRandomNumberGenerator.nextBytes().toHex();
	}
	//根据用户名组成目标salt
	public static String constructSalt(String username,String orginSalt) {
        String salt = Base64.encodeToString(((orginSalt + PRIVATE_SALT + username)).getBytes());
		return salt;
	}
	//得到当前用户输入密码的加密后字符串
	public static String getEncodedPassword(String inputUsername,String intputPassword,String orginSalt) {
		return new SimpleHash(ALGORITHM_NAME, intputPassword, constructSalt(inputUsername,orginSalt),HASH_ITERATIONS).toHex();
	}
	
}
