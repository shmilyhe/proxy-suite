package io.shmilyhe.socketapi.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import io.shmilyhe.socketapi.commons.Action;
import io.shmilyhe.socketapi.commons.IProtocol;
import io.shmilyhe.socketapi.commons.RawProtocol;
import io.shmilyhe.socketapi.commons.TunnelAction;
import io.shmilyhe.socketapi.commons.TunnelProtocol;
import io.shmilyhe.socketapi.server.ClientWorker;
import io.shmilyhe.socketapi.server.GlobWorker;
import io.shmilyhe.socketapi.server.ServerHandler;
import io.shmilyhe.socketapi.server.TunnelClientWorker;

public class FinalHandler implements ServerHandler {
	static IProtocol sproto=new TunnelProtocol();
	static IProtocol dproto=new RawProtocol();

	public FinalHandler(){}
	GlobWorker gw;
	public FinalHandler(GlobWorker gw){
		this.gw=gw;
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
				System.out.println("c:"+datas[2]);
				Socket s = new Socket(datas[0],Integer.parseInt(datas[1]));
				s.setOOBInline(false);
				//s.sendUrgentData(data);
				//Socket s,ServerHandler h,IProtocol protocol,GlobWorker server
				ClientWorker cw = new TunnelClientWorker(s,this,dproto,gw);
				gw.addClientWorker(cw);
				//cw.setReConnect(true);
				//System.out.println(worker.getIp()+"---"+worker.getPort());
				Socket ss= new Socket(worker.getIp(),worker.getPort());
				ss.setOOBInline(false);
				
				ClientWorker sw = new TunnelClientWorker(ss,this,sproto,gw);
				gw.addClientWorker(sw);
				cw.setOutClinet(sw);
				sw.setOutClinet(cw);
				TunnelAction ta = new TunnelAction(1,datas[2].getBytes());
				sw.Call(ta);
			} catch (IOException e) {
				System.out.println(datas[0]+"=="+datas[1]);
				e.printStackTrace();
			}
		}else if("d".equals(url)){
			a.setTunnel(true);
			//System.out.println(new String(a.getDatas()));
			return a;
		}else if("s".equals(url)){
			worker.close();
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
