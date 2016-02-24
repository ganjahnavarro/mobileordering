package com.mobile.mobileordering.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobile.mobileordering.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by simplyph on 1/25/2016.
 */
public class FetchOrders extends Service {

    StringRequest stringRequest;
    RequestQueue requestQueue;

    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        stringRequest = request("http://opres.heliohost.org/order/getorder");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requestQueue.add(stringRequest);
            }
        }, 0, 1 * 60 * 1000);//5 Minutes
    }

    private StringRequest request(String uri) {

        return new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equalsIgnoreCase("[]")) {
                            if(MainActivity.function_9343213 != null)
                                MainActivity.function_9343213.notify("orders", 1, MainActivity.field_13432423.build());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
