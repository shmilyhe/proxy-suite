package com.eshore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import org.junit.Test;

import com.eshore.socketapi.client.Client;
import com.eshore.socketapi.client.ICallback;
import com.eshore.socketapi.commons.Action;
import com.eshore.socketapi.commons.SimpleProtocol;
import com.eshore.socketapi.server.Server;
import com.eshore.socketapi.server.TestServerHandler;

public class TestClientAndServer {


	@Test
	public void test1(){
		 Server  server=null;
		/**
		 * 创建服务端
		 */
		try {
			 server =	new Server(3000,new TestServerHandler(),new SimpleProtocol(),4);
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try {
			Client c = new Client("127.0.0.1",3000,new SimpleProtocol());
			
			//订阅服务端数据
			c.subscribe("wx_id12345", "token", new ICallback(){

				@Override
				public void doCallback(Action a) {
					try {
						System.out.println("接收到服务端的主动请求："+a.getAction()+" data:"+new String(a.getDatas(),"utf-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
				}
				
			});
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			{
				Action a = new Action();
				a.setAction("call from server");
				a.setDatas("test send data to client ".getBytes());
				server.callClient("wx_id12345", a);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			{
				Action a = new Action();
				a.setAction("queryUserList");
				a.addAttribute("name", "龙裕朝");
				Action resp = c.invoke(a);
				System.out.println("服务端返回："+a.getAttribute("name"));
			}
			
			{
				Action a = new Action();
				a.setAction("queryUserList2");
				a.addAttribute("name", "龙裕朝2");
				Action resp = c.invoke(a);
				System.out.println("服务端返回："+a.getAttribute("name"));
			}
			{
				Action a = new Action();
				a.setAction("queryUserList3");
				a.addAttribute("name", "龙裕朝3");
				Action resp = c.invoke(a);
				System.out.println("服务端返回："+a.getAttribute("name"));
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
}
