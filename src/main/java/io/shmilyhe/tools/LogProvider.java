package io.shmilyhe.tools;

/**
 *  log 提供者
 * @author eshore
 *
 */

public interface LogProvider {
	Log getLogger(Class<?> clazz);
}
