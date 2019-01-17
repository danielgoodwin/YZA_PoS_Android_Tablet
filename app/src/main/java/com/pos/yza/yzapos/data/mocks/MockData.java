package com.pos.yza.yzapos.data.mocks;

import com.pos.yza.yzapos.data.representations.CategoryProperty;
import com.pos.yza.yzapos.data.representations.Product;
import com.pos.yza.yzapos.data.representations.ProductCategory;
import com.pos.yza.yzapos.data.representations.ProductProperty;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dlolpez on 31/12/17.
 */

public class MockData {

    static ArrayList<CategoryProperty> catprops1 = new ArrayList<CategoryProperty>();

    public static ArrayList<Product> getMockProducts() {
        ArrayList<Product> products = new ArrayList<Product>();

        ArrayList<ProductProperty> productProp = new ArrayList<ProductProperty>();
        productProp.add(new ProductProperty(1,1, "Color"));
        productProp.add(new ProductProperty(1, 1,"Type"));

        ProductCategory cat = new ProductCategory(1,"Cat1", catprops1);

        //products.add(new Product(3.5, "kg", cat, productProp));
        //products.add(new Product(18.0, "kg", cat,productProp));
        //products.add(new Product(25.0, "cm", cat,productProp));
        //products.add(new Product(24.50, "lt", cat,productProp));

        return products;
    }

    public static ArrayList<ProductCategory> getMockCategories() {
        ArrayList<ProductCategory> categories = new ArrayList<ProductCategory>();

        ArrayList<String> properties_1 =
                new ArrayList<String>( Arrays.asList("Color", "Type", "Size", "Thickness"));
        ArrayList<String> properties_2 =
                new ArrayList<String>( Arrays.asList("Color", "Type", "Size"));
        ArrayList<String> properties_3 =
                new ArrayList<String>();

        categories.add(new ProductCategory(1,"Cat1", catprops1));
        categories.add(new ProductCategory(2, "Cat2", catprops1));
        categories.add(new ProductCategory(3, "Cat3", null));

        return categories;
    }

    static {

        catprops1.add(new CategoryProperty(1, "Color"));
        catprops1.add(new CategoryProperty(1, "Type"));

    }
}
