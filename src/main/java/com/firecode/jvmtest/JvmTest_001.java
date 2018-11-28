package com.firecode.jvmtest;

/**
 * -Xmx20m                         #最大堆大小
 * -Xms5m                          #初始堆大小
 * -XX:+UseSerialGC                #串行垃圾回收器
 * -XX:+PrintGC                    #打印GC日期
 * -XX:+PrintGCDetails             #打印GC详细信息
 * -XX:+PrintCommandLineFlags      #打印虚拟机参数
 * 
 * 总结：
 * 在实际应用中可以将初始堆大小和最大堆大小设置相等，这样可以减少程序运行时垃圾回收次数，从而提高效率。
 * 
 * 附录：
 * https://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html         #JVM 参数详解
 * https://tech.meituan.com/g1.html                                         #G1详解
 * https://www.nowcoder.com/discuss/129446?type=0&order=0&pos=22&page=0     #美团
 * @author JIANG
 */
@SuppressWarnings("unused")
public class JvmTest_001 {
	
	public static void main(String[] args) {
		System.err.println("堆可用大小："+Runtime.getRuntime().freeMemory()/1024);
		System.err.println("初始堆大小："+Runtime.getRuntime().totalMemory()/1024);
		System.err.println("最大堆大小："+Runtime.getRuntime().maxMemory()/1024);
		
		byte[] bytes1 = new byte[1024 * 1024 * 1];
		System.out.print("分配1M内存\n\n");
		
		System.err.println("堆可用大小："+Runtime.getRuntime().freeMemory()/1024);
		System.err.println("初始堆大小："+Runtime.getRuntime().totalMemory()/1024);
		System.err.println("最大堆大小："+Runtime.getRuntime().maxMemory()/1024);
		
		byte[] bytes4 = new byte[1024 * 1024 * 4];
		System.out.print("分配4M内存\n\n");
		
		System.err.println("堆可用大小："+Runtime.getRuntime().freeMemory()/1024);
		System.err.println("初始堆大小："+Runtime.getRuntime().totalMemory()/1024 + "，由于初始堆大小不够用，所以堆增大了，但不会大于 '最大堆大小'");
		System.err.println("最大堆大小："+Runtime.getRuntime().maxMemory()/1024);
		
		/*
		 * 测试内存溢出
		 * 
		byte[] bytes20 = new byte[1024 * 1024 * 20];
		System.out.print("分配20M内存\n\n");
		
		System.err.println("堆可用大小："+Runtime.getRuntime().freeMemory()/1024);
		System.err.println("初始堆大小："+Runtime.getRuntime().totalMemory()/1024);
		System.err.println("最大堆大小："+Runtime.getRuntime().maxMemory()/1024);*/
	}
}
