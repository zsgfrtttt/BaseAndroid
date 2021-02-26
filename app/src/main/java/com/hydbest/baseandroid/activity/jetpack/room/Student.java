package com.hydbest.baseandroid.activity.jetpack.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Student {
    //id > 0
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "age")
    private int age;

    @ColumnInfo(name = "sex")
    private boolean boy;
//    @ColumnInfo(name = "score")
//    private int score;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Room只能识别一个构造函数
     * @param id
     * @param name
     * @param age
     */
    @Ignore
    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

//    public int getScore() {
//        return score;
//    }
//
//    public void setScore(int score) {
//        this.score = score;
//    }

    public boolean isBoy() {
        return boy;
    }

    public void setBoy(boolean boy) {
        this.boy = boy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
    }
}
