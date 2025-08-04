import java.util.Arrays;

/**
 * 某老师现要录入班级学生信息（姓名，性别，年龄，成绩），学生信息存储在数组中。请使用方法完成
 */

public class Teacher {
//    Student[] stuTable = new Student[100];
//
//    public void storeStuInfo(Student stu) {
//        for(int i = 0; i < stuTable.length; i++){
//            if(stuTable[i] == null){
//                stuTable[i] = stu;
//            }
//        }
//    }


//    public Student[] stus = {}; //刚开始的时候 一个学生也没有
//
//    public void addStu(Stu stu){//添加一个学生信息
//            stus = Arrays.copyOf(stus, stus.length + 1); //先对数组进行扩容
//            stus[stus.length - 1] = stu;
//    }

    public Student[] stus = {};

    public void StoreStuInfo(Student stuInfo){
        stus = Arrays.copyOf(stus, stus.length+1);
        stus[stus.length - 1] = stuInfo;
    }
}
