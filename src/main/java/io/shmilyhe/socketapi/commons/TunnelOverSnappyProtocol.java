package io.shmilyhe.socketapi.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.xerial.snappy.Snappy;

import io.shmilyhe.tools.Log;
import io.shmilyhe.tools.Logger;
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
	static Log log=Logger.getLogger(TunnelOverSnappyProtocol.class);
	@Override
	public Action read(InputStream ins) {
		Action a=super.read(ins);
		if(a!=null){
			if(a.getDatas()!=null&&a.getDatas().length>0)
			try {
				byte[] or = Snappy.uncompress(a.getDatas());
				a.setDatas(or);
			} catch (IOException e) {
				log.error("fail to uncompress ,data length :\t",a.getDatas().length,"\t contents:",new String(a.getDatas()));
				//e.printStackTrace();
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
