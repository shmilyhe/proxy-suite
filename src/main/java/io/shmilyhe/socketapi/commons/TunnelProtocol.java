package io.shmilyhe.socketapi.commons;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.shmilyhe.tools.Log;
import io.shmilyhe.tools.Logger;
/**
 * 简单的传输协议
 *
 * 传输的结构
 * len 32bit (Integer)
 * data (len bytes)
 * @author eshore
 *
 */
public class TunnelProtocol implements IProtocol {
	static Log log=Logger.getLogger(TunnelProtocol.class);
	public static void main(String args[]){
		int v=20030;
		int ch1=(v >>> 8) & 0xFF;
		int ch2=(v >>> 0) & 0xFF;
		System.out.println(ch1+":"+ch2);
		int v2 =(ch1 << 8) + (ch2 << 0);
		System.out.println("v2:"+v2+" "+(v==v2));
		
		
	}
	
	 public final void writeShort(OutputStream out,int v) throws IOException {
	        out.write((v >>> 8) & 0xFF);
	        out.write((v >>> 0) & 0xFF);
	    }
	private  int readUnsignedShort(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch1 << 8) + (ch2 << 0);
    }
	
	private  int readInt(InputStream in) throws IOException {
	        int ch1 = in.read();
	        int ch2 = in.read();
	        int ch3 = in.read();
	        int ch4 = in.read();
	        if ((ch1 | ch2 | ch3 | ch4) < 0)
	            throw new EOFException();
	        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	    }
	   
	private  void writeInt(OutputStream out,int v) throws IOException {
	        out.write((v >>> 24) & 0xFF);
	        out.write((v >>> 16) & 0xFF);
	        out.write((v >>>  8) & 0xFF);
	        out.write((v >>>  0) & 0xFF);
	    }
	   
	private void writeBytes(OutputStream out, byte[] bytes) throws IOException{
		if(bytes==null||bytes.length==0){
			writeInt(out,0);
			return;
		}
		writeInt(out,bytes.length);
		out.write(bytes);
	}
	private void writeString(OutputStream out, String str) throws IOException{
		if(str==null){
			writeInt(out,0);
			return;
		}
		writeBytes(out,str.getBytes("utf-8"));
	}
	   
	   
	   
	@Override
	public Action read(InputStream ins) {
		Action a = null;
		try {
			if(ins.available()<=0){
				//System.out.println("tunnel read empty!");
				log.warm("tunnel read empty!");
			}
			int type=ins.read();
			a= new TunnelAction(type);
			int len =readUnsignedShort(ins);
			if(len==0){
				a.setDatas(new byte[0]);
				return a;
			}
			byte b[] = new byte[len];
			ins.read(b);
			a.setDatas(b);
			//System.out.println("ta rec:"+new String(a.getDatas()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	
	@Override
	public void write(OutputStream out, Action action)throws IOException {
		// TODO Auto-generated method stub
		TunnelAction ta =(TunnelAction)action;
		if(ta==null){
			//System.out.println("empty action:");
			return;
		}
		out.write(ta.type);
		byte b[]=ta.getDatas();
		//System.out.println("ta:"+new String(ta.getDatas()));
		if(b==null||b.length==0){
			writeShort(out,0);
		}else{
			writeShort(out,b.length);
			out.write(b);
		}
		out.flush();
	}

}
