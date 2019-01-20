package com.pos.yza.yzapos.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.pos.yza.yzapos.data.representations.Payment;
import com.pos.yza.yzapos.data.representations.Product;
import com.pos.yza.yzapos.data.representations.ProductCategory;
import com.pos.yza.yzapos.data.representations.ProductProperty;
import com.pos.yza.yzapos.data.source.CategoriesDataSource;
import com.pos.yza.yzapos.data.source.ProductsDataSource;
import com.pos.yza.yzapos.data.source.data.LocalDatabaseHelper;

import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */

public class ProductsLocalDataSource implements ProductsDataSource {

    private static ProductsLocalDataSource INSTANCE;
    private static final String TAG = "LocalDataSource";

    private LocalDatabaseHelper mDB;

    public static ProductsLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductsLocalDataSource();
            INSTANCE.mDB = LocalDatabaseHelper.getInstance(context);
        }
        return INSTANCE;
    }

    private ProductsLocalDataSource() {}


    @Override
    public void getProducts(@NonNull LoadProductsCallback callback) {
        checkNotNull(callback);
        Log.i(TAG, "getProducts");

        List<Product> objlist = mDB.getAllProducts();
        callback.onProductsLoaded(objlist);

    }

    @Override
    public void getProduct(@NonNull String productId, @NonNull GetProductCallback callback) {
        checkNotNull(callback);
        Product obj = mDB.getProduct(Long.valueOf(productId));
        if (obj == null)
        {
            callback.onDataNotAvailable();
        }
        else {
            callback.onProductLoaded(obj);
        }
    }

    @Override
    public void getProductsByCategory(ProductCategory category, @NonNull LoadProductsCallback callback) {
        List<Product> objlist = mDB.getAllProductsForCategory(category);
        callback.onProductsLoaded(objlist);
    }

    @Override
    public void saveProduct(@NonNull Product product) {
        Log.i(TAG, "saveProduct");
        mDB.insertProduct(product);
    }

    @Override
    public void refreshProducts() {

    }

    @Override
    public void deleteAllProducts() {

    }

    @Override
    public void deleteProduct(@NonNull String productId) {
        Log.i(TAG, "deleteProduct");

        Product obj = mDB.getProductByName(productId);
        if (obj != null) {
            mDB.deleteProduct(obj);
        }
    }

    @Override
    public void editProduct(@NonNull String productId, @NonNull HashMap<String, String> edits,
                            @NonNull List<ProductProperty> propertyEdits) {

    }
}
