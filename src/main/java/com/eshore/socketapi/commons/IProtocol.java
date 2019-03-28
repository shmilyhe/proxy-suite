package com.eshore.socketapi.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 传输协议
 * @author eshore
 *
 */

public interface IProtocol {
	/**
	 * 从流中读取事件
	 * @param ins input
	 * @return Action
	 */
	Action read(InputStream ins);
	
	/**
	 * 往流中写Action
	 * @param out output
	 * @param action the action to write
	 * @throws IOException
	 */
	void write (OutputStream out,Action action)throws IOException;
}
