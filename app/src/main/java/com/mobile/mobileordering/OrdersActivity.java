package com.mobile.mobileordering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobile.mobileordering.util.OrderJSONParser;
import com.mobile.mobileordering.util.OrderManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity {

    public static ArrayList<OrderManager> field_23124314 = new ArrayList<>();
    ProgressBar field_789434523;
    ListView field_123413124;
    RequestQueue param_3423423;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        param_3423423 = Volley.newRequestQueue(this);
        field_789434523 = (ProgressBar) findViewById(R.id.progressBar);
        field_789434523.setVisibility(View.VISIBLE);

        ImageButton logout = (ImageButton) findViewById(R.id.ibOrdersLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ImageButton refresh = (ImageButton) findViewById(R.id.ibOrdersRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshListView();
            }
        });

        StringRequest stringRequest = request("http://opres.heliohost.org/order/getorder");
        param_3423423.add(stringRequest);
    }

    private StringRequest request(String uri) {

        return new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        field_789434523.setVisibility(View.GONE);
                        field_23124314 = OrderJSONParser.parseFeedOrder(response);
                        field_123413124 = (ListView) findViewById(R.id.lvOrders);
                        field_123413124.setAdapter(new OrdersAdapter(OrdersActivity.this));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
    }

    private StringRequest updateRequest(final int status, final int id, final String name) {

        return new StringRequest(Request.Method.POST, "http://opres.heliohost.org/order/updateorder",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(OrdersActivity.this, "Void " + name, Toast.LENGTH_SHORT).show();
                        refreshListView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("status", String.valueOf(status));
                params.put("id", String.valueOf(id));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
    }

    private void refreshListView() {
        field_23124314 = new ArrayList<>();
        field_789434523.setVisibility(View.VISIBLE);
        StringRequest stringRequest = request("http://opres.heliohost.org/order/getorder");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void printDialog(String name, int qty, int price) {
        Intent printIntent = new Intent(OrdersActivity.this, PrintDialogActivity.class);
        File tempReceipt;

        try {
            String tempName = "receipt.txt";
            tempReceipt = File.createTempFile("receipt", "txt");

            String about = "x" + String.valueOf(qty) + " " + name;
            int total = qty * price;

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempReceipt));
            bufferedWriter.write("Official Receipt");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write(about);
            bufferedWriter.newLine();
            bufferedWriter.write("Php " + String.valueOf(price));
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("Total: Php " + String.valueOf(total) + ".00");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("THANK YOU ^_^");
            bufferedWriter.close();

            printIntent.setDataAndType(Uri.fromFile(tempReceipt), getMimeType(tempName));
            printIntent.putExtra("title", "receipt");
            startActivity(printIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private class OrdersAdapter extends BaseAdapter {

        protected Context context;

        public OrdersAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            return field_23124314.size();
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
                LayoutInflater layoutInflater;
                layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.custom_view_orders, parent, false);
                view.setPadding(8, 8, 8, 8);
            } else {
                view = convertView;
            }

            TextView items = (TextView) view.findViewById(R.id.tvOrdersItem);
            TextView table = (TextView) view.findViewById(R.id.tvOrdersTable);
            Button receipt = (Button) view.findViewById(R.id.bOrdersReceipt);
            Button voidOrder = (Button) view.findViewById(R.id.bOrdersVoid);

            final OrderManager orderManager = field_23124314.get(position);

            String value = "x" + String.valueOf(orderManager.getQty()) + " " + orderManager.getName();

            items.setText(value);
            table.setText(String.valueOf(orderManager.getTableid()));

            voidOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = updateRequest(2, orderManager.getId(), orderManager.getName());
                    param_3423423.add(stringRequest);
                }
            });

            receipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = updateRequest(1, orderManager.getId(), orderManager.getName());
                    param_3423423.add(stringRequest);
                    printDialog(orderManager.getName(), orderManager.getQty(), orderManager.getPrice());
                }
            });

            return view;
        }
    }
}
