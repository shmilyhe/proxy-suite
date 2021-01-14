package io.shmilyhe.socketapi.commons;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 简单的传输协议
 *
 * 传输的结构
 * len 32bit (Integer)
 * data (len bytes)
 * @author eshore
 *
 */
public class SimpleProtocol implements IProtocol {
	static String TOKEN="TOKEN";
	static String ACTION="ACTION";
	static String DATAS="DATAS";
	static String END="END";
	
	
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
		Action a = new Action();
		while(true){
			try {
				int len =this.readInt(ins);
				if(len==0)continue;
				byte[] b = new byte[len];
				ins.read(b, 0, len);
				String h=new String(b);
				if(END.equals(h)){
					break;
				}
				int vlen =this.readInt(ins);
				if(vlen==0)continue;
				byte[] v=new byte[vlen];
				ins.read(v,0,vlen);
				if(ACTION.equals(h)){
					a.setAction( new String(v,"utf-8"));
				}else if(TOKEN.equals(h)){
					a.setToken( new String(v,"utf-8"));
				}else if(DATAS.equals(h)){
					a.setDatas(v);
				}else{
					a.addAttribute(h, new String(v,"utf-8"));
				}
				
			} catch (IOException e) {
				break;
			}
			
			
		}
		return a;
	}

	
	@Override
	public void write(OutputStream out, Action action)throws IOException {
		// TODO Auto-generated method stub
	
			writeString(out,ACTION);
			writeString(out,action.getAction());
			writeString(out,TOKEN);
			writeString(out,action.getToken());
			if(action.getDatas()!=null&&action.getDatas().length!=0){
			 writeString(out,DATAS);
			 writeBytes(out,action.getDatas());
			}
			Map ext =action.getExt();
			if(ext!=null&&ext.size()>0)
				for(Object o :ext.entrySet()){
					Entry e=(Entry)o;
					writeString(out,""+e.getKey());
					writeString(out,""+e.getValue());
				}
			
		
	
			writeString(out,END);
			out.flush();
	
	}

}
