package com.echarging.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class OneStoreVerify {

private static final Logger log = Logger.getLogger(IOS_Verify.class);
	
	private static class TrustAnyTrustManager implements X509TrustManager {  
        
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
      
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
      
        public X509Certificate[] getAcceptedIssuers() {  
            return new X509Certificate[]{};  
        }  
    }  
      
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {  
        public boolean verify(String hostname, SSLSession session) {  
            return true;  
        }  
    }  
    private static final String url_sandbox="https://iapdev.tstore.co.kr/digitalsignconfirm.iap";  
    private static final String url_verify="https://iap.tstore.co.kr/digitalsignconfirm.iap";  
      
      
    /** 
     * 苹果服务器验证 
     * @param receipt 账单 
     * @url 要验证的地址 
     * @return null 或返回结果 
     */  
    public static String buyAppVerify(String receipt,String verifyState)  
    {  
       String url=url_verify;  
       if(verifyState!=null&&verifyState.equals("sandbox")){  
           url=url_sandbox;  
       }  
       byte[] entitydata = receipt.getBytes();  
       HttpsURLConnection conn = null;
       try{  
           SSLContext sc = SSLContext.getInstance("SSL");  
           sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());  
           URL console = new URL(url);  
           conn = (HttpsURLConnection) console.openConnection();  
           conn.setSSLSocketFactory(sc.getSocketFactory());  
           conn.setHostnameVerifier(new TrustAnyHostnameVerifier());  
           conn.setRequestMethod("POST");  
           conn.setRequestProperty("content-type", "application/json; charset=utf-8");  
           conn.setRequestProperty("Proxy-Connection", "Keep-Alive"); 
           conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));  
           conn.setDoInput(true);  
           conn.setDoOutput(true);  
           BufferedOutputStream hurlBufOus=new BufferedOutputStream(conn.getOutputStream());  
           hurlBufOus.write(entitydata);  
           hurlBufOus.flush();  
            InputStream is = conn.getInputStream();  
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));  
            String line = null;  
            StringBuffer sb = new StringBuffer();  
            while((line = reader.readLine()) != null){  
              sb.append(line);  
            }  
            log.info("OneStore_Verify response:"+sb.toString());
            return sb.toString();  
       }catch(Exception ex)  
       {  
           ex.printStackTrace();  
       } finally{
    	   if (conn!=null) {
			conn.disconnect();
		}
       } 
       return null;  
    }  
}
