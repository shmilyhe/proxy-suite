package io.shmilyhe.socketapi.server;

import java.net.Socket;

import io.shmilyhe.socketapi.commons.Action;
import io.shmilyhe.socketapi.commons.IProtocol;
import io.shmilyhe.socketapi.flow.Flow;

/**
 * 外层的Worker
 * @author eshore
 *
 */
public class OutterTunnelClientWorker extends TunnelClientWorker {

	public OutterTunnelClientWorker(Socket s, ServerHandler h, IProtocol protocol, GlobWorker server) {
		super(s, h, protocol, server);
	}
	/**
	 * 流量统计
	 */
	Flow flow;
	boolean calculate;
	public void setFlow(Flow f){
		this.flow=f;
		calculate=true;
	}
	@Override
	public boolean Call(Action a) {
		if(calculate&&a!=null){
			byte[] d=a.getDatas();
			if(d!=null)flow.calculateWrite(d.length);
		}
		return super.Call(a);
	}
	@Override
	protected Action read() {
		// TODO Auto-generated method stub
		Action a=super.read();
		if(a!=null&&calculate){
			byte[] d=a.getDatas();
			if(d!=null)flow.calculateRead(d.length);
		}
		return a;
	}
	
	

	
	
	
}
