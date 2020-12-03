package com.ariel.healthbit;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Details
{
    public double height;
    public double weight;
    public String gender;
    public String date;

    public Details()
    {
    }
    public Details(double height,double weight, Date datebirth,String gender)
    {
        this.height=height;
        this.weight=weight;
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        this.date = simpleDateFormat.format(datebirth);
        this.gender=gender;
    }
    public Details (Details d)
    {
        this.height=d.height;
        this.weight=d.weight;
        this.gender=d.gender;
        this.date=d.date;
    }

}
