package com.pos.yza.yzapos.data.representations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Lopez on 27/12/17.
 */

public class ProductCategory {

    public static final String LCL_TABLE_NAME = "product_category";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    // Create table SQL query
    public static final String LCL_CREATE_TABLE =
            "CREATE TABLE " + LCL_TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT "
                    + ")";

    private int id;
    private String name;
    private List<CategoryProperty> propertyList;
    private List<Product> products;

    public ProductCategory()
    {
        this.propertyList = null;
        this.products = new ArrayList<>();
    }

    public ProductCategory(int id, String name) {
        this.id = id;
        this.name = name;
        this.propertyList = null;
        this.products = new ArrayList<>();
    }


    public ProductCategory(int id, String name, List<CategoryProperty> propertyList) {
        this.id = id;
        this.name = name;
        this.propertyList = propertyList;
        this.products = new ArrayList<>();
    }

    public ProductCategory(String name, List<CategoryProperty> propertyList) {
        this.id = -1;
        this.name = name;
        this.propertyList = propertyList;
        this.products = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryProperty> getPropertyList() {
        return propertyList;
    }

    public List<Product> getProducts() { return products; }

    public void addProduct(Product product) {
        if (product != null) {
            products.add(product);
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    public String detailString(){
        String properties = "";
        for (CategoryProperty p : propertyList){
            properties = properties.toString() + p.getName() + " ";
        }
        return properties;
    }
}

