package com.chat.service.impl;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.chat.service.IClientService;
import com.chat.util.DateUtil;

@Service
public class ClientService implements IClientService {

	public Socket loadSocket() {
		InetAddress inetaddress;
		try {
			inetaddress = InetAddress.getLocalHost();
			String ip = inetaddress.getHostAddress();
			Random r = new Random(1000);
			Socket socket = new Socket(ip,3676);
			//	生成客户端后，自动给服务端发送信息，用以生成客户端序列
			OutputStream os = socket.getOutputStream();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
			bw.write(socket.getInetAddress().getHostAddress()+"请求登陆！");
			return socket;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
