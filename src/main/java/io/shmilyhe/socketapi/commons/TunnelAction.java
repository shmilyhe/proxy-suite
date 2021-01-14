package io.shmilyhe.socketapi.commons;

public class TunnelAction extends Action {
	static String[] actions=new String[]{
			"d",//data 0
			"c",//connect 1
			"e",//error 2
			"a",//active 3
			"u",//unknow 4
			"l",//login 5
			"s", //shutdown 6
			"f" //connect failed 7
	};
	int type;
	public TunnelAction(int type,byte[] data){
		this.type=type;
		if(type>=actions.length)this.type=4;
		super.setDatas(data);
	}
	public TunnelAction(int type){
		this.type=type;
		if(type>=actions.length)this.type=4;
	}
	
	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return actions[type];
	}
	

}
