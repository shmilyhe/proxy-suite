package io.shmilyhe.tools;

import java.util.*;
/**
 * 一个简单的yaml语法解析器 ，不支持 flow 语法即{a:123,b:123}
 * @author eshore
 *
 */
public class SimpleYaml {
	Object lv[]=new Object [10];
	int lvt[]=new int [10];
	int lvbc[]=new int [10];
	String lvn[]=new String [10];
	int maxDeep=10;
	
	int lastDeep=0;
	int level=0;
	
	public Object parse(String str){
		String lines[]=str.split("[\r\n]+");
		SimpleYaml ysl = new SimpleYaml();
		for(String line:lines){
			ysl.pline(line);
		}
		return ysl.lv[0];
	}
	
	/**
	 * 处理一行
	 * @param str
	 */
	private void pline(String str){
		
		int ban_count=0;//空格数
		boolean isList=false;
		int name_start=-1;//键值开始
		int name_end=-1;//键值结束
		int value_start=-1;//数值开始
		int value_end=-1;//数值结束
		boolean isCountBlank=true;
		/**
		 * 遍历当前字符串
		 */
		for(int i=0;i<str.length();i++){
			char c =str.charAt(i);
			if(' '==c){
				//计算前面的空格数
				if(isCountBlank)
				ban_count++;
			}else if('-'==c){
				isCountBlank=false;
				if(i-ban_count==0){
					value_start=i;
					isList=true;
				}
			}else if(':'==c){
				isCountBlank=false;
				if(value_start<0){
					value_start=i;
					name_end=i;
				}
			}else if('#'==c){
				isCountBlank=false;
				if(name_start<0){
					return;
				}
			}else{
				isCountBlank=false;
				if(name_start<0){
					name_start=i;
				}
			}
		}
		
		int last_l=level;
		int cur_level= findLevel(ban_count);
		extendLevel(cur_level);
		
		//当层级发生变化时，置空当前层级
		if(last_l>cur_level){
			for(int i=cur_level;i<=last_l;i++){
				if(i==0)continue;
				//当list 下有非list的时候不置空
				if(lvt[i]==1&&i<lvt.length-1&&lvn[i+1]!=null)continue;
				lvn[i]=null;
				lv[i]=null;
				lvt[i]=0;
			}
			
		}
		
		
		String name=null;
		String value=null;
		if(!isList){
			if(name_end>0){
				name=str.substring(name_start>0?name_start:0, name_end);
			}else{
				name=str.substring(name_start>0?name_start:0);
			}
			if(value_start>0){
				value=str.substring(value_start+1);
			}
			
			lvt[cur_level]=0;
		}else{
			lvt[cur_level]=1;
			if(value_start>0){
				value=str.substring(value_start+1);
			}

		}
		add(cur_level,isList?1:0,name,value);
		lastDeep=ban_count;
	}
	
	/**
	 * 查找当前的层级
	 * @param bl
	 * @return
	 */
	private int findLevel(int bl){
					if(bl>lastDeep){
						level++;
						lvbc[level]=bl;
					}else if(bl<lastDeep){
						for(int i=level;i>=0;i--){
							if(lvbc[i]==bl){
								level=i;
								lvbc[level]=bl;
							}
						}
					}else{
						lvbc[level]=bl;
					}
		return level;
	}
	
	/**
	 *
	 * @param level 层级
	 * @param type 类型
	 * @param name 名称
	 * @param value 数据
	 */
	@SuppressWarnings("rawtypes")
	private void add(int level,int type,String name,String value){
		if(type==0){
			Map m=(Map)lv[level];
			if(m==null){
				m=new HashMap();
				lv[level]=m;
				addToParent(level,m);
			}
			if(value!=null){
				m.put(name==null?lvn[level]:name, value);
			}
		}else{
			List l=(List)lv[level];
			if(l==null){
				l=new ArrayList();
				lv[level]=l;
				addToParent(level,l);
			}
			if(value!=null&&value.length()>0){
				l.add(value);
			}
		}
		lvn[level]=name;
	}
	
	/**
	 * 把当前的数据与上一层关联
	 * @param level
	 * @param o
	 */
	private void addToParent(int level,Object o){
		if(level==0)return;
		int type=lvt[level-1];
		if(type==1){
			List pl=(List)lv[level-1];
			pl.add(o);
		}else{
			Map pm=(Map) lv[level-1];
			pm.put(lvn[level-1], o);
		}
		
	}
	
	private void extendLevel(int level){
		if(maxDeep>level)return;
		int newDeep=maxDeep*2;
		Object lv_[]=new Object [newDeep];
		int lvt_[]=new int [newDeep];
		int lvbc_[]=new int [newDeep];
		String lvn_[]=new String [newDeep];
		System.arraycopy(lv, 0, lv_, 0, lv.length);
		System.arraycopy(lvt, 0, lvt_, 0, lvt.length);
		System.arraycopy(lvbc, 0, lvbc_, 0, lvbc.length);
		System.arraycopy(lvn, 0, lvn_, 0, lvn.length);
		lv=lv_;
		lvt=lvt_;
		lvbc=lvbc_;
		lvn=lvn_;
		maxDeep=newDeep;
		
		
		
	}
	
	
	public static void main(String[] arg){
		{
			/**
			 
companies:
    -
        id: 1
        name: company1
        price: 200W
    -
        id: 2
        name: company2
        price: 500W
			 
			 */
			SimpleYaml s =new SimpleYaml();
			s.pline("companies:");
			s.pline("    -");
			s.pline("        id: company1");
			s.pline("        name: company1");
			s.pline("        price: 200W");
			s.pline("    -");
			s.pline("        id: company2");
			s.pline("        name: company2");
			s.pline("        price: 250W");
			Object o=s.lv[0];
			System.out.println("---");
		}
		System.out.println("---------------------------------------------------");
		{
			SimpleYaml s =new SimpleYaml();
			s.pline("#123123123");
			s.pline("-");
			s.pline("   -123123");
			s.pline("   -default:127.0.0.1:8080:8080");
			s.pline("   -1");
			s.pline("   -");
			s.pline("     -123");
			s.pline("     -1234");
			s.pline("-");
			s.pline("  -");
			s.pline("    a:1");
			s.pline("    b:2");
			s.pline("  -");
			s.pline("    c:3");
			s.pline("    d:4");
			s.pline("  -default:127.0.0.1:8080:8080");
			s.pline("  -2");
			Object o=s.lv[0];
			System.out.println("---");
		}
	}
}
