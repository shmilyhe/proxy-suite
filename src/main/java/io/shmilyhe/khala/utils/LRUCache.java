package io.shmilyhe.khala.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

    /**
     * LRU 缓存
     * 实现按访问的时间，保留最近访问的缓存方案
     * 
     * @author eric
     * 
     * 2017-07-12
     *
     * @param <K> 键
     * @param <V> 值
     */

    public class LRUCache<K, V> implements Cache<K, V> {
    	static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    	/**
    	 * 回收过时数据
    	 * @param cache
    	 */
    	public static void addWacher(final LRUCache cache){
    		fixedThreadPool.submit(new Callable(){
				@Override
				public Object call() throws Exception {
					cache.recycle();
					//System.out.println("======================================");
					return null;
				}
    			
    		});
    	}
    	
    	/**
    	 * 回收频率
    	 */
    	private long recycleTime=600;
    	
    	private int recycleFrequency=500;
    	
    	int count=0;
    	long last =System.currentTimeMillis();
    	private void count(){
    		/*if(count++>recycleFrequency){
    			count=0;
    			addWacher(this);
    		}*/
    		if(System.currentTimeMillis()-last>recycleTime){
    			addWacher(this);
    			last=System.currentTimeMillis();
    		}
    	}
    	
    	/**
    	 * 最大的缓存个数，超出的按最后的访问时算保留最近访问的
    	 */
    	private int maxSize = 65535;
    	
    	/**
    	 * 缓存时长，为0时没有时间限制
    	 */
    	private long cacheTime =60*1000*60*2;
    	
    	

    	/**
    	 * 缓存的元素个数
    	 */
    	private int size;
    	
    	/**
    	 * 缓存MAP
    	 */
    	private Map<K, CacheNode<K, V>> cache;
    	
    	CacheNode<K, V> head;
    	CacheNode<K, V> tail;
    	
    	/**
    	 * 设置缓存时长，单位为毫秒
    	 * @param ms
    	 */
    	public void setCacheTime(long ms){
    		cacheTime=ms;
    	}
    	
    	/**
    	 * 获取默认缓存时长
    	 * @return
    	 */
    	public long getCacheTime(){
    		return cacheTime;
    	}

    	/**
    	 * 获取元素个数
    	 * @return
    	 */
    	public int size() {
    		return size;
    	}

    	/**
    	 * 
    	 * @param maxSize 最大的缓存个数
    	 */
    	public LRUCache(int maxSize) {
    		this.maxSize = maxSize;
    		//cache = new TreeMap<K, CacheNode<K, V>>();
    		cache = new HashMap<K, CacheNode<K, V>>(maxSize);

    	}
    	
    	/**
    	 * 
    	 * @param maxSize 最大的缓个数
    	 * @param time 缓存时长 单位：毫秒 为0时长久有效
    	 */
    	public LRUCache(int maxSize,long time) {
    		this(maxSize);
    		setCacheTime(time);
    	}

    	/**
    	 * 默认构造器，默认缓存个数65535 ，缓存时长为0即不计算时长
    	 * 
    	 */
    	public LRUCache() {
    		cache = new HashMap<K, CacheNode<K, V>>(maxSize);
    	}

    	
    	/**
    	 * 增加元素到缓存中
    	 * @param k key 键
    	 * @param v value 值
    	 */
    	public  void put(K k, V v) {
    		put(k,v,cacheTime);
    	}
    	
    	/**
    	 * 增加元素到缓存中
    	 * @param k key 键
    	 * @param v value 值
    	 * @param time 缓存时长 0则不算
    	 */
    	@SuppressWarnings("unchecked")
    	public synchronized void put(K k, V v,long time) {
    		count();
    		if (head == null) {
    			tail = new CacheNode(k, v,time);
    			head = tail;
    			tail.pre = head;
    			cache.put(k, head);
    			size++;
    			return;
    		}
    		
    		if (cache.containsKey(k)) {
    			CacheNode<K, V> n = cache.get(k);
    			n.setValue(v);
    			n.updateTime();
    			n.setCacheTime(time);
    			n.setRemoved(false);
    			if (n != null && n == head) {
    				// System.out.println(n);
    				return;
    			}
    			if (n == tail) {
    				tail = tail.pre;
    				// System.out.println("uuuuuuuu="+k);
    			}
    			n.remove();
    			head.appendTo(n);
    			// n.insertBefore(head);
    			if (n.pre == null && n.next == null)
    				throw new RuntimeException("000000");
    			if (tail.pre == null && tail.next == null)
    				throw new RuntimeException("000tail000:" + k + " tail:"
    						+ tail.key);
    			// head.appendTo(n);
    			head = n;
    			return;
    		}

    		if (size >= maxSize) {

    			// if((Integer)k==99)System.out.println("==0000000000==="+tail.key);
    			CacheNode n = new CacheNode(k, v,time);
    			head.appendTo(n);
    			head = n;
    			cache.put(k, n);
    			// System.out.println("==remove==="+tail.key+"----"+k);
    			cache.remove(tail.getKey());
    			CacheNode ot = tail;
    			if (tail != null) {

    				tail = tail.pre;
    				if (tail == null) {
    					//System.out.println("==" + ot.key);
    				}else{
    					tail.next = null;
    				}
    				// ot.remove();
    			} else {
    				//System.out.println(k);
    			}
    			if (n.pre == null && n.next == null)
    				throw new RuntimeException("0000==00");

    			return;
    		}

    		{
    			CacheNode n = new CacheNode(k, v,time);
    			head.appendTo(n);
    			head = n;
    			cache.put(k, n);
    			size++;
    		}

    	}

    	/**
    	 * 获取缓存值
    	 * @param k
    	 * @return value
    	 */
    	public V get(K k) {
    		CacheNode<K, V> n = cache.get(k);
    		if (n == null)
    			return null;
    		return n.getValue();
    	}

    	/**
    	 * 获取所有的key
    	 * @return keys
    	 */
    	public Collection<K> keys() {
    		return cache.keySet();
    	}

    	/**
    	 * 获取所有的值
    	 * @return
    	 */
    	public Collection<V> values() {
    		ArrayList<V> al = new ArrayList<V>();

    		CacheNode<K, V> flag = head;
    		while (flag != null) {
    			if(!flag.isExpire())
    			al.add(flag.getValue());
    			flag = flag.next;
    			if (flag == head)break;
    				//throw new RuntimeException("reply!");
    		}
    		return al;
    	}
    	
    	public Collection<CacheNode<K, V>> nodes() {
    		ArrayList<CacheNode<K, V>> al = new ArrayList<CacheNode<K, V>>();

    		CacheNode<K, V> flag = tail;
    		while (flag != null) {
    			if(!flag.isExpire()){
    			al.add(flag);
    			}else{
    				flag.value=null;
    				flag.setRemoved(true);
    			}
    			flag = flag.pre;
    			if (flag == tail)break;
    				//throw new RuntimeException("reply!");
    		}
    		return al;
    	}
    	

    	@Override
    	public V remove(K k) {
    		// TODO Auto-generated method stub
    		CacheNode<K, V> n = cache.get(k);
    		if(n==null)return null;
    		V v=n.getValue();
    		n.setRemoved(true);
    		n.setValue(null);
    		//n.
    		return v;
    	}

    	@Override
    	public boolean containsKey(Object key) {
    		// TODO Auto-generated method stub
    		CacheNode<K, V> n = cache.get(key);
    		if(n==null)return false;
    		if(n.isExpire()||n.isRemoved())return false;
    		return true;
    	}
    	
    	/**
    	 * 回收超时的
    	 */
    	public void recycle(){
    		try{
	    		Collection<CacheNode<K, V>> nodes = this.nodes();
	    		if(nodes!=null)
	    		for(CacheNode<K, V> n :nodes){
	    			if(n.isExpire()){
	    				n.setRemoved(true);
	    				n.setValue(null);
	    				//System.out.println("*************************"+n.key);
	    			}
	    		}
    		}catch(Throwable t){
    			t.printStackTrace();
    		}
    		
    		
    	}

		public int getRecycleFrequency() {
			return recycleFrequency;
		}

		public void setRecycleFrequency(int recycleFrequency) {
			this.recycleFrequency = recycleFrequency;
		}

    	
    }

