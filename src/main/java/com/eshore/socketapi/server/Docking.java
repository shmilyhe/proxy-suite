package com.eshore.socketapi.server;

import java.net.Socket;

import com.eshore.khala.utils.LRUCache;
import com.eshore.socketapi.commons.IProtocol;

public class Docking {
	static LRUCache<String,Docking> cache=new LRUCache<String,Docking> (1000);
	
	public static void  addDocking(Docking d){
		cache.put(d.getId(), d);
	}

	public static Docking getDocking(String id){
		Docking doc = cache.get(id);
		if(doc==null)System.out.println("==============find empty=============");
		return doc;
	}
	
	
	
	ClientWorker in;
	ClientWorker out;
	
	public ClientWorker getIn() {
		return in;
	}

	public void setIn(ClientWorker in) {
		this.in = in;
	}

	public ClientWorker getOut() {
		return out;
	}

	public void setOut(ClientWorker out) {
		this.out = out;
	}



	private String id;
	private Socket socket;
	private ServerHandler handle;
	private IProtocol protocol;
	
	public Docking(){}
	public Docking(String id,Socket socket,ServerHandler handle, IProtocol protocol){
		this.handle=handle;
		this.socket=socket;
		this.id=id;
		this.protocol=protocol;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public ServerHandler getHandle() {
		return handle;
	}
	public void setHandle(ServerHandler handle) {
		this.handle = handle;
	}
	
	public void run(){}

	public IProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(IProtocol protocol) {
		this.protocol = protocol;
	}

}
