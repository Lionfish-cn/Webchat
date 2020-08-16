package com.chat.websocket;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chat.entity.Record;
import com.chat.service.impl.RecordService;
import com.util.DateUtil;
import com.util.StringUtil;
import com.util.generate.IDGenerator;

@ServerEndpoint(value="/webSocket/{username}",configurator = HttpSessionConfigurator.class)
public class LocalWebSocket {
	
	private static Integer onlineCount = 0;
	private Session session;
	private String userId;
	
	private static CopyOnWriteArraySet<LocalWebSocket> webSocketSet = new CopyOnWriteArraySet<LocalWebSocket>();
	private static Map<String,Session> maps = new ConcurrentHashMap<String, Session>();//用户名和session路由表
	private static List<String> list = new ArrayList<String>();//记录用户名
	
	@OnOpen
	public void OnOpen(@PathParam("username")String username,Session session,EndpointConfig config) {
		try {
			addOnlineCount();
			this.session = session;
			webSocketSet.add(this);
			this.userId = username;
			list.add(username);
			maps.put(username, session);

			String message = getMessage("["+username+"]连接聊天室，当前在线人数为"+onlineCount+"人！","notice",list);
			broadcast(message);;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getMessage(String message,String notice,List list) {
		JSONObject json = new JSONObject();
		json.put("message", message);
		json.put("type", notice);
		json.put("list", list);
		return json.toJSONString();
	}
	
	@OnClose
	public void OnClose() {
		webSocketSet.remove(this);
		list.remove(this.userId);
		maps.remove(this.userId);
		subOnlineCount();
		
		String message = getMessage("["+this.userId+"]离开聊天室，当前在线人数为"+onlineCount+"人！","notice",list);
		broadcast(message);;
	}
	
	@OnMessage
	public void OnMessage(String message) {
		JSONObject chat = JSON.parseObject(message);
		JSONObject  _message = chat.getJSONObject("message");
		String to_ = _message.getString("to");
		String from_ = _message.getString("from");
		if(StringUtil.isNotNull(to_)) {
			_message.put("type", "message");
			_message.put("avatarPath","https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
			sendSingleton(from_, _message.toString());
			String[] tos = to_.split(";");
			for (String to : tos) {
				if(!to.equals(from_)) {
					sendSingleton(to, _message.toJSONString());
				}
			}
		}else {
			broadcast(message);
		}
	}
	
	@OnError
	public  void OnError(Throwable t) {
		t.printStackTrace();
	}
	
	public void sendSingleton(String to,String message) {
		Session session = maps.get(to);
		if(session!=null) {
			try {
				synchronized (session) {
					session.getBasicRemote().sendText(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void broadcast(String message) {
		for(LocalWebSocket socket:webSocketSet) {
			try {
				socket.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addOnlineCount() {
		try {
			LocalWebSocket.onlineCount = new OnlineCount(LocalWebSocket.onlineCount, "add").call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void subOnlineCount() {
		try {
			LocalWebSocket.onlineCount = new OnlineCount(LocalWebSocket.onlineCount,"sub").call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *	 保存聊天记录 i/o
	 */
	public void saveChatRecord(String from,String to,String record) {
		String path = "D://WebChatRecord//"+from + "//" + to;
		File dirFile = new File(path);
		if(!dirFile.exists()) {//判断文件夹是否存在
			dirFile.mkdirs();
		}
		//如果文件夹存在
		Date d = new Date();
		String fileDate  = DateUtil.convertDateToString(d, "yyyy-MM-dd");
		File file = new File(path+"//" + to +":" + fileDate+".txt");
		try {
			file.createNewFile();
			FileReader fr = new FileReader(file);
			FileWriter fw = new FileWriter(file);
			if(fr.read()!=-1) {
				record = "\n"+record;	
			}
			fw.write(record);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Autowired
	private RecordService recordService;
	
	public void saveChatRecord(JSONObject record_) {
		String record = record_.getString("content");
		Date sTime = DateUtil.convertStringToDate(record_.getString("time"), "yyyy-MM-dd HH:mm");
		String to = record_.getString("to");
		String from = record_.getString("from");
		
		Record r = new Record();
		r.setId(IDGenerator.generatorID());
		r.settRecord(record);
		r.settSend(from);
		r.settTarget(to);
		r.settTime(sTime);
		
		recordService.insert(r);
	}
	
	class OnlineCount implements Callable<Integer>{
		
		private Integer onlineCount = 0;
		private String type = "";
		
		public OnlineCount(Integer onlineCount,String type) {
			this.onlineCount = onlineCount;
			this.type = type;
		}
		
		@Override
		public Integer call() throws Exception {
			if("sub".equals(this.type)) {
				onlineCount -= 1;
			}else {
				onlineCount += 1;
			}
			return onlineCount;
		}
		
	}
}
