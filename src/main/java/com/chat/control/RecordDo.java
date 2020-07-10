package com.chat.control;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chat.entity.Login;
import com.chat.entity.Record;
import com.chat.service.impl.LoginService;
import com.chat.service.impl.RecordService;
import com.chat.websocket.Client;
import com.chat.websocket.ServerThread;

@Controller
@RequestMapping("/recordDo")
public class RecordDo {

	@Autowired
	private RecordService recordService;
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/saveRecord")
	public String saveRecord(Record record) {
		recordService.insert(record);
		
		String target = record.gettTarget();
		Login login = loginService.selectByPrimaryKey(target);
			String ip = login.gettIp();
			List<Client> cs = ServerThread.getClients();
			for (Client client : cs) {
			    Socket s = client.getSocket();
				String host = s.getInetAddress().getHostAddress();
				if(ip.equals(host)) {
					try {
						new DataOutputStream(s.getOutputStream()).writeUTF(record.gettRecord());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		
		return "success";
	}
	
	@RequestMapping("/ajaxQuery")
	public void  ajaxQuery(String ids,HttpServletResponse response) {
		JSONArray array = new JSONArray();
		String[] idss = ids.split(";");
		for (String id : idss) {
			List<Record> records = recordService.selectByTargetIdOrSendId(id);
			for (Record r : records) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(String.valueOf(r.gettTarget()),new String(r.gettRecord()));
				jsonObject.put(String.valueOf(r.gettSend()),new String(r.gettRecord()));
				array.add(jsonObject);
			}
		}
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(array.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
