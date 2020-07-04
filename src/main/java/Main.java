import org.json.JSONArray;

import queryManager.QueryMaker;

public class Main {

	public static void main(String[] args) {
		String query = "SELECT ?noc ?name {?country a :Country; :countryName ?name; :NOC ?noc.}";
		
		JSONArray results = QueryMaker.query(query);
		
		for(int i =0; i<results.length();i++) {
			System.out.println("NOC: " + results.getJSONObject(i).getJSONObject("noc").getString("value")+
					"\tNAME: "+ results.getJSONObject(i).getJSONObject("name").getString("value"));
		}

	}

}
