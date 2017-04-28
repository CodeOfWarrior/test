import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonTest {
public static void main(String[] args) {
	String str = "{\"status\":0, \"environment\":\"Sandbox\",\n" +
			"\"receipt\":{\"receipt_type\":\"ProductionSandbox\", \"adam_id\":0, \"app_item_id\":0, \"bundle_id\":\"com.adfox.legendofbattleships\",\n" + 
			"\"application_version\":\"5.0.2\", \"download_id\":0, \"version_external_identifier\":0,\n" + 
			"\"receipt_creation_date\":\"2016-07-18 08:51:52 Etc/GMT\", \"receipt_creation_date_ms\":\"1468831912000\",\n" + 
			"\"receipt_creation_date_pst\":\"2016-07-18 01:51:52 America/Los_Angeles\", \"request_date\":\"2016-07-18 08:51:56 Etc/GMT\",\n" + 
			"\"request_date_ms\":\"1468831916881\", \"request_date_pst\":\"2016-07-18 01:51:56 America/Los_Angeles\",\n" + 
			"\"original_purchase_date\":\"2013-08-01 07:00:00 Etc/GMT\", \"original_purchase_date_ms\":\"1375340400000\",\n" + 
			"\"original_purchase_date_pst\":\"2013-08-01 00:00:00 America/Los_Angeles\", \"original_application_version\":\"1.0\",\n" + 
			"\"in_app\":[{\"quantity\":\"1\", \"product_id\":\"com.adfox.battleships.60\", \"transaction_id\":\"1000000224176426\",\n" + 
			"\"original_transaction_id\":\"1000000224176426\", \"purchase_date\":\"2016-07-18 08:51:52 Etc/GMT\",\n" + 
			"\"purchase_date_ms\":\"1468831912000\", \"purchase_date_pst\":\"2016-07-18 01:51:52 America/Los_Angeles\",\n" + 
			"\"original_purchase_date\":\"2016-07-18 08:51:52 Etc/GMT\", \"original_purchase_date_ms\":\"1468831912000\",\n" + 
			"\"original_purchase_date_pst\":\"2016-07-18 01:51:52 America/Los_Angeles\", \"is_trial_period\":\"false\"\n" + 
			"}]\n" + 
			"}}";
	JSONObject obj = JSONObject.fromObject(str).getJSONObject("receipt");
	System.out.println( obj.getString("original_purchase_date_pst"));
	System.out.println( obj.getString("in_app"));
	String s =  obj.getString("in_app");
	JSONObject ob = JSONArray.fromObject(s).getJSONObject(0);
	System.out.println(ob.get("product_id"));
	
	
}
}
