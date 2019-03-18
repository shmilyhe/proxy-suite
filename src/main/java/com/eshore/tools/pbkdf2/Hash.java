package com.eshore.tools.pbkdf2;

/**
 * 散列接口
 * @author eshore
 *
 */
public interface Hash {
	/**
	 * 重置
	 */
	void reset();
	/**
	 * 更新散列
	 * @param bytes
	 */
	void write(byte[] bytes);
	
	/**
	 * 更新散列
	 * @param bytes
	 * @param off
	 * @param len
	 */
	void write(byte[] bytes ,int off,int len);
	
	/**
	 * 计算散列结果
	 * @param bytes
	 * @return
	 */
	byte[] sum(byte[] bytes);
	
	
	int size();
	
	/**
	 * 分割的块大小
	 * @return
	 */
	int blockSize();
	
	/**
	 * 获取一个新的实例
	 * 避免工厂模式构建过多的工厂，直接从实例中创建
	 * @return
	 */
	Hash getHash();
}
