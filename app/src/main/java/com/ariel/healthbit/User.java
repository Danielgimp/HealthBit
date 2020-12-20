package com.ariel.healthbit;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String name;
    public String lname;
    public String email;
    public String Started;
    public String phone;
    public String  admin="false";


    public User() {}

    public User(String name, String lname, String email, String phone)
    {
        this.name=name;
        this.lname=lname;
        this.email=email;
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        this.Started = simpleDateFormat.format(new Date());
        this.phone=phone;

    }
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("lname", lname);
        result.put("phone", phone);
        return result;
    }


}
