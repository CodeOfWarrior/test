package com.echarging.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class PushGameServerUtil {
	private static final Logger log = Logger.getLogger(PushGameServerUtil.class);
//	public static String sendMsg(String url,JSONObject json){
//		HttpClient client = new DefaultHttpClient();
//		HttpHost target = new HttpHost("http://adfoxelb-1151387133.ap-southeast-1.elb.amazonaws.com", 80,"http" );
//		HttpPost post = new HttpPost(url);
//		
//		String response = null;
//	    try {
//	      StringEntity s = new StringEntity(json.toString());
//	      s.setContentEncoding("UTF-8");
//	      s.setContentType("application/json");//发送json数据需要设置contentType
//	      post.setEntity(s);
//	      HttpResponse res = client.execute(target,post);
//	      if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//	        HttpEntity entity = res.getEntity();
//	        String result = EntityUtils.toString(entity);// 返回json格式：
//	        log.info("gameServerResponse:"+result);
//	        response = result;
//	      }
//	    } catch (Exception e) {
//	    	
//	      throw new RuntimeException(e);
//	    }
//	    return response;
//	}
	
	public static String sendMsgJson(String strURL,JSONObject json) throws Exception{
		String result = "error";
		URL url = new URL(strURL);// 创建连接  
        HttpURLConnection connection = (HttpURLConnection) url  
                .openConnection();  
        connection.setDoOutput(true); 
        connection.setDoInput(true);  
        connection.setUseCaches(false);  
        connection.setInstanceFollowRedirects(true);  
        connection.setRequestMethod("POST"); // 设置请求方式  
        connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式  
        connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式  
        connection.connect();  
        OutputStreamWriter out = new OutputStreamWriter(  
                connection.getOutputStream(), "UTF-8"); // utf-8编码  
        out.append(json.toString());  
        out.flush();  
        out.close();  
        // 读取响应  
        int length = (int) connection.getContentLength();// 获取长度  
        InputStream is = connection.getInputStream();  
        if (length != -1) {  
            byte[] data = new byte[length];  
            byte[] temp = new byte[512];  
            int readLen = 0;  
            int destPos = 0;  
            while ((readLen = is.read(temp)) > 0) {  
                System.arraycopy(temp, 0, data, destPos, readLen);  
                destPos += readLen;  
            }  
            result = new String(data, "UTF-8"); // utf-8编码  
            log.info("gameServerResponse="+result);
            if(connection != null){
            	connection.disconnect();
            }
            if(is != null){
            	is.close();
            }
            
            return result;  
        }
        return result;
	}
}
