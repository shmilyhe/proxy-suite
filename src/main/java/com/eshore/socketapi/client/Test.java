package com.eshore.socketapi.client;

import java.io.IOException;

import com.eshore.socketapi.flow.Flow;
import com.eshore.socketapi.server.GlobWorker;
import com.eshore.socketapi.server.InnerServer;
import com.eshore.socketapi.server.OutServer;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//HttpURLConnection hu;
		//Socket ss= new Socket("14.215.177.38",443);
		GlobWorker gw = new GlobWorker();
		
		InnerServer iserver = new InnerServer(4100,gw);
		
	
		
		//new OutServer("default", "blog.sina.com.cn",80,4200, gw) ;
	    //new OutServer("default", "eshore.cn",80,4400, gw) ;
		//new OutServer("default", "www.bilibili.com",443,4500, gw) ;
		//new OutServer("default", "blog.sina.com.cn",80,4300, gw) ;
		//new OutServer("default", "192.168.199.11",8080,4400, gw) ;
		//10.18.97.143
		//new OutServer("default", "10.18.97.143",8080,4400, gw) ;
		
		//new OutServer("default", "192.168.199.154",3306,3306, gw) ;
		//new OutServer("default", "192.168.199.154",3306,3307, gw) ;
		//new OutServer("default", "132.122.237.215",3306,3306, gw) ;
		//new OutServer("default", "132.122.237.215",3306,3307, gw) ;
		new OutServer("default", "eshore.cn",80,3308, gw) ;
		//new OutServer("default", "127.0.0.1",8888,3309, gw) ;
		final OutServer a= new OutServer("default", "127.0.0.1",8888,3311, gw) ;
		
		new Thread(){
			
			public void run(){
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("rebuild");
				 try {
					 a.close();
					new OutServer("default", "127.0.0.1",8888,3311, gw) ;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}.start();
		//new OutServer("default", "eshore.cn",80,3310, gw) ;
		//ProxyClient client = new ProxyClient("127.0.01",4100,gw);
		
		Test2.main(null);
		
		new Thread(){
			public void run(){
				while(true){
					try {
						for(com.eshore.socketapi.server.ClientWorker w : GlobWorker.allClients()){
							System.out.println(w.getName()+"://"+w.getIp()+":"+w.getPort()+"");
						}
						for(Flow w : Flow.allFlow()){
							System.out.println(w);
						}
						
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
	}

}
