package com.ariel.healthbit;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manager all the products that are going into the store.
 * Each storeProduct node contains the following information\members:
 * 1. Name - the name of the product.
 * 2. kCal - how many calories the given product has in total.
 * 3. Price - The price of the product.
 * 4. SubType - A description on the Item.
 * 5. UnitsInStock - Represents the units in stock.
 * This class can perform the following actions:
 * get Units In Stock of each item, get the name of each item, get the total calories of each item , get the Price of each item, get the description of each item.
 * set Units In Stock of each item, set the name of each item, set the total calories of each item , set the Price of each item , set the description of each item.
 */

public class storeProduct extends HashMap {
    /**
     * Represents the name of the item
     */
    public String name;
    /**
     * Represents how many calories the given product has in total.
     */
    public double kcal;
    /**
     * The price of the product.
     */
    public double price;
    /**
     * A description on the Item from storeProduct class.
     */
    public String subType;
    /**
     * Represents the units in stock.
     */
    public int UnitsInStock=0;

    /**
     * @return The units in stock of a given product.
     */
    public int getUnitsInStock() {
        return this.UnitsInStock;
    }

    /**
     * Set The amount of units in stock of a given product.
     */
    public void setUnitsInStock(int unitsInStock) {
        UnitsInStock = unitsInStock;
    }

    /**
     * @return The name of a given product.
     */
    public String getName() {
        return name;
    }

    /**
     * Default constructor for testing scenarios.
     */
    public storeProduct()
    {
        this.name ="Strawberry Protein Shake";
        kcal=250;
        subType="Delicious shake to meet you're nutritious needs";
        price=100;
        this.UnitsInStock=1;
    }

    /**
     *  Constructor made for updating an existing product in updateProducts.
     */
    public storeProduct(String name,double kcal,double price,String subType)
    {
        this.name=name;
        this.kcal=kcal;
        this.price=price;
        this.subType=subType;
        this.UnitsInStock=UnitsInStock;
    }

    /**
     *  Constructor made To retrieve the updated storeProducts in the database (located in store java file, line 65).
     */
    public storeProduct(String name,double kcal,double price,String subType,int uis)
    {
        this.name=name;
        this.kcal=kcal;
        this.price=price;
        this.subType=subType;
        UnitsInStock=uis;
    }

    /**
     *  Copy constructor for storeProduct class
     */
    public storeProduct(storeProduct toCopy)
    {
        this.name=toCopy.getName();
        this.kcal=toCopy.getKcal();
        this.subType=toCopy.getSubType();
        this.price=toCopy.getPrice();
        this.UnitsInStock=toCopy.getUnitsInStock();

    }


    /**
     * @return Get the total calories of each item.
     */
    public double getKcal() {
        return kcal;
    }

    /**
     * @return Get's the Price of each item.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return Get's the description of each item.
     */
    public String getSubType() {
        return subType;
    }

    /**
     * Set's the name of each product.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Set's the total calories of each item.
     */
    public void setKcal(double kcal) {
        this.kcal = kcal;
    }
    /**
     * Set's the the Price of each item.
     */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * Set's the description of each item.
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }



}
