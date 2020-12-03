package com.ariel.healthbit;

public class storeProduct {

    public String name;
    public double kcal,price;
    public String subType; //this  meant to explain more about the product...if it's a shake than what flavor...etc
    int UnitsInStock=0;

    public int getUnitsInStock() {
        return UnitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        UnitsInStock = unitsInStock;
    }

    public String getName() {
        return name;
    }

    public storeProduct()
    {
        name="Strawberry Protein Shake";
        kcal=250;
        subType="Delicious shake to meet you're nutritious needs";
        price=100;
        UnitsInStock++;
    }

    public storeProduct(String name,double kcal,double price,String subType)
    {
        this.name=name;
        this.kcal=kcal;
        this.price=price;
        this.subType=subType;
        UnitsInStock++;
    }

}
