package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtil {

	public static String connection(String url,String requestMethod) {
		StringBuffer sb = new StringBuffer();
		try {
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)u.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(10000);
			if(requestMethod!=null)
				connection.setRequestMethod(requestMethod);
			connection.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			String line  = null;
			while((line = br.readLine())!=null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
