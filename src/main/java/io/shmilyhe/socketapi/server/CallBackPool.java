package io.shmilyhe.socketapi.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.shmilyhe.socketapi.commons.Action;

import java.util.Set;
/**
 * 保存订阅关系的池
 * @author eshore
 *
 */
public class CallBackPool {
	public static Map<String,List> workerMap = new HashMap<String,List> ();
	public static Set<String> subscribeSet =new HashSet<String>();
	
	/**
	 * 调用客户端
	 * @param id 客户端订阅的id
	 * @param a 调用内容
	 */
	public static void call(String id, Action a){
		List list =workerMap.get(id);
		if(list==null||list.size()==0)return;
		for(Object o:list){
			ClientWorker w =(ClientWorker)o;
			try{
				if(w.available){
					w.Call(a);
				}
			}catch(Exception e){}
		}
	}
	
	/**
	 * 获取客户端
	 * @param id 客户端订阅的id
	 */
	public static ClientWorker getClient(String id){
		List list =workerMap.get(id);
		if(list==null||list.size()==0) return null;
		for(Object o:list){
			ClientWorker w =(ClientWorker)o;
			try{
				if(w.available){
					return w;
				}
			}catch(Exception e){}
		}
		return null;
	}
	
	/**
	 *  订阅
	 * @param id 订阅id
	 * @param worker 客户端的引用
	 */
	public  static void subscribe(String id,ClientWorker worker){
		synchronized (workerMap) {
			List list = workerMap.get(id);
			if(list==null){
				list=new ArrayList();
				workerMap.put(id, list);
			}
			list.add(worker);
		}
		
	}
	
	/**
	 * 启动回收机制
	 */
	public static void startRecycleWorker(){
		Thread t1 =new Thread(){
			public void run(){
				 recycleWorker();
			}
		};
		t1.setDaemon(true);
		t1.start();
	}
	
	/**
	 * 订阅列表回收机制....
	 */
	public static void recycleWorker(){
		synchronized(workerMap){
			if(workerMap.isEmpty())return;
			Set<Entry<String,List>>  es =workerMap.entrySet();
			if(es==null||es.size()==0)return;
			int exitCount=0;
			for(Entry<String,List> e:es){
				List wl =e.getValue();
				if(wl==null||wl.isEmpty()){
					workerMap.remove(e.getKey());
				}else{
					List nl=new ArrayList();
					for(Object o:wl){
						ClientWorker cw =(ClientWorker)o;
						if(cw.available){
							nl.add(cw);
						}else{
							exitCount++;
						}
					}
					//当列表有回收时更新
					if(nl.size()<wl.size()){
						workerMap.put(e.getKey(), nl);
					}
					
				}
			}
			System.out.println("回收"+exitCount+"个订阅连接");
		}
	}
}
