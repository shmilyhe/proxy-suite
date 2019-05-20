package com.eshore.socketapi.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.eshore.socketapi.commons.Action;
import com.eshore.socketapi.commons.IProtocol;
import com.eshore.socketapi.commons.SimpleProtocol;

public class Client {
	IProtocol p;
	private String ip;
	private int port;
	public Client(String ip,int port,IProtocol p) throws UnknownHostException, IOException{
		this.p=p;
		s= new Socket(ip,port);
		this.ip=ip;
		this.port=port;
		out = s.getOutputStream();
		in =s.getInputStream();
	}
	
	private Socket s;
	private Socket callback;
	InputStream in;
	OutputStream out;
	
	InputStream callbackIn;
	OutputStream callbackOut;
	
	
	
	public boolean logon(String token){
		Action a= new Action();
		a.setAction("logon");
		a.setToken(token);
		Action b=  invoke(a);
		return "logon".equals(b.getAction());
	}
	
	ICallback handle;
	
	public void subscribe(String id,String token,final ICallback handle) throws UnknownHostException, IOException{
		callback= new Socket(ip,port);
		callbackIn=callback.getInputStream();
		callbackOut=callback.getOutputStream();
		Action a= new Action();
		a.setAction("subscribe");
		a.setToken(token);
		a.addAttribute("wx_id", id);
		p.write(callbackOut, a);
		this.handle=handle;
		Thread th = new Thread(){
			public void run(){
				while(true){
					try {
						if(callbackIn.available()>0){
							Action a = p.read(callbackIn);
							try{
								handle.doCallback(a);
							}catch(Exception e){}
						}else{
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		};
		th.setDaemon(true);
		th.start();
	}
	
	
	public Action invoke(Action a){
		try {
			p.write(out, a);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	    return p.read(in);
	}
	
	
	public static void main(String agrs[]) throws UnknownHostException, IOException{
		{
			Client c = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c2 = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c3 = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c4 = new Client("127.0.0.1",3000,new SimpleProtocol());
			long bt = System.currentTimeMillis();
			c.logon("9090ooooooooooooo0");
			
			c.logon("9090ooooooo090909oooooo0");
			for(int i=0;i<10000;i++){
				c.logon("==================");
				c2.logon("00000000000000000000");
				c3.logon("00000000000000000000");
				c4.logon("00000000000000000000");
			}
			long time=System.currentTimeMillis()-bt;
			double tps=(40000d/time)*1000;
			System.out.println("time:"+time+"\t tps:"+tps);
		}
		{
			Client c = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c2 = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c3 = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c4 = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c5 = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c6 = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c7 = new Client("127.0.0.1",3000,new SimpleProtocol());
			Client c8 = new Client("127.0.0.1",3000,new SimpleProtocol());
			long bt = System.currentTimeMillis();
			c.logon("9090ooooooooooooo0");
			
			c.logon("9090ooooooo090909oooooo0");
			for(int i=0;i<10000;i++){
				c.logon("==================");
				c2.logon("00000000000000000000");
				c3.logon("00000000000000000000");
				c4.logon("00000000000000000000");
				c5.logon("==================");
				c6.logon("00000000000000000000");
				c7.logon("00000000000000000000");
				c8.logon("00000000000000000000");
			}
			long time=System.currentTimeMillis()-bt;
			double tps=(80000d/time)*1000;
			System.out.println("time:"+time+"\t tps:"+tps);
		}
		
	}
}
