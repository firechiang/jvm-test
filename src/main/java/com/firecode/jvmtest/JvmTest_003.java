package com.firecode.jvmtest;

/**
 * -XX:+HeapDumpOnOutOfMemoryError    #内存溢出时导出整个堆信息
 * -XX:HeapDumpPath=d:/Test03.dump    #内存溢出时导出整个堆信息存放的地址
 * 
 * 附录：mat 内存溢出分析工具（可分析 Test03.dump 文件）
 * @author JIANG
 */
public class JvmTest_003 {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//第一次配置：  -Xmx2m -Xms2m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=d:/Test03.dump
		
		byte[] bytes = null;
		for (int i = 0; i < 10; i++) {
			bytes = new byte[1204 * 1024 * 1];
		}
	}
}
