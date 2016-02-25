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

public class OrderService extends Service {

    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        stringRequest = request("http://mobileordering-gnjb.rhcloud.com/orders.php?status=" + Constants.ORDER_STATUS_PENDING);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requestQueue.add(stringRequest);
            }
        }, 0, 60 * 1000);//1 Minute
    }

    private StringRequest request(String uri) {

        return new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equalsIgnoreCase("[]")) {
                            if(MainActivity.notificationManager != null)
                                MainActivity.notificationManager.notify("orders", 1, MainActivity.notificationBuilder.build());
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
