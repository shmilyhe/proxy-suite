package io.shmilyhe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TestWatch {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 ServerSocket server = new ServerSocket(20000);
		
		new Thread(){
			public void run(){
				Socket s=null;
				try {
					s = server.accept();
					System.out.println("接收");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while(true){
					try {
						s.sendUrgentData(0);
						//s.getOutputStream();
						Thread.sleep(500);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		Socket client = new Socket("127.0.0.1",20000);
		//client.sendUrgentData(data);
		client.close();
		//ArrayList lst;
	}

}
