package com.eshore.tools.pbkdf2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1 implements Hash {

	MessageDigest digest;
	public  Sha1(){
		try {
			digest = MessageDigest.getInstance("SHA-1"); 
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
		return new Sha1();
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
