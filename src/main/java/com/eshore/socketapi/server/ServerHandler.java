package com.eshore.socketapi.server;

import com.eshore.socketapi.commons.Action;

/**
 * 处理客户端请求
 * @author eshore
 *
 */
public interface ServerHandler {
	
	/**
	 * 处理客户端请求
	 * @param a 客户端请求
	 * @param worker 与客户端保持会话的工作类
	 * @return 处理结果
	 */
	Action  handle(Action a,ClientWorker worker);
	
	/**
	 * 当发生错误时
	 * @param a action
	 * @param worker
	 */
	void onError(Action a,ClientWorker worker);
}
