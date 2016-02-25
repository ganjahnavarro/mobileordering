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
import com.mobile.mobileordering.util.Order;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity {

    public static ArrayList<Order> orders = new ArrayList<>();
    private ProgressBar progressBar;
    private ListView ordersListView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        requestQueue = Volley.newRequestQueue(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

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
        requestQueue.add(stringRequest);
    }

    private StringRequest request(String uri) {

        return new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        orders = OrderJSONParser.parseFeedOrder(response);
                        ordersListView = (ListView) findViewById(R.id.lvOrders);
                        ordersListView.setAdapter(new OrdersAdapter(OrdersActivity.this));
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
        orders = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
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
            return orders.size();
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

            final Order order = orders.get(position);

            String value = "x" + String.valueOf(order.getQty()) + " " + order.getName();
            value = value.replace("adsfasdf", "");

            items.setText(value);
            table.setText(String.valueOf(order.getTableid()));

            voidOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = updateRequest(2, order.getId(), order.getName());
                    requestQueue.add(stringRequest);
                }
            });

            receipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = updateRequest(1, order.getId(), order.getName());
                    requestQueue.add(stringRequest);
                    printDialog(order.getName(), order.getQty(), order.getPrice());
                }
            });

            return view;
        }
    }
}
