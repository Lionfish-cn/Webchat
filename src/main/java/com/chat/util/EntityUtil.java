package com.chat.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityUtil {
	
	/**
	 * 	根据List<T> 自动生成 args内属性为key属性值为value的map
	 * @param <T>
	 * @param t
	 * @param args
	 * @return
	 */
	public static <T> List<Map> entityListToMap(List<? extends T> t,List<String> args){
		List<Map> rList = new ArrayList<Map>();
		for (T obj : t) {
			Class clz = obj.getClass();
			int length = args.size();
			while(true) {
				Map<String,Object> map = new HashMap<String, Object>();
				for (String arg : args) {
					try {
						Object rObj = clz.getMethod("get"+arg).invoke(obj);
						if(rObj instanceof Date) {
							rObj = DateUtil.convertDateToString((Date)rObj, "yyyy-MM-dd HH:mm");
						}
						map.put(arg, rObj);
					} catch (Exception e) {
						e.printStackTrace();
					}
					length--;
				}
				rList.add(map);
				if(length<=0) {
					break;
				}
			}
		}
		return rList;
		
	}
	
}
