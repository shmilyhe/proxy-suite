package com.eshore.socketapi.client;

import com.eshore.socketapi.commons.Action;
/**
 * 客户端接收到服务端主动消息的回调
 * @author eshore
 *
 */
public interface ICallback {
	/**
	 * 
	 * @param a
	 */
	void doCallback(Action a );
}
