package com.eshore.socketapi.server;

public class ConnDocking extends Docking {

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
	}

}