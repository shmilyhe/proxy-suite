package io.shmilyhe.socketapi.client;

import java.io.IOException;

import io.shmilyhe.socketapi.server.GlobWorker;
import io.shmilyhe.socketapi.server.InnerServer;
import io.shmilyhe.socketapi.server.OutServer;

public class Test2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//HttpURLConnection hu;
		//Socket ss= new Socket("14.215.177.38",443);
		GlobWorker gw = new GlobWorker();
		
		//InnerServer iserver = new InnerServer(4100,gw);
		
	
		
		//new OutServer("default", "blog.sina.com.cn",80,4200, gw) ;
		//new OutServer("default", "eshore.cn",80,4400, gw) ;
		//new OutServer("default", "blog.sina.com.cn",80,4300, gw) ;
		//new OutServer("default", "192.168.199.170",80,4300, gw) ;
		
		
		ProxyClient client = new ProxyClient("127.0.01",4100,gw);
		
		new Thread(){
			public void run(){
				while(true){
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
	}

}
