package com.firecode.jvmtest.mem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


/**
 * 内存溢出测试
 */
public class MemTest {
	
	
	/**
	 * 堆内存溢出
	 * -Xmx32m -Xms32m
	 */
	/*public static void main(String[] args){
	    List<String> stringList = new ArrayList<>();
		while(true){
			stringList.add(UUID.randomUUID().toString());
		}
	}*/
	
	/**
	 * 非堆内存溢出（Metaspace（元空间）溢出）
	 * -Xmx32m -Xms32m
	 */
	public static void main(String[] args){
		List<Class<?>> classList = new ArrayList<>();
		while(true){
			classList.addAll(Metaspace.createClasses());
		}
	}
	
	
	/*
	 * https://blog.csdn.net/bolg_hero/article/details/78189621
	 * 继承ClassLoader是为了方便调用defineClass方法，因为该方法的定义为protected
	 * */
	private static class Metaspace extends ClassLoader {
		
	    public static List<Class<?>> createClasses() {
	        // 类持有
	        List<Class<?>> classes = new ArrayList<Class<?>>();
	        // 循环1000w次生成1000w个不同的类。
	        for (int i = 0; i < 10000000; ++i) {
	            ClassWriter cw = new ClassWriter(0);
	            // 定义一个类名称为Class{i}，它的访问域为public，父类为java.lang.Object，不实现任何接口
	            cw.visit(Opcodes.V1_1, Opcodes.ACC_PUBLIC, "Class" + i, null,
	                    "java/lang/Object", null);
	            // 定义构造函数<init>方法
	            MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>",
	                    "()V", null, null);
	            // 第一个指令为加载this
	            mw.visitVarInsn(Opcodes.ALOAD, 0);
	            // 第二个指令为调用父类Object的构造函数
	            mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object",
	                    "<init>", "()V");
	            // 第三条指令为return
	            mw.visitInsn(Opcodes.RETURN);
	            mw.visitMaxs(1, 1);
	            mw.visitEnd();
	            Metaspace test = new Metaspace();
	            byte[] code = cw.toByteArray();
	            // 定义类
	            Class<?> exampleClass = test.defineClass("Class" + i, code, 0, code.length);
	            classes.add(exampleClass);
	        }
	        return classes;
	    }
	}

}
