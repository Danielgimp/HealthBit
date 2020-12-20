package com.ariel.healthbit;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order
{
    String userUID;
    double totalPrice;

    public ArrayList<productOrder> getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(ArrayList<productOrder> itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    ArrayList<productOrder> itemQuantity;

    public Order(String userUID){
       this.userUID=userUID;
       this.itemQuantity=new ArrayList<productOrder>();
       this.totalPrice=0;
    }

    public Order(String userUID,ArrayList<productOrder> itemQuantity,double totalPrice){
        this.userUID=userUID;
        this.itemQuantity=itemQuantity;
        this.totalPrice=totalPrice;
    }



    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void FillProductInMap(String name,double price)
    {
        boolean flag=false;
        int index=-1;
        for(int i=0;i<itemQuantity.size();i++)
        {
            if(itemQuantity.get(i).getItem()==name)
            {
                itemQuantity.get(i).setAmount(itemQuantity.get(i).getAmount()+1);
                itemQuantity.get(i).setPrice(itemQuantity.get(i).getPrice()+price);
                flag=true;
                index=i;
            }

        }
        if(!flag)
        {
          productOrder prd=new productOrder(name,1, price);
          itemQuantity.add(prd);
        }
    }

    public int getItemQuantity(String productName) {
        for(int i=0;i<itemQuantity.size();i++)
        {
            if(itemQuantity.get(i).getItem()==productName)
            {
                return itemQuantity.get(i).getAmount();
            }

        }
        return 0;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double price)
    {
        double tPrice=0;
        for(int i=0;i<itemQuantity.size();i++)
        {
            tPrice+=itemQuantity.get(i).getPrice();
        }
        this.totalPrice=tPrice;

    }





}
