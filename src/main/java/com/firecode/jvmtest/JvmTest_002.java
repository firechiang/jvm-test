package com.firecode.jvmtest;

/**
 * -Xmx20m                           #最大堆大小
 * -Xms20m                           #初始堆大小
 * -Xmn1m                            #初始新生代大小（和 -XX:NewRatio 配置选其中一个），新生代的大小影响老年代大小：堆 = 新生代 + 老年代；这个参数对应用GC影响很大（可以减少GC的次数），一般设置为整个堆空间的1/3或1/4
 * -XX:NewRatio=2                    #老年代和新生代比列 ，这里就是 老年代 是新生代的两倍
 * -XX:SurvivorRatio=2               #新生代里面的比例配置  （eden = 2） = （form=1） + （to=1）
 * -XX:+UseSerialGC                  #串行垃圾回收器
 * -XX:+PrintGCDetails               #打印GC详细信息
 * 
 * 总结：
 * 不同的堆分配，对系统的执行会有一定的影响，应该根据系统的情况合理的配置；
 * 尽可能的将对象预留在新生代，减少老年代的GC次数（原因：老年代的GC是 "Full GC"（全面GC） 会造成系统停顿，假死）；
 * 除了可以设置新生代绝对大小（-Xmn），还可以使用（-XX:NewRatio）设置老年代和新生代的比列
 * 
 * @author JIANG
 */
public class JvmTest_002 {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//第一次配置：  -XX:SurvivorRatio=2 -Xmx20m -Xms20m -Xmn1m -XX:+UseSerialGC -XX:+PrintGCDetails
		//解释：-XX:SurvivorRatio=2（eden 2 = form 1 + to 1） 新生代里面的比例配置
		
		//第二次配置：  -XX:SurvivorRatio=2 -Xmx20m -Xms20m -Xmn7m -XX:+UseSerialGC -XX:+PrintGCDetails
		
		//第三次配置：  -XX:NewRatio=2 -Xmx20m -Xms20m -XX:+UseSerialGC -XX:+PrintGCDetails
		//解释：-XX:NewRatio=2 （老年代 是新生代的两倍）
		
		byte[] bytes = null;
		for (int i = 0; i < 10; i++) {
			bytes = new byte[1204 * 1024 * 1];
		}
	}
}
