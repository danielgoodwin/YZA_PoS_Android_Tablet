package com.pos.yza.yzapos.data.representations;

import java.util.List;

/**
 * Created by Dlolpez on 31/12/17.
 */

public final class Staff  {

    public static final String LCL_TABLE_NAME = "staff";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMPLOYEENO = "employeeno";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_PHONENUMBER = "phonenumber";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_HOMEADDRESS = "homeaddress";

    // Create table SQL query
    public static final String LCL_CREATE_TABLE =
            "CREATE TABLE " + LCL_TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FIRSTNAME + " TEXT, "
                    + COLUMN_EMPLOYEENO + " TEXT, "
                    + COLUMN_LASTNAME + " TEXT, "
                    + COLUMN_PHONENUMBER + " TEXT, "
                    + COLUMN_EMAIL + " TEXT, "
                    + COLUMN_HOMEADDRESS + " TEXT "
                    + ")";

    int staffId;
    private String employeeNo;
    String firstName;
    String lastName;
    String phoneNumber;
    String email;
    String homeAddress;

    public Staff(){

    }

    public Staff(int staffId, String employeeNo, String firstName,
                 String lastName, String phoneNumber,
                 String email, String homeAddress){

        this.staffId = staffId;
        this.employeeNo  = employeeNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.homeAddress = homeAddress;
    }

    public Staff(String employeeNo, String firstName,
                 String lastName, String phoneNumber,
                 String email, String homeAddress){

        this.staffId = -1;
        this.employeeNo = employeeNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.homeAddress = homeAddress;
    }


    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int id) {
        this.staffId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName ;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLasttName(String lastName) {
        this.lastName = lastName ;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber = number ;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email ;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String address) {
        this.homeAddress = homeAddress ;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }
}
