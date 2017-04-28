

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpGetRequest {
	
	public static void httpGetRequest(String url){
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();    
	        if (entity != null) {    
	            InputStream instreams = entity.getContent();
	            String str = convertStreamToString(instreams);
	            System.out.println("Do something");   
	            System.out.println(str);  
	            // Do not need the rest    
	            httpGet.abort();    
	        }  
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String convertStreamToString(InputStream is) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
       
        String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {  
                sb.append(line + "\n");      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            try {      
                is.close();      
            } catch (IOException e) {      
               e.printStackTrace();      
            }      
        }      
        return sb.toString();      
    }
	
	public static String httpPostRequest(String path,Map<String,String> map) throws IOException{
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<String, String> m:map.entrySet()) {
			buffer.append(m.getKey()).append("=").append(URLEncoder.encode(m.getValue(),"UTF-8")).append("&");
		}
		buffer.deleteCharAt(buffer.length() - 1);  
        byte[] entitydata = buffer.toString().getBytes();  
        URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        conn.setConnectTimeout(5 * 1000);  
        conn.setRequestMethod("POST");  
        conn.setDoOutput(true);  
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
        conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));  
        OutputStream os = conn.getOutputStream();  
        os.write(entitydata);  
        os.flush();  
        os.close();  
        if(conn.getResponseCode() == 200){
        	System.out.println(conn.getResponseMessage());
        	byte[] buf = new byte[1024];  
            StringBuffer sb = new StringBuffer();  
            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            System.out.println(in.available()); 
            String input;
            while((input = br.readLine())!=null) {  
                sb.append(input);  
            }  
            System.out.println("result:" + sb.toString());  
            in.close();  
        	
           return "";  
        }  
        return "";  
	}
	
	public static String httpPostRequestJson(String path,String json) throws IOException{
        byte[] entitydata = json.getBytes();  
        URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        conn.setConnectTimeout(5 * 1000);  
        conn.setRequestMethod("POST");  
        conn.setDoOutput(true);  
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");  
        conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));  
        OutputStream os = conn.getOutputStream();  
        os.write(entitydata);  
        os.flush();  
        os.close();  
        if(conn.getResponseCode() == 200){
        	System.out.println(conn.getResponseMessage());
        	byte[] buf = new byte[1024];  
            StringBuffer sb = new StringBuffer();  
            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            System.out.println(in.available()); 
            String input;
            while((input = br.readLine())!=null) {  
                sb.append(input);  
            }  
            System.out.println("result:" + sb.toString());  
            in.close();  
        	
           return "";  
        }  
        return "";  
	}

}
