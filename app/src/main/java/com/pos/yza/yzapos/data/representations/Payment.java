package com.pos.yza.yzapos.data.representations;

import com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Dalzy Mendoza on 16/1/18.
 */

public class Payment {
    public enum State {OK, REFUND};

    private int paymentId;
    private Date dateTime;
    private double amount;
//  private Branch branch;
    private int branchId;
    private Transaction transaction;
    private State state;

    public static final String LCL_TABLE_NAME = "transactionpayment";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "paymenttimestamp";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TRANSACTIONID = "transactionid";
    public static final String COLUMN_AMOUNT = "amount";

    // Create table SQL query
    public static final String LCL_CREATE_TABLE =
            "CREATE TABLE " + LCL_TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TIMESTAMP + " TIMESTAMP, "
                    + COLUMN_STATUS + " TEXT, "
                    + COLUMN_TRANSACTIONID + " INTEGER, "
                    + COLUMN_AMOUNT + " NUMERIC(10,2) "
                    + ")";

    public Payment()
    {

    }

    public Payment(double amount, int branchId, Transaction transaction) {
        this.setPaymentId(-1);
        this.setDateTime(new Date(0,0,0));
        this.setAmount(amount);
        this.setBranchId(branchId);
        this.setTransaction(transaction);
        this.setState(State.OK);
    }


    public Payment(Date dateTime, double amount, int branchId, Transaction transaction) {
        this.setPaymentId(-1);
        this.setDateTime(dateTime);
        this.setAmount(amount);
        this.setBranchId(branchId);
        this.setTransaction(transaction);
        this.setState(State.OK);
    }

    public Payment(int paymentId, Date dateTime, double amount, int branchId, Transaction transaction, State state) {
        this.setPaymentId(paymentId);
        this.setDateTime(dateTime);
        this.setAmount(amount);
        this.setBranchId(branchId);
        this.setTransaction(transaction);
        this.setState(state);
    }

    public static State getState(String state){
        if(state.equals("OK")){
            return State.OK;
        }
        if(state.equals("REFUND")){
            return State.REFUND;
        }
        return null;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public int getBranchId() {
        return branchId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public State getState() {
        return state;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String toString(){
        return "Id: " + getPaymentId() + " Amount: " + getAmount() + " Branch: " + getBranchId();
    }

    public HashMap<String,String> toHashMap(){
        HashMap<String, String> toReturn = new HashMap<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        toReturn.put(TransactionsRemoteDataSource.PAYMENT_DATE_TIME, df.format(getDateTime()));
        toReturn.put(TransactionsRemoteDataSource.PAYMENT_AMOUNT, getAmount() + "");
        toReturn.put(TransactionsRemoteDataSource.PAYMENT_BRANCH_ID, getBranchId() + "");

        // default doesnt include state; assumes state is OK for this function

        return toReturn;
    }
}
