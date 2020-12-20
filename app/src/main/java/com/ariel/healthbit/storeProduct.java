package com.ariel.healthbit;

import java.util.HashMap;
import java.util.Map;

public class storeProduct extends HashMap {

    public String name;
    public double kcal,price;
    public String subType; //this  meant to explain more about the product...if it's a shake than what flavor...etc
    public int UnitsInStock=0;

    public int getUnitsInStock() {
        return this.UnitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        UnitsInStock = unitsInStock;
    }

    public String getName() {
        return name;
    }

    public storeProduct()
    {
        this.name ="Strawberry Protein Shake";
        kcal=250;
        subType="Delicious shake to meet you're nutritious needs";
        price=100;
        this.UnitsInStock=1;
    }

    public storeProduct(String name,double kcal,double price,String subType)
    {
        this.name=name;
        this.kcal=kcal;
        this.price=price;
        this.subType=subType;
        UnitsInStock++;
    }
    public storeProduct(String name,double kcal,double price,String subType,int uis)
    {
        this.name=name;
        this.kcal=kcal;
        this.price=price;
        this.subType=subType;
        UnitsInStock=uis;
    }


    public storeProduct(storeProduct toCopy)
    {
        this.name=toCopy.getName();
        this.kcal=toCopy.getKcal();
        this.subType=toCopy.getSubType();
        this.price=toCopy.getPrice();
        this.UnitsInStock=toCopy.getUnitsInStock();

    }


    public double getKcal() {
        return kcal;
    }

    public double getPrice() {
        return price;
    }

    public String getSubType() {
        return subType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}
