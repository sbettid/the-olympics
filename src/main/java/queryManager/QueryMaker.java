package queryManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class QueryMaker {

	private static final String prefix = "PREFIX : <http://www.semanticweb.org/davide/olympic#>";
	
	public static JSONArray query(String query) {
		
		
		try {

			URL url = new URL("http://localhost:8080/sparql");

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/sparql-query; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);

			String queryString = prefix + query;

			try (OutputStream os = con.getOutputStream()) {
				byte[] input = queryString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				
				System.out.println(response.toString());
				
				JSONObject obj = new JSONObject(response.toString());
				
				JSONArray arr = obj.getJSONObject("results").getJSONArray("bindings");
				
				return arr;
				
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONArray();
		
	}
	
}
