package io.shmilyhe.socketapi.server;

import java.net.Socket;

import io.shmilyhe.socketapi.commons.Action;
import io.shmilyhe.socketapi.commons.IProtocol;

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


	@Override
	protected synchronized Action read() {
		return super.read();
	}
	
	

}
