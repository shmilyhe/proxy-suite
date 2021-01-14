package io.shmilyhe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.shmilyhe.tools.pbkdf2.PBKDF2;
import io.shmilyhe.tools.pbkdf2.Sha256;

public class UserManger {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserManger um = new UserManger();
		/**
		 * 生成测试的用户
		 * 
		 */
		System.out.println("生成测试的用户");
		ArrayList<String> tks = new ArrayList<String>();
		for(int i=0;i<10;i++){
			String account ="user"+i;
			String passwd ="123456";
			String token =um.encrypt(account, passwd);
			tks.add(token);
			//System.out.println(token);
		}
		
		/**
		 * 把用户数据加载到 UserManger中
		 */
		System.out.println("把用户数据加载到 UserManger中");
		um.load(tks);
		
		
		/**
		 * 验证用户
		 */
		System.out.println("验证用户");
		{
			String account="user0";
			String passwd="123456";
			System.out.println("account:"+account+"\t passwd:"+passwd+"结果：\t"+ um.auth(account, passwd));
		}
		
		{
			String account="user0";
			String passwd="1234567";
			System.out.println("account:"+account+"\t passwd:"+passwd+"结果：\t"+ um.auth(account, passwd));
		}
		
		{
			String account="user1";
			String passwd="123456";
			System.out.println("account:"+account+"\t passwd:"+passwd+"结果：\t"+ um.auth(account, passwd));
		}
		
		/**
		 * 输出所有已加载的用户信息
		 */
		System.out.println("输出所有已加载的用户信息");
		List<String> userTokens =um.getAllTokens();
		for(String t:userTokens){
			System.out.println(t);
		}
		
		
	}
	

	HashMap<String,String> userMap = new HashMap<String,String>();
	
	/**
	 * 添加一批验证数据
	 * @param userdatas  userdatas
	 */
	public void load(List<String> userdatas){
		if(userdatas==null)return;
		for(String t:userdatas){
			addTokenToMap(t);
		}
	}
	
	
	/**
	 * 获取所用的用户数据
	 * @return
	 */
	public List<String> getAllTokens(){
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(userMap.values());
		return list;
	}
	
	
	/**
	 * 添加一批验证数据
	 * @param userdatas  userdatas
	 */
	public void load(String[] userdatas){
		if(userdatas==null)return;
		for(String t:userdatas){
			addTokenToMap(t);
		}
	}
	
	/**
	 * 添加一条用户验证数据
	 * @param token token
	 */
	public void addTokenToMap(String token){
		if(token==null)return;
		String[] tks =token.split("\\$");
		if(tks==null||tks.length!=3)return;
		userMap.put(tks[0], token);
	}
	
	/**
	 * 把账号密码转成一个token,用于保存
	 * @param account 账号
	 * @param passwd 密码
	 * @return 加密后的token
	 */
	public String  encrypt(String account,String passwd){
		String uuid =UUID.randomUUID().toString().replaceAll("-", "");
		String code=PBKDF2.pbkdf2(passwd, uuid ,10000,50,new Sha256());
		return account+"$"+code+"$"+uuid;
	}
	
	/**
	 * 检验账号密码是否正确
	 * @param account 账号
	 * @param passwd 密码
	 * @return 是否通过
	 */
	public boolean auth(String account,String passwd){
		String token = userMap.get(account);
		if(token==null)return false;
		String[] tks =token.split("\\$");
		if(tks==null||tks.length!=3)return false;
		String code=PBKDF2.pbkdf2(passwd, tks[2] ,10000,50,new Sha256());
		return code.equals(tks[1]);
	}

}
