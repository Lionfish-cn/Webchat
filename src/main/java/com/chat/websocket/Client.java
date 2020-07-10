package com.chat.websocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client extends Thread {
	
	private Socket socket;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	
	public Client(Socket socket) {
		this.socket = socket;
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String UTF) {
		try {
			dos.writeUTF(UTF);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		List<Client> clients = ServerThread.getClients();
		String message = "";
		while(true) {
			try {
				Map<String,String> maps = new HashMap<String,String>();
				Map<String,Socket> sockets = new HashMap<String,Socket>();
				for(Client c : clients) {
					message = c.dis.readUTF();
					String regex = "\\[!!{.*?}]";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(message);
					String ip = "";
					while(matcher.find()) {
						ip = matcher.group(1);
					}
					maps.put(ip, message);
					sockets.put(c.getSocket().getInetAddress().getHostAddress(), c.getSocket());
				}
				for(String key : maps.keySet()) {
					Socket s = sockets.get(key);
					dos = new DataOutputStream(s.getOutputStream());
					dos.writeUTF(maps.get(key));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
