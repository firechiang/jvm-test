package com.firecode.jvmtest;

/**
 * -Xss1m                           #线程最大栈空间
 * 
 * @author JIANG
 */
public class JvmTest_004 {
	
	private static int count = 0;
	
	private static void  a(){
		count++;
		a();
	}
	
	public static void main(String[] args) {
		try{
			a();
		}catch(Throwable e){
			System.err.println("最大栈深度："+count);
			e.printStackTrace();
		}
	}
}
