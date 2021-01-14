/**
 * PBKDF2 的实现
 */

package io.shmilyhe.tools.pbkdf2;

import io.shmilyhe.tools.Bytes;

/**
 * PBKDF2 的实现
 * @author eshore
 *
 */

public class PBKDF2 {
	
	public static void main(String[] args) {
		System.out.println(pbkdf2("Admin","4wcsVGKRvi",10000,50,new Sha256()));
		System.out.println(pbkdf2("Admin","4wcsVGKRvi",10000,50,new Sha1()));
		System.out.println(pbkdf2("Admin","4wcsVGKRvi",10000,50,new MD5()));
	}
	
	
	
	/**
	 * 
	 * @param pwd 密码
	 * @param salt 盐
	 * @param it 迭代次数
	 * @param keyLen 密钥长度
	 * @param hash 散列算法
	 * @return
	 */
	public static String pbkdf2(String pwd,String salt,int it,int keyLen ,Hash hash){
		HMAC  prf = new HMAC(pwd.getBytes(),hash);
		int hashLen =prf.size();
		int numBlocks =(keyLen + hashLen - 1) / hashLen;
		byte[] buf  = new byte[4];
		StringBuilder sb = new StringBuilder();
		byte[] dk=null;
		
		for(int block=1;block<=numBlocks;block++){
			byte[] hmac=null;
			prf.reset();
			prf.write(salt.getBytes());
			buf[0] = (byte)(block >> 24);
			buf[1] = (byte)(block >> 16);
			buf[2] = (byte)(block >> 8);
			buf[3] = (byte)(block);
			prf.write(buf);

			dk=prf.sum(null);
			hmac=dk;
			for(int i=2;i<=it;i++){
				prf.reset();
				prf.write(dk);
				dk=prf.sum(null);
				for(int j=0;j<hmac.length;j++){
					hmac[j]  =(byte) (hmac[j] ^dk[j]);
				}
			}
			sb.append(Bytes.toHexString(hmac));
		}
		if(sb.length()>numBlocks*keyLen)
		return sb.substring(0, numBlocks*keyLen).toString();
		return sb.toString();
	}
	

}
