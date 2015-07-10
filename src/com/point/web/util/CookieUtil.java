package com.point.web.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 	DES 全称为Data Encryption Standard即数据加密算法，它是IBM公司研究成功并公开发表的。
	DES算法的入口参数有三个：Key、Data、Mode。其中Key为8个字节共64位，是DES算法的工作密钥；Data也为8个字节64位，是要被加密或被解密的数据；
	Mode为DES的工作方式，有两种：加密或解密。
	DES算法是这样工作的：如Mode为加密，则用Key 去把数据Data进行加密，生成Data的密码形式（64位）作为DES的输出结果；
	如Mode为解密，则用Key去把密码形式的数据Data解密，还原为Data的明码形式（64位）作为DES的输出结果。
	在通信网络的两端，双方约定一致的Key，在通信的源点用Key对核心数据进行 DES加密，然后以密码形式在公共通信网（如电话网）中传输到通信网络的终点，
	数据到达目的地后，用同样的Key对密码数据进行解密，便再现了明码形式的核心数据。
	这样，便保证了核心数据（如PIN、MAC等）在公共通信网中传输的安全性和可靠性。 通过定期在通信网络的源端和目的端同时改用新的Key，
	便能更进一步提高数据的保密性。初始Key值为64位，但DES算法规定，其中第8、 16、......64位是奇偶校验位，不参与DES运算。故Key 实际可用位数便只有56位。
	3-DES（TripleDES）：该算法被用来解决使用 DES 技术的 56 位时密钥日益减弱的强度，其方法是：使用两个密钥对明文运行 DES 算法三次，从而得到 112 位有效密钥强度。
	TripleDES 又称为 DESede（表示加密、解密和加密这三个阶段）。
	扩展阅读：http://zh.wikipedia.org/zh/3DES
 *
 */
public class CookieUtil {
	/** 指定加密算法为DESede */
	private static String ALGORITHM = "DESede";
	/** 指定密钥存放文件 */
	private static String KEYFile = "KeyFile";

	/**
	 * 生成密钥
	 */
	private static void generateKey() throws Exception {
		/** DES算法要求有一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();
		/** 为DES算法创建一个KeyGenerator对象 */
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		/** 利用上面的随机数据源初始化这个KeyGenerator对象 */
		kg.init(sr);
		/** 生成密匙 */
		SecretKey key = kg.generateKey();
		/** 用对象流将生成的密钥写入文件 */
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(KEYFile));
		oos.writeObject(key);
		/** 清空缓存，关闭文件输出流 */
		oos.close();
	}

	/**
	 * 加密方法
	 *
	 * source 源数据
	 */
	public static String encrypt(String source) throws Exception {
		generateKey();
		/** 将文件中的SecretKey对象读出 */
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(KEYFile));
		SecretKey key = (SecretKey) ois.readObject();
		/** 得到Cipher对象来实现对源数据的DES加密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] b = source.getBytes();
		/** 执行加密操作 */
		byte[] b1 = cipher.doFinal(b);
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(b1);
	}

	/**
	 * 解密密钥 cryptograph:密文
	 */
	public static String decrypt(String cryptograph) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(KEYFile));
		SecretKey key = (SecretKey) ois.readObject();
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b1 = decoder.decodeBuffer(cryptograph);
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	 /**    
     * BASE64解密   
   * @param key          
     * @return          
     * @throws Exception          
     */              
    public static byte[] decryptBASE64(String key) throws Exception {               
        return (new BASE64Decoder()).decodeBuffer(key);               
    }               
                  
    /**         
     * BASE64加密   
   * @param key          
     * @return          
     * @throws Exception          
     */              
    public static String encryptBASE64(byte[] key) throws Exception {               
        return (new BASE64Encoder()).encodeBuffer(key);               
    }   
}