package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class ProductEvent implements DatabaseReference.CompletionListener {

    private int type;
    private int count;
    private String productID;
    private String start;

    public ProductEvent() {

    }
    public ProductEvent(int type, int count, String productID, String start) {
        this.type = type;
        this.count = count;
        this.productID = productID;
        this.start = start;
    }

    public int getType() {
        return type;
    }
    public int getCount() {
        return count;
    }
    public String getProductID() {
        return productID;
    }
    public String getStart() {
        return start;
    }

    public void setType(int type) {
        this.type = type;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setProductID(String productID) {
        this.productID = productID;
    }
    public void setStart(String start) {
        this.start = start;
    }


    @Override
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

    }
}
