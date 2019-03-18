package com.eshore.socketapi.server;
/**
 * 工作线程
 * @author eshore
 *
 */
public class GlobWorkerThread extends Thread {
	private GlobWorker server;
	private boolean working;
	public GlobWorkerThread(GlobWorker server){
		this.server=server;
	}
	public void run(){
		int eCount=0;
		while(true){
			ClientWorker w= server.getWorker();
			if(w==null){
				trySleep(500);
				continue;
			}
			//System.out.println(Thread.currentThread().getName());
			/**
			 * 当工作任务不在执行时，启动任务。（这里使用同步是用了双确认，以保证效率）
			 * 当启动任务但，任务中没有数据处理时 闲置计数器加1
			 */
			if(!w.isWorking()){
				working=true;
				if(!w.work()){
					eCount++;
					/**
					 * 当连续闲置任务超50时，睡一下
					 */
					if(eCount>=50){
						trySleep(1);
						eCount=0;
					}
				}else{
					eCount=0;
				}
				working=false;
			}
			
		}
		
	}
	private void trySleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public boolean isWorking() {
		return working;
	}
	
}
