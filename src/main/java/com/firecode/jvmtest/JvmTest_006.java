package com.firecode.jvmtest;

import java.util.HashMap;
import java.util.Map;

/**
 * -XX:MaxTenuringThreshold=15       #新生代最大年龄，默认15
 * 
 * 测试进入老年代的对象，查看第16次GC时 eden 已清空（DefNew: 284642K->0K）
 * @author JIANG
 */
@SuppressWarnings("unused")
public class JvmTest_006 {
	
	public static void main(String[] args) {
		//参数 -Xmx1024m -Xms1024m -XX:+UseSerialGC -XX:MaxTenuringThreshold=15 -XX:+PrintGCDetails 
		Map<Integer,byte[]> map = new HashMap<>();
		for (int i = 0; i < 5; i++) {
			byte[] bytes = new byte[1024*1024];
			map.put(i, bytes);
		}
		
		for (int k = 0; k < 20; k++) {
			for (int j = 0; j < 300; j++) {
				byte[] bytes = new byte[1024*1024];
			}
		}
	}
}
