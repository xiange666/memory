package com.album.service;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;

public class AesCbcUtil 
{
	public static final String CODE_TYPE = "UTF-8";
	public static final String AES_TYPE = "AES/CBC/PKCS7Padding";
	/** 解密

	 * @param result 加密后的密文byte数组

	 * @param key 解密用密钥
	 * @throws Exception 

	 */

	public static String decrypt( String encrypted,String aes_key,String iv) throws Exception {

		
		System.out.println(iv.getBytes().length);
		Cipher cipher;
		byte[] result=null ;
		try {
			byte[] byteMi = new BASE64Decoder().decodeBuffer(encrypted); 
			byte[] byteIV = new BASE64Decoder().decodeBuffer(iv);
			byte[] byteKey = new BASE64Decoder().decodeBuffer(aes_key);
			SecretKeySpec key = new SecretKeySpec(byteKey, "AES");  
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			//初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY

			try {
				cipher.init(Cipher.DECRYPT_MODE, key,generateIV(byteIV));
				result = cipher.doFinal(byteMi);
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
 

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		} catch (NoSuchPaddingException e) {

			e.printStackTrace();

		} catch (InvalidKeyException e) {

			e.printStackTrace();

		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();

		} catch (BadPaddingException e) {

			e.printStackTrace();

		}

		return new String(result, CODE_TYPE);  

		
	}
	
	public static AlgorithmParameters generateIV(byte[] iv) throws Exception{   
		AlgorithmParameters params = AlgorithmParameters.getInstance("AES"); 
		params.init(new IvParameterSpec(iv)); 
		return params; 
	}


}
