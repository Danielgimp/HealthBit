package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class TipEvent implements DatabaseReference.CompletionListener{

    private String date;
    private String context;


    public TipEvent() {

    }
    public TipEvent(String date, String context) {
        this.date = date;
        this.context = context;
    }

    public String getDate() {
        return date;
    }
    public String getContext() {
        return context;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setContext(String context) {
        this.context = context;
    }


    @Override
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

    }
}
