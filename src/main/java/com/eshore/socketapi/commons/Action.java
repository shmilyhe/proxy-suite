package com.eshore.socketapi.commons;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件类
 * @author eshore
 *
 */

public class Action {
	private String removeIp;//对端IP
	private String action;//事件名称
	private String token;//权限令牌，做权限认证时
	private byte datas[];//传输的数据
	private Map ext=new HashMap();//额外的属性
	private String connId;
	private int type;//0,tunnel 1,raw
	boolean tunnel;
	
	public boolean isTunnel() {
		return tunnel;
	}

	public void setTunnel(boolean tunnel) {
		this.tunnel = tunnel;
	}

	/**
	 * 
	 */
	public Action(){
		//throw new RuntimeException("-----------");
	}
	
	public Action(String action, String token,byte datas[]){
		
		this.action=action;
		this.token=token;
		this.datas=datas;
		//throw new RuntimeException("-----------");
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public byte[] getDatas() {
		return datas;
	}
	public void setDatas(byte[] datas) {
		this.datas = datas;
	}

	public Map getExt() {
		return ext;
	}

	public void setExt(Map ext) {
		this.ext = ext;
	}
	
	public String getAttribute(String name){
		return ""+this.ext.get(name);
	}
	
	public void addAttribute(String name,String v){
		this.ext.put(name, v);
	}

	public String getRemoveIp() {
		return removeIp;
	}

	public void setRemoveIp(String removeIp) {
		this.removeIp = removeIp;
	}

	public String getConnId() {
		return connId;
	}

	public void setConnId(String connId) {
		this.connId = connId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
