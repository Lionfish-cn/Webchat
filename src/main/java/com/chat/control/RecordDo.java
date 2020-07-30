package com.chat.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.chat.entity.Login;
import com.chat.entity.Record;
import com.chat.service.impl.LoginService;
import com.chat.service.impl.RecordService;
import com.chat.util.ArrayUtil;
import com.chat.util.DateUtil;
import com.chat.util.EntityUtil;
import com.chat.util.StringUtil;
import com.chat.util.common.Constants;
import com.chat.util.generate.IDGenerator;
import com.chat.util.map.Page;

@Controller
@RequestMapping("/recordDo")
public class RecordDo {

	@Autowired
	private RecordService recordService;

	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/searchRecord")
	public String searchRecord(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			String rowsize = request.getParameter("rowSize");
			String pageno = request.getParameter("pageNo");
			Integer rowSize = Integer.parseInt(StringUtil.isNull(rowsize)?"10":rowsize);
			Integer pageNo = Integer.parseInt(StringUtil.isNull(pageno)?"1":pageno);
			
			if(pageNo==null||pageNo==0) {
				pageNo = 1;
			}
			
			if(rowSize==null||rowSize==0) {
				rowSize = 10;
			}
			
			Page page = new Page();
			page.settPageNo(pageNo);
			page.settPageSize(rowSize*pageNo);
			page.settPageStart(1);
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("pageStart", page.gettPageStart());
			map.put("pageSize", page.gettPageSize());
			
			List<Record> records = recordService.selectPage(map);
			
			List<String> l = new ArrayList<String>();
			l.add("Id");
			l.add("tRecord");
			l.add("tTarget");
			l.add("tSend");
			l.add("tTime");
			
			List<Map> rMap  = EntityUtil.entityListToMap(records,l);
			for (Map map2 : rMap) {
				Object tsend = map2.get("tSend");
				Object tTarget = map2.get("tTarget");
				Login send = loginService.selectByUsername(tsend.toString());
				Login target = loginService.selectByUsername(tTarget.toString());
				map2.put("sendAvatar", getImageUrl(send));
				map2.put("targetAvatar", getImageUrl(target));
			}
			map.put("pageNo", pageNo);
			map.put("rowSize",rowSize);
			map.put("list", ArrayUtil.removeEmptyCollection(rMap));
			
			PrintWriter out = response.getWriter();
			out.write(JSONObject.toJSONString(map));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/saveRecord")
	public void saveRecord(HttpServletRequest request,HttpServletResponse response) {
		String to = request.getParameter("to");
		String from = request.getParameter("from");
		String time = request.getParameter("time");
		String content = request.getParameter("content");
		
		Record r = new Record();
		r.setId(IDGenerator.generatorID());
		r.settRecord(content);
		r.settTarget(to);
		r.settSend(from);
		r.settTime(DateUtil.convertStringToDate(time, "yyyy-MM-dd HH:mm"));
		r.settUsername(from);
		int i = recordService.insert(r);
		try {
			PrintWriter out = response.getWriter();
			out.write(i);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping("/searchChatPerson")
	public void searchChatPerson(String from,HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
			List<String> usernames = recordService.searchChatPerson(from);
			if(ArrayUtil.isEmpty(usernames)) {
				out.write("{'error','null'}");
			}else {
				Map<String,Object> rMap = new HashMap<String, Object>();
				List<Map<String,Object>> persons = new ArrayList<Map<String,Object>>();
				for (String username : usernames) {
					Login l = loginService.selectByUsername(username);
					Map<String,Object> m = new HashMap<String, Object>();
					m.put("userid", l.gettUsername());
					m.put("name", l.gettNickName());
					m.put("imageUrl",getImageUrl(l));
					persons.add(m);
				}
				rMap.put("list", JSONObject.toJSONString(persons));
				out.write(JSONObject.toJSONString(rMap));
			}
			out.flush();
			out.close();
		} catch (Exception e) {
		}
		
		return;
		
	}
	
	public String getImageUrl(Login l) {
		String imageUrl = l.gettImage();
		if(StringUtil.isNull(l.gettImage())) {
			imageUrl = Constants.DEF_IMAGE_URL; 
		}
		
		return imageUrl;
	}
}
