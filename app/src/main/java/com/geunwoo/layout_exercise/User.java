package com.geunwoo.layout_exercise;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable{
    String SID;
    String password;
    String name;
    String phone;
    String age;
    int gender;
    String preference;

    public User(String SID, String password, String name, String phone, String age, int gender, String preference){
        this.SID = SID;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.preference = preference;
    }

}
