## JVM 参数配置
#####
在实际应用中可以将初始堆大小和最大堆大小设置相等，这样可以减少程序运行时垃圾回收次数，从而提高效率。
```bash
-Xmx20m                           #最大堆大小
-Xms5m                            #初始堆大小
-XX:+UseSerialGC                  #串行垃圾回收器
-XX:+PrintGC                      #打印GC日期
-XX:+PrintGCDetails               #打印GC详细信息
-XX:+PrintCommandLineFlags        #打印虚拟机参数
```
#####
不同的堆分配，对系统的执行会有一定的影响，应该根据系统的情况合理的配置；
 尽可能的将对象预留在新生代，减少老年代的GC次数（原因：老年代的GC是 "Full GC"（全面GC） 会造成系统停顿，假死）；
   除了可以设置新生代绝对大小（-Xmn），还可以使用（-XX:NewRatio）设置老年代和新生代的比列。
```bash
-Xmx20m                           #最大堆大小
-Xms20m                           #初始堆大小
-Xmn1m                            #初始新生代大小（和 -XX:NewRatio 配置选其中一个），新生代的大小影响老年代大小：堆 = 新生代 + 老年代；这个参数对应用GC影响很大（可以减少GC的次数），一般设置为整个堆空间的1/3或1/4
-XX:NewRatio=2                    #老年代和新生代比列 ，这里就是 老年代 是新生代的两倍
-XX:SurvivorRatio=2               #新生代里面的比例配置  （eden = 2） = （form=1） + （to=1）
-XX:+UseSerialGC                  #串行垃圾回收器
-XX:+PrintGCDetails               #打印GC详细信息
```