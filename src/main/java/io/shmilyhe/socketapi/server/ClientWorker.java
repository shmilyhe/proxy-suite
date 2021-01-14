package io.shmilyhe.socketapi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import io.shmilyhe.socketapi.commons.Action;
import io.shmilyhe.socketapi.commons.IProtocol;
import io.shmilyhe.tools.Log;
import io.shmilyhe.tools.Logger;
/**
 * 每个一个连接对应一个这样的独立worker
 * @author eshore
 *
 */
public class ClientWorker {
	static Log log=Logger.getLogger(ClientWorker.class);
	private boolean tunnel;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	ClientWorker outClinet;
	
	//当连接已断开是否保持不回收，true 则不回收
	private boolean keepWhileBreak=false;
	
	public ClientWorker getOutClinet() {
		return outClinet;
	}
	public void setOutClinet(ClientWorker outClinet) {
		this.outClinet = outClinet;
	}
	
	boolean holdding;
	private String dockid;
	
	public boolean isHoldding() {
		return holdding;
	}
	public void setHoldding(boolean holdding) {
		this.holdding = holdding;
	}
	

	String name;
	GlobWorker server;
	String ip;
	InputStream in;
	OutputStream out;
	Socket s;
	int port;
	boolean available=true;
	boolean working;
	IProtocol protocol;
	ServerHandler serverHandler;
	boolean reConnect;
	public boolean isReConnect() {
		return reConnect;
	}
	public void setReConnect(boolean reConnect) {
		this.reConnect = reConnect;
	}
	public ClientWorker(){}
	/**
	 * 创建一个worker
	 * @param s 对端的socket
	 * @param h 业务处理handler
	 * @param protocol 传输协议
	 * @param server server 的反引用
	 */
	public ClientWorker(Socket s,ServerHandler h,IProtocol protocol,GlobWorker server){
		this.server=server;
		this.s=s;
		this.protocol=protocol;
		serverHandler=h;
		ip=s.getInetAddress().getHostAddress();
		port=s.getPort();
		//System.out.println("accepet:"+ip+":"+port);
		try {
			in=s.getInputStream();
			out=s.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static Action ping=new Action("ping","ping",null);
	
	/**
	 * 当前worker 是否可用
	 * @return
	 */
	public boolean isAvailable() {
		if(s==null)return false;
		if(s.isClosed())return false;
		return available;
	}
	
	/**
	 * 关闭并释放资源
	 */
	public void close(){
		try {s.shutdownInput();} catch (IOException e) {}
		try {s.shutdownOutput();} catch (IOException e) {}
		try {in.close();} catch (IOException e) {}
		try {out.close();} catch (IOException e) {}
		try {s.close();} catch (IOException e) {}
		available=false;
		//System.out.println("["+name+"] closed dockid:"+dockid);
	}


	/**
	 * 是否处理在工作状态中
	 * @return
	 */
	public boolean isWorking() {
		return working;
	}

	public boolean reConnct(){
		 try {
			 this.close();
			s = new Socket(ip,port);
			in=s.getInputStream();
			out=s.getOutputStream();
			available=true;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return false;
	}

	/**
	 * 调用对端
	 * @param a
	 * @return
	 */
	public boolean Call(Action a){
		try {
			protocol.write(out, a);
			out.flush();
		} catch (IOException e) {
			if(reConnect){
				if( reConnct()){
					try {
						protocol.write(out, a);
					} catch (IOException e1) {
						available=false;
						e1.printStackTrace();
						return false;
					}
				}
			}else{
				sendError(null,this);
				byte[] data  =null;
				String action=null;
				String ds=null;
				if(a!=null){
					action=a.getAction();
					data=a.getDatas();
					if(data!=null){
						if(data.length>1024){
							ds=new String(data,0,1024);
						}else{
							ds=new String(data);
						}
					}
				}
				
				log.error("-->[",this.getName(),"]",ip,":",port," ERROR:",e,"\r\n action:",action,"\r\n",ds);
				//System.out.println("-->["+this.getName()+"]"+ip+":"+port+" ERROR:"+e.getMessage()+"\r\n action:"+action+"\r\n"+ds);
				
				//e.printStackTrace();
				available=false;
				return false;
			}
			
		}
		return true;
	}
	
	private final synchronized boolean toggleWork(boolean t){
		if(working==true){
			if(t==true)return false;
			else{
				working=false;
				return false;
			}
		}else{
			if(t==true){
				working=true;
				return true;
			}else{
				return false;
			}
		}
	}
	static long FREQ_OF_PING=10000;
	
	long lastPing=0;
	public boolean pingClient(){
		if(true) throw new RuntimeException("ping");
		long t =System.currentTimeMillis();
		if(t-lastPing<FREQ_OF_PING)return true;
		try {
			protocol.write(out, ping);
			lastPing=t;
			return true;
		} catch (IOException e) {
			available=false;
			sendError(null,this);
			return false;
		}
	}
	
	private void sendError(Action a,ClientWorker cw){
		try{
			this.serverHandler.onError(null, this);
		}catch(Exception e){
			
		}
	}
	
	

	/**
	 * 当前队列位置
	 */
	private int qindex =0;
	//缓存10个队列元素，超出10个直接报错
	private void queueAction(Action a){
		if(respQueue==null)respQueue= new Action[10];
		respQueue[qindex++]=a;
	}
	
	
	private void flushOutQueue(){
		if(qindex==0)return;
		ClientWorker c = this.getOutClinet();
		if(c==null)return ;
		for(Action a:respQueue){
			c.Call(a);
		}
		respQueue=null;
		qindex=0;
	}
	
	private Action[] respQueue=null; 
	
	/**
	 * 读取一个数据包
	 * @return action
	 */
	protected Action read(){
		return protocol.read(in);
	}
	
	/**
	 * 处理接收的数据
	 * @return
	 */
	public  boolean  work(){
		if(holdding)return false;
		if(!toggleWork(true))return false;
		//working=true;
		if(!available||s.isClosed()||!pingClient()){
			if(isReConnect()&&reConnct()){
				available=true;
				toggleWork(false);
				return true;
			}
			
			if(available==true){
				server.dropOneClient();
				sendError(null,this);
			}
			
			available=false;
			toggleWork(false);
			//working=false;
			return false;
		}
		//先处理队列的数据
		flushOutQueue();
		Action a =null;
		try {
			long bt=System.currentTimeMillis();
			int av =in.available();
			long avt =System.currentTimeMillis();
			if(avt-bt>1000)
			log.debug("check available:",avt-bt,"ms");
			if(av<=0){
				toggleWork(false);
				//trySleep(500);
				return false;
			}
			//System.out.println("work av:"+av);
			a =read();//protocol.read(in);
			long ret =System.currentTimeMillis();
			log.debug("read:",ret-avt,"ms");
			if(a.getConnId()==null){
				a.setConnId(this.getDockid());
				a.setType(1);
			}
			Action response =serverHandler.handle(a,this);
			if(response!=null)
				if(!response.isTunnel()){//是否需要转发
					//protocol.write(out, response);
					Call(response);
				}else{
					ClientWorker c = this.getOutClinet();
					//System.out.println(c.ip);
					if(c!=null){
						if(!c.Call(response)){
						sendError(a,this);
						}
					}else{
						//当还没有完成对接时，先队列缓存
						queueAction(response);
						//System.out.println("[error]=============missing outter"+this.getName());
					}
					//c.protocol.write(c.out, response);
					
					//protocol.write(out, response);
				}
			toggleWork(false);
			log.debug("write:",System.currentTimeMillis()-ret,"ms");
			return true;
		} catch (IOException e) {
			sendError(a,this);
			available=false;
			e.printStackTrace();
			toggleWork(false);
			this.close();
			if(this.getOutClinet()!=null)this.getOutClinet().close();
			server.dropOneClient();
			return false;
		}
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isKeepWhileBreak() {
		return keepWhileBreak;
	}
	public void setKeepWhileBreak(boolean keepWhileBreak) {
		this.keepWhileBreak = keepWhileBreak;
	}
	public String getDockid() {
		return dockid;
	}
	public void setDockid(String dockid) {
		this.dockid = dockid;
	}
	public boolean isLogon() {
		return logon;
	}
	public void setLogon(boolean logon) {
		this.logon = logon;
	}
	public String getLoginNotice() {
		return loginNotice;
	}
	public void setLoginNotice(String loginNotice) {
		this.loginNotice = loginNotice;
	}
	public boolean isTunnel() {
		return tunnel;
	}
	public void setTunnel(boolean tunnel) {
		this.tunnel = tunnel;
	}
	/**
	 * 是否登陆成功
	 */
	private boolean logon;
	
	/**
	 * 登陆通知
	 */
	private String loginNotice;
	
	private void trySleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}
}
