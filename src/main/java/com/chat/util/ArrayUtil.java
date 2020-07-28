package com.chat.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ArrayUtil {
	
	public static boolean isEmpty(Collection coll) {
		return coll.isEmpty() || coll.size() <= 0;
	}
	
	/**
	 *	移除集合的空元素
	 * @param <T>
	 * @param colls
	 * @return
	 */
	public static <T> List<T> removeEmptyCollection(List<? extends T> colls){
		List<T> rlist = new ArrayList<T>();
		rlist.addAll(colls);
		int i = 0;
		for (T t : colls) {
			if(t == null) {
				rlist.remove(i);
			}else {
				if(t instanceof Map) {
					Map m = (Map)t;
					if(m.isEmpty()) {
						rlist.remove(i);
					}
				}else if(t instanceof List) {
					List l = (List)t;
					if(isEmpty(l)) {
						rlist.remove(i);
					}
				}else if(t instanceof String) {
					String s = (String)t;
					if(StringUtil.isNull(s)) {
						rlist.remove(i);
					}
				}
			}
			i++;
		}
		return rlist;
	}
	
}
