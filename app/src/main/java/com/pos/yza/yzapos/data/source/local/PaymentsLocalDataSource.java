package com.pos.yza.yzapos.data.source.local;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pos.yza.yzapos.data.representations.Payment;
import com.pos.yza.yzapos.data.representations.Transaction;
import com.pos.yza.yzapos.data.source.PaymentsDataSource;
import com.pos.yza.yzapos.data.source.data.LocalDatabaseHelper;
import com.pos.yza.yzapos.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */

public class PaymentsLocalDataSource implements PaymentsDataSource {

    private static PaymentsLocalDataSource INSTANCE;
    private static final String TAG = "LocalDataSource";

    private LocalDatabaseHelper mDB;

    public static PaymentsLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PaymentsLocalDataSource();
            INSTANCE.mDB = LocalDatabaseHelper.getInstance(context);
        }
        return INSTANCE;
    }

    private PaymentsLocalDataSource() {}


    public void getPaymentById(@NonNull String paymentId,
                               @NonNull GetPaymentCallback callback){
        checkNotNull(callback);
        Payment obj = mDB.getPayment(Long.valueOf(paymentId));
        if (obj == null)
        {
            callback.onDataNotAvailable();
        }
        else {
            callback.onPaymentLoaded(obj);
        }

    }

    public void savePayment(@NonNull Payment payment){
        Log.i("savePayment", "in remote data source");
        mDB.insertPayment(payment);

    }

    public void refundPayment(@NonNull String paymentId){
        Log.i("refundPayment", "in remote data source");

        Payment obj = mDB.getPayment(Long.valueOf( paymentId));
        if (obj != null) {
            mDB.refundPayment(obj);
        }
    }

}
