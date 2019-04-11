package com.eshore.socketapi.server;

import java.net.Socket;
import java.util.Collection;

import com.eshore.khala.utils.LRUCache;
import com.eshore.socketapi.commons.IProtocol;
import com.eshore.tools.Log;
import com.eshore.tools.Logger;

public class Docking {
	static Log log=Logger.getLogger(Docking.class);
	static LRUCache<String,Docking> cache=new LRUCache<String,Docking> (1000);
	static{
		/**
		 * 开始监视连接超时
		 */
		Thread th = new Thread(){
			private void trySleep(long t){
				try {
					Thread.sleep(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void run() {
				while(true){
					if(cache==null||cache.size()==0){
						trySleep(500);
						continue;
					}
					Collection<Docking> ds = cache.values();
					if(ds==null||ds.size()==0){
						trySleep(500);
						continue;
					}
				   for(Docking d :ds){
					   if(!d.isConnected()&&d.checkTimeOut()){
						   try{
							   d.doTimeOut();
						   }catch(Exception e){
						   }
					   }
				   }
				   trySleep(500);
				}
			}
		};
		th.setDaemon(true);
		th.start();
	}
	
	/**
	 * 当连接超时的时候处理
	 */
	public void doTimeOut(){
		
	}
	
	public static void  addDocking(Docking d){
		cache.put(d.getId(), d);
	}

	/**
	 * 是否已建立连接
	 */
	private boolean connected;
	
	public static Docking getDocking(String id){
		Docking doc = cache.get(id);
		if(doc==null){
			log.warm("find empty! sid:",id);
		}
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
	/**
	 * 开始连接时间
	 */
	private long dockTime=System.currentTimeMillis();
	
	/**
	 * 建立连接超时，默认是1秒
	 */
	private long timeOut=1000;
	
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

	public boolean checkTimeOut(){
		return System.currentTimeMillis()-dockTime>timeOut;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
}
