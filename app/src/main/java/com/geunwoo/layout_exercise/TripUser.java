package com.geunwoo.layout_exercise;

import android.os.Parcelable;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TripUser implements Serializable{
    private User user;
    private Double[] start_location;
    private Double[] destination;
    private int gender;
    private int age;
    private String comment;

    TripUser(User person, Double[] start, Double[] dest, int hopegender, int hopeage, String comment){
        this.user = person;
        this.start_location = start;
        this.destination = dest;
        this.gender = hopegender;
        this.age = hopeage;
        this.comment = comment;
    }

    public String getName(){
        return user.name;
    }

    public Double[] getStart_location(){
        return start_location;
    }

    public String forPrint(){
        String answer = "";
        answer += String.format("희망 성별: %s \n", gender==1 ? "남자" : "여자");
        answer += String.format("희망 연령대: %s", age);

        return answer;
    }

    public String forMatching(){
        String answer = "";
        answer += String.format("이름: %s \n연락처: %s \n유저 성별: %s \n유저 여행스타일: %s \n  ",this.user.name,
                this.user.phone, this.user.gender==1 ? "남자":"여자", this.user.preference);

        return answer;
    }

    public Double[] getDestination() {
        return destination;
    }



}
