package io.shmilyhe.socketapi.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import io.shmilyhe.socketapi.commons.Action;
import io.shmilyhe.socketapi.commons.IProtocol;
import io.shmilyhe.socketapi.commons.RawProtocol;
import io.shmilyhe.socketapi.commons.TunnelAction;
import io.shmilyhe.socketapi.commons.TunnelOverSnappyProtocol;
import io.shmilyhe.socketapi.commons.TunnelProtocol;
import io.shmilyhe.socketapi.server.ClientWorker;
import io.shmilyhe.socketapi.server.GlobWorker;
import io.shmilyhe.socketapi.server.ServerHandler;
import io.shmilyhe.socketapi.server.TunnelClientWorker;
import io.shmilyhe.tools.Log;
import io.shmilyhe.tools.Logger;

public class ClientHandler implements ServerHandler {
	static Log log=Logger.getLogger(ClientHandler.class);
	 IProtocol sproto=new TunnelProtocol();
	static IProtocol dproto=new RawProtocol();

	public ClientHandler(){}
	GlobWorker gw;
	FinalHandler finalhandler;
	public ClientHandler(GlobWorker gw,TunnelProtocol p){
		this.gw=gw;
		finalhandler=new FinalHandler(gw);
		sproto=p;
	}
	
	public ClientHandler(GlobWorker gw){
		this(gw,new TunnelOverSnappyProtocol());
	}
	
	
	@Override
	public Action handle(Action a, ClientWorker worker) {
		// TODO Auto-generated method stub
		if(a==null)return null;
		String url=a.getAction();
		if("c".equals(url)){
			//String url=clientIp+":"+clientPort+":"+conid;
			byte[] data=a.getDatas();
			String[] datas=new String(data).split(":");
			try {
				log.debug("received from server sid:",datas[2]);
				Socket s = new Socket(datas[0],Integer.parseInt(datas[1]));
				s.setOOBInline(false);
				//s.sendUrgentData(data);
				//Socket s,ServerHandler h,IProtocol protocol,GlobWorker server
				ClientWorker cw = new TunnelClientWorker(s,this,dproto,gw);
				cw.setName("final");
				cw.setDockid(datas[2]);
				gw.addClientWorker(cw);
				//cw.setReConnect(true);
				//System.out.println(worker.getIp()+"---"+worker.getPort());
				Socket ss= new Socket(worker.getIp(),worker.getPort());
				ss.setOOBInline(false);
				
				ClientWorker sw = new TunnelClientWorker(ss,this,sproto,gw);
				gw.addClientWorker(sw);
				cw.setOutClinet(sw);
				sw.setOutClinet(cw);
				sw.setName("client-doc");
				TunnelAction ta = new TunnelAction(1,datas[2].getBytes());
				//System.out.println("act:"+datas[2]);
				sw.Call(ta);
				
			} catch (IOException e) {
				//System.out.println(datas[0]+"=="+datas[1]);
				log.debug("can't create connection :",datas[0],":",datas[1]);
				try{
					Socket ss= new Socket(worker.getIp(),worker.getPort());
					ss.setOOBInline(false);
					ClientWorker sw = new TunnelClientWorker(ss,this,sproto,gw);
				
					sw.setName("client-doc");
					TunnelAction ta = new TunnelAction(7,datas[2].getBytes());
					//System.out.println("act:"+datas[2]);
					sw.Call(ta);
				}catch(Exception ex){}
				e.printStackTrace();
			}
		}else if("d".equals(url)){
			a.setTunnel(true);
			//System.out.println(new String(a.getDatas()));
			return a;
		}else if("s".equals(url)){
			TunnelAction ta = new TunnelAction(6,new byte[0]);
			worker.Call(ta);
			if(worker.getOutClinet()!=null){
				worker.getOutClinet().Call(ta);
			}
			worker.close();
			return null;
		}else if("a".equals(url)){
			//登陆成功
			worker.setLogon(true);
			worker.setLoginNotice("logoned");
			log.info("登陆成功");
			return null;
		}else if("f".equals(url)){
			//登陆失败
			worker.setLoginNotice("failed to logon!");
			log.info("登陆失败");
			return null;
		}
		
		/**
		 * 不处理
		 */
		return null;
	}

	TunnelAction ta = new TunnelAction(6,new byte[0]);
	@Override
	public void onError(Action a, ClientWorker worker) {
		// TODO Auto-generated method stub
		if(worker.getOutClinet()!=null){
			worker.getOutClinet().Call(ta);
		}
		worker.close();
	}

}
