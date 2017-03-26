package com.ratanapps.movieexplorer;

import android.app.Application;

/**
 * Created by Shivam on 25-Mar-17.
 */

public class MyApplication extends Application
{
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static MyApplication getContext()
    {
        return mInstance;
    }
}
