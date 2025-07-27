/**
 * Scanner基本用法
 * @author EthanDawn
 */
import java.util.Scanner;

public class Example3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入三位学生成绩：");
        int score1 = sc.nextInt();
        int score2 = sc.nextInt();
        int score3 = sc.nextInt();
        int average = (score1 + score2 + score3) / 3;
        System .out.println(average);
    }
}
