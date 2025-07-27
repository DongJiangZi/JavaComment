package com.xq.demo3;

/**
 * 测试单例对象
 */
public class TestSingleton {
    public static void main(String[] args) {
        Singleton o1 = Singleton.getInstance();
        Singleton o2 = Singleton.getInstance();
        System.out.println(o1 == o2);
    }
}
