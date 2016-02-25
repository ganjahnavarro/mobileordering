package com.mobile.mobileordering;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobile.mobileordering.util.OrderService;
import com.mobile.mobileordering.util.JSONParser;
import com.mobile.mobileordering.util.LayoutManager;
import com.mobile.mobileordering.util.Order;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static NotificationManager notificationManager;
    public static NotificationCompat.Builder notificationBuilder;

    protected int MARGIN_DP = 4;
    protected static int AVAILABLE_WIDTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AVAILABLE_WIDTH = getAvailableWidth();

        setupNotificationBuilder();

        Intent fetchOrdersIntent = new Intent(MainActivity.this, OrderService.class);
        startService(fetchOrdersIntent);

        loadListeners();
    }

    private void volleyTest(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlHeliosGet ="http://mobileordering-gnjb.rhcloud.com/orders.php?status=33";

        StringRequest orderRequest = new StringRequest(Request.Method.GET, urlHeliosGet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<Order> orders = JSONParser.parseFeedOrder(response);

                            if(orders != null){
                                for(Order order : orders){
                                    System.out.println("@MobileOrdering Order: " + order.getId() + " - " + order.getName() + " " + order.getQty());
                                }
                            }
                        } catch (Exception e){
                            System.out.println("@MobileOrdering Error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("@MobileOrdering Volley Helios Error: " + error.getMessage());
            }
        });
        queue.add(orderRequest);
    }

    private void loadListeners() {
        Button customer = (Button) findViewById(R.id.bMainCustomer);
        ImageButton admin = (ImageButton) findViewById(R.id.ibMainAdmin);

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              TODO
                volleyTest();

                LayoutManager layoutManager = new LayoutManager(MainActivity.this);
                View loginView = layoutManager.inflate(R.layout.custom_dialog_main);

                final EditText username = (EditText) loginView.findViewById(R.id.etMainUsername);
                final EditText password = (EditText) loginView.findViewById(R.id.etMainPassword);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Login")
                        .setView(loginView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                TODO
//                                String user = "papis";
//                                String pass = "2011";
//
//                                if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
//                                    Toast.makeText(MainActivity.this, "Incomplete Credentials", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                if (username.getText().toString().matches(user) && password.getText().toString().matches(pass)) {
                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                    finish();
                                    startActivity(intent);
//                                } else {
//                                    Toast.makeText(MainActivity.this, "Wrong Username / Password", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Application")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public int getAvailableWidth() {
        // Screensize
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        // Display scale density
        float scale = getResources().getDisplayMetrics().density;

        // Margin Computation
        int marginPx = (int) (MARGIN_DP * scale + 0.5f);
        int width = size.x - marginPx;

        return width;
    }

    public void setupNotificationBuilder() {
        notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.download)
                .setContentTitle("New Orders")
                .setAutoCancel(true);

        //TODO
        notificationBuilder.setContentTitle("Test Title");
        notificationBuilder.setContentText("Test description");

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(MainActivity.this);
        taskStackBuilder.addParentStack(ItemsActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
}
