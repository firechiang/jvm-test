#### [JVM相关命令参考文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/index.html)
#### 查看全部JVM运行参数值，[官方参考文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/index.html)
```bash
# 启动当前应用打印JVM命令行参数（=表示初始值，:=表示修改后的值）（注意：-version可以理解为启动了一个应用）
$ java -XX:+PrintCommandLineFlags -version

# 查看当前应用JVM初始参数的值（=表示初始值，:=表示修改后的值）（注意：-version可以理解为启动了一个应用）
$ java -XX:+PrintFlagsInitial -version

# 查看当前应用JVM最终运行时参数的值（注意：-version可以理解为启动了一个应用）
$ java -XX:+PrintFlagsFinal -version

# 启动当前应用解锁一些JVM的实验性参数（就是有些JVM参数是实验性的，不能用加上这个就能用了）（注意：-version可以理解为启动了一个应用）
$ java -XX:+UnlockExperimentalVMOptions -version

# 启动当前应用解锁一些JVM的诊断性参数（就是有些JVM参数是实验性的，不能用加上这个就能用了）（注意：-version可以理解为启动了一个应用）
$ java -XX:+UnlockDiagnosticVMOptions -version
```

#### jinfo命令查看JVM运行时参数值简单使用，详情和描述信息的意思可参考[官方参考文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jinfo.html#BCGEBFDD)
```bash
$ jinfo -flags [进程ID]                  # 查看某个应用所有被我们手动修改过的JVM参数
$ jinfo -flag MaxHeapSize [进程ID]       # 查看某个应用的最大堆内存
$ jinfo -flag [垃圾回收器的名称] [进程ID] # 查看某个应用是否启用了某种垃圾回收器
```

#### jstat命令查看JVM统计信息简单使用，详情和描述信息的意思可参考[官方参考文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html#BEHHGFAE)
```bash
# 查看某个应用的类装载信息（示例：jstat -class 2584 1000 10）
$ jstat -class [进程ID] [打印间隔时间(毫秒)] [打印次数]

# 查看某个应用的垃圾回收的信息（示例：jstat -gc 2584 1000 10）
$ jstat -gc [进程ID] [打印间隔时间(毫秒)] [打印次数]

# 查看某个应用的JIT编译信息（编译成本地代码）（示例：jstat -compiler 2584 1000 10）
$ jstat -compiler [进程ID] [打印间隔时间(毫秒)] [打印次数]
```

#### jmap命令导出和打印JVM内存信息简单使用（导出文件后可使用Eclipse MAT工具查看），详情和描述信息的意思可参考[官方参考文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jmap.html#CEGCECJB)
```bash
# 导出某个应用的内存信息（可以看到每个对象的内存信息）
$ jmap -dump:format=b,file=/home/heap-test.hprof [进程ID]
```

#### jstack命令查看JVM里面线程信息简单使用，详情和描述信息的意思可参考[官方参考文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstack.html#BABGJDIF)，[线程状态官方说明](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr034.html)，[线程状态切换说明](https://mp.weixin.qq.com/s/GsxeFM7QWuR--Kbpb7At2w)
```bash
# 查看某个应用所有线程的信息
$ jstack [进程ID]

# 将某个应用所有线程的信息导出到test_test.txt文件
$ jstack [进程ID] > test_test.txt
```




