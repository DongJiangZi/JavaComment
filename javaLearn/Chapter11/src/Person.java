public class Person{
    private String name;

    private int age;

    private String secret;

    public Person(String name, int age, String secret){
        this.name = name;
        this.age = age;
        this.secret = secret;
    }

    public String getName(){
        return name;
    }
}
