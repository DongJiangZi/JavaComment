package com.xq.demo1;

/**
 * static关键字:
 *   static可以修饰变量，这个变量就是静态变量
 *      访问静态变量的时候，我们可以直接使用类名.静态变量名去访问。
 *   小结： 什么时候用静态变量? 就是成员变量的值固定不变，我们可以将其设置成静态变量，因为在内存中不会占用过多的空间。静态变量只会
 *   在堆内存中维护一份数据。
 *         静态变量如何使用? 在数据类型前面加上一个static关键字。在调用的时候使用类名调用就可以了。
 *   在JDK8之后，静态变量是保存在堆内存中的。在类加载的时候初始化，并且只会被JVM加载1次。
 *
 *
 *   static修饰一个方法，这个方法就是静态方法。访问静态方法的时候，我们也可以使用类名直接访问
 *   小结：静态方法的声明：在方法的返回值前面加上一个static关键字
 *        静态方法的调用：
 *                     可以直接通过类名调用。也可以通过实例对象调用，但是不推荐。如果实例对象为null。使用空对象调用静态方法也不会报错，
 *                     因为静态方法的调用本身和对象是无关的。
 *                     在静态方法中不能使用非静态的资源(普通的变量、普通的方法)；在普通的方法中是可以直接访问静态的资源(静态的变量  静态的方法)
 *       为什么将一个方法声明称静态的方法:
 *                     一些具有工具性质的方法，我们可以使用静态关键字static修饰，将其描述称一个静态方法，方便调用。
 *                     比如: 读取项目中全局配置文件的方法。这个读取资源的方法在项目中的很多地方都用到了。为了避免写重复代码。我们将其定义成一个静态方法
 *                          这样不用写很多重复冗余的代码。并且通过类名调用即可。
 *                          在进行数据库的操作。 首先必须获取数据库连接。对数据表操作完毕之后，还需要关闭数据库连接资源。我们可以考虑将获取数据库连接的方法
 *                          关闭数据库连接的方法设置成静态方法。
 *
 */
public class ChinesePerson { // 中国人类

   /* int i; // 实例变量
    static int j; // 静态变量

    public static void main(String[] args) {
        int i = 0; //局部变量
    }*/

    // 身份证号
    String idCard;

    // 姓名
    String name;

    // 国籍
    static String country = "中国";

    public ChinesePerson(String idCard, String name) {
        this.idCard = idCard;
        this.name = name;
    }

    // 定义一个静态方法
    public static void test(){
        System.out.println("静态方法test执行了.....");

        // 报错，因为在静态方法中不能访问非静态的方法
        // display();
        // System.out.println(name);

        // 在静态方法中可以访问静态的资源(静态方法、静态变量)
        test02();
        System.out.println(country);
    }

    public static void test02(){
        System.out.println("这是test02静态方法");
    }

    // 展示用户信息的方法 这是一个非静态的方法
    public void display(){
        // 在普通方法的内部是可以直接访问静态的资源(静态变量、静态方法)
        test02();
        System.out.println(country);
        System.out.println("身份证编号:" + this.idCard + ",姓名:" + this.name + ",国籍:" + ChinesePerson.country);
    }
}
