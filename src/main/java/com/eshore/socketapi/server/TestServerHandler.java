package com.eshore.socketapi.server;

import com.eshore.socketapi.commons.Action;

/**
 * 一个处理客户端的标准范例
 * 接入客服务端请求，并处理
 * @author eshore
 *
 */
public class TestServerHandler implements ServerHandler {

	@Override
	public Action handle(Action a,ClientWorker worker) {
		//处理订阅业务
		if("subscribe".equals(a.getAction())){
			//获取 "wx_id" 属性
			String id =a.getAttribute("wx_id");
			//把当前worker 添加到订阅列表中
			CallBackPool.subscribe(id, worker);
			System.out.println("[Sever] 客户端订阅：id:"+id);
		}else {
			//打印出客户端的 action 字段
			//System.out.println("[Server] 接收到客服端请求："+a.getAction());
		}
		//原样回复客户端的数据。
		//一般需要根据业务构造正确的返回值
		return a;
	}

	@Override
	public void onError(Action a, ClientWorker worker) {
		// TODO Auto-generated method stub
		
	}

}
