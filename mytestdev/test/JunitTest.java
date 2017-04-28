

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.echarging.util.MD5;
import com.echarging.util.OneStoreVerify;

import net.sf.json.JSONObject;


public class JunitTest {
	@Test
	public void chargeOrder(){
		StringBuffer buffer = new StringBuffer();
		String path = "http://52.74.122.110:8338/eChargingSystem/chargeOrder.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("passportName", "ad3aea06b78d48fcab798a580adb4872");
		map.put("gameId", "30");
		map.put("gatewayId", "30001");
		map.put("channelId", "1");
		map.put("chargeMoney", "150");
		map.put("productId", "ltjd101000001");
		map.put("code", "11111");
		map.put("key", MD5.getMD5("ad3aea06b78d48fcab798a580adb4872"+30+"30001"+1));
		try {
			while (true) {
				HttpGetRequest.httpPostRequest(path, map);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void login(){
		StringBuffer buffer = new StringBuffer();
		String path = "http://127.0.0.1:8008/eChargingSystem/getProductList.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("gameId", "1");
		map.put("type", "1");
		map.put("key", MD5.getMD5("1"));
		try {
			HttpGetRequest.httpPostRequest(path, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void validateToken(){
		StringBuffer buffer = new StringBuffer();
		String path = "http://127.0.0.1:8008/ePassSystem/validateToken.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("passportName", "15d7d1e0067b453e8a4bdde757bcd29d");
		map.put("password", "WApfyFga+CH4Zg9lHJWbfbiIvKeZPvDDF+Zu6g3tb67HbEY8JFP+uAaVxSzvwn48OvAcVka2ms+XPEhfMuh4puapfuAkvJlWcfoy6+iYmM3dUiWEUOlCUQ8RS2ryd6NhJMWNPloVvHynNEAB5Csp6aetTXuH9KfBaliDODt0h8A=");
		map.put("id", "123456");
		map.put("token", "cc3t5c8h");
		map.put("type", "1");
		map.put("key", MD5.getMD5("15d7d1e0067b453e8a4bdde757bcd29dcc3t5c8h"));
		try {
			HttpGetRequest.httpPostRequest(path, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getPassportID(){
		StringBuffer buffer = new StringBuffer();
		String path = "http://127.0.0.1:8008/ePassSystem/getPassportByID.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", "123456");
		map.put("gameId", "12");
		map.put("key", MD5.getMD5("12345613"));
		try {
			HttpGetRequest.httpPostRequest(path, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getRegisterRandom(){
		StringBuffer buffer = new StringBuffer();
		String path = "http://127.0.0.1:8008/ePassSystem/registerRandom.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", "123456");
		map.put("gameId", "12");
		map.put("key", MD5.getMD5("12345612"));
		try {
			HttpGetRequest.httpPostRequest(path, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void getGatewayList(){
		StringBuffer buffer = new StringBuffer();
		String path = "http://127.0.0.1:8008/ePassSystem/getGatewayList.do";
		Map<String,String> map = new HashMap<String,String>();
		map.put("gameId", "12");
		map.put("key", MD5.getMD5("12"));
		try {
			HttpGetRequest.httpPostRequest(path, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void molChargeCallBack(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("http://127.0.0.1:8008/eChargingSystem/molChargeCallback.do?");
		buffer.append("applicationCode=JtzieOwt55u6EteEcItyOCVRRIPLuros&referenceId=10000101&paymentId=MPO362478&version=v1&amount=2999&currencyCode=USD&paymentStatusCode=00&paymentStatusDate=2016-07-05T03:38:43Z&customerId=3n81qwnc7&signature=110cc32863d32b5294829ae1bcb3bb37&VirtualCurrencyAmount=");
		HttpGetRequest.httpGetRequest(buffer.toString());
	}
	
	@Test
	public void oneStoreCallBack(){
		String path = "https://iapdev.tstore.co.kr/digitalsignconfirm.iap";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("txid", "TSTORE0004_20150515102510XXXXXXXXXXXXXXX");
		jsonObject.put("appid", "OA00012345");
		jsonObject.put("signdata", "MIIH7QYJKoZIhvcNAQcCoIIH3jCCB9oCAQExDzANBglghkgBZQMEAMIIH7QYJKdDFDFFEFEFEFoZIhvcNAQcCoIIH3jCCB9oCAQExDzANBglghkgBZQMEA");
		try {
			String result = OneStoreVerify.buyAppVerify(jsonObject.toString(), "sandbox");
			System.out.println("OneStore_Verify response:"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
