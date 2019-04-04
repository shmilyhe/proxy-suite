package com.eshore.socketapi.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.xerial.snappy.Snappy;
/**
 * 简单的传输协议
 * Snappy压缩
 *
 * 传输的结构
 * len 32bit (Integer)
 * data (len bytes)
 * @author eshore
 *
 */
public class TunnelOverSnappyProtocol extends TunnelProtocol {

	@Override
	public Action read(InputStream ins) {
		Action a=super.read(ins);
		if(a!=null){
			if(a.getDatas()!=null&&a.getDatas().length>0)
			try {
				byte[] or = Snappy.uncompress(a.getDatas());
				a.setDatas(or);
			} catch (IOException e) {
				System.out.println(a.getDatas().length);
				e.printStackTrace();
			}
		}
		return a;
	}

	@Override
	public void write(OutputStream out, Action action) throws IOException {
		if(action!=null){
			byte[] or=action.getDatas();
			if(or!=null&&or.length>0){
				byte[] cd = Snappy.compress(or);
				action.setDatas(cd);
			}
		}
		super.write(out, action);
	}
	
}
