package io.shmilyhe.socketapi.client;

import io.shmilyhe.socketapi.commons.Action;
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
