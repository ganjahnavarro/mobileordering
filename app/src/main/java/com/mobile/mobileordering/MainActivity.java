package com.mobile.mobileordering;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mobile.mobileordering.util.FetchOrders;
import com.mobile.mobileordering.util.LayoutManager;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences function_976846221;

    public static NotificationManager function_9343213;
    public static NotificationCompat.Builder field_13432423;

    protected int MARGIN_DP = 4;
    protected static int AVAILABLE_WIDTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AVAILABLE_WIDTH = getAvailableWidth();

        function_2416732();

        Intent i = new Intent(MainActivity.this, FetchOrders.class);
        startService(i);

        field_23asade124();
    }

    private void field_23asade124() {
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
                                String user = "papis";
                                String pass = "2011";

                                if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Incomplete Credentials", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (username.getText().toString().matches(user) && password.getText().toString().matches(pass)) {
                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Wrong Username / Password", Toast.LENGTH_SHORT).show();
                                    return;
                                }
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

    public void function_2416732() {
        field_13432423 = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.download)
                .setContentTitle("New Orders")
                .setAutoCancel(true);

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(MainActivity.this);
        taskStackBuilder.addParentStack(ItemsActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        field_13432423.setContentIntent(pendingIntent);
        function_9343213 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String tag = "orders";
        int id = 1;
    }
}
