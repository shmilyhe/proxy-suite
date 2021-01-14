package io.shmilyhe;

import java.io.IOException;
import java.net.UnknownHostException;

import io.shmilyhe.socketapi.client.Client;
import io.shmilyhe.socketapi.commons.SimpleProtocol;

public class Test2 {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		for(int i=0;i<30;i++){
			new Thread(){
				public void run(){
					Client c;
					try {
						long bt=System.currentTimeMillis();
						c = new Client("127.0.0.1",3000,new SimpleProtocol());
						for(int i=0;i<1000;i++){
							c.logon("9090ooooooooooooo0");
							c.logon("9090ooooooooooooo0");
						}
						long et =System.currentTimeMillis();
						
						System.out.println((2000d/(et-bt))*1000);
						
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}.start();
		
		//System.out.println( );
		}
		//System.out.println( c.logon("9090ooooooooooooo0"));
		//System.out.println( c.logon("9090ooooooooooooo0"));

	}

}
