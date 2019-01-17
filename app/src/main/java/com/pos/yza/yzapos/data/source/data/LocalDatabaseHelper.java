package com.pos.yza.yzapos.data.source.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.pos.yza.yzapos.data.representations.ProductCategory;
import com.pos.yza.yzapos.data.representations.Product;


/**
 *
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "yzapos_db";

    private static LocalDatabaseHelper sInstance;

    public static synchronized LocalDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new LocalDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(ProductCategory.LCL_CREATE_TABLE);
        db.execSQL(Product.LCL_CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ProductCategory.LCL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Product.LCL_TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    //
    // Product Category Handling

    public long insertProductCategory(ProductCategory category) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.

        values.put(ProductCategory.COLUMN_NAME, category.getName());

        // insert row
        //long id = db.insert(ProductCategory.LCL_TABLE_NAME, null, values);
        long id = db.insertWithOnConflict(ProductCategory.LCL_TABLE_NAME, null,
                    values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateProductCategory(category);
        }

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public ProductCategory getProductCategory(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ProductCategory.LCL_TABLE_NAME,
                new String[]{ProductCategory.COLUMN_ID, ProductCategory.COLUMN_NAME},
                ProductCategory.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare object
        ProductCategory obj = new ProductCategory(
                cursor.getInt(cursor.getColumnIndex(ProductCategory.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ProductCategory.COLUMN_NAME)));

        // close the db connection
        cursor.close();

        //
        // Get Properties


        //
        // Get Products

        return obj;
    }

    public ProductCategory getProductCategoryByName(String name) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ProductCategory.LCL_TABLE_NAME,
                new String[]{ProductCategory.COLUMN_ID, ProductCategory.COLUMN_NAME},
                ProductCategory.COLUMN_NAME + "=?",
                new String[]{name}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare object
        ProductCategory obj = new ProductCategory(
                cursor.getInt(cursor.getColumnIndex(ProductCategory.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ProductCategory.COLUMN_NAME)));

        // close the db connection
        cursor.close();

        //
        // Get Properties


        //
        // Get Products

        return obj;
    }


    public List<ProductCategory> getAllProductCategory() {
        List<ProductCategory> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ProductCategory.LCL_TABLE_NAME + " ORDER BY " +
                ProductCategory.COLUMN_NAME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProductCategory obj = new ProductCategory();
                obj.setId(cursor.getInt(cursor.getColumnIndex(ProductCategory.COLUMN_ID)));
                obj.setName(cursor.getString(cursor.getColumnIndex(ProductCategory.COLUMN_NAME)));

                list.add(obj);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return list;
    }

    public int getProductCategoryCount() {
        String countQuery = "SELECT  * FROM " + ProductCategory.LCL_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateProductCategory(ProductCategory obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(obj.COLUMN_NAME, obj.getName());

        // updating row
        return db.update(obj.LCL_TABLE_NAME, values, obj.COLUMN_ID + " = ?",
                new String[]{String.valueOf(obj.getId())});
    }

    public void deleteProductCategory(ProductCategory category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductCategory.LCL_TABLE_NAME, ProductCategory.COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }


    //
    // Product Handling

    public long insertProduct(Product product) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.

        values.put(Product.COLUMN_NAME, product.getName());
        values.put(Product.COLUMN_PRICE, product.getUnitPrice());
        values.put(Product.COLUMN_MEASURE, product.getUnitMeasure());
        values.put(Product.COLUMN_CATEGORY, product.getCategory().getId());

        // insert row
        long id = db.insertWithOnConflict(Product.LCL_TABLE_NAME, null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateProduct(product);
        }

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Product getProduct(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Product.LCL_TABLE_NAME,
                new String[]{Product.COLUMN_ID, Product.COLUMN_NAME,Product.COLUMN_PRICE,Product.COLUMN_MEASURE,
                        Product.COLUMN_CATEGORY},
                Product.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare object
        Product obj = new Product(
                cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)),
                cursor.getDouble(cursor.getColumnIndex(Product.COLUMN_PRICE)),
                cursor.getString(cursor.getColumnIndex(Product.COLUMN_MEASURE)),
                null, null);

        ProductCategory category = getProductCategory(cursor.getInt(cursor.getColumnIndex
                                (Product.COLUMN_CATEGORY)));
        obj.setCategory(category);

        // close the db connection
        cursor.close();

        //
        // Get Properties


        return obj;
    }

    public Product getProductByName(String name) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Product.LCL_TABLE_NAME,
                new String[]{Product.COLUMN_ID, Product.COLUMN_NAME,Product.COLUMN_PRICE,Product.COLUMN_MEASURE,
                        Product.COLUMN_CATEGORY},
                Product.COLUMN_NAME + "=?",
                new String[]{name}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare object
        Product obj = new Product(
                cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)),
                cursor.getDouble(cursor.getColumnIndex(Product.COLUMN_PRICE)),
                cursor.getString(cursor.getColumnIndex(Product.COLUMN_MEASURE)),
                null, null);

        ProductCategory category = getProductCategory(cursor.getInt(cursor.getColumnIndex
                (Product.COLUMN_CATEGORY)));

        obj.setCategory(category);

        // close the db connection
        cursor.close();

        //
        // Get Properties


        return obj;
    }


    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Product.LCL_TABLE_NAME + " ORDER BY " +
                ProductCategory.COLUMN_NAME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product obj = new Product();
                obj.setId(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)));
                obj.setName(cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)));
                obj.setUnitMeasure(cursor.getString(cursor.getColumnIndex(Product.COLUMN_MEASURE)));
                obj.setUnitPrice(cursor.getDouble(cursor.getColumnIndex(Product.COLUMN_PRICE)));

                ProductCategory category = getProductCategory(cursor.getInt(cursor.getColumnIndex
                        (Product.COLUMN_CATEGORY)));

                obj.setCategory(category);


                list.add(obj);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return list;
    }

    public List<Product> getAllProductsForCategory(ProductCategory category) {
        List<Product> list = new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Product.LCL_TABLE_NAME,
                new String[]{Product.COLUMN_ID, Product.COLUMN_NAME,Product.COLUMN_PRICE,Product.COLUMN_MEASURE,
                        Product.COLUMN_CATEGORY},
                Product.COLUMN_CATEGORY + "=?",
                new String[]{String.valueOf(category.getId())}, null, null, null, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product obj = new Product();
                obj.setId(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)));
                obj.setName(cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)));

                obj.setUnitMeasure(cursor.getString(cursor.getColumnIndex(Product.COLUMN_MEASURE)));
                obj.setUnitPrice(cursor.getDouble(cursor.getColumnIndex(Product.COLUMN_PRICE)));

                ProductCategory objCat = getProductCategory(cursor.getInt(cursor.getColumnIndex
                        (Product.COLUMN_CATEGORY)));

                obj.setCategory(objCat);

                list.add(obj);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return list;
    }

    public int getProductCount() {
        String countQuery = "SELECT  * FROM " + Product.LCL_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateProduct(Product obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(obj.COLUMN_NAME, obj.getName());

        // updating row
        return db.update(obj.LCL_TABLE_NAME, values, obj.COLUMN_ID + " = ?",
                new String[]{String.valueOf(obj.getId())});
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Product.LCL_TABLE_NAME, Product.COLUMN_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        db.close();
    }


}
