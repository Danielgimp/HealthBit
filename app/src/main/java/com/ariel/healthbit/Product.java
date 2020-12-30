package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class Product implements DatabaseReference.CompletionListener {

    private int cal;
    private String name;

    public Product() {

    }
    public Product(int cal, String name) {
        this.cal = cal;
        this.name = name;
    }

    public int getCal() {
        return cal;
    }
    public String getName() {
        return name;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

    }
}
