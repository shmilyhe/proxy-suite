package com.eshore;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.eshore.socketapi.commons.Action;
import com.eshore.socketapi.commons.SimpleProtocol;

public class TestProtocol {

	/**
	 * 测试协议解释是否正确
	 * @throws IOException 
	 */
	@Test
	public void test1() throws IOException{
		DataOutputStream dpo;
		//dpo.writeShort(v);
		
		SimpleProtocol sp = new SimpleProtocol();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Action a = new Action();
		a.setAction("test");
		a.setToken("logintoken");
		a.setDatas("中国人".getBytes());
		a.addAttribute("t1", "10");
		a.addAttribute("t2", "3");
		sp.write(out, a);
		ByteArrayInputStream in= new ByteArrayInputStream(out.toByteArray());
		Action b = sp.read(in);
		assertEquals("invaild action","test",b.getAction());
		assertEquals("invaild token","logintoken",b.getToken());
		assertEquals("invaild attribute t1","10",b.getAttribute("t1"));
		try {
			assertEquals("invaild datas","中国人",new String(b.getDatas(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(b.getAction());
		
	}
}
