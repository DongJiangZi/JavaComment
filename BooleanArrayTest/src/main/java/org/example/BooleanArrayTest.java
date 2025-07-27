package org.example;
import org.openjdk.jol.info.ClassLayout;

public class BooleanArrayTest {
    public static void main(String[] args) {
        boolean[] arr = new boolean[10];  // 创建 boolean 数组

        System.out.println(ClassLayout.parseInstance(arr).toPrintable());
    }
}
