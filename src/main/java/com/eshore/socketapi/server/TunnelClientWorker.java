package com.eshore.socketapi.server;

import java.io.IOException;
import java.net.Socket;

import com.eshore.khala.utils.Login;
import com.eshore.socketapi.commons.IProtocol;
import com.eshore.socketapi.commons.TunnelAction;
import com.eshore.tools.Log;
import com.eshore.tools.Logger;
/**
 * 每个一个连接对应一个这样的独立worker
 * @author eshore
 *
 */
public class TunnelClientWorker extends ClientWorker{
	static Log log=Logger.getLogger(TunnelClientWorker.class);
	/**
	 * 创建一个worker
	 * @param s 对端的socket
	 * @param h 业务处理handler
	 * @param protocol 传输协议
	 * @param server server 的反引用
	 */
	public TunnelClientWorker(Socket s,ServerHandler h,IProtocol protocol,GlobWorker server){
		this.server=server;
		this.s=s;
		this.protocol=protocol;
		serverHandler=h;
		ip=s.getInetAddress().getHostAddress();
		port=s.getPort();
		//System.out.println("accepet:"+ip+":"+port);
		//this.reConnect=true;
		try {
			in=s.getInputStream();
			out=s.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String id="default";
	private String token="";


	public String getId() {
		return Login.getAccount();
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getToken() {
		return Login.getToken();
	}


	public void setToken(String token) {
		this.token = token;
	}
	
	
	public void login(){
		Call(new TunnelAction(5,("id="+getId()+";token="+getToken() ).getBytes()));
	}

	
	
	@Override
	public boolean reConnct() {
		//System.out.println("==reConnect==");
		if(super.reConnct()){
			this.login();
			log.info("reconnected!");
			return true;
		}
		log.warm("reconnect failed!");
		return false;
	}


	public boolean pingClient(){
		long t =System.currentTimeMillis();
		if(t-lastPing<FREQ_OF_PING)return true;
		try {
			s.sendUrgentData(0xFF);
			//protocol.write(out, ping);
			lastPing=t;
			return true;
		} catch (IOException e) {
			 //reConnct();
			return false;
		}
	}
	

}
