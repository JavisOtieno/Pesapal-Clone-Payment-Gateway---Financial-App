package com.javy.pesapalclone.models;

import java.io.Serializable;

public class Product implements Serializable {

    public String id;
    public String name;
    public String type;
    public String target_amount;
    public String remaining_amount;
    public String target_type;
    public String description;
    public String image;
    public String date;

    public Product() {
    }

//    public Account(String name, boolean section) {
//        this.name = name;
//        this.section = section;
//    }

}
