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
import com.chat.entity.Record;
import com.chat.service.impl.RecordService;
import com.chat.util.ArrayUtil;
import com.chat.util.DateUtil;
import com.chat.util.EntityUtil;
import com.chat.util.StringUtil;
import com.chat.util.generate.IDGenerator;
import com.chat.util.map.Page;

@Controller
@RequestMapping("/recordDo")
public class RecordDo {

	@Autowired
	private RecordService recordService;
	
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
			Map<String,Object> m = new HashMap<String, Object>();
			rMap.add(m);
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
	public void searchChatPerson(String from,HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			List<String> usernames = recordService.searchChatPerson(from);
			if(ArrayUtil.isEmpty(usernames)) {
				out.write("{'error','null'}");
			}else {
				Map<String,Object> rMap = new HashMap<String, Object>();
				rMap.put("list", usernames);
				out.write(JSONObject.toJSONString(rMap));
			}
			out.flush();
			out.close();
		} catch (Exception e) {
		}
		
		return;
		
	}
}
