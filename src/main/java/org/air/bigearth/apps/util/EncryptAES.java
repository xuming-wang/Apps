package org.air.bigearth.apps.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
  

/**
 * 加密解密工具类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
public class EncryptAES {
	
	private static final int KEYSIZE_AES128 = 16;
	private static final int BUFFERSIZE = 1024;
	

	
	/**
	 * 加密字符串
	 *
	 * @param str 密钥
	 * @param key
	 * @return
	 */
	public static String encryptString(String str,String key){
		if(str!=null){
			try {
				byte[] byteArray = cryptString(str.getBytes("UTF-8"),key,Cipher.ENCRYPT_MODE);
				return parseByte2HexStr(byteArray);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 解密字符串
	 *
	 * @param str 密钥
	 * @param key
	 * @return
	 */
	public static String decryptString(String str,String key){
		if(str!=null){
			try {
				byte[] byteArray = cryptString(parseHexStr2Byte(str),key,Cipher.DECRYPT_MODE);
				return new String(byteArray);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 要加密(解密)字符串二进制数组
	 *
	 * @param contentByte
	 * @param key 密钥
	 * @param cryptMode 加密或解密
	 * @return
	 */
	private static byte[] cryptString(byte[] contentByte, String key,int cryptMode) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128, new SecureRandom(key.getBytes()));
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] keyByte = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(cryptMode, secretKeySpec);// 初始化
			return cipher.doFinal(contentByte);
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
		return null;
	}

	/**
	 * 加密文件
	 *
	 * @param inputFile  源文件(需要加密的文件)
	 * @param outputFile 加密后的输出文件
	 * @param key 密钥
	 * @return
	 */
	public static boolean encryptFile(File inputFile,File outputFile,String key){
		InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
        	inputStream = new FileInputStream(inputFile);
        	outputStream = new FileOutputStream(outputFile);
        	return crypt(inputStream,outputStream,key,Cipher.ENCRYPT_MODE);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(inputStream!=null){
        		try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		inputStream = null;
        	}
        	if(outputStream!=null){
        		try {
        			outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		outputStream = null;
        	}
        }
        return false;
	}

	/**
	 * 解密文件
	 *
	 * @param inputFile 源文件(需要解密的文件)
	 * @param outputFile  解密后的输出文件
	 * @param key 密钥
	 * @return
	 */
	public static boolean decryptFile(File inputFile,File outputFile,String key){
		InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
        	inputStream = new FileInputStream(inputFile);
        	outputStream = new FileOutputStream(outputFile);
        	return crypt(inputStream,outputStream,key,Cipher.DECRYPT_MODE);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(inputStream!=null){
        		try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		inputStream = null;
        	}
        	if(outputStream!=null){
        		try {
        			outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		outputStream = null;
        	}
        }
        return false;
	}

	/**
	 * 加密文件
	 *
	 * @param inputStream  源文件流
	 * @param outputStream 加密后的文件流
	 * @param key 密钥
	 * @return
	 */
	public static boolean encryptFileStream(InputStream inputStream,OutputStream outputStream,String key){
        return crypt(inputStream,outputStream,key,Cipher.ENCRYPT_MODE);
	}
	
	/**
	 * 解密文件
	 *
	 * @param inputStream 加密(需要解密)的文件流
	 * @param outputStream 解密后的文件流
	 * @param key 密钥
	 * @return
	 */
	public static boolean decryptFileStream(InputStream inputStream,OutputStream outputStream,String key){
        return crypt(inputStream,outputStream,key,Cipher.DECRYPT_MODE);
	}

	/**
	 * 加密文件
	 *
	 * @param inputStream 源文件
	 * @param outputStream 加(解)密后输出的文件
	 * @param key 密钥
	 * @param cryptMode
	 * @return
	 */
	private static boolean crypt(InputStream inputStream,OutputStream outputStream,String key,int cryptMode){
		boolean isComplete = false;
        Cipher cipher = getCipher(getSecretKey(key),cryptMode);
        
        CipherInputStream cipherInput = null;
        CipherOutputStream cipherOutput = null;
        try{
        	byte[] buffer = new byte[BUFFERSIZE];
            int r;
        	if(Cipher.ENCRYPT_MODE == cryptMode){
        		cipherInput = new CipherInputStream(inputStream, cipher);
        		while ((r = cipherInput.read(buffer)) > 0) {
        			outputStream.write(buffer, 0, r);
                }
        		isComplete = true;
        	}else if(Cipher.DECRYPT_MODE == cryptMode){
        		cipherOutput = new CipherOutputStream(outputStream, cipher);
            	while ((r = inputStream.read(buffer)) >=0) {  
                	cipherOutput.write(buffer, 0, r);
                }
            	isComplete = true;
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(cipherInput!=null){
        		try {
					cipherInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		cipherInput = null;
        	}
        	if(cipherOutput!=null){
        		try {
        			cipherOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		cipherOutput = null;
        	}
        	if(inputStream!=null){
        		try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		inputStream = null;
        	}
        	if(outputStream!=null){
        		try {
        			outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		outputStream = null;
        	}
        }
        return isComplete;
	}

	/**
	 * 获取SecretKeySpec对象
	 *
	 * @param key 密钥
	 * @return
	 */
	private static SecretKeySpec getSecretKey(String key){
        byte[] keyByte = key.getBytes();
        byte[] byteArray = new byte[KEYSIZE_AES128];
		for (int i = 0; i < byteArray.length; i++) {
			if (i < keyByte.length){
				byteArray[i] = keyByte[i];
			}else{
				byteArray[i] = 0;
			}
		}
        return new SecretKeySpec(byteArray, "AES");
	}

	/**
	 * 获取Cipher对象
	 *
	 * @param secretKey 密钥对象
	 * @param cryptMode 加密/解密类型
	 * @return
	 */
	private static Cipher getCipher(SecretKeySpec secretKey,int cryptMode){
		Cipher cipher = null;
		try{
			IvParameterSpec ivParamSpec = new IvParameterSpec(new byte[KEYSIZE_AES128]);
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(cryptMode, secretKey, ivParamSpec);
		}catch(InvalidKeyException e){
			e.printStackTrace();
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(NoSuchPaddingException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return cipher;
	}
	
	/**
	 * 将二进制数组转换成16进制字符串
	 *
	 * @param byteArray 二进制
	 * @return  String 16进制字符串
	 */
	private static String parseByte2HexStr(byte[] byteArray) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			String hex = Integer.toHexString(byteArray[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			buffer.append(hex.toUpperCase());
		}
		return buffer.toString();
	}

	/**
	 * 将16进制字符串转换为二进制数组
	 *
	 * @param hexStr 16进制字符串
	 * @return byte[] 二进制数组
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if(hexStr!=null && !"".equals(hexStr)){
			byte[] byteArray = new byte[hexStr.length() / 2];
			for (int i = 0; i < hexStr.length() / 2; i++) {
				int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
				int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
				byteArray[i] = (byte) (high * 16 + low);
			}
			return byteArray;
		}
		return null;
	}
}