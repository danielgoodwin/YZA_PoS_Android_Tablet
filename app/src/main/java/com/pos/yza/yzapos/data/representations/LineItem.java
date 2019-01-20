package com.pos.yza.yzapos.data.representations;

import com.pos.yza.yzapos.SessionStorage;
import com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource;

import java.util.HashMap;

/**
 * Created by Dalzy Mendoza on 16/1/18.
 */

public class LineItem {
    private int lineItemId;
    private int quantity;
    private double amount;
    private Transaction transaction;
    //    Product product;
    private int productId;

    public static final String LCL_TABLE_NAME = "transactionline";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCTID = "productid";
    public static final String COLUMN_TRANSACTIONID = "transactionid";
    public static final String COLUMN_AMOUNT = "amount";

    // Create table SQL query
    public static final String LCL_CREATE_TABLE =
            "CREATE TABLE " + LCL_TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_QUANTITY + " INTEGER, "
                    + COLUMN_PRODUCTID + " INTEGER, "
                    + COLUMN_TRANSACTIONID + " INTEGER, "
                    + COLUMN_AMOUNT + " NUMERIC(10,2) "
                    + ")";

    public LineItem()
    {
        transaction = null;
    }

    public LineItem(int quantity, double amount,
                    Transaction transaction, int productId){
        this.setLineItemId(-1);
        this.setQuantity(quantity);
        this.setAmount(amount);
        this.setTransaction(transaction);
        this.setProductId(productId);
    }

    public LineItem(int quantity, double amount, int productId){
        this.setLineItemId(-1);
        this.setQuantity(quantity);
        this.setAmount(amount);
        this.setProductId(productId);
    }


    public LineItem(int lineItemId, int quantity, double amount,
                    Transaction transaction, int productId){
        this.setLineItemId(lineItemId);
        this.setQuantity(quantity);
        this.setAmount(amount);
        this.setTransaction(transaction);
        this.setProductId(productId);
    }

    public int getLineItemId() {
        return lineItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAmount() {
        return amount;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public int getProductId() {
        return productId;
    }

    public String toString(){
        return SessionStorage.getProduct(getProductId()).getName() + " * " + getQuantity();
    }

    public HashMap<String, String> toHashMap(){
        HashMap<String, String> toReturn = new HashMap<>();
        toReturn.put(TransactionsRemoteDataSource.LINE_ITEM_AMOUNT, this.getAmount() + "");
        toReturn.put(TransactionsRemoteDataSource.LINE_ITEM_PRODUCT_ID, this.getProductId() + "");
        toReturn.put(TransactionsRemoteDataSource.LINE_ITEM_QUANTITY, this.getQuantity() + "");

        return toReturn;

    }

    public void setLineItemId(int lineItemId) {
        this.lineItemId = lineItemId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
