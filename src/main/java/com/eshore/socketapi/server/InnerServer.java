package com.eshore.socketapi.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import com.eshore.socketapi.commons.RawProtocol;
import com.eshore.socketapi.commons.TunnelAction;
import com.eshore.socketapi.commons.TunnelOverSnappyProtocol;
import com.eshore.socketapi.commons.TunnelProtocol;

public class InnerServer implements Closeable{

	public static void main(String[] args) {

	}

	 ServerSocket s ;
	public InnerServer(int outPort,final GlobWorker gw,final TunnelProtocol p) throws IOException{
		System.out.println("starting Inner  Service...");
		 s = new ServerSocket(outPort);
		ServerHandler hadler = new CommandHandler();
		Thread accepter = new Thread(){
			public void run(){
				while(true){
					Socket socket;
					try {
						socket = s.accept();
						TunnelClientWorker cw =	new TunnelClientWorker(socket,hadler,p,gw);
						cw.setName("inner");
						gw.addClientWorker(cw);
						//GlobWorker.addClient(clientId, w);
					}catch(Exception e){
						//gw.addClientWorker(new ClientWorker(socket,hadler,p,gw));
						e.printStackTrace();
					}
					
				}
			}
		};
		accepter.setDaemon(true);
		accepter.start();
		System.out.println("done.");
	}
	public InnerServer(int outPort,final GlobWorker gw) throws IOException{
		this(outPort,gw,new TunnelOverSnappyProtocol());
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		if(s!=null)s.close();
	}

}
