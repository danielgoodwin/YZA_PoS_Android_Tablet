package com.pos.yza.yzapos.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.pos.yza.yzapos.data.source.data.LocalDatabaseHelper;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

public class MockServerSingleton {

    private final String TAG = "MockServerSingleton";
    private static MockServerSingleton sInstance;
    private MockWebServer mWebServer;

    public static synchronized MockServerSingleton getInstance() {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new MockServerSingleton();
        }
        return sInstance;
    }

    private MockServerSingleton() {
        Log.i(TAG, "MockServerSingleton()");
        mWebServer = new MockWebServer();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mWebServer.start();
                    Log.i(TAG, "MockWebServer Started");
                } catch (IOException io)
                {
                    //
                    Log.w(TAG, "Error starting MockWebServer " + io.getMessage());
                }
            }
        });

    }

    public MockWebServer getServer()
    {
        return mWebServer;
    }

}
