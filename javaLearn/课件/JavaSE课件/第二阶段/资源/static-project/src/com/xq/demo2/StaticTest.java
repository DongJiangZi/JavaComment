package com.xq.demo2;

/**
 * 静态代码块的使用
 *   静态代码块在main方法之前执行 说明：静态代码块在类加载的时候执行，并且只执行1次
 *   在一个类中，我们可以定义多个静态代码块，多个静态代码块尊徐自上而下的执行顺序
 */
public class StaticTest {

    // 定义一个非静态的变量
    String name = "eric";

    // 声明一个静态的变量
    static int i = 100;

    static {
        // 不能在静态代码块中访问非静态的成员变量
        // System.out.println(name);
        System.out.println(i);
        // 定义代码
        System.out.println("静态代码块1执行了.....");
        // 报错，因为静态资源按照自上而下的加载顺序，此时静态变量j还没有初始化
        // System.out.println(j);
    }

    static int j = 101;

    static {
        System.out.println("静态代码块2执行了.....");
    }

    public static void main(String[] args) {
        System.out.println("main 方法执行了....");
    }

    static {
        System.out.println("静态代码块3执行了.....");
    }
    
}
