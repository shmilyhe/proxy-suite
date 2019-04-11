package com.eshore.socketapi.server;

import com.eshore.tools.Log;
import com.eshore.tools.Logger;

public class ConnDocking extends Docking {
	static Log log=Logger.getLogger(ConnDocking.class);
	GlobWorker gw;
	public ConnDocking(String id,GlobWorker gw){
		this.gw=gw;
		this.setId(id);
	}
	@Override
	public void run() {
		
		ClientWorker out=getOut();
		ClientWorker in=getIn();
		out.setOutClinet(in);
		in.setOutClinet(out);
		gw.addClientWorker(getOut());
		this.setConnected(true);
	}
	
	@Override
	public void doTimeOut() {
		if(isConnected())return;
		this.setConnected(true);
		getOut().close();
		log.warm("docking time out");
	}
	
	

}
