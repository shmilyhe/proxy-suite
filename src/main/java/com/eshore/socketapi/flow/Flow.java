package com.eshore.socketapi.flow;

import java.util.Collection;
import java.util.Date;

import com.eshore.khala.utils.LRUCache;
/**
 * 流量统计类
 * @author eshore
 *
 */
public class Flow {
	static LRUCache<String,Flow> cache=new LRUCache<String,Flow> (1000);
	public static void main(String[] args) {

	}
	
	public Flow( int outPort,int innerPort, String innerIp,String clientName){
		this.outPort=outPort;
		this.innerPort=innerPort;
		this.innerIp=innerIp;
		this.clientName=clientName;
		addFlow(this);
	}
	
	/**
	 * 添加流量管理
	 * @param f 流量
	 */
	public static void addFlow(Flow f){
		cache.put(""+f.getOutPort(), f);
	}
	
	/**
	 * 获取所有的流量信息
	 * @return 流量列表
	 */
	public static Collection<Flow> allFlow(){
		return cache.values();
	}
	
	private int outPort;//外网端口
	private int innerPort;//内网端口
	private String innerIp;//内网IP
	private String clientName;//客户端名称
	
	private long read;//读数据
	private long write;//写数据
	
	private Date createTime=new Date();//创建时间
	
	
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{")
		.append("outPort:").append(outPort).append(",")
		.append("innerPort:").append(innerPort).append(",")
		.append("innerIp:\"").append(innerIp).append("\",")
		.append("clientName:\"").append(clientName).append("\",")
		.append("read:").append(read).append(",")
		.append("write:").append(write).append(",")
		.append("readRate:").append(this.readPerS()).append(",")
		.append("writeRate:").append(this.writePerS()).append("")
		.append("}");
		return sb.toString();
	}
	
	private  long[] tmpRead =new long[10];
	private  long[] tmpWrite=new long[10];
	private  long[] tmpReadTime =new long[10];
	private  long[] tmpWriteTime= new long[10];
	volatile int  rIndex=0;
	volatile int wIndex=0;
	
	private int getRIndex(){
		if(rIndex>=10){
			rIndex=0;
		}
		return rIndex++;
	}
	
	private int getWIndex(){
		if(wIndex>=10){
			wIndex=0;
		}
		return wIndex++;
	}
	
	/**
	 * 计算读取数据
	 * @param len
	 */
	public void calculateRead(long len){
		read+=len;
		long t=System.currentTimeMillis();
		int i=getRIndex();
		tmpRead[i]=len;
		tmpReadTime[i]=t;
	}
	
	/**
	 * 
	 * 计算写入数据
	 * @param len
	 */
	public void calculateWrite(long len){
		write+=len;
		long t=System.currentTimeMillis();
		int i=getWIndex();
		tmpWrite[i]=len;
		tmpWriteTime[i]=t;
	}
	
	/**
	 * 计算每秒的读取量
	 * 计算方法记录最近的 9次流量/时间差
	 * 因为这个数据供参考，在同一个量级即可，尽量提高性能，不需要锁定资源。
	 * @return 每秒流量
	 */
	public long readPerS(){
		long ct =System.currentTimeMillis();
		long minTime=0;
		long minRead=0;
		long maxTime=0;
		long sum=0;
		for(int i=0;i<tmpReadTime.length;i++){
			long t = tmpReadTime[i];
			if(minTime==0){
				minTime=t;
				maxTime=t;
				minRead=tmpRead[i];
			}else if(minTime>t){
				minTime=t;
				minRead=tmpRead[i];
			}else if(t>maxTime){
				maxTime=t;
			}
			sum+=tmpRead[i];
		}
		sum-=minRead;
		if( ct-maxTime>10000)return 0;
		long dev =maxTime-minTime;
		if(dev==0)dev=1;
		return (sum*1000)/dev;
	}
	
	/**
	 * 计算每秒的写入量
	 * 计算方法记录最近的 9次流量/时间差
	 * 因为这个数据供参考，在同一个量级即可，尽量提高性能，不需要锁定资源。
	 * @return 每秒流量
	 */
	public long writePerS(){
		long ct =System.currentTimeMillis();
		long minTime=0;
		long minWrite=0;
		long maxTime=0;
		long sum=0;
		for(int i=0;i<tmpWriteTime.length;i++){
			long t = tmpWriteTime[i];
			if(minTime==0){
				minTime=t;
				maxTime=t;
				minWrite=tmpWrite[i];
			}else if(minTime>t){
				minTime=t;
				minWrite=tmpWrite[i];
			}else if(t>maxTime){
				maxTime=t;
			}
			sum+=tmpWrite[i];
		}
		if( ct-maxTime>10000)return 0;
		sum-=minWrite;
		long dev =maxTime-minTime;
		if(dev==0)dev=1;
		return (sum*1000)/dev;
	}

	public int getOutPort() {
		return outPort;
	}

	public void setOutPort(int outPort) {
		this.outPort = outPort;
	}

	public int getInnerPort() {
		return innerPort;
	}

	public void setInnerPort(int innerPort) {
		this.innerPort = innerPort;
	}

	public String getInnerIp() {
		return innerIp;
	}

	public void setInnerIp(String innerIp) {
		this.innerIp = innerIp;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public long getRead() {
		return read;
	}

	public void setRead(long read) {
		this.read = read;
	}

	public long getWrite() {
		return write;
	}

	public void setWrite(long write) {
		this.write = write;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	

}
