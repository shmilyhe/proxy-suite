package io.shmilyhe.socketapi.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import io.shmilyhe.socketapi.commons.TunnelOverSnappyProtocol;
import io.shmilyhe.socketapi.commons.TunnelProtocol;
import io.shmilyhe.tools.Log;
import io.shmilyhe.tools.Logger;

public class InnerServer implements Closeable{
	static Log log=Logger.getLogger(InnerServer.class);
	public static void main(String[] args) {

	}

	 ServerSocket s ;
	public InnerServer(int outPort,final GlobWorker gw,final TunnelProtocol p) throws IOException{
		log.info("Starting Inner  Service...");
		 s = new ServerSocket(outPort);
		ServerHandler hadler = new CommandHandler();
		Thread accepter = new Thread(){
			public void run(){
				while(true){
					Socket socket;
					try {
						socket = s.accept();
						TunnelClientWorker cw =	new ConcurrentTunnelClientWorker(socket,hadler,p,gw);
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
		log.info("Done.");
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
