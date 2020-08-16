package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String convertDateToString(Date d,String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		if(d!=null) {
			return sdf.format(d);
		}
		return null;
	}
	
	public static Date convertStringToDate(String dStr,String pattern) {
		SimpleDateFormat sdf =  new SimpleDateFormat(pattern);
		if(StringUtil.isNotNull(dStr)) {
			try {
				return sdf.parse(dStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
