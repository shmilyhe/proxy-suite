package com.eshore.socketapi.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.eshore.tools.Log;
import com.eshore.tools.Logger;
/**
 * 简单的传输协议
 *
 * 传输的结构
 * len 32bit (Integer)
 * data (len bytes)
 * @author eshore
 *
 */
public class RawProtocol implements IProtocol {
	static Log log=Logger.getLogger(RawProtocol.class);
	public static void main(String args[]){
		int v=20000;
		int ch1=(v >>> 8) & 0xFF;
		int ch2=(v >>> 0) & 0xFF;
		System.out.println(ch1+":"+ch2);
		int v2 =(ch1 << 8) + (ch2 << 0);
		System.out.println("v2:"+v2+" "+(v==v2));
		
		
	}
	
	   
	@Override
	public Action read(InputStream ins) {
		Action a = null;
		try {
			int av=ins.available();
			//System.out.println(av);
			if(av>20480)av=20480;
			if(av<=0){
				log.warm("raw read empty!");
				return a;
			}
			byte b[] = new byte[av];
			ins.read(b);
			a=new TunnelAction(0);
			a.setDatas(b);
			//System.out.println(new String(a.getDatas()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}

	
	@Override
	public void write(OutputStream out, Action action)throws IOException {
		// TODO Auto-generated method stub
		if(action==null)return ;
		byte b[]=action.getDatas();
		if(b==null){
			b=new byte[0]; 
			//System.out.println("error:"+action.getAction());
		}
		//System.out.println("w:"+new String(b));
		out.write(b);
		//System.out.println("w end=========");
		out.flush();
	}

}
