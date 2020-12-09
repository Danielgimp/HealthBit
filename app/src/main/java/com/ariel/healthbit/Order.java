package com.ariel.healthbit;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Order {
    String OrderUID;
    Map<String, Integer> itemQuantity;
    double totalPrice;

    public Order(String ID){
       this.OrderUID=ID;
       this.itemQuantity=new HashMap<>();
       this.totalPrice=0;
    }


    public void setOrderUID(String orderUID) {
        OrderUID = orderUID;
    }

    public void FillProductInMap(String name)
    {
        if(!this.itemQuantity.containsKey(name))
        {
            itemQuantity.put(name,1);
        }
        else
        {
            itemQuantity.put(name,itemQuantity.get(name)+1);
        }
    }

    public int getItemQuantity(String productName) {
        for(Map.Entry<String,Integer> entry : itemQuantity.entrySet())
        {
            if(entry.getKey()==productName)
            {
                return entry.getValue();
            }

        }
        return 0;
    }

    public double getTotal(){
        for (Map.Entry<String,Integer> entry : itemQuantity.entrySet())
        {
            DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference("products");
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, storeProduct> td = (Map<String, storeProduct>) dataSnapshot.getValue();
                    for (Map.Entry<String, storeProduct> innerEntry : td.entrySet()) {
                        HashMap nana=innerEntry.getValue();
                        String name= (String) nana.get("name");
                        Long Lprice=(Long) nana.get("price");
                        double price=Lprice.doubleValue();
                        if(entry.getKey()==name)
                        {
                            totalPrice+=entry.getValue()*price;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            };
            productsRefernce.addValueEventListener(postListener);
        }


        return totalPrice;
    }




}
