package com.eshore.socketapi.server;


import com.eshore.khala.utils.Login;
import com.eshore.socketapi.commons.Action;
import com.eshore.socketapi.commons.TunnelAction;
import com.eshore.tools.Log;
import com.eshore.tools.Logger;

/**
 * 一个处理客户端的标准范例
 * 接入客服务端请求，并处理
 * @author eshore
 *
 */
public class CommandHandler implements ServerHandler {
	static Log log=Logger.getLogger(CommandHandler.class);
	@Override
	public Action handle(Action a,ClientWorker worker) {
		if(a==null)return null;
		if("d".equals(a.getAction())){
			a.setTunnel(true);
			return a;
		} else if("c".equals(a.getAction())){
			String id=new String(a.getDatas());
			
			Docking doc =	Docking.getDocking(id);
			doc.out.setHoldding(false);
			worker.setName("inner-doc");
			doc.setIn(worker);
			doc.run();
			//System.out.println("doc:"+id);
		} else if("f".equals(a.getAction())){
			String id=new String(a.getDatas());
			//System.out.println("fdoc:"+id);
			Docking doc =	Docking.getDocking(id);
			doc.doTimeOut();
			//doc.out.setHoldding(false);
			//worker.setName("inner-doc");
			//doc.setIn(worker);
			//doc.run();
		}  else if("l".equals(a.getAction())){
			String data =new String(a.getDatas());
			String[] p=data.split(";");
			String id=null;
			String token=null;
			for(String s:p){
				String kv[]=s.split("=");
				if(kv.length!=2)continue;
				if("id".equals(kv[0]))id=kv[1];
				else if("token".equals(kv[0]))token=kv[1];
			}
			if(Login.login(token)){
				if(id!=null)GlobWorker.addClient(id, worker);
				log.info("登陆成功！ip:",worker.getIp(),"id:",id);
				return new TunnelAction(3,new byte[0]);
			}else{
				log.warm("非法登陆！ip:",worker.getIp(),"id:",id,"token:",token);
				worker.Call(new TunnelAction(7,new byte[0]));
				worker.close();
				
				//System.out.println("非法登陆！");
			}
			
		}else if("s".equals(a.getAction())){
			worker.close();
			return null;
		}else {
			//打印出客户端的 action 字段
			//System.out.println("[Server] 接收到客服端请求："+a.getAction());
		}
		//原样回复客户端的数据。
		//一般需要根据业务构造正确的返回值
		return null;
	}
	TunnelAction ta = new TunnelAction(6,new byte[0]);
	@Override
	public void onError(Action a, ClientWorker worker) {
		// TODO Auto-generated method stub
		ClientWorker out=worker.getOutClinet();
		if(out!=null){
			//out.Call(ta);
			if("front".equals(out.getName()))
			out.close();
			else
			out.Call(ta);
		}
		worker.close();
	}
	
}
