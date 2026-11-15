package com.javy.pesapalclone.models;

import java.io.Serializable;

//import java.io.Serializable;

public class Account implements Serializable {

    public String id;
    public String account_name;
    public String phone;
    public String website;
    public String address;
    public String date;

    public Account() {
    }

//    public Account(String name, boolean section) {
//        this.name = name;
//        this.section = section;
//    }

}