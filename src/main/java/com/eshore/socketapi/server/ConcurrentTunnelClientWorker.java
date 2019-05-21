package com.eshore.socketapi.server;

import java.net.Socket;

import com.eshore.socketapi.commons.Action;
import com.eshore.socketapi.commons.IProtocol;

public class ConcurrentTunnelClientWorker extends TunnelClientWorker {

	public ConcurrentTunnelClientWorker(Socket s, ServerHandler h, IProtocol protocol, GlobWorker server) {
		super(s, h, protocol, server);
	}

	
	@Override
	public boolean isTunnel() {
		return true;
	}


	@Override
	public synchronized boolean Call(Action a) {
		return super.Call(a);
	}
	
	

}
