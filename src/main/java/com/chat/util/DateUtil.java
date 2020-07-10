package com.chat.util;

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
}
