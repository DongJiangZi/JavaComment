package com.xq.demo3;

/**
 * 静态内部类的使用场景：创建单例对象
 *   在并发场景下面，单例对象的获取是线程安全的，并且实现了单例对象的创建是按需创建的。
 */
public class Singleton {

    private Singleton(){

    }

    static class SingletonHandler{
        private static Singleton singleton = new Singleton();
    }

    public static Singleton getInstance(){
        return SingletonHandler.singleton;
    }
}
