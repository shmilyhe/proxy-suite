package io.shmilyhe.socketapi.client;

import java.io.IOException;

import io.shmilyhe.socketapi.server.GlobWorker;
import io.shmilyhe.socketapi.server.InnerServer;
import io.shmilyhe.socketapi.server.OutServer;

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
		//new OutServer("default", "eshore.cn",80,3308, gw) ;
		new OutServer("default", "127.0.0.1",8888,3309, gw) ;
		new OutServer("default", "127.0.0.1",8888,3311, gw) ;
		//new OutServer("default", "eshore.cn",80,3310, gw) ;
		//ProxyClient client = new ProxyClient("127.0.01",4100,gw);
		
		Test2.main(null);
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
