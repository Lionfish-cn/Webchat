package com.chat.util;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64Util {
	
	public static Decoder getDecoder() {
		return Base64.getDecoder();
	}
	
	public static Encoder getEncoder() {
		return Base64.getEncoder();
	}
	
	public static String base64Encoder(String data) {
		Encoder e = getEncoder();
		return  e.encodeToString(data.getBytes());
	}
	
	public static String base64Decoder(String data) {
		Decoder d= getDecoder();
		return d.decode(data).toString();
	}
	
}
