package com.outofjungle.lightbulb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Pachube {

	private String title;
	private HashMap<String, Integer> datastream = new HashMap<String, Integer>();

	private String api_uri;
	private String api_key;

	public Pachube() {
		this.title = "Pachube Dashboard";
	}
	
	public void setUri(String uri) {
		api_uri = uri;
	}
	
	public void setKey(String key) {
		api_key = key;
	}
	
	public String title() {
		return title;
	}
	
	public Integer get(String name) {
		int value = 0;
		if (datastream.containsKey(name)) {
			value = datastream.get(name);
		} else {
			Log.i("DEBUG", "Datastream key not found");
		}
		return value;
	}
	
	public Integer set(String name, Integer value) throws JSONException {
		
		if (datastream.containsKey(name)) {
			datastream.put(name, value);

			String data_uri = api_uri + "/datastreams/" + name;

			JSONObject content = new JSONObject();
			content.put("current_value", value);

			Log.i("SET", name + " = " + content.toString());
			PUT(data_uri, content.toString());
		} else {
			Log.i("DEBUG", "Datastream key not found");
		}

		return get(name);
	}
	
	public void fetch() {
		JSONObject feed;
		try {
			feed = new JSONObject(GET(api_uri));
			title = feed.optString("title");
			JSONArray feed_data = feed.getJSONArray("datastreams");
			
			for (int i = 0; i < feed_data.length(); ++i) {
				
				JSONObject data = feed_data.getJSONObject(i);
				String name = data.optString("id");
				Integer value = data.optInt("current_value");
				datastream.put(name, value);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String GET(String uri) {
		InputStream stream = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpGet request = new HttpGet(uri);
			request.addHeader("X-PachubeApiKey", api_key);

			HttpResponse response = httpclient.execute(request);
			stream = response.getEntity().getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}

		BufferedReader r = new BufferedReader(new InputStreamReader(stream));
		StringBuilder content = new StringBuilder();
		String line;

		try {
			while ((line = r.readLine()) != null) {
				content.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content.toString();
	}
	
	private String PUT(String uri, String data) {
		InputStream stream = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPut request = new HttpPut(uri);
			request.addHeader("X-PachubeApiKey", api_key);

			request.setEntity(new StringEntity(data));
			request.addHeader("Content-Type", "application/json");
			request.addHeader("Accept", "application/json");

			HttpResponse response = httpclient.execute(request);
			stream = response.getEntity().getContent();

		} catch (Exception e) {
			e.printStackTrace();
		}

		BufferedReader r = new BufferedReader(new InputStreamReader(stream));
		StringBuilder content = new StringBuilder();
		String line;
		try {
			while ((line = r.readLine()) != null) {
				content.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}
