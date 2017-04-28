package com.echarging.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;

public class RSAUtils {
	
		/**
		 * RSA加密公钥
		 */
		private static final String DEFAULT_PUBLIC_KEY=     
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0kGMyi0cqxSqlECeXvz6IYlnM\n" +
			"Sc7RxAfm2cr2Y/h56++uhhr0G1osk+IFgRmTIfLMnv2mKv9bGqO3DV0TTL0UOODq\n" + 
			"+Q8ewcm0BXQy3KI2J2XAWSSD6Zi7BdYF4CHdmWWkawZqvY01Nw7B2JD4k78sjl0P\n" + 
			"20QYQrsgnX0jtnyPZQIDAQAB";
	    /**
	     * RSA私钥
	     */    
	    public static final String DEFAULT_PRIVATE_KEY=   
	    	"MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALSQYzKLRyrFKqUQ\n" +
	    	"J5e/PohiWcxJztHEB+bZyvZj+Hnr766GGvQbWiyT4gWBGZMh8sye/aYq/1sao7cN\n" + 
	    	"XRNMvRQ44Or5Dx7BybQFdDLcojYnZcBZJIPpmLsF1gXgId2ZZaRrBmq9jTU3DsHY\n" + 
	    	"kPiTvyyOXQ/bRBhCuyCdfSO2fI9lAgMBAAECgYEAru4DbIBKnkEOI4QXF4iMCI1f\n" + 
	    	"+e63k7ma/ieZ3kLUAyp0tlvt2Ph20CElI7gDEuwc0ehmTInzgku4uCsWNRBvTHoD\n" + 
	    	"Qv1CKIpsWa3B3yPmxuFROmftgBFdrjTqjdbRJv6vwtzlpXmiyaTfPREYZcbxr5P9\n" + 
	    	"xyZh0UuWlYCgIX4S0TECQQDqKeRv61Y1wwE0HB+JBsMGqQdkWzIg8SB2G+vOj1tJ\n" + 
	    	"C585pDE8Uo5c9hR8GrvPhs+9v6eboKFDxp+LXdv6X9VfAkEAxWbuBN6J3wRwrzPt\n" + 
	    	"0mG/o4jz4iBtSWGohJA0zalKZhChri4jQEOvtSS/z26+DSMYfWesEPXMte2Mcbj0\n" + 
	    	"nkYtuwJAfjAHJ3zl32Dv5BoHsHsRXCR4lE9Ey1XFzYAsagr9AXce3hwqYDyI9XWz\n" + 
	    	"AlIRxbr7dmCyvP4qCXEfByaUZe+A9wJBAJhc0epAOfpA2xjf4/g2Kkx3MIagsLmq\n" + 
	    	"/v8Sry7uM9aDj8d5JQw9MimGq+XXfCl3pTdmBZExp0fkijpcn7JqSHECQQDaNF81\n" + 
	    	"5aP5j3kv19i1BeP/ILQRA7EfMbXhuCeba3WdWndU4csmS/1eLZ7GISeM0MZpUTad\n" + 
	    	"3jFLxBm909ekVDdS";
	    /**  
	     * 私钥  
	     */    
	    private RSAPrivateKey privateKey;    
	    
	    /**  
	     * 公钥  
	     */    
	    private RSAPublicKey publicKey;    
	        
	    /**  
	     * 字节数据转字符串专用集合  
	     */    
	    private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};    
	    /**  
	     * 获取私钥  
	     * @return 当前的私钥对象  
	     */    
	    public RSAPrivateKey getPrivateKey() {    
	        return privateKey;    
	    }    
	    
	    /**  
	     * 获取公钥  
	     * @return 当前的公钥对象  
	     */    
	    public RSAPublicKey getPublicKey() {    
	        return publicKey;    
	    }    
	    /**  
	     * 随机生成密钥对  
	     */    
	    public void genKeyPair(){    
	        KeyPairGenerator keyPairGen= null;    
	        try {    
	            keyPairGen= KeyPairGenerator.getInstance("RSA");    
	        } catch (NoSuchAlgorithmException e) {    
	            e.printStackTrace();    
	        }    
	        keyPairGen.initialize(1024, new SecureRandom());    
	        KeyPair keyPair= keyPairGen.generateKeyPair();    
	        this.privateKey= (RSAPrivateKey) keyPair.getPrivate();    
	        this.publicKey= (RSAPublicKey) keyPair.getPublic();    
	    }    
	    
	    /**  
	     * 从文件中输入流中加载公钥  
	     * @param in 公钥输入流  
	     * @throws Exception 加载公钥时产生的异常  
	     */    
	    public void loadPublicKey(InputStream in) throws Exception{    
	        try {    
	            BufferedReader br= new BufferedReader(new InputStreamReader(in));    
	            String readLine= null;    
	            StringBuilder sb= new StringBuilder();    
	            while((readLine= br.readLine())!=null){    
	                if(readLine.charAt(0)=='-'){    
	                    continue;    
	                }else{    
	                    sb.append(readLine);    
	                    sb.append('\r');    
	                }    
	            }    
	            loadPublicKey(sb.toString());    
	        } catch (IOException e) {    
	            throw new Exception("公钥数据流读取错误");    
	        } catch (NullPointerException e) {    
	            throw new Exception("公钥输入流为空");    
	        }    
	    }    
	    /**  
	     * 从字符串中加载公钥  
	     * @param publicKeyStr 公钥数据字符串  
	     * @throws Exception 加载公钥时产生的异常  
	     */    
	    public void loadPublicKey(String publicKeyStr) throws Exception{    
	        try {    
	            BASE64Decoder base64Decoder= new BASE64Decoder();    
	            byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);  
	            KeyFactory keyFactory= KeyFactory.getInstance("RSA");    
	            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);    
	            this.publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);    
	        } catch (NoSuchAlgorithmException e) {    
	            throw new Exception("无此算法");    
	        } catch (InvalidKeySpecException e) {    
	            throw new Exception("公钥非法");    
	        } catch (IOException e) {    
	            throw new Exception("公钥数据内容读取错误");    
	        } catch (NullPointerException e) {    
	            throw new Exception("公钥数据为空");    
	        }    
	    }    
	    
	    /**  
	     * 从文件中加载私钥  
	     * @param keyFileName 私钥文件名  
	     * @return 是否成功  
	     * @throws Exception   
	     */    
	    public void loadPrivateKey(InputStream in) throws Exception{    
	        try {    
	            BufferedReader br= new BufferedReader(new InputStreamReader(in));    
	            String readLine= null;    
	            StringBuilder sb= new StringBuilder();    
	            while((readLine= br.readLine())!=null){    
	                if(readLine.charAt(0)=='-'){    
	                    continue;    
	                }else{    
	                    sb.append(readLine);    
	                    sb.append('\r');    
	                }    
	            }    
	            loadPrivateKey(sb.toString());    
	        } catch (IOException e) {    
	            throw new Exception("私钥数据读取错误");    
	        } catch (NullPointerException e) {    
	            throw new Exception("私钥输入流为空");    
	        }    
	    }    
	    
	    public void loadPrivateKey(String privateKeyStr) throws Exception{    
	        try {    
	            BASE64Decoder base64Decoder= new BASE64Decoder();    
	            byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);    
	            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);    
	            KeyFactory keyFactory= KeyFactory.getInstance("RSA");    
	            this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);    
	        } catch (NoSuchAlgorithmException e) {    
	            throw new Exception("无此算法");    
	        } catch (InvalidKeySpecException e) {    
	            e.printStackTrace();  
	            throw new Exception("私钥非法");    
	        } catch (IOException e) {    
	            throw new Exception("私钥数据内容读取错误");    
	        } catch (NullPointerException e) {    
	            throw new Exception("私钥数据为空");    
	        }    
	    }    
	    
	    /**  
	     * 公钥加密过程  
	     * @param publicKey 公钥  
	     * @param plainTextData 明文数据  
	     * @return  
	     * @throws Exception 加密过程中的异常信息  
	     */    
	    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{    
	        if(publicKey== null){    
	            throw new Exception("加密公钥为空, 请设置");    
	        }    
	        Cipher cipher= null;    
	        try {    
	            cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider());    
	            cipher.init(Cipher.ENCRYPT_MODE, publicKey);    
	            byte[] output= cipher.doFinal(plainTextData);    
	            return output;    
	        } catch (NoSuchAlgorithmException e) {    
	            throw new Exception("无此加密算法");    
	        } catch (NoSuchPaddingException e) {    
	            e.printStackTrace();    
	            return null;    
	        }catch (InvalidKeyException e) {    
	            throw new Exception("加密公钥非法,请检查");    
	        } catch (IllegalBlockSizeException e) {    
	            throw new Exception("明文长度非法");    
	        } catch (BadPaddingException e) {    
	            throw new Exception("明文数据已损坏");    
	        }    
	    }    
	    
	    /**  
	     * 私钥解密过程  
	     * @param privateKey 私钥  
	     * @param cipherData 密文数据  
	     * @return 明文  
	     * @throws Exception 解密过程中的异常信息  
	     */    
	    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception{    
	        if (privateKey== null){    
	            throw new Exception("解密私钥为空, 请设置");    
	        }    
	        Cipher cipher= null;    
	        try {    
	            cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider());    
	            cipher.init(Cipher.DECRYPT_MODE, privateKey);    
	            byte[] output= cipher.doFinal(cipherData);    
	            return output;    
	        } catch (NoSuchAlgorithmException e) {    
	            throw new Exception("无此解密算法");    
	        } catch (NoSuchPaddingException e) {    
	            e.printStackTrace();    
	            return null;    
	        }catch (InvalidKeyException e) {    
	            throw new Exception("解密私钥非法,请检查");    
	        } catch (IllegalBlockSizeException e) {    
	            throw new Exception("密文长度非法");    
	        } catch (BadPaddingException e) {    
	            throw new Exception("密文数据已损坏");    
	        }           
	    }    
	    
	    
	    /**  
	     * 私钥加密过程  
	     * @param privateKey 公钥  
	     * @param plainTextData 明文数据  
	     * @return  
	     * @throws Exception 加密过程中的异常信息  
	     */    
	    public byte[] encryptPrivate(RSAPrivateKey privateKey, byte[] plainTextData) throws Exception{    
	        if(privateKey== null){    
	            throw new Exception("加密私钥为空, 请设置");    
	        }    
	        Cipher cipher= null;    
	        try {    
	            cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider());    
	            cipher.init(Cipher.ENCRYPT_MODE, privateKey);    
	            byte[] output= cipher.doFinal(plainTextData);    
	            return output;    
	        } catch (NoSuchAlgorithmException e) {    
	            throw new Exception("无此加密算法");    
	        } catch (NoSuchPaddingException e) {    
	            e.printStackTrace();    
	            return null;    
	        }catch (InvalidKeyException e) {    
	            throw new Exception("加密私钥非法,请检查");    
	        } catch (IllegalBlockSizeException e) {    
	            throw new Exception("明文长度非法");    
	        } catch (BadPaddingException e) {    
	            throw new Exception("明文数据已损坏");    
	        }    
	    }
	    
	    /**  
	     * 公钥解密过程  
	     * @param publicKey 公钥  
	     * @param cipherData 密文数据  
	     * @return 明文  
	     * @throws Exception 解密过程中的异常信息  
	     */    
	    public byte[] decryptPublic(RSAPublicKey publicKey, byte[] cipherData) throws Exception{    
	        if (publicKey== null){    
	            throw new Exception("解密公钥为空, 请设置");    
	        }    
	        Cipher cipher= null;    
	        try {    
	            cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider());    
	            cipher.init(Cipher.DECRYPT_MODE, publicKey);    
	            byte[] output= cipher.doFinal(cipherData);    
	            return output;    
	        } catch (NoSuchAlgorithmException e) {    
	            throw new Exception("无此解密算法");    
	        } catch (NoSuchPaddingException e) {    
	            e.printStackTrace();    
	            return null;    
	        }catch (InvalidKeyException e) {    
	            throw new Exception("解密公钥非法,请检查");    
	        } catch (IllegalBlockSizeException e) {    
	            throw new Exception("密文长度非法");    
	        } catch (BadPaddingException e) {    
	            throw new Exception("密文数据已损坏");    
	        }           
	    }
	    
	        
	    /**  
	     * 字节数据转十六进制字符串  
	     * @param data 输入数据  
	     * @return 十六进制内容  
	     */    
	    public static String byteArrayToString(byte[] data){    
	        StringBuilder stringBuilder= new StringBuilder();    
	        for (int i=0; i<data.length; i++){    
	            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移    
	            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);    
	            //取出字节的低四位 作为索引得到相应的十六进制标识符    
	            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);    
	            if (i<data.length-1){    
	                stringBuilder.append(' ');    
	            }    
	        }    
	        return stringBuilder.toString();    
	    }
	    
	    public RSAUtils(){
	    	  //加载私钥    
	        try {    
	            loadPrivateKey(RSAUtils.DEFAULT_PRIVATE_KEY);   
	            System.out.println("加载私钥成功");    
	        } catch (Exception e) {    
	            System.err.println(e.getMessage());    
	            System.err.println("加载私钥失败");    
	        }    
	    }
	    
	    
	    
	    public static void main(String[] args){    
	    	RSAUtils rsaEncrypt= new RSAUtils();    
	       // rsaEncrypt.genKeyPair();    
	        //加载公钥    
	        try {    
	            rsaEncrypt.loadPublicKey(RSAUtils.DEFAULT_PUBLIC_KEY);    
	            System.out.println("加载公钥成功");    
	        } catch (Exception e) {    
	            System.err.println(e.getMessage());    
	            System.err.println("加载公钥失败");    
	        }    
	    
	        //加载私钥    
	        try {    
	            rsaEncrypt.loadPrivateKey(RSAUtils.DEFAULT_PRIVATE_KEY);    
	            System.out.println("加载私钥成功");    
	        } catch (Exception e) {    
	            System.err.println(e.getMessage());    
	            System.err.println("加载私钥失败");    
	        }    
	    
	        //测试字符串    
	        String encryptStr= "123456";    
	        System.out.println("私钥长度："+rsaEncrypt.getPrivateKey().toString().length());  
	        System.out.println("公钥长度："+rsaEncrypt.getPublicKey().toString().length());  
	        try {    
	            //公钥加密    
	            byte[] cipher = org.apache.commons.codec.binary.Base64.encodeBase64(rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), encryptStr.getBytes()));    
	            //私钥解密
	            System.out.println(new String(cipher));
	            
	        } catch (Exception e) {    
	            System.err.println(e.getMessage());    
	        }    
	    }  

}
