package com.chat.filter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.chat.websocket.ServerThread;

import jdk.nashorn.internal.objects.Global;
import jdk.net.Sockets;

/**
 * @author SUWP
 * 	启动加载服务端
 */
public class RunFilter implements ServletContextListener{
	
	private ServerThread serverThread;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		if(serverThread==null) {
			serverThread = new ServerThread(null);
			serverThread.start();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if(serverThread!=null) {
			serverThread.closeSocketServer();
			serverThread.interrupt();
		}
	}
}
