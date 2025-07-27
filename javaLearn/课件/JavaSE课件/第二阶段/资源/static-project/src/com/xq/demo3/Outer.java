package com.xq.demo3;

/**
 * 静态内部类的使用
 */
public class Outer {

    // 定义一个外部类的静态方法
    public static void out(){
        System.out.println("这是out外部类里面定义的静态方法");
    }

    // 定义一个外部类的非静态方法
    public void show(){
        System.out.println("这是out外部类里面定义的非静态方法");
    }

    static class Inner{ // 静态内部类

        static String username = "eric";

        // 在内部类里面定义一个非静态的方法
        public void inner(){ // 在内部类的普通方法中可以访问外部类的静态方法，但是不能访问外部类的普通方法
            out();
            // 报错
            // show();
        }

        // 在内部类里面定义一个静态的方法
        public static void staticInner(){ // 在内部类的静态方法中可以访问外部类的静态方法，不能访问外部类的普通方法
            out();
            // 报错
            // show();
        }
    }
}
