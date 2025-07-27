package com.xq.demo1;

import java.util.concurrent.Callable;

/**
 * @author kriss
 * @version 1.0.0
 * @date 2025-06-09 16:52
 * @description TODO
 */
public class TestPerson {
    public static void main(String[] args) {
        // 创建三个中国人对象
        ChinesePerson zhangsan = new ChinesePerson("1001","张三");
        ChinesePerson lisi = new ChinesePerson("1002","李四");
        ChinesePerson wangwu = new ChinesePerson("1003","王五");

        // 使用实例对象调用display方法
        zhangsan.display();
        lisi.display();
        wangwu.display();

        System.out.println(ChinesePerson.country);

        // 不推荐使用对象调用静态变量，使用类名调用静态变量更加方便
        /*System.out.println(zhangsan.country);
        System.out.println(lisi.country);
        System.out.println(wangwu.country);*/

        // 静态方法的调用
        lisi.test();
       ChinesePerson c = null;
        // 使用一个空对象去访问静态方法，是不会报空指针异常的。因为静态方法的调用和对象无关。
        // 虽然我们通过对象进行静态方法的调用。在编译的时候，编译器会将对象替换成类名进行调用。
       c.test();
       // 使用类名调用静态方法
       ChinesePerson.test();
    }
}
