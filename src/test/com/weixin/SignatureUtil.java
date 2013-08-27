package com.weixin;

import java.security.MessageDigest;
import java.util.Arrays;

public class SignatureUtil {

	/**
	 * 签名
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static String signature(String token, String timestamp, String nonce) {
		String[] tmpArr = { token, timestamp, nonce };
		Arrays.sort(tmpArr);
		String tmpStr = arrayToString(tmpArr);
		tmpStr = SHA1Encode(tmpStr);
		return tmpStr;
	}

	/**
	 * 数组转字符串
	 * @param arr
	 * @return
	 */
	public static String arrayToString(String[] arr) {
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			bf.append(arr[i]);
		}
		return bf.toString();
	}

	/**
	 * sha1加密
	 * @param sourceString
	 * @return
	 */
	public static String SHA1Encode(String sourceString) {
		String resultString = null;
		try {
			resultString = new String(sourceString);
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			resultString = byte2hexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

	/**
	 * byte转16进制String
	 * @param bytes
	 * @return
	 */
	public static final String byte2hexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}

	public static void main(String[] args) {
		System.out.print(SHA1Encode("12345671348831860ELFT"));
	}
}
