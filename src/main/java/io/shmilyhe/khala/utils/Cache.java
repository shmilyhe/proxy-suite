package io.shmilyhe.khala.utils;




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

public interface Cache<K, V> {
	
	/**
	 * 获取元素个数
	 * @return
	 */
	public int size() ;

	
	
	/**
	 * 增加元素到缓存中
	 * @param k key 键
	 * @param v value 值
	 */
	public  void put(K k, V v) ;
	
	/**
	 * 增加元素到缓存中
	 * @param k key 键
	 * @param v value 值
	 * @param time 缓存时长 0则不算
	 */
	@SuppressWarnings("unchecked")
	public  void put(K k, V v,long time);

	/**
	 * 获取缓存值
	 * @param k key
	 * @return value
	 */
	public V get(K k);
	
	/**
	 * 
	 * @param k key
	 * @return value
	 */
	public V remove(K k);
	
	/**
	 * 
	 * @param key
	 * @return isContains
	 */
	public boolean containsKey(Object key);

}
