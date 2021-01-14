package io.shmilyhe.socketapi.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Test3 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println(httpget("http://192.168.199.11:8080/login"));
		for(int i=0;i<100;i++){
			new Thread(){
				public void run(){
					int count=0;
					while(true){
						try {
							httpget("http://127.0.0.1:4400");
							if(count++%100==0){
								System.out.println(count);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
		
	}

	
	public static String httpget(String url) throws IOException{
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)  u.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		InputStream in = conn.getInputStream();
		String rest =asString(in);
		in.close();
		return rest;
	} 
	
	
	private static  String asString(InputStream in) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len=0;
		while ((len=in.read(b))>0){
			out.write(b, 0, len);
		}
		return new String(out.toByteArray(),"utf-8");
	}
}
