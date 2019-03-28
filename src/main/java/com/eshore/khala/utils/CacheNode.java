package com.eshore.khala.utils;

/**
 * 缓存节点
 * @author eric
 *
 * @param <K> 键
 * @param <V> 值
 */
public class CacheNode<K,V> {
	/**
	 * 键
	 */
	K key;
	
	/**
	 * 值
	 */
	V value;
	CacheNode next;
	CacheNode pre;
	long lastAccess=System.currentTimeMillis();
	long cacheTime=0;
	
	
	public long getCacheTime() {
		return cacheTime;
	}


	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}


	private boolean removed=false;
	
	
	
	/**
	 * 是否过期
	 * @return
	 */
	public boolean isExpire(){
		if(cacheTime==0)return false;
		return System.currentTimeMillis()-lastAccess>=cacheTime;
	}
	
	
	/**
	 * 更新最后访问时间
	 * 
	 */
	public void updateTime(){
		lastAccess=System.currentTimeMillis();
	}
	
	/**
	 * 
	 * @param k 键
	 * @param t 值
	 */
	public CacheNode(K k,V t){
		key =k;
		value =t;
	}
	
	/**
	 * 
	 * @param k 键值
	 * @param t 值
	 * @param time 缓存时长，单位为豪秒
	 */
	public CacheNode(K k,V t,long time){
		key =k;
		value =t;
		cacheTime=time;
	}
	
	public CacheNode(){
		
	}
	
	
	
	/**
	 * 移除
	 */
	public void remove(){
		//if(next!=null)
		if(next!=null)
		next.pre=pre;
		if(pre!=null)
		pre.next=next;
		next = null;
		pre = null;
	}
	
	/**
	 * 手插到 n 节点前面
	 * @param n node
	 */
	public void insertBefore(CacheNode n){
		next =n;
		pre =n.pre;
		n.pre=this;
	}
	
	/**
	 * 连接到 n 节点之后
	 * @param n node
	 */
	public void appendTo(CacheNode n){
		
		/*if(n.next!=null){
			next=n.next;
			n.next.pre=this;
		}*/
		n.next=this;
		pre=n;
		
		//if(n.next==null&&n.pre==null)throw new RuntimeException("========01========"+n);
		//if(next==null&&pre==null)throw new RuntimeException("========02========"+n);
	}
	
	/**
	 * 获取 键值
	 * @return key
	 */
	public K getKey(){
		return key;
	}
	
	/**
	 * 设置键值
	 * @param k
	 */
	public void setKey(K k){
	  key=k;
	}
	
	/**
	 * 获取值
	 * @return value
	 */
	public V getValue(){
		if(removed)return null;
		if(cacheTime==0)return value;
		if(System.currentTimeMillis()-lastAccess>cacheTime)return null;
		return value;
	}
	
	/**
	 * 设置值
	 * @param t
	 */
	public void setValue(V t){
		value =t;
	}
	
	
	public String toString(){
		return "key:"+key+"\t"+"value:"+value+"   "+(pre==null?"-pre:null":"pre:"+pre.key)+(next==null?"-next:null":"next:"+next.key);
	}


	/**
	 * 是否已移除
	 * @return is removed
	 */
	public boolean isRemoved() {
		return removed;
	}

	

	/**
	 * 
	 * @param removed is remove
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
}