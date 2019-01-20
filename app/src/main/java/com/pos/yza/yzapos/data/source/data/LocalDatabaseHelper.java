package com.pos.yza.yzapos.data.source.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.util.Log;

import com.pos.yza.yzapos.data.representations.LineItem;
import com.pos.yza.yzapos.data.representations.Payment;
import com.pos.yza.yzapos.data.representations.ProductCategory;
import com.pos.yza.yzapos.data.representations.Product;
import com.pos.yza.yzapos.data.representations.Staff;
import com.pos.yza.yzapos.data.representations.Transaction;
import com.pos.yza.yzapos.data.source.ProductsDataSource;
import com.pos.yza.yzapos.util.Parsers;


/**
 *
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 4;
    private static final String TAG = "LocalDbHelper";

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

        // create tables
        db.execSQL(ProductCategory.LCL_CREATE_TABLE);
        db.execSQL(Product.LCL_CREATE_TABLE);
        db.execSQL(Staff.LCL_CREATE_TABLE);
        db.execSQL(Payment.LCL_CREATE_TABLE);
        db.execSQL(LineItem.LCL_CREATE_TABLE);
        db.execSQL(Transaction.LCL_CREATE_TABLE);

    }

    private void addLclStaff()
    {
        Staff obj = new Staff("1001", "First", "Last", "01234567",
                "xx@xx.com", "123 Any Street");
        insertStaff(obj);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ProductCategory.LCL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Product.LCL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Staff.LCL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Payment.LCL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LineItem.LCL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Transaction.LCL_TABLE_NAME);

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
                Product.COLUMN_NAME + " ASC";

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
        values.put(obj.COLUMN_PRICE, obj.getUnitPrice());
        values.put(obj.COLUMN_MEASURE, obj.getUnitPrice());
        values.put(obj.COLUMN_CATEGORY, obj.getCategory().getId());

        // updating row
        return db.update(Product.LCL_TABLE_NAME, values, Product.COLUMN_ID + " = ?",
                new String[]{String.valueOf(obj.getId())});
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Product.LCL_TABLE_NAME, Product.COLUMN_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        db.close();
    }


    //
    // Staff Handling

    public long insertStaff(Staff staff) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.

        values.put(Staff.COLUMN_FIRSTNAME, staff.getFirstName());
        values.put(Staff.COLUMN_LASTNAME, staff.getLastName());
        values.put(Staff.COLUMN_PHONENUMBER, staff.getPhoneNumber());
        values.put(Staff.COLUMN_EMAIL, staff.getEmail());
        values.put(Staff.COLUMN_HOMEADDRESS, staff.getHomeAddress());

        // insert row
        long id = db.insertWithOnConflict(Staff.LCL_TABLE_NAME, null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateStaff(staff);
        }

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Staff getStaff(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Staff.LCL_TABLE_NAME,
                new String[]{Staff.COLUMN_ID, Staff.COLUMN_EMPLOYEENO,Staff.COLUMN_FIRSTNAME,
                        Staff.COLUMN_LASTNAME,Staff.COLUMN_PHONENUMBER,
                        Staff.COLUMN_EMAIL, Staff.COLUMN_HOMEADDRESS},
                Staff.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare object
        Staff obj = new Staff(
                cursor.getInt(cursor.getColumnIndex(Staff.COLUMN_ID)),

                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_EMPLOYEENO)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_FIRSTNAME)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_LASTNAME)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_PHONENUMBER)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_HOMEADDRESS)));

        // close the db connection
        cursor.close();

        //
        // Get Properties


        return obj;
    }
    public Staff getStaffByEmployeeNo(String id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Staff.LCL_TABLE_NAME,
                new String[]{Staff.COLUMN_ID, Staff.COLUMN_EMPLOYEENO,Staff.COLUMN_FIRSTNAME,
                        Staff.COLUMN_LASTNAME,Staff.COLUMN_PHONENUMBER,
                        Staff.COLUMN_EMAIL, Staff.COLUMN_HOMEADDRESS},
                Staff.COLUMN_EMPLOYEENO + "=?",
                new String[]{id}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare object
        Staff obj = new Staff(
                cursor.getInt(cursor.getColumnIndex(Staff.COLUMN_ID)),

                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_EMPLOYEENO)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_FIRSTNAME)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_LASTNAME)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_PHONENUMBER)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(Staff.COLUMN_HOMEADDRESS)));

        // close the db connection
        cursor.close();

        //
        // Get Properties


        return obj;
    }

    public List<Staff> getAllStaff() {
        List<Staff> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Staff.LCL_TABLE_NAME + " ORDER BY " +
                Staff.COLUMN_EMPLOYEENO + " ASC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Staff obj = new Staff(
                        cursor.getInt(cursor.getColumnIndex(Staff.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Staff.COLUMN_EMPLOYEENO)),
                        cursor.getString(cursor.getColumnIndex(Staff.COLUMN_FIRSTNAME)),
                        cursor.getString(cursor.getColumnIndex(Staff.COLUMN_LASTNAME)),
                        cursor.getString(cursor.getColumnIndex(Staff.COLUMN_PHONENUMBER)),
                        cursor.getString(cursor.getColumnIndex(Staff.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(Staff.COLUMN_HOMEADDRESS)));

                list.add(obj);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return list;
    }


    public int getStaffCount() {
        String countQuery = "SELECT  * FROM " + Staff.LCL_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateStaff(Staff obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(obj.COLUMN_FIRSTNAME, obj.getFirstName());
        values.put(obj.COLUMN_LASTNAME, obj.getLastName());
        values.put(obj.COLUMN_PHONENUMBER, obj.getPhoneNumber());
        values.put(obj.COLUMN_EMAIL, obj.getEmail());
        values.put(obj.COLUMN_HOMEADDRESS, obj.getHomeAddress());

        // updating row
        return db.update(Staff.LCL_TABLE_NAME, values, obj.COLUMN_ID + " = ?",
                new String[]{String.valueOf(obj.getStaffId())});
    }

    public void deleteStaff(Staff staff) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Staff.LCL_TABLE_NAME, Staff.COLUMN_ID + " = ?",
                new String[]{String.valueOf(staff.getStaffId())});
        db.close();
    }


    //
    // Transaction Handling

    public long insertTransaction(Transaction obj) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.


        long id = -1;
        db.beginTransaction();
        try {

            values.clear();
            values.put(Transaction.COLUMN_CLIENTFIRSTNAME, obj.getClientFirstName());
            values.put(Transaction.COLUMN_CLIENTSURNAME, obj.getClientSurname());
            values.put(Transaction.COLUMN_TIMESTAMP, obj.getDateTime() + "");
            values.put(Transaction.COLUMN_BRANCHID, obj.getBranchId() + "");
            values.put(Transaction.COLUMN_STAFFID, obj.getStaffId());
            values.put(Transaction.COLUMN_STATUS, obj.getStatus().toString());
            values.put(Transaction.COLUMN_AMOUNT, obj.getAmount() + "");


            // insert row
            id = db.insert(Transaction.LCL_TABLE_NAME, null, values);

            if (id != -1 && obj.getLineItems() != null)
            {
                for (int idx =0; idx < obj.getLineItems().size(); idx++) {
                    values.clear();
                    LineItem item = obj.getLineItems().get(idx);
                    values.put(LineItem.COLUMN_TRANSACTIONID, id + "");
                    values.put(LineItem.COLUMN_PRODUCTID, item.getProductId() + "");
                    values.put(LineItem.COLUMN_QUANTITY, item.getQuantity() + "");
                    values.put(LineItem.COLUMN_AMOUNT, item.getAmount() + "");

                    db.insert(LineItem.LCL_TABLE_NAME, null, values);
                }

            }

            if (id != -1 && obj.getPayments() != null)
            {
                for (int idx =0; idx < obj.getPayments().size(); idx++) {
                    values.clear();
                    Payment item = obj.getPayments().get(idx);
                    values.put(Payment.COLUMN_TRANSACTIONID, id + "");
                    values.put(Payment.COLUMN_STATUS, item.getState().toString() + "");
                    values.put(Payment.COLUMN_TIMESTAMP, "" + "");
                    values.put(Payment.COLUMN_AMOUNT, item.getAmount() + "");

                    db.insert(Payment.LCL_TABLE_NAME, null, values);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to save transaction " + e.getMessage());
            id = -1;
        } finally {
            db.endTransaction();
            db.close();
        }

        // return newly inserted row id
        return id;
    }

    public Transaction getTransaction(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Transaction.LCL_TABLE_NAME,
                new String[]{Transaction.COLUMN_ID, Transaction.COLUMN_AMOUNT,Transaction.COLUMN_BRANCHID,
                        Transaction.COLUMN_CLIENTFIRSTNAME,Transaction.COLUMN_CLIENTSURNAME,
                        Transaction.COLUMN_STAFFID, Transaction.COLUMN_STATUS, Transaction.COLUMN_TIMESTAMP},
                Transaction.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Transaction obj =  null;

        if (cursor != null) {
            cursor.moveToFirst();

            Date timestamp = Parsers.parseDjangoDateTime(cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_TIMESTAMP)));
            Transaction.Status status = Transaction.getStatus(cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_STATUS)));

             obj = new Transaction(
                    cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_CLIENTFIRSTNAME)),
                    cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_CLIENTSURNAME)),
                    timestamp,
                    cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_BRANCHID)),
                    status,
                    cursor.getDouble(cursor.getColumnIndex(Transaction.COLUMN_AMOUNT)),
                    cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_STAFFID)));

            // close the db connection
            cursor.close();

            //
            // Get Line Items
            Cursor linecursor = db.query(LineItem.LCL_TABLE_NAME,
                    new String[]{LineItem.COLUMN_ID, LineItem.COLUMN_AMOUNT,LineItem.COLUMN_PRODUCTID,
                           LineItem.COLUMN_QUANTITY,LineItem.COLUMN_TRANSACTIONID,},
                    LineItem.COLUMN_TRANSACTIONID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);


            if (linecursor.moveToFirst()) {
                do {

                    LineItem lineItem = new LineItem(
                            linecursor.getInt(linecursor.getColumnIndex(LineItem.COLUMN_ID)),
                            linecursor.getInt(linecursor.getColumnIndex(LineItem.COLUMN_QUANTITY)),
                            linecursor.getDouble(linecursor.getColumnIndex(LineItem.COLUMN_AMOUNT)),
                            obj, linecursor.getInt(linecursor.getColumnIndex(LineItem.COLUMN_PRODUCTID))
                    );

                    obj.getLineItems().add(lineItem);

                } while (linecursor.moveToNext());
            }
            linecursor.close();

            //
            // Get Payments
            Cursor paymentcursor = db.query(Payment.LCL_TABLE_NAME,
                    new String[]{Payment.COLUMN_ID, Payment.COLUMN_AMOUNT,Payment.COLUMN_STATUS, Payment.COLUMN_TIMESTAMP,
                            Payment.COLUMN_TRANSACTIONID,},
                    Payment.COLUMN_TRANSACTIONID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (paymentcursor.moveToFirst()) {
                do {

                    Payment.State paystatus = Payment.getState(paymentcursor.getString(
                            paymentcursor.getColumnIndex(Payment.COLUMN_STATUS)));
                    Date paytimestamp = Parsers.parseDjangoDateTime(paymentcursor.getString(
                            paymentcursor.getColumnIndex(Payment.COLUMN_TIMESTAMP)));

                    Payment payItem = new Payment(
                            paymentcursor.getInt(paymentcursor.getColumnIndex(LineItem.COLUMN_ID)),
                            paytimestamp,
                            paymentcursor.getDouble(paymentcursor.getColumnIndex(LineItem.COLUMN_AMOUNT)),
                            -1,
                            obj, paystatus
                    );

                    obj.getPayments().add(payItem);

                } while (paymentcursor.moveToNext());
            }
            paymentcursor.close();


        }
        return obj;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Transaction.LCL_TABLE_NAME + " ORDER BY " +
                Transaction.COLUMN_ID + " DESC LIMIT 50 ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Transaction obj =  null;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (cursor != null) {

                    Date timestamp = Parsers.parseDjangoDateTime(cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_TIMESTAMP)));
                    Transaction.Status status = Transaction.getStatus(cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_STATUS)));

                    obj = new Transaction(
                            cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_CLIENTFIRSTNAME)),
                            cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_CLIENTSURNAME)),
                            timestamp,
                            cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_BRANCHID)),
                            status,
                            cursor.getDouble(cursor.getColumnIndex(Transaction.COLUMN_AMOUNT)),
                            cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_STAFFID)));



                    //
                    // Get Line Items
                    Cursor linecursor = db.query(LineItem.LCL_TABLE_NAME,
                            new String[]{LineItem.COLUMN_ID, LineItem.COLUMN_AMOUNT,LineItem.COLUMN_PRODUCTID,
                                    LineItem.COLUMN_QUANTITY,LineItem.COLUMN_TRANSACTIONID,},
                            LineItem.COLUMN_TRANSACTIONID + "=?",
                            new String[]{String.valueOf(obj.getTransactionId())}, null, null, null, null);


                    if (linecursor.moveToFirst()) {
                        do {

                            LineItem lineItem = new LineItem(
                                    linecursor.getInt(linecursor.getColumnIndex(LineItem.COLUMN_ID)),
                                    linecursor.getInt(linecursor.getColumnIndex(LineItem.COLUMN_QUANTITY)),
                                    linecursor.getDouble(linecursor.getColumnIndex(LineItem.COLUMN_AMOUNT)),
                                    obj, linecursor.getInt(linecursor.getColumnIndex(LineItem.COLUMN_PRODUCTID))
                            );

                            obj.getLineItems().add(lineItem);

                        } while (linecursor.moveToNext());
                    }
                    linecursor.close();

                    //
                    // Get Payments
                    Cursor paymentcursor = db.query(Payment.LCL_TABLE_NAME,
                            new String[]{Payment.COLUMN_ID, Payment.COLUMN_AMOUNT,Payment.COLUMN_STATUS, Payment.COLUMN_TIMESTAMP,
                                    Payment.COLUMN_TRANSACTIONID,},
                            Payment.COLUMN_TRANSACTIONID + "=?",
                            new String[]{String.valueOf(obj.getTransactionId())}, null, null, null, null);


                    //public Payment(int paymentId, Date dateTime, double amount, int branchId, Transaction transaction, State state) {

                    if (paymentcursor.moveToFirst()) {
                        do {

                            Payment.State paystatus = Payment.getState(paymentcursor.getString(
                                    paymentcursor.getColumnIndex(Payment.COLUMN_STATUS)));
                            Date paytimestamp = Parsers.parseDjangoDateTime(paymentcursor.getString(
                                    paymentcursor.getColumnIndex(Payment.COLUMN_TIMESTAMP)));

                            Payment payItem = new Payment(
                                    paymentcursor.getInt(paymentcursor.getColumnIndex(LineItem.COLUMN_ID)),
                                    paytimestamp,
                                    paymentcursor.getDouble(paymentcursor.getColumnIndex(LineItem.COLUMN_AMOUNT)),
                                    -1,
                                    obj, paystatus
                            );

                            obj.getPayments().add(payItem);

                        } while (paymentcursor.moveToNext());
                    }
                    paymentcursor.close();


                }
                list.add(obj);

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return list;
    }


    public int getTransactionCount() {
        String countQuery = "SELECT  * FROM " + Transaction.LCL_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int cancelTransaction(Transaction obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        int ret = -1;
        db.beginTransaction();
        try {

            values.put(obj.COLUMN_STATUS, Transaction.Status.CANCEL.toString());

            // updating row
            ret = db.update(Transaction.LCL_TABLE_NAME, values, obj.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(obj.getTransactionId())});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to cancel transaction " + e.getMessage());
            ret = -1;
        } finally {
            db.endTransaction();
            db.close();
        }

        return ret;
    }

    public int refundTransaction(Transaction obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        int ret = -1;
        db.beginTransaction();
        try {

            values.put(obj.COLUMN_STATUS, Transaction.Status.REFUND.toString());

            // updating row
            ret = db.update(Transaction.LCL_TABLE_NAME, values, obj.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(obj.getTransactionId())});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to refund transaction " + e.getMessage());
            ret = -1;
        } finally {
            db.endTransaction();
            db.close();
        }

        return ret;
    }

    public void deleteTransaction(Transaction obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(Payment.LCL_TABLE_NAME, Payment.COLUMN_TRANSACTIONID + " = ?",
                    new String[]{String.valueOf(obj.getTransactionId())});
            db.delete(LineItem.LCL_TABLE_NAME, LineItem.COLUMN_TRANSACTIONID + " = ?",
                    new String[]{String.valueOf(obj.getTransactionId())});
            db.delete(Transaction.LCL_TABLE_NAME, Transaction.COLUMN_ID + " = ?",
                new String[]{String.valueOf(obj.getTransactionId())});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete transaction");
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    public void deleteOldTransactions() {
        //

    }

    //
    // Payment Handling

    public long insertPayment(Payment payment) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.

        values.put(Payment.COLUMN_STATUS, payment.getState().toString());
        values.put(Payment.COLUMN_TRANSACTIONID, payment.getTransaction().getTransactionId() + "");
        values.put(Payment.COLUMN_TIMESTAMP, payment.getDateTime() + "");
        values.put(Payment.COLUMN_AMOUNT, payment.getAmount() + "");

        // insert row
        long id = db.insert(Payment.LCL_TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Payment getPayment(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Payment.LCL_TABLE_NAME,
                new String[]{Payment.COLUMN_ID, Payment.COLUMN_STATUS,Payment.COLUMN_AMOUNT,Payment.COLUMN_TRANSACTIONID,
                        Payment.COLUMN_TIMESTAMP},
                Payment.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Payment obj = null;

        if (cursor != null) {
            cursor.moveToFirst();

            Payment.State paystatus = Payment.getState(cursor.getString(
                    cursor.getColumnIndex(Payment.COLUMN_STATUS)));
            Date paytimestamp = Parsers.parseDjangoDateTime(cursor.getString(
                    cursor.getColumnIndex(Payment.COLUMN_TIMESTAMP)));

            Transaction trans = getTransaction(cursor.getInt(cursor.getColumnIndex(
                    LineItem.COLUMN_TRANSACTIONID)));

            obj = new Payment(
                    cursor.getInt(cursor.getColumnIndex(LineItem.COLUMN_ID)),
                    paytimestamp,
                    cursor.getDouble(cursor.getColumnIndex(LineItem.COLUMN_AMOUNT)),
                    -1, trans, paystatus
            );

            // close the db connection
            cursor.close();
        }


        return obj;
    }

    public int refundPayment(Payment obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        int ret = -1;
        db.beginTransaction();
        try {

            values.put(obj.COLUMN_STATUS, Payment.State.REFUND.toString());

            // updating row
            ret = db.update(Transaction.LCL_TABLE_NAME, values, obj.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(obj.getPaymentId())});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to refund payment " + e.getMessage());
            ret = -1;
        } finally {
            db.endTransaction();
            db.close();
        }

        return ret;
    }

}
