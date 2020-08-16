package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {
	
	/**
	 * 	获取当前的内网ip地址
	 */
	public static String getCurrentIp(HttpServletRequest request) throws Exception{
		String ip = request.getHeader("x-forwarded-for");
		
		if(StringUtil.isNull(ip)||"unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		
		if(StringUtil.isNull(ip)||"unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		
		if(StringUtil.isNull(ip)||"unknown".equalsIgnoreCase(ip)) {
			InetAddress address = Inet4Address.getLocalHost();
			ip = address.getHostAddress();
		}
		return ip;
	}
	
	/**
	 * 	获取外网ip
	 * @return
	 */
	public static String getInet4Ip() {
		String ip = "";
		try {
			String ipUrl = "http://ip.chinaz.com/";
			String result = HTTPUtil.connection(ipUrl, null);
			if(StringUtil.isNotNull(result)) {
				Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)<\\/dd>");
				Matcher matcher = p.matcher(result);
				while(matcher.find()) {
					ip = matcher.group(1);
				}
			}
			System.out.println(ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}
}
