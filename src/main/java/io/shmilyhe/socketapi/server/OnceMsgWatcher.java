package io.shmilyhe.socketapi.server;

import java.net.Socket;

import io.shmilyhe.socketapi.commons.Action;

public abstract class OnceMsgWatcher {
	String id;
	Action action;
	Socket socket;
	public OnceMsgWatcher(String id){
		this.id=id;
	}
	
	public void setAction(Action a){
		action = a;
	}
	
	public void setSocket(Action a){
		action = a;
	}
	
	public abstract void doWatch();
}

