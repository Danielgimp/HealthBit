package com.ariel.healthbit;

/**
 * This class Contains information on a single item inside an Order of a costumer, it contains the Item name, the amount of units purchased of this product, and price per unit.
 * The class members are: Item as item name, amount - amount of purchased units for this item, and the price for one unit.
 * This class can: Create a product, get a product price, set a product price, set a product amount, get an item name, and get the product amount.
 */
public class productOrder {
    /**
     * Represents the name of the item
     */
    String Item;
    /**
     * Describes how many units are ordered within the same item.
     */
    int amount;
    /**
     * Describes the price for each unit.
     */
    double price=0;

    /**
     * Copy constructor for a given Order
     * @params prd - an existing productOrder node
     */
    public productOrder(productOrder prd) {
        this.Item = prd.getItem();
        this.amount = prd.getAmount();
        this.price=prd.getPrice();
    }
    /**
     * Built for Firebase imports (required) , does nothing.
     */
    public productOrder(){};

    /**
     * Returns the price of a given product
     * @return the price of a given product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the amount of a given product.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Constructor specifically for Order class, this constructor creates a new productOrder in case there is no such productOrder in the Order Arraylist Member of Order.
     */
    public productOrder(String item, int amount, double price) {
        this.Item = item;
        this.amount = amount;
        this.price=price;
    }

    /**
     * Sets the Item name.
     */
    public void setItem(String item) {
        Item = item;
    }
    /**
     * Sets the amount of a given product.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    /**
     * Returns the name of a given product.
     * @return Name of a product in String data type.
     */
    public String getItem() {
        return Item;
    }
    /**
     * Returns the amount of a given product.
     * @return amount of a product in Integer data type.
     */
    public int getAmount() {
        return amount;
    }
}
