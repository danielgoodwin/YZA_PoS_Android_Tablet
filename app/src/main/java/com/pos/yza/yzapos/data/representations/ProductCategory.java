package com.pos.yza.yzapos.data.representations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Lopez on 27/12/17.
 */

public class ProductCategory {

    private int id;
    private String name;
    private List<CategoryProperty> propertyList;

    public ProductCategory(int id, String name, List<CategoryProperty> propertyList) {
        this.id = id;
        this.name = name;
        this.propertyList = propertyList;
    }

    public ProductCategory(String name, List<CategoryProperty> propertyList) {
        this.id = -1;
        this.name = name;
        this.propertyList = propertyList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CategoryProperty> getPropertyList() {
        return propertyList;
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

