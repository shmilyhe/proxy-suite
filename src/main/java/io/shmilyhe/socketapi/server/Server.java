package io.shmilyhe.socketapi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import io.shmilyhe.socketapi.commons.Action;
import io.shmilyhe.socketapi.commons.IProtocol;
import io.shmilyhe.socketapi.commons.SimpleProtocol;

/**
 * 服务端
 * @author eshore
 *
 */
public class Server {
	 ArrayList<ClientWorker> clientList= new ArrayList<ClientWorker>();
	 int changeCount=0;
	 int loop=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new Server(3000,new TestServerHandler(),new SimpleProtocol(),Runtime.getRuntime().availableProcessors());
			System.out.println("服务端启动成功");
			while(true)
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static ArrayList<ClientWorker> removeNotavailable(List<ClientWorker>  list){
		ArrayList<ClientWorker>list2 = new ArrayList<ClientWorker>();
		for(ClientWorker w:list){
			if(w.isAvailable())list2.add(w);
			else{
				System.out.println("client exit ! ip:"+w.ip+":"+w.port);
			}
		}
		return list2;
	}
	
	public void dropOneClient(){
		//System.out.println("dropOneClient");
		changeCount++;
	}
	
	
	/**
	 * 更新worker列表
	 */
	private void updateWorkerList(){
		try{
			ArrayList<ClientWorker>list2 =removeNotavailable(clientList);
			System.out.println("共回收"+(clientList.size()-list2.size())+"个线接！"+changeCount);
			changeCount=0;
			clientList=list2;
		}catch(Exception e){}
	}
	
	
	/**
	 * 通知客户端
	 * @param id
	 * @param a
	 */
	public void callClient(String id,Action a){
		CallBackPool.call(id, a);
	}
	
	private Boolean lock=true;
	
	public ClientWorker getWorker(){
		if(clientList==null)return null;
		int size=clientList.size();
		if(size==0)return null;
		int i=0;
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
	/**
	 * 创建服务端
	 * @param port 监听端口
	 * @param hadler 客户端处理回调
	 * @param p 传输协议
	 * @param wokerSize 工作线程数
	 * @throws IOException
	 */
	public Server(int port,final ServerHandler hadler,final IProtocol p, int wokerSize) throws IOException{
		final ServerSocket s = new ServerSocket(port);
		final GlobWorker _this=new GlobWorker();
		 
		Thread accepter = new Thread(){
			public void run(){
				while(true){
					Socket socket;
					try {
						socket = s.accept();
						System.out.println("worker:"+clientList.size()+" change:"+changeCount);
						//当客户端断线超过阀值时回收 断线的客户对worker
						if(_this.changeCount>10){
							_this.updateWorkerList();
							//启动回收订阅机制
							CallBackPool.startRecycleWorker();
						}
						_this.addClientWorker(new ClientWorker(socket,hadler,p,_this));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		};
		accepter.setDaemon(true);
		accepter.start();
		
		/*for(int i=0;i<wokerSize;i++){
			WorkerThread worker= new WorkerThread(this);
			worker.setDaemon(true);
			worker.start();
		}*/
		
		
	}

}
