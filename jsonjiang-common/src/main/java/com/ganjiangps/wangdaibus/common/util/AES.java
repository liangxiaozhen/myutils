package com.ganjiangps.wangdaibus.common.util;

 
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

 
public class AES {

	private static String rawKey = "5f873ea8a4b766a925d4c2982a3bd60a";

	/**
	 * AES
	 */

	private final static String algorithm = "AES";

	/*
	 * 转为十六进制
	 */
	private static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if ((buf[i] & 0xff) < 0x10)
				strbuf.append("0");

			strbuf.append(Long.toString(buf[i] & 0xff, 16));
		}
		return strbuf.toString();
	}

	/*
	 * 转为二进制
	 */
	private static byte[] asBin(String src) {
		if (src.length() < 1)
			return null;
		byte[] encrypted = new byte[src.length() / 2];
		for (int i = 0; i < src.length() / 2; i++) {
			int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);

			encrypted[i] = (byte) (high * 16 + low);
		}
		return encrypted;
	}

	/*
	 * 得到密钥key
	 */
	public static String getRawKey() {

		try {
			KeyGenerator kgen = KeyGenerator.getInstance(algorithm);// 获取密匙生成器
			kgen.init(128); // 192 and 256 bits may not be available

			SecretKey skey = kgen.generateKey();// 生成密匙，可用多种方法来保存密匙
			byte[] raw = skey.getEncoded();
			return asHex(raw);
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 进行加密
	 */
	public static String getEncrypt(String message) {
		if(message==null | "".equals(message)){
			return "";
		}
		return getEncrypt(message, rawKey);
	}

	/**
	 * 进行解密
	 */

	public static String getDecrypt(String message) {
		if(message==null | "".equals(message)){
			return "";
		}
		return getDecrypt(message, rawKey);
	}

	/*
	 * 加密
	 */
	public static String getEncrypt(String message, String rawKey) {
		byte[] key = asBin(rawKey);
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);

			Cipher cipher = Cipher.getInstance(algorithm);// 创建密码器

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(message.getBytes("UTF-8"));

			return asHex(encrypted);
		} catch (Exception e) {
			return "";
		}

	}

	/*
	 * 解密
	 */
	public static String getDecrypt(String encrypted, String rawKey) {
		byte[] tmp = asBin(encrypted);
		byte[] key = asBin(rawKey);

		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);

			Cipher cipher = Cipher.getInstance(algorithm);

			cipher.init(Cipher.DECRYPT_MODE, skeySpec);

			byte[] decrypted = cipher.doFinal(tmp);

			return new String(decrypted,"utf-8");
		} catch (Exception e) {
			return "";
		}

	}

	public static void main(String[] args) throws Exception {

//		String message = "shuangqian_95epay";
//
		String rawKey = getRawKey(); // 得到钥匙
//		String rawKey = "ed252e0d0b862d0888430d93175f862f";
		System.out.println("Key = " + rawKey);
//
//		 String encrypted = getEncrypt(message, rawKey);
//		 System.out.println("org text = " + message);// 原数据
//		 System.out.println("encrypted = " + encrypted);// 加密以后
//
////		String encrypted = "48065cc41d7564a0ceda1c9c4ab95b4c";
//
//		String decrypted = getDecrypt(encrypted, rawKey);// 解密串
//
//		System.out.println("decrypted = " + decrypted);

		
		System.out.println("从光头开始：   "+AES.getEncrypt("9930040050240250015  18510355503"));
		
		System.out.println("从光头开始： "+AES.getEncrypt("18973654294")); 
 
 	    System.out.println(AES.getDecrypt("56bdd9380684b492dcf35c1313dc0fa3"));//e838da580cde1087692f655f8d2c1248=======还款测试用户1
	    	
	    System.out.println("================");//55ed29e3e06ac5b31c600805b58656c8=======还款测试用户1
 	    System.out.println(AES.getEncrypt("123456777"));//55ed29e3e06ac5b31c600805b58656c8=======还款测试用户1
	    System.out.println(AES.getEncrypt("nike1990"));//55ed29e3e06ac5b31c600805b58656c8=======还款测试用户1
	    System.out.println(AES.getEncrypt("秋圣"));//55ed29e3e06ac5b31c600805b58656c8=======还款测试用户1
	    System.out.println(AES.getEncrypt("voov"));//55ed29e3e06ac5b31c600805b58656c8=======还款测试用户1
	}
}
