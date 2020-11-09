package com.traidev.shokanjali;


public class User {

    private String mobile,name,city;

    public User( String name,String mobile,String city)
    {
        this.name = name;
        this.mobile = mobile;
        this.city = city;
    }

    public User() {

    }


    public String getName() {
        return name;
    }
    public String getMobile() {
        return mobile;
    }
    public String getCity() {
        return city;
    }

    public String setCity(String city) {
        return city;
    }


}

