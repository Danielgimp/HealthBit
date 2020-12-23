package com.ariel.healthbit;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This class manages order as an Finished order and ready to be sent\pulled from Firebase DB.
 * The class members are: userID String (Firebase UID of the user purchasing the current Order), TotalOrder cost and Arraylist of productOrder Class holding the Item, amount and price
 * for each and every item on the current order.
 * This class can(Methods): create an finished order, set User UID, add (or fill) products to the current order, get the quantity of each item on request and set the total order price.
 */
public class Order
{
    String userUID;
    double totalPrice;
    ArrayList<productOrder> itemQuantity;

    /**
     * @return Arraylist consisted of productOrder class members
     */
    public ArrayList<productOrder> getItemQuantity() {
        return itemQuantity;
    }

    /**
     * Built for Firebase imports (required) , does nothing.
     */
    public Order(){};
    /**
     * A copy constructor, input is an existing Order class Node, an a copy if Order class is created.
     * @param  order Input the Order node to create.
     */
    public Order(Order order)
    {
        this.userUID=order.userUID;
        this.totalPrice=order.totalPrice;
        this.itemQuantity=order.itemQuantity;
    }

    /**
     * Constructor for Order Class
     * @param  userUID Name of the user making the order.
     */
    public Order(String userUID){
       this.userUID=userUID;
       this.itemQuantity=new ArrayList<productOrder>();
       this.totalPrice=0;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    /**
     * Fills the Arraylist With a productOrder class, if the product is non-existent a new node will be created for the product, Otherwise the function adds a +1 to existing product.
     * @param  name Name of the product.
     * @param price The price of the product
     */
    public void FillProductInArrayList(String name,double price)
    {
        boolean flag=false;
        int index=-1;
        for(int i=0;i<itemQuantity.size();i++)
        {
            if(itemQuantity.get(i).Item==name)
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

    /**
     * Returns the item Quantitiy by iterating on the inner Array-List of this class
     * @param  productName Name of the product.
     * @return Quantity of the given product.
     */
    public int getItemQuantity(String productName) {
        for(int i=0;i<itemQuantity.size();i++)
        {
            if(itemQuantity.get(i).Item==productName)
            {
                return itemQuantity.get(i).getAmount();
            }

        }
        return 0;
    }

    /**
     * Use to get the Total Order Price.
     * @return Returns the Total Order Price.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the price of the Order
     * @return Technically nothing, but internally the total price is updated.
     */
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
