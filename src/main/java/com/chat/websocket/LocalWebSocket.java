package com.chat.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chat.util.StringUtil;

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
