package com.eshore.tools;
/**
 *  log 封装接口，屏闭不同logger 之关的差异，
 * @author eshore
 *
 */
public interface Log {
	/**
	 * 
	 * @param a info
	 */
	void info(Object ...a);
	/**
	 * 
	 * @param a warmning
	 */
	void warm(Object ...a);
	/**
	 * 
	 * @param a error
	 */
	void error(Object ...a);
	/**
	 * 
	 * @param a debug info
	 */
	void debug(Object ...a);
}
