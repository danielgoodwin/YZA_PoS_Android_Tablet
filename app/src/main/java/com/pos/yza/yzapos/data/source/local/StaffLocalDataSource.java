package com.pos.yza.yzapos.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.pos.yza.yzapos.data.representations.Product;
import com.pos.yza.yzapos.data.representations.ProductCategory;
import com.pos.yza.yzapos.data.representations.ProductProperty;
import com.pos.yza.yzapos.data.representations.Staff;
import com.pos.yza.yzapos.data.source.ProductsDataSource;
import com.pos.yza.yzapos.data.source.StaffDataSource;
import com.pos.yza.yzapos.data.source.data.LocalDatabaseHelper;

import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */

public class StaffLocalDataSource implements StaffDataSource {

    private static StaffLocalDataSource INSTANCE;
    private static final String TAG = "LocalDataSource";

    public final static String CATEGORY_NAME = "name",
                               CATEGORY_PROPERTIES = "properties",
                               CATEGORY_PROPERTY_NAME = "name";

    private RequestQueue mRequestQueue;
    private LocalDatabaseHelper mDB;

    public static StaffLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new StaffLocalDataSource();
            INSTANCE.mDB = LocalDatabaseHelper.getInstance(context);
        }
        return INSTANCE;
    }

    private StaffLocalDataSource() {}

    @Override
    public void getAllStaff(@NonNull LoadStaffCallback callback) {
        checkNotNull(callback);
        Log.i(TAG, "getAllStaff");

        List<Staff> objlist = mDB.getAllStaff();
        callback.onStaffLoaded(objlist);
    }

    @Override
    public void saveStaff(@NonNull Staff staff) {
        Log.i(TAG, "saveStaff");
        mDB.insertStaff(staff);
    }

    @Override
    public void refreshStaff() {

    }

    @Override
    public void deleteAllStaff() {

    }

    @Override
    public void deleteStaff(@NonNull ModifyStaffCallback callback, @NonNull String staffId) {
        Log.i(TAG, "deleteStaff");

        Staff obj = mDB.getStaffByEmployeeNo(staffId);
        if (obj != null) {
            mDB.deleteStaff(obj);
        }
    }

    @Override
    public void editStaff(@NonNull String staffId, @NonNull HashMap<String, String> edits) {

    }
}
