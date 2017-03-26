package com.ratanapps.movieexplorer.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ratanapps.movieexplorer.MyApplication;

/**
 * Created by Shivam on 25-Mar-17.
 */

public class VolleySingleton
{
    private static VolleySingleton mInstance=null;
    private static RequestQueue requestQueue=null;

    private VolleySingleton()
    {
        requestQueue= Volley.newRequestQueue(MyApplication.getContext());
    }

    public static VolleySingleton getInstance()
    {
        if(mInstance==null)
            mInstance=new VolleySingleton();

        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        return requestQueue;
    }

}
