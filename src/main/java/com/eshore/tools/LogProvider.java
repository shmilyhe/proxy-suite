package com.eshore.tools;

/**
 *  log 提供者
 * @author eshore
 *
 */

public interface LogProvider {
	Log getLogger(Class<?> clazz);
}
