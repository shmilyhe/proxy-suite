package com.eshore;

import org.junit.Test;

import com.eshore.tools.pbkdf2.PBKDF2;
import com.eshore.tools.pbkdf2.Sha256;

public class TestPbkdf2 {

	@Test
	public void test1(){
		/**
		 * gogs 采用的密钥算法
		 */
		System.out.println(PBKDF2.pbkdf2("Admin","4wcsVGKRvi",10000,50,new Sha256()));
	}
}
