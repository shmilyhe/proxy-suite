package com.eshore.tools.pbkdf2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.eshore.tools.Bytes;

public class Sha384 implements Hash {

	public static void main(String[] args){
		System.out.println(Bytes.toHexString(new Sha384().sum("abc".getBytes())));
	}
	
	MessageDigest digest;
	public  Sha384(){
		try {
			digest = MessageDigest.getInstance("SHA-384"); 
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
		return 48;
	}
	@Override
	public Hash getHash() {
		// TODO Auto-generated method stub
		return new Sha384();
	}
	@Override
	public int blockSize() {
		// TODO Auto-generated method stub
		return 128;
	}
	@Override
	public void write(byte[] bytes, int off, int len) {
		// TODO Auto-generated method stub
		digest.update(bytes, off, len);
		
	}
	
	

}
