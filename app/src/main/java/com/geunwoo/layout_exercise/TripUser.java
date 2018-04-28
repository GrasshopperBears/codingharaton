package com.geunwoo.layout_exercise;

public class TripUser{
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

    public String printStart(){
        String answer = String.format("(%.2f,%.2f)", start_location[0], start_location[1]);
        return answer;
    }

    public String printDest(){
        String answer = String.format("(%.2f,%.2f)", destination[0], destination[1]);
        return answer;
    }


}
