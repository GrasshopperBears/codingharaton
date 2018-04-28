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

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof TripUser)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        TripUser c = (TripUser) o;

        // Compare the data members and return accordingly
        return c.user.equals(this.user) && c.start_location.equals(this.start_location)
                &&c.destination.equals(this.destination) && c.gender == this.gender
                && c.age == this.age && c.comment.equals(this.comment);
    }



}
