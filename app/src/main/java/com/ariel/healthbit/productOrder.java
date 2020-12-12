package com.ariel.healthbit;

public class productOrder {
    String Item;
    int amount;
    double price=0;


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public productOrder(String item, int amount, double price) {
        this.Item = item;
        this.amount = amount;
        this.price=price;
    }

    public void setItem(String item) {
        Item = item;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItem() {
        return Item;
    }

    public int getAmount() {
        return amount;
    }
}
