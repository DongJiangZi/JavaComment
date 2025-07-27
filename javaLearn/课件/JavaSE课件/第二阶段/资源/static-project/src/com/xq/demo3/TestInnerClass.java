package com.xq.demo3;

/**
 * 测试静态内部类的使用
 */
public class TestInnerClass {
    public static void main(String[] args) {
        // 调用静态内部类中的静态方法
        Outer.Inner.staticInner();
        // 访问静态内部类中的静态变量
        System.out.println(Outer.Inner.username);

        // 访问静态内部类中的非静态的方法
        Outer.Inner inner = new Outer.Inner();
        inner.inner();
    }
}
