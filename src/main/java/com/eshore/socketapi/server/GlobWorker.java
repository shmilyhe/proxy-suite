package com.eshore.socketapi.server;

import java.util.ArrayList;
import java.util.List;

import com.eshore.khala.utils.LRUCache;
import com.eshore.tools.Log;
import com.eshore.tools.Logger;

public class GlobWorker {
	static Log log=Logger.getLogger(GlobWorker.class);
	ArrayList<ClientWorker> clientList= new ArrayList<ClientWorker>();
	static LRUCache<String,ClientWorker> cache=new LRUCache<String,ClientWorker> (1000);
	 int changeCount=0;
	 int loop=0;
	 int wokerSize=4;
	 
	 public void addClientWorker(ClientWorker w){
		 clientList.add(w);
		 if(changeCount>10){
				updateWorkerList();
		}
	 }
	 public GlobWorker (){
		 for(int i=0;i<wokerSize;i++){
			 GlobWorkerThread worker= new GlobWorkerThread(this);
				worker.setDaemon(true);
				worker.start();
			}
	 }
	 public void dropOneClient(){
			//System.out.println("dropOneClient");
			changeCount++;
	}
	 
	 /**
		 * 更新worker列表
		 */
		public void updateWorkerList(){
			try{
				ArrayList<ClientWorker>list2 =removeNotavailable(clientList);
				log.info("Recycles ",(clientList.size()-list2.size())," connections！",changeCount);
				changeCount=0;
				clientList=list2;
			}catch(Exception e){}
		}
		
		private static ArrayList<ClientWorker> removeNotavailable(List<ClientWorker>  list){
			ArrayList<ClientWorker>list2 = new ArrayList<ClientWorker>();
			for(ClientWorker w:list){
				if(w.isAvailable()||w.isKeepWhileBreak())list2.add(w);
				else{
					log.info("Recycling ",w.ip,":",w.port);
					//System.out.println("client exit ! ip:"+w.ip+":"+w.port);
				}
			}
			return list2;
		}
		
		private Boolean lock=true;
		
		public ClientWorker getWorker(){
			
			if(clientList==null)return null;
			int size=clientList.size();
			if(size==0)return null;
			int i=0;
			//System.out.println("size:"+size);
			synchronized (lock) {
				if(loop>=size){
					loop=0;
				}
				i=loop;
				loop++;
			}
			ClientWorker w =null;
			try{
				w = clientList.get(i);
			}catch(Exception e){}
			return w;
		}
		
		public static ClientWorker getClientWork(String clientId){
			ClientWorker w =cache.get(clientId);
			if(w==null||!w.isAvailable())return null;
			return w;
		}
		
		public static void addClient(String clientId,ClientWorker w){
			cache.put(clientId, w);
		}
		
		
}
