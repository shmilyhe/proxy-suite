package com.eshore.socketapi.client;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


import com.eshore.socketapi.commons.TunnelOverSnappyProtocol;
import com.eshore.socketapi.commons.TunnelProtocol;
import com.eshore.socketapi.server.*;
public class ProxyClient {
	//static IProtocol sproto=new TunnelProtocol();
	static TunnelProtocol sproto=new TunnelOverSnappyProtocol();
	//static IProtocol dproto=new RawProtocol();
	//static ClientHandler handle= new ClientHandler();
	
	public ProxyClient(){}
	TunnelClientWorker server;
	public ProxyClient(String ip,int port,GlobWorker gw) throws UnknownHostException, IOException{
		this(ip,port,gw,sproto);
	}
	
	public ProxyClient(String ip,int port,GlobWorker gw,TunnelProtocol p) throws UnknownHostException, IOException{
		Socket s = new Socket(ip,port);
		ClientHandler handle= new ClientHandler(gw,p);
		server =new TunnelClientWorker( s, handle,p,gw);
		server.setId("default");
		server.setToken("1");
		gw.addClientWorker(server);
		server.login();
		server.setReConnect(true);
		server.setKeepWhileBreak(true);
	}
}
