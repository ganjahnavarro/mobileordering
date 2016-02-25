package com.mobile.mobileordering;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.mobileordering.util.LayoutManager;
import com.mobile.mobileordering.util.PrefsManager;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private String menuList[] = {"Orders", "Inventory", "Report", "Customer Feedback", "Set Table"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ListView menuListView = (ListView) findViewById(R.id.lvMenu);
        menuListView.setAdapter(new MenuAdapter(this));
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String menuName = menuList[position];

                switch (menuName) {
                    case "Orders":
                        Intent ordersIntent = new Intent(MenuActivity.this, OrdersActivity.class);
                        startActivity(ordersIntent);
                        break;

                    case "Inventory":
                        Intent inventoryIntent = new Intent(MenuActivity.this, InventoryActivity.class);
                        startActivity(inventoryIntent);
                        break;

                    case "Report":
                        Intent reportIntent = new Intent(MenuActivity.this, ReportActivity.class);
                        startActivity(reportIntent);
                        break;

                    case "Set Table":
                        String items[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                                "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35"};

                        final int[] selected = new int[1];

                        new AlertDialog.Builder(MenuActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("Set Table Number")
                                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selected[0] = which;
                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PrefsManager prefsManager = new PrefsManager(MenuActivity.this);
                                        SharedPreferences.Editor editor = prefsManager.getPreferences().edit();
                                        if (selected[0] == -1) {
                                            editor.putInt(prefsManager.TABLE, 1);
                                            Toast.makeText(MenuActivity.this, "Default Table Number", Toast.LENGTH_LONG).show();
                                        } else {
                                            editor.putInt(prefsManager.TABLE, (selected[0] + 1));
                                            Toast.makeText(MenuActivity.this, "Table has been set to " + String.valueOf(selected[0] + 1) + "", Toast.LENGTH_LONG).show();
                                        }
                                        editor.apply();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                        break;

                    case "Customer Feedback":
                        Intent feedBackIntent = new Intent(MenuActivity.this, ReportActivity.class);
                        startActivity(feedBackIntent);
                        break;

                    default:
                        return;
                }
            }
        });


        ImageButton logout = (ImageButton) findViewById(R.id.ibMenuLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    private class MenuAdapter extends BaseAdapter {
        private Context context;

        public MenuAdapter(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            return menuList.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutManager layoutManager = new LayoutManager(context);
                view = layoutManager.inflate(R.layout.custom_view_menu, parent);
            } else {
                view = convertView;
            }

            TextView label = (TextView) view.findViewById(R.id.tvMenuItem);
            label.setText(menuList[position]);

            return view;
        }
    }

    @Override
    public void onBackPressed() {

        new android.support.v7.app.AlertDialog.Builder(this)
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
}
