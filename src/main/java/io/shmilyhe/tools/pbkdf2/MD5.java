package io.shmilyhe.tools.pbkdf2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 implements Hash {

	MessageDigest digest;
	public  MD5(){
		try {
			digest = MessageDigest.getInstance("MD5"); 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void reset() {
		digest.reset();
	}

	@Override
	public void write(byte[] bytes) {
		digest.update(bytes);
	}

	@Override
	public byte[] sum(byte[] bytes) {
		if(bytes!=null){
			digest.update(bytes);
		}
		return digest.digest();
	}
	@Override
	public int size() {
		return 32;
	}
	@Override
	public Hash getHash() {
		// TODO Auto-generated method stub
		return new MD5();
	}
	@Override
	public int blockSize() {
		// TODO Auto-generated method stub
		return 64;
	}
	@Override
	public void write(byte[] bytes, int off, int len) {
		// TODO Auto-generated method stub
		digest.update(bytes, off, len);
		
	}
	
	

}
