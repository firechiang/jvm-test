package com.firecode.jvmtest;

import java.util.HashMap;
import java.util.Map;

/**
 * -XX:PretenureSizeThreshold=1000      #对象超过多大直接进入老年代（单位K），一般是一个新生对象太大而 eden 区装不下，直接进入老年代。（但是要注意TLAB区域优先分配空间）
 * 
 * @author JIANG
 */
public class JvmTest_007 {
	
	public static void main(String[] args) {
		//参数 -Xmx30m -Xms30m -XX:+UseSerialGC -XX:+PrintGCDetails -XX:PretenureSizeThreshold=1000 
		//为什么对象在新生代，原因：这些对象在 TLAB 区（线程独立空间），可以禁用 TLAB 区（-XX:-UseTLAB）再试一下
		Map<Integer,byte[]> map = new HashMap<>();
		for (int i = 0; i < 5 * 1024; i++) {
			byte[] bytes = new byte[1024];
			map.put(i, bytes);
		}
	}
}
