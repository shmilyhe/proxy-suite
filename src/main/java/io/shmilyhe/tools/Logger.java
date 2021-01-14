package io.shmilyhe.tools;


public class Logger {
	
	private static LogProvider loggerProvider= new DefaultLoggerProvider();
	
	
	public static void setLogProvider(LogProvider provider){
		loggerProvider=provider;
	}
	
	public static  Log getLogger(Class clazz){
		return  loggerProvider.getLogger(clazz);
	}
	
	
}
