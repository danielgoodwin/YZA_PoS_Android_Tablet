package com.pos.yza.yzapos.data.representations;

import com.pos.yza.yzapos.SessionStorage;
import com.pos.yza.yzapos.util.Formatters;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dalzy Mendoza on 12/1/18.
 */

public final class Transaction {

    public static final String LCL_TABLE_NAME = "trans";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CLIENTFIRSTNAME = "clientfirstname";
    public static final String COLUMN_CLIENTSURNAME = "clientsurname";
    public static final String COLUMN_TIMESTAMP = "trans_timestamp";
    public static final String COLUMN_BRANCHID = "branchid";
    public static final String COLUMN_STAFFID = "staffid";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_AMOUNT = "amount";

    // Create table SQL query
    public static final String LCL_CREATE_TABLE =
            "CREATE TABLE " + LCL_TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CLIENTFIRSTNAME + " TEXT, "
                    + COLUMN_CLIENTSURNAME + " TEXT, "
                    + COLUMN_TIMESTAMP + " TIMESTAMP, "
                    + COLUMN_BRANCHID + " INTEGER, "
                    + COLUMN_STAFFID + " INTEGER, "
                    + COLUMN_STATUS + " TEXT, "
                    + COLUMN_AMOUNT + " NUMERIC(10,2) "
                    + ")";



    private int transactionId;
    private String clientFirstName;
    private String clientSurname;
    private Date dateTime;
//    private Branch branch;
    private int branchId;
    private ArrayList<LineItem> lineItems;
    private ArrayList<Payment> payments;
    private int staffId;
    private Status status;
    private double amount;

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public enum Status {OK, CANCEL, REFUND};

    public Transaction()
    {
        lineItems = new ArrayList<>();
        payments = new ArrayList<>();
    }

    public Transaction(int transactionId){
        this.setTransactionId(transactionId);
        this.setClientFirstName("");
        this.setClientSurname("");
        this.setDateTime(new Date(0,0,0));
        this.setBranchId(-1);
        this.lineItems = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.setAmount(0);
        this.setStaffId(-1);
        this.setStatus(Status.OK);
    }

    public Transaction(String state){
        this.setTransactionId(-1);
        this.setClientFirstName("");
        this.setClientSurname("");
        this.setDateTime(new Date(0,0,0));
        this.setBranchId(-1);
        this.lineItems = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.setAmount(0);
        this.setStaffId(-1);
        this.setStatus(Transaction.getStatus(state));
    }

    public Transaction(String clientFirstName, String clientSurname, int branchId){
        this.setTransactionId(-1);
        this.setClientFirstName(clientFirstName);
        this.setClientSurname(clientSurname);
        this.setDateTime(new Date(0,0,0));
        this.setBranchId(branchId);
        this.lineItems = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.setAmount(0);
        this.setStaffId(-1);
        this.setStatus(Status.OK);
    }

    public Transaction(String clientFirstName, String clientSurname, int branchId, int staffId){
        this.setTransactionId(-1);
        this.setClientFirstName(clientFirstName);
        this.setClientSurname(clientSurname);
        this.setDateTime(new Date(0,0,0));
        this.setBranchId(branchId);
        this.lineItems = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.setAmount(0);
        this.setStaffId(staffId);
        this.setStatus(Status.OK);
    }

    public Transaction(int transactionId, String clientFirstName,
                       String clientSurname, int branchId){
        this.setTransactionId(transactionId);
        this.setClientFirstName(clientFirstName);
        this.setClientSurname(clientSurname);
        this.setDateTime(new Date(0,0,0));
        this.setBranchId(branchId);
        this.lineItems = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.setAmount(0);
        this.setStaffId(-1);
        this.setStatus(Status.OK);
    }

    public Transaction(int transactionId, String clientFirstName,
                       String clientSurname, int branchId, double amount){
        this.setTransactionId(transactionId);
        this.setClientFirstName(clientFirstName);
        this.setClientSurname(clientSurname);
        this.setDateTime(new Date(0,0,0));
        this.setBranchId(branchId);
        this.lineItems = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.setAmount(amount);
        this.setStaffId(-1);
        this.setStatus(Status.OK);
    }

    public Transaction(int transactionId, String clientFirstName,
                       String clientSurname, int branchId,
                       Date dateTime, double amount){
        this.setTransactionId(transactionId);
        this.setClientFirstName(clientFirstName);
        this.setClientSurname(clientSurname);
        this.setDateTime(dateTime);
        this.setBranchId(branchId);
        this.lineItems = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.setAmount(amount);
        this.setStaffId(-1);
        this.setStatus(Status.OK);
    }

    public Transaction(int transactionId, String clientFirstName,
                       String clientSurname, int branchId,
                       ArrayList<LineItem> lineItems,
                       ArrayList<Payment> payments, double amount){
        this.setTransactionId(transactionId);
        this.setClientFirstName(clientFirstName);
        this.setClientSurname(clientSurname);
        this.setDateTime(getDateTime());
        this.setBranchId(branchId);
        this.lineItems = lineItems;
        this.payments = payments;
        this.setStaffId(-1);
        this.setAmount(amount);
    }

    public Transaction(int transactionId, String clientFirstName,
                       String clientSurname, Date dateTime,
                       int branchId, Status status, double amount, int staffId) {
        this.setTransactionId(transactionId);
        this.setClientFirstName(clientFirstName);
        this.setClientSurname(clientSurname);
        this.setDateTime(dateTime);
        this.setBranchId(branchId);
        this.lineItems = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.setStatus(status);
        this.setStaffId(staffId);
        this.setAmount(amount);
    }

    public void setLineItems(ArrayList<LineItem> lineItems){
        this.setAmount(0);

        for(LineItem lineItem : lineItems){
            this.setAmount(this.getAmount() + lineItem.getAmount());
        }

        this.lineItems = lineItems;
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }

    public void addLineItem(LineItem lineItem){
        this.setAmount(this.getAmount() + lineItem.getAmount());
        lineItems.add(lineItem);
    }

    public void addPayment(Payment payment){
        payments.add(payment);
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public int getBranchId() {
        return branchId;
    }

    public int getStaffId() {
        return staffId;
    }

    public ArrayList<LineItem> getLineItems() {
        return lineItems;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public double getAmount() {
        double amount = 0;

        for(LineItem lineItem : lineItems){
            amount += lineItem.getAmount();
        }

        return amount;
    }

    public static Status getStatus(String status){
        if(status.equals("OK")){
            return Status.OK;
        }
        else if(status.equals("REFUND")){
            return Status.REFUND;
        }
        else if(status.equals("CANCEL")){
            return Status.CANCEL;
        }
        else
            return null;
    }

    public String toString() {
        String toReturn = "Id: " + getTransactionId() + " Client: " + getClientFirstName() +
                          " " + getClientSurname() + "\n";

        toReturn += "Items: ";
        for(LineItem l : lineItems){
            toReturn += l.toString() + "\n";
        }

        toReturn += "Payments: ";
        for(Payment p : payments){
            toReturn += p.toString() + "\n";
        }

        return toReturn;
    }

    public String getToolbarTitle() {
        String title = getTransactionId() + ": " + getClientFirstName() +
                       " " + getClientSurname() + " | ";
        title += SessionStorage.getStaffById(getStaffId()).getName();
        return title;
    }

    public String getSummary() {
        String toReturn = "Id: " + getTransactionId() + " Client: " + getClientFirstName() +
                " " + getClientSurname() + "\n";

        toReturn += "Balance: " + Formatters.amountFormat.format(getBalance());
        return toReturn;
    }

    public double getTotalPaid() {
        double totalPaid = 0;
        for (Payment p: payments) {
            totalPaid += p.getAmount();
        }
        return totalPaid;
    }

    public double getBalance() {
        return Math.max(getAmount() - getTotalPaid(), 0);
    }
}
