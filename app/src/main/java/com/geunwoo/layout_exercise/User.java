package com.geunwoo.layout_exercise;

public class User {
    String SID;
    String password;
    String name;
    String phone;
    String age;
    int gender;
    int[] preference;

    public User(String SID, String password, String name, String phone, String age, int gender, int[] preference){
        this.SID = SID;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.preference = preference;
    }

}
