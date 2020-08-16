package com.webmap.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.util.HTTPUtil;
import com.util.IpUtil;

@Controller
@RequestMapping("/bMapDo")
public class BMapDo {

	@RequestMapping("/toMap")
	public String toMap(HttpServletRequest request) {
		try {
			String ip = IpUtil.getInet4Ip();

			String bMapUrl = "http://api.map.baidu.com/location/ip?ak=VGe3VPxiTlYaaPBIGG1I4DafYFKQgAiT&ip="+ip
					+ "&coor=bd09ll";
			String result = HTTPUtil.connection(bMapUrl, null);
			JSONObject jsonObject = JSONObject.parseObject(result);
			if(jsonObject.containsKey("content")) {
				JSONObject _content = jsonObject.getJSONObject("content");
				JSONObject _point = _content.getJSONObject("point");
				String x = _point.getString("x");//当前城市中心点经度
				String y = _point.getString("y");//当前城市中心点维度
				
				request.setAttribute("longitude", x);
				request.setAttribute("latitude", y);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "bmap/bmap-gui";
	}
	
}
