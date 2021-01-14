package io.shmilyhe.khala.utils;

import java.util.UUID;

import io.shmilyhe.tools.pbkdf2.PBKDF2;
import io.shmilyhe.tools.pbkdf2.Sha256;

public class Login {
	private static String passwd="4wcsVGKRvi";
	private static String account="default";

	public static String getAccount() {
		return account;
	}

	public static void setAccount(String account) {
		Login.account = account;
	}

	public static String getPasswd() {
		return passwd;
	}

	public static void setPasswd(String passwd) {
		Login.passwd = passwd;
	}
	
	
	public static boolean login(String token){
		if(token==null)return false;
		String strs[]=token.split("@");
		if(strs.length!=4)return false;
		String u=strs[0];	
		String uuid = strs[1];
		String time= strs[2];
		String code= strs[3];
		String pre =u+"@"+uuid+"@"+time+"@";
		String code2=PBKDF2.pbkdf2(pre, passwd ,10000,50,new Sha256());
		return code2.equals(code);
	}
	
	public static String  getToken(){
		String uuid =UUID.randomUUID().toString();
		String time=""+System.currentTimeMillis();
		String pre =account+"@"+uuid+"@"+time+"@";
		String code=PBKDF2.pbkdf2(pre, passwd ,10000,50,new Sha256());
		return pre+code;
	}
	
	public static void main(String[] agrs){
		System.out.println(login(getToken()));
	}
	
	
}
