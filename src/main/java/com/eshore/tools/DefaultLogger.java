package com.eshore.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultLogger implements Log {

	public DefaultLogger(){}

	private String className;
	public DefaultLogger(Class clazz){
		if(clazz==null)className="null";
		className=clazz.getName();
	}
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public void info(Object... a) {
		// TODO Auto-generated method stubi
		System.out.print(sdf.format(new Date()));
		System.out.print("\t");
		System.out.print("[INFO]\t");
		System.out.print(className);
		System.out.print("\t");
		if(a!=null)
		for(Object o:a){
			System.out.print(o);
		}
		System.out.println();
	}

	@Override
	public void warm(Object... a) {
		// TODO Auto-generated method stub
		System.out.print(sdf.format(new Date()));
		System.out.print("\t");
		System.out.print("[WAR]\t");
		System.out.print(className);
		System.out.print("\t");
		if(a!=null)
		for(Object o:a){
			System.out.print(o);
		}
		System.out.println();
	}

	@Override
	public void error(Object... a) {
		// TODO Auto-generated method stub
		System.err.print(sdf.format(new Date()));
		System.err.print("\t");
		System.err.print("[ERROR]\t");
		System.err.print(className);
		System.err.print("\t");
		if(a!=null)
		for(Object o:a){
			System.err.print(o);
		}
		System.err.println();
	}
	
	public static void main(String args[]){

	}

	@Override
	public void debug(Object... a) {
		// TODO Auto-generated method stub
		if(true)return;
		
		System.out.print(sdf.format(new Date()));
		System.out.print("\t");
		System.out.print("[DEBUG]\t");
		System.out.print(className);
		System.out.print("\t");
		if(a!=null)
		for(Object o:a){
			System.out.print(o);
		}
		System.out.println();
	}


}
