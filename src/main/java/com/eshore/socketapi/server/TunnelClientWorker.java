package com.eshore.socketapi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.eshore.socketapi.commons.Action;
import com.eshore.socketapi.commons.IProtocol;
/**
 * 每个一个连接对应一个这样的独立worker
 * @author eshore
 *
 */
public class TunnelClientWorker extends ClientWorker{
	
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
		try {
			in=s.getInputStream();
			out=s.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			return false;
		}
	}
	

}
