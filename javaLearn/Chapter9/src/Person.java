public class Person {
    public String name;

    public String sex;

    public int age;

    public Person(){
        this.name = "默认玩家";
    }
    public void eat() {
        String name ="leiyidong";
        System.out.println(this.name + "吃饭");
    }
    public void sleep() {
        System.out.println("人睡觉");
    }
    public void work() {
        System.out.println("人工作");
    }
}
