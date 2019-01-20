package com.pos.yza.yzapos.data.source.local;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pos.yza.yzapos.data.representations.Branch;
import com.pos.yza.yzapos.data.representations.LineItem;
import com.pos.yza.yzapos.data.representations.Payment;
import com.pos.yza.yzapos.data.representations.Transaction;
import com.pos.yza.yzapos.data.source.TransactionsDataSource;
import com.pos.yza.yzapos.data.source.data.LocalDatabaseHelper;
import com.pos.yza.yzapos.util.Constants;
import com.pos.yza.yzapos.util.Parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource.CLIENT_FIRST_NAME;
import static com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource.CLIENT_LAST_NAME;
import static com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource.LINE_ITEMS;
import static com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource.PAYMENTS;
import static com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource.STAFF_ID;
import static com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource.TRANSACTION_AMOUNT;
import static com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource.TRANSACTION_BRANCH_ID;
import static com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource.TRANSACTION_ID;

/**
 *
 */

public class TransactionsLocalDataSource implements TransactionsDataSource {

    private static TransactionsLocalDataSource INSTANCE;
    private static final String TAG = "LocalDataSource";

    private LocalDatabaseHelper mDB;

    public static TransactionsLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TransactionsLocalDataSource();
            INSTANCE.mDB = LocalDatabaseHelper.getInstance(context);
        }
        return INSTANCE;
    }

    private TransactionsLocalDataSource() {}


    @Override
    public void getTransactions(@NonNull final LoadTransactionsCallback callback){
        checkNotNull(callback);

        List<Transaction> list = mDB.getAllTransactions();
        if (list == null)
        {
            callback.onDataNotAvailable();
        }
        else {
            callback.onTransactionsLoaded(list);
        }

    }

    public void getTransactionById(@NonNull String transactionId, @NonNull GetTransactionCallback callback){
        checkNotNull(callback);

        Transaction obj = mDB.getTransaction(Long.valueOf(transactionId));
        callback.onTransactionLoaded(obj);

    }


    public void saveTransaction(@NonNull Transaction transaction) {

        mDB.insertTransaction(transaction);
    }


    public void saveTransaction(@NonNull Transaction transaction,
                                Response.Listener<JSONObject> responseListener){
        Log.i("saveTransaction", "in local data source");

        long transId = mDB.insertTransaction(transaction);

        //
        //

        HashMap<String,String> params = new HashMap<String, String>();
        params.put(CLIENT_FIRST_NAME, transaction.getClientFirstName());
        params.put(CLIENT_LAST_NAME, transaction.getClientSurname());
        params.put(TRANSACTION_BRANCH_ID, transaction.getBranchId() + "");
        params.put(TRANSACTION_AMOUNT, transaction.getAmount() + "" );
        params.put(STAFF_ID, transaction.getStaffId() + "");
        params.put(TRANSACTION_ID, transId + "");

        JSONObject paramsJson = new JSONObject(params);

        try {

            // Add lineitems

            JSONArray itemsJson = new JSONArray();
            for(LineItem lineItem : transaction.getLineItems()){
                JSONObject itemJson = new JSONObject(lineItem.toHashMap());
                itemsJson.put(itemJson);
            }
            paramsJson.put(LINE_ITEMS, itemsJson);
            Log.i("saveTransaction", itemsJson.toString());

            // Add payments

            JSONArray paymentsJson = new JSONArray();
            for(Payment payment : transaction.getPayments()){
                JSONObject paymentJson = new JSONObject(payment.toHashMap());
                paymentsJson.put(paymentJson);
            }
            paramsJson.put(PAYMENTS, paymentsJson);

            Log.i("trans", paramsJson.toString());

            responseListener.onResponse(paramsJson);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void refreshTransactions(){

    }

    public void deleteOldTransactions(){
        Log.i("deleteOldTrans", "in remote data source");
        mDB.deleteOldTransactions();
    }

    public void deleteAllTransactionsByBranch(){

    }

    public void cancelTransaction(@NonNull String transactionId){
        Log.i("cancelTrans", "in remote data source");
        Transaction obj = mDB.getTransaction(Long.valueOf(transactionId));
        if (obj != null) {
            mDB.cancelTransaction(obj);
        }
    }

    public void refundTransaction(@NonNull String transactionId){
        Log.i("refundTrans", "in remote data source");
        Transaction obj = mDB.getTransaction(Long.valueOf(transactionId));
        if (obj != null) {
            mDB.refundTransaction(obj);
        }

    }

    public void sendReport(@NonNull Branch branch, @NonNull int year, @NonNull int month, @NonNull int day) {
        Log.i("sendReport", "in remote data source");

    }

}


