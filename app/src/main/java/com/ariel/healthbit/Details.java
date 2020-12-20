package com.ariel.healthbit;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Details
{


    public double height;
    public double weight;
    public String gender;
    public String date;
    public ArrayList<Double> weights;


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

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
        this.weights=new ArrayList<>();
        this.weights.add(weight);
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }


    @Exclude
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("height", height);
        result.put("weight", weight);
        result.put("gender", gender);
        result.put("weights",weights);
        return result;
    }



}
