/**
 * HMAC 实现
 * ipad = the byte 0x36 repeated B times
 *opad = the byte 0x5C repeated B times.
 *
 *HMAC(text) = H( K XOR opad, H(K XOR ipad, text))
 *
 */
package com.eshore.tools.pbkdf2;

import com.eshore.tools.Bytes;

/**
 * 
 * @author eshore
 *
 */
public class HMAC {
	final static int SIZE_SHA256=32;
	final static  int SIZE_SHA224=28;
	final static int BlockSize = 64;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HMAC h=	new HMAC("1".getBytes(),new Sha256());
		byte[] b = h.sum("1".getBytes());
		System.out.println(Bytes.toHexString(b));
		//System.out.println(hmac("abc","1",new Sha384()));
		System.out.println(hmac("abc","1",new Sha256()));
		System.out.println(hmac("abc","1",new Sha1()));
		System.out.println(hmac("abc","1",new MD5()));
		System.out.println(hmac("abc","1",new Sha512()));
		System.out.println(hmac("abc","1",new Sha384()));
	}
	public static String hmac(String password,String salt,Hash h){
		HMAC mac=	new HMAC(salt.getBytes(),h);
		return Bytes.toHexString(mac.sum(password.getBytes()));
	}
	
	Hash inner;
	Hash outer;
	int blocksize=BlockSize;
	int size=32;
	byte[] ipad;
	byte[] opad;
	public HMAC(byte[] key,Hash hash){
		inner=hash.getHash();
		outer=hash.getHash();
		blocksize=inner.blockSize();
		size =inner.size();
		ipad= new byte[blocksize];
		opad= new byte[blocksize];
		if(key.length>blocksize){
			outer.write(key);
			key=outer.sum(null);
		}
		System.arraycopy(key, 0, ipad, 0, key.length);
		System.arraycopy(key, 0, opad, 0, key.length);
		for(int i=0;i<ipad.length;i++){
			ipad[i] ^=0x36;
		}
		for(int i=0;i<opad.length;i++){
			opad[i] ^=0x5c;
		}
		inner.write(ipad);
	}
	
	
	
	
	public int size(){
		return size;
	}
	
	public void reset(){
		inner.reset();
	    inner.write(ipad);
	}
	
	public void write(byte[] b){
		inner.write(b);
	}
	
	public void write(byte[] b,int off,int len){
		inner.write(b,off,len);
	}
	
	public byte[] sum(byte[] b){
		byte[] in =inner.sum(b);
		outer.reset();
		outer.write(opad);
		outer.write(in);
		return outer.sum(null);
	}

}
