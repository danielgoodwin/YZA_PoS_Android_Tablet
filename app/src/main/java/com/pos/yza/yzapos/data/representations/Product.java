package com.pos.yza.yzapos.data.representations;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Dlolpez on 26/12/17.
 */

public final class Product {

    public static final String LCL_TABLE_NAME = "product";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_MEASURE = "uom";
    public static final String COLUMN_CATEGORY = "category";

    // Create table SQL query
    public static final String LCL_CREATE_TABLE =
            "CREATE TABLE " + LCL_TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_PRICE + " NUMERIC(10,2), "
                    + COLUMN_MEASURE + " TEXT, "
                    + COLUMN_CATEGORY + " INTEGER "
                    + ")";


    int id;
    String name;
    Double unitPrice;
    String unitMeasure;
    ArrayList<ProductProperty> properties;
    ProductCategory category;

    public Product(){

        this.category = null;
        this.properties = new ArrayList<>();
    }


    public Product(int id,String name,  Double unitPrice, String unitMeasure, ProductCategory category,
                   ArrayList<ProductProperty> properties){
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitMeasure = unitMeasure;
        this.category = category;
        this.properties = properties;
    }

    public Product(String name, Double unitPrice, String unitMeasure, ProductCategory category,
                   ArrayList<ProductProperty> properties){
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitMeasure = unitMeasure;
        this.category = category;
        this.properties = properties;
    }

    public void setProperties(){}

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double price) {
        this.unitPrice = price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(String uom) {
        this.unitMeasure = uom;
    }

    public ArrayList<ProductProperty> getProperties() {
        return properties;
    }

    public int getProductPropertyId(int categoryPropertyId) {
        for (ProductProperty productProperty : properties) {
            Log.i("Product", "prod prop's cat prop id: " + productProperty.getCategoryPropertyId());
            if (productProperty.getCategoryPropertyId() == categoryPropertyId){
                return productProperty.getProductPropertyId();
            }
        }
        return -1;
    }

    public String getProductPropertyValue(int categoryPropertyId) {
        for (ProductProperty productProperty : properties) {
            if (productProperty.getCategoryPropertyId() == categoryPropertyId){
                return productProperty.getValue();
            }
        }
        return "";
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    /*
    public String getName() {
        String name = category.getName();
        for (ProductProperty property : properties){
            name += " " + property.getValue();
        }
        return name.trim();
    }
    */

    @Override
    public String toString(){
        String toReturn = getName() + "\n properties: ";

        for (ProductProperty property: properties) {
            toReturn += property;
        }

        return toReturn;
    }
}

