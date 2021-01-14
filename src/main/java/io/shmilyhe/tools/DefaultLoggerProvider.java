package io.shmilyhe.tools;

public class DefaultLoggerProvider implements LogProvider {

	@Override
	public Log getLogger(Class<?> clazz) {
		// TODO Auto-generated method stub
		return new DefaultLogger(clazz);
	}

}
