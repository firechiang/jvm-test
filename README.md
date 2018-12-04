## JVM 基本结构
```bash
 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _          _ _ _ _ _ _ _ _ _ _ 
|                                                    |       |                   |
|                    类加载子系统                     |       |      Java栈       |
|_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ |       |_ _ _ _ _ _ _ _ _ _|   
 _ _ _ _ _ _ _      _ _ _ _ _ _ _     _ _ _ _ _ _ _ _         _ _ _ _ _ _ _ _ _ _ 
|             |   |              |   |               |       |                   |
|    方法区   |    |   Java堆     |   |   直接内存    |       |     本地方法区     |
|_ _ _ _ _ _ _|   | _ _ _ _ _ _ _|   |_ _ _ _ _ _ _ _|       |_ _ _ _ _ _ _ _ _ _|
 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _          _ _ _ _ _ _ _ _ _ _ 
|                                                    |       |                   |
|                    垃圾回收系统                     |       |     PC寄存器       |
|_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ |       |_ _ _ _ _ _ _ _ _ _|
 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ __ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
|                                                                                |
|                                   执行引擎                                      |
|_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ __ _ _ _ _ _ _ _ _ _ _ _ _ _ _|
```
注：每一方法都是一个栈帧
## JVM 参数配置
```bash
-                                      #标准参数，所有JVM都应该支持
-X                                     #非标，每个JVM实现有所不同
-XX                                    #不稳定参数，下个版本可能会取消
```
####
在实际应用中可以将初始堆大小和最大堆大小设置相等，这样可以减少程序运行时垃圾回收次数，从而提高效率。
```bash
-Xmx20m                                #最大堆大小
-Xms5m                                 #初始堆大小
-XX:+PrintGC                           #打印GC日期
-XX:+PrintHeapAtGC                     #GC前后都会将GC堆的概要状况输出到log中
-XX:+PrintGCDetails                    #打印GC详细信息
-XX:+PrintCommandLineFlags             #打印虚拟机参数

-XX:+UseSerialGC                       #串行垃圾回收器

-XX:+UseG1GC                           #G1垃圾收集器，一步步图解G1：https://mp.weixin.qq.com/s/ZwlT89vsvD2e0qEuxZto3Q；详解：https://tech.meituan.com/g1.html
-XX:G1HeapRegionSize=16m               #G1收集器，设置java堆被分割的大小。这个大小范围在1M到32M之间（看情况使用）

-XX:+UseParNewGC                       #ParNew是Serial收集器的多线程版本（新生代并发收集器）。Server模式下默认新生代收集器，除了Serial收集器之外，一般与CMS或G1配个使用

-XX:+UseConcMarkSweepGC                #CMS收集器，回收老年代，标记清除法的实现，缺点：内存空间碎片问题，垃圾回收后的空间不是连续的，当再分配大对象时，因为没有连续的空间分配，会出现Full GC
-XX:+CMSParallelRemarkEnabled          #减少第二次暂停的时间，开启并行 remark
-XX:CMSInitiatingOccupancyFraction=80  #（看情况使用）默认CMS是在tenured generation沾满68%的时候开始进行CMS收集，如果你的年老代增长不是那么快，并且希望降低CMS次数的话，可以适当调高此值
-XX:+UseCMSCompactAtFullCollection     #（已废弃，不建议使用）开启对老年代的压缩，是碎片减少

-XX:+UseAdaptiveSizePolicy             #（配合和"设置期望最大GC停顿时间"，一起使用，不兼容CMS）并行收集器会自动选择年轻代区大小和相应的Survivor区比例，以达到目标系统规定的最低相应时间或者收集频率等，此值建议使用并行收集器时，一直打开
-XX:+UseFastAccessorMethods            #get/set方法转成本地代码
-XX:+AggressiveOpts                    #启用这个参数，则每当JDK版本升级时，JVM都会使用最新加入的优化技术
-XX:+DisableExplicitGC                 #禁用立即GC（System.gc），将GC全面交由垃圾收集器管理
-XX:+ExplicitGCInvokesConcurrent       #（看情况使用）当调用System.gc()的时候， 执行并行gc。默认是禁用的
-XX:+CMSScavengeBeforeRemark           #（看情况使用）如果remark还是过长的话，强制remark之前开始一次minor gc，减少remark的暂停时间，但是在remark之后也将立即开始又一次minor gc
-XX:ParallelGCThreads=2                #（看情况使用）设置年轻代的并行收集线程数，默认是(cpu <= 8) ? cpu : 3 + ((cpu * 5) / 8)

-XX:MaxGCPauseMillis=200               #设置期望最大GC停顿时间(配合G1使用)（JVM会尽力实现，但不保证达到），年轻代垃圾回收的最长时间，如果无法满足此时间，JVM会自动调整年轻代大小，以满足此值
-XX:LargePageSizeInBytes=128m          #内存分页大小对性能的提升
-Djava.awt.headless=true               #开启Headless模式（缺少显示屏模式），解决 tomcat bug
```
####
偏向锁
所谓的偏向，就是偏心，即锁会偏向于当前已经占有锁的线程 。
大部分情况是没有竞争的（某个同步块大多数情况都不会出现多线程同时竞争锁），所以可以通过偏向来提高性能。即在无竞争时，
之前获得锁的线程再次获得锁时，会判断是否偏向锁指向我，那么该线程将不用再次获得锁，直接就可以进入同步块。
偏向锁的实施就是将对象头Mark的标记设置为偏向，并将线程ID写入对象头Mark，
当其他线程请求相同的锁时，偏向模式结束
```bash
-XX:+UseBiasedLocking                  #启用偏向锁
-XX:BiasedLockingStartupDelay=0        #表示系统启动几秒钟后启用偏向锁。默认为4秒，原因在于，系统刚启动时，一般数据竞争是比较激烈的，此时启用偏向锁会降低性能
```
####
不同的堆分配，对系统的执行会有一定的影响，应该根据系统的情况合理的配置；
 尽可能的将对象预留在新生代，减少老年代的GC次数（原因：老年代的GC是 "Full GC"（全面GC） 会造成系统停顿，假死）；
   除了可以设置新生代绝对大小（-Xmn），还可以使用（-XX:NewRatio）设置老年代和新生代的比列。
```bash
-Xss1m                                 #线程最大栈空间
-Xmn1m                                 #初始新生代大小（和 -XX:NewRatio 配置选其中一个），新生代的大小影响老年代大小：堆 = 新生代 + 老年代；这个参数对应用GC影响很大（可以减少GC的次数），一般设置为整个堆空间的1/3或1/4
-XX:NewRatio=2                         #老年代和新生代比列 ，这里就是 老年代 是新生代的两倍
-XX:SurvivorRatio=2                    #新生代里面的比例配置  （eden = 2） = （form=1） + （to=1）
```
####
Eclipse插件 Mat，内存溢出分析工具（可分析 Test03.dump 文件）。推荐使用  VisualVM （Java性能监控工具）。
```bash
-XX:+HeapDumpOnOutOfMemoryError        #内存溢出时导出整个堆信息
-XX:HeapDumpPath=d:/Test03.dump        #内存溢出时导出整个堆信息存放的地址
```
####
一般对象首次创建会被放置在新生代的 eden 区，如果没有GC的介入，则对象不会离开 eden 区。只有对象的年龄达到一定的大小，才会进入老年代，
对象的年龄是由经历GC次数决定的，在新生代每次GC之后如果对象没有被回收则年龄加1，虚拟机由提供参数来控制新生代最大年龄，当超过这个年龄
范围就会晋升老年代。
```bash
-XX:MaxTenuringThreshold=15            #新生代最大年龄，默认15
-XX:PretenureSizeThreshold=100         #对象超过多大直接进入老年代（单位K），一般是一个新生对象太大而 eden 区装不下，直接进入老年代。（但是要注意TLAB区域优先分配空间，TLAB说明看下面）
```
####
TLAB全称是Thread Local Allocation Buffer（栈上分配）即线程本地分配缓存，就是线程专用的内存分配区域，为了加速对象分配而生。每一个线程都会产生一个TLAB，该线程独享的工作区域，java虚拟机使用这种TLAB区来避免多线程冲突问题，提高内存分配效率，TLAB一般不会太大，当大对象无法在TLAB分配时，则直接分配在堆上。
（TLAB空间参数一般不需要调整，看实际情况）
```bash
-XX:+UseTLAB                           #启用 TLAB（启用有助于提高性能，默认已启用）
-XX:TLABSize=10                        #设置TLAB大小
-XX:TLABRefillWasteFraction=64         #设置进入TLAB空间单个对象的最大值，它是一个比例值，默认64，即对象大于整个空间的1/64，则在堆创建对象
-XX:+ResizeTLAB                        #启用自动调整 TLABRefillWasteFraction 阈值
-XX:+PrintTLAB                         #启用打印 TLAB 信息
-XX:-DoEscapeAnalysis                  #禁用（逃逸分析，开启有助于提高性能，默认开启），要想打印 TLAB 信息，需禁用
-XX:+EliminateAllocations              #开启标量替换（允许将对象打撒在分配的栈上）（默认开启）
```
#### 对象创建内存分配流程
```bash
对象创建  --> 尝试栈上分配  ——（失败）——> 尝试TLAB分配  ——（失败）——> 是否进入老年代  ——（失败）——> eden分配
                  |                         |                         |
               （满足）                   （满足）                   （满足）         
                  |                         |                         |
                栈分配                    TLAB分配                 老年代分配
```
## GC算法
#### 引用计数法：
对象被其它所引用时计数器加1，而当引用失效时则减1；弊端：无法处理循环引用的情况，还有就是加减消耗系统性能。
#### 标记清除法：
分标记和清除两个阶段进行处理内存中的对象；弊端：内存空间碎片问题，垃圾回收后的空间不是连续的，不连续的内存空间的工作效率要低于连续的内存空间。
#### 复制算法：
将内存空间分为两块，每次只使用其中一块，在垃圾回收时，将正在使用的内存对象复制到未被使用的内存块中去，然后去清除正在使用的内存块中所有的对象，反复去交换两个内存的角色，以完成垃圾回收。
（java 新生代中的  from 和 to 空间就是使用这种算法）
#### 标记压缩法：
标记压缩法在标记清除法之上做了优化，把存活的对象压缩到内存的一端，而后进行垃圾清理。
（java老年代使用的就是标记压缩法）
#### 分代算法：
根据对象的特点把内存分成N块，而后根据每块内存的特点使用不同的算法。
对于新生代和老年代来说，新生代回收频率很高，但是每次回收耗时很短，而老年代回收频率较低，但耗时较长，所以尽量减少老年代的GC。
#### 分区算法：
将内存分为N多个独立的小空间，每个小空间都可以独立使用，这样细粒度的控制回收，而不对整个内存进行GC，从而提升性能，并减少GC的停顿时间。

## 强引用、软引用、弱引用、虚引用的概念
#### 强引用（StrongReference）
强引用就是指在程序代码之中普遍存在的，比如下面这段代码中的object和str都是强引用：
```bash
Object object = new Object();
```
只要某个对象有强引用与之关联，JVM必定不会回收这个对象，即使在内存不足的情况下，JVM宁愿抛出OutOfMemory错误也不会回收这种对象。
#### 软引用（SoftReference）
软引用是用来描述一些有用但并不是必需的对象，在Java中用java.lang.ref.SoftReference类来表示。对于软引用关联着的对象，只有在内存不足的时候JVM才会回收该对象。因此，这一点可以很好地用来解决OOM的问题，并且这个特性很适合用来实现缓存：比如网页缓存、图片缓存等。
下面是一个使用示例：
```bash
SoftReference<String> sr = new SoftReference<String>(new String("hello"));
System.out.println(sr.get());
```
软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被JVM回收，这个软引用就会被加入到与之关联的引用队列中。
#### 弱引用（WeakReference）
弱引用也是用来描述非必需对象的，当JVM进行垃圾回收时，无论内存是否充足，都会回收被弱引用关联的对象。在java中，用java.lang.ref.WeakReference类来表示。下面是使用示例：
```bash
WeakReference<String> sr = new WeakReference<String>(new String("hello"));
System.out.println(sr.get());
System.gc();                 //通知JVM的gc进行垃圾回收
System.out.println(sr.get());
```
第二个输出结果是null，这说明只要JVM进行垃圾回收，被弱引用关联的对象必定会被回收掉。不过要注意的是，这里所说的被弱引用关联的对象是指只有弱引用与之关联，如果存在强引用同时与之关联，则进行垃圾回收时也不会回收该对象（软引用也是如此）。
#### 虚引用（PhantomReference）
虚引用和前面的软引用、弱引用不同，它并不影响对象的生命周期。在java中用java.lang.ref.PhantomReference类表示。如果一个对象与虚引用关联，则跟没有引用与之关联一样，在任何时候都可能被垃圾回收器回收。
```bash
ReferenceQueue<String> queue = new ReferenceQueue<String>();
PhantomReference<String> pr = new PhantomReference<String>(new String("hello"), queue);
System.out.println(pr.get());
```
要注意的是，虚引用必须和引用队列关联使用，当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会把这个虚引用加入到与之 关联的引用队列中。程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。如果程序发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取必要的行动。
