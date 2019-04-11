package com.eshore.socketapi.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import com.eshore.socketapi.commons.RawProtocol;
import com.eshore.socketapi.commons.TunnelAction;
import com.eshore.tools.Log;
import com.eshore.tools.Logger;

public class OutServer implements Closeable{
	static Log log=Logger.getLogger(OutServer.class);
	public static void main(String[] args) {

	}
	 ServerSocket s ;
	ClientWorker client ;
	boolean shutdown;
	private String id;
	private String clientIp;
	private int clientPort;
	private int outPort;
	public OutServer(String id,String clientIp,int clientPort,int outPort,final GlobWorker gw) throws IOException{
		log.info("Mapping:",id,"\t",clientIp,":",clientPort,"->",outPort,"...");
		this.id=id;
		this.clientIp=clientIp;
		this.clientPort=clientPort;
		this.outPort=outPort;
		RawProtocol p = new RawProtocol();
		s= new ServerSocket(outPort);
		ServerHandler hadler = new CommandHandler();
		Thread accepter = new Thread(){
			public void run(){
				while(!shutdown){
					Socket socket;
					if(client==null||!client.isAvailable())client=GlobWorker.getClientWork(id);
					try {
						socket = s.accept();
						if(client==null){
							socket.close();
							continue;
						}
						String conid=UUID.randomUUID().toString();
						String url=clientIp+":"+clientPort+":"+conid;
						TunnelAction ta = new TunnelAction(1,url.getBytes());
						ConnDocking cdk = new ConnDocking(conid,gw);
						cdk.setOut(new TunnelClientWorker(socket,hadler,p,gw));
						//cdk.getOut().setHoldding(true);
						cdk.getOut().setName("front");
						/*cdk.setHandle(hadler);
						cdk.setProtocol(p);
						cdk.setSocket(socket);*/
						ConnDocking.addDocking(cdk);
						//cdk.
						//System.out.println("acc:"+conid);
						client.Call(ta);
						
						//gw.addClientWorker(new ClientWorker(socket,hadler,p,gw));
					} catch (IOException e) {
						//e.printStackTrace();
						log.error(e);
					}
					
				}
			}
		};
		accepter.setDaemon(true);
		accepter.start();
		log.info("Done.");
	}
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		shutdown=true;
		s.close();
		log.info("Shutting down:",id,"\t",clientIp,":",clientPort,"->",outPort,"...");
	}

}
