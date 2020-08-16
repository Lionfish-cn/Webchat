package com.util;

public class StringUtil {
	
	public static boolean isNull(String str) {
		return str==null||str.length()<=0||"".equals(str);
	}
	
	public static boolean isNotNull(String str) {
		return !isNull(str);
	}
	
	 /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
	
}
