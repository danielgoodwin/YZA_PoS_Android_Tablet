package com.pos.yza.yzapos.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.RequestQueue;

import com.pos.yza.yzapos.data.representations.CategoryProperty;
import com.pos.yza.yzapos.data.representations.ProductCategory;
import com.pos.yza.yzapos.data.source.CategoriesDataSource;
import com.pos.yza.yzapos.data.source.data.LocalDatabaseHelper;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */

public class CategoriesLocalDataSource implements CategoriesDataSource {

    private static CategoriesLocalDataSource INSTANCE;
    private static final String TAG = "LocalDataSource";

    public final static String CATEGORY_NAME = "name",
                               CATEGORY_PROPERTIES = "properties",
                               CATEGORY_PROPERTY_NAME = "name";

    private RequestQueue mRequestQueue;
    private LocalDatabaseHelper mDB;

    public static CategoriesLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CategoriesLocalDataSource();
            INSTANCE.mDB = LocalDatabaseHelper.getInstance(context);
        }
        return INSTANCE;
    }

    private CategoriesLocalDataSource() {}

    @Override
    public void getCategories(@NonNull LoadCategoriesCallback callback) {
        checkNotNull(callback);
        Log.i(TAG, "getCategories");

        List<ProductCategory> categories = mDB.getAllProductCategory();
        callback.onCategoriesLoaded(categories);

    }

    @Override
    public void saveCategory(@NonNull ProductCategory category) {
        Log.i(TAG, "saveCategory");
        mDB.insertProductCategory(category);
    }

    @Override
    public void refreshCategory() {

    }

    @Override
    public void deleteAllCategories() {

    }

    @Override
    public void deleteCategory(@NonNull String productId) {
        Log.i(TAG, "deleteCategory");

        ProductCategory category = mDB.getProductCategoryByName(productId);
        if (category != null) {
            mDB.deleteProductCategory(category);
        }
    }

}
