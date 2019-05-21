package com.eshore.socketapi.server;

import java.io.IOException;
import java.net.Socket;

import com.eshore.socketapi.commons.Action;
import com.eshore.socketapi.commons.IProtocol;

public class EndTunnelClientWorker extends TunnelClientWorker {

	public EndTunnelClientWorker(Socket s, ServerHandler h, IProtocol protocol, GlobWorker server) {
		super(s, h, protocol, server);
	}

	
	
	




	@Override
	public boolean reConnct() {
		 try {
			 this.close();
			 log.debug("reconnect...\t",this.getDockid(),"\t",ip,":",port);
			s = new Socket(ip,port);
			in=s.getInputStream();
			out=s.getOutputStream();
			available=true;
			log.debug("reconnected!\t",this.getDockid(),"\t",ip,":",port);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return false;
	}
	

}
