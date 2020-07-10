package com.chat.websocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerThread extends Thread {
	private Log log = LogFactory.getLog(ServerThread.class);
	
	
	protected static List<Client> clients = new ArrayList<Client>();
	
	private ServerSocket serverSocket = null;
	
	public ServerThread(ServerSocket serverSocket) {
		try {
			if (serverSocket == null) {
				this.serverSocket = new ServerSocket(0);
				log.info("Start a server,the port is "+ this.serverSocket.getLocalPort());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(!this.isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				log.info("Have a client entry,the client name is "+socket.getInetAddress().getHostAddress());
				if (socket != null && !socket.isClosed()) {
					InetAddress inetAddress = socket.getInetAddress();
					Client c = new Client(socket);
					new Thread(c,inetAddress.getHostAddress()).start();;
					clients.add(c);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeSocketServer(){
        try {
            if(null!=serverSocket && !serverSocket.isClosed())
            {
                serverSocket.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	public static List<Client> getClients() {
		return clients;
	}

	public static void setClients(List<Client> clients) {
		ServerThread.clients = clients;
	}

}
