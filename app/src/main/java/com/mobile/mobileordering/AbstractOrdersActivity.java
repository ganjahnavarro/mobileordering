package com.mobile.mobileordering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.mobile.mobileordering.util.Constants;
import com.mobile.mobileordering.util.JSONParser;
import com.mobile.mobileordering.util.LayoutManager;
import com.mobile.mobileordering.util.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractOrdersActivity extends AppCompatActivity {

    private List<List<Order>> mapList;
    public static LinkedHashMap<Integer, List<Order>> ordersMap = new LinkedHashMap<>();

    private ProgressBar progressBar;
    private ListView ordersListView;
    private RequestQueue requestQueue;

    protected abstract Context getContext();
    protected abstract String getStatus();

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
                Intent intent = new Intent(getContext(), MainActivity.class);
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

        StringRequest stringRequest = request("http://mobileordering-gnjb.rhcloud.com/orders.php?status=" + getStatus());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private StringRequest request(String uri) {
        return new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        List<Order> orders = JSONParser.parseFeedOrder(response);

                        Iterator<Order> orderIterator = orders.iterator();
                        while(orderIterator.hasNext()){
                            Order order = orderIterator.next();
                            if(isKitchenOrders() && order.getCooked() == 1){
                                orderIterator.remove();
                            }
                        }

                        ordersMap = new LinkedHashMap<>();
                        for(Order order : orders){
                            if(ordersMap.get(order.getBatchid()) != null){
                                ordersMap.get(order.getBatchid()).add(order);
                            } else {
                                List<Order> list = new ArrayList<>();
                                list.add(order);
                                ordersMap.put(order.getBatchid(), list);
                            }
                        }
                        mapList = new ArrayList<List<Order>>(ordersMap.values());

                        ordersListView = (ListView) findViewById(R.id.lvOrders);
                        ordersListView.setAdapter(new OrdersAdapter(getContext()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
    }

    private StringRequest updateRequest(final String status, final int batchid, final String message) {
        return new StringRequest(Request.Method.POST, "http://mobileordering-gnjb.rhcloud.com/updateorder.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(message != null){
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
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
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
                params.put("batchid", String.valueOf(batchid));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
    }

    private StringRequest updateCookedRequest(final int batchid, final String message) {
        return new StringRequest(Request.Method.POST, "http://mobileordering-gnjb.rhcloud.com/updatecookedorder.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(message != null){
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
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
                Map<String, String> params = new HashMap<>();
                params.put("batchid", String.valueOf(batchid));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
    }

    private void refreshListView() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = request("http://mobileordering-gnjb.rhcloud.com/orders.php?status=" + getStatus());
        stringRequest.setShouldCache(false);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void printDialog(final int batchid) {
        StringRequest request = postRequest(batchid);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private StringRequest postRequest(final int batchid) {
        return new StringRequest(Request.Method.POST, "http://mobileordering-gnjb.rhcloud.com/sendreceipt.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),
                                "Receipt was successfully submitted to printer.",
                                Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("batchid", String.valueOf(batchid));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
    }

    private class OrdersAdapter extends BaseAdapter {

        protected Context context;

        public OrdersAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return ordersMap.size();
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

            List<Order> orders = mapList.get(position);

            String value = "";
            for(Order order : orders){
                value += "x" + order.getQty() + " " + order.getName() + "\n";
            }
            items.setText(value);

            table.setText(String.valueOf(orders.get(0).getTableid()));
            loadOrderActions(view, orders);
            return view;
        }

        private void loadOrderActions(View view, final List<Order> orders) {
            LinearLayout forPendingLayout = (LinearLayout) view.findViewById(R.id.layoutForPending);
            LinearLayout forPaidLayout = (LinearLayout) view.findViewById(R.id.layoutForPaid);

            forPendingLayout.setVisibility(isForPending() ? View.VISIBLE : View.GONE);
            forPaidLayout.setVisibility(!isForPending() ? View.VISIBLE : View.GONE);

            System.out.println("@@@@ Test: " + orders.get(0).getBatchid());

            if(isForPending()){
                loadForPendingActions(view, orders);
            } else {
                loadForPaidActions(view, orders);
            }
        }

        private void loadForPendingActions(View view, final List<Order> orders) {
            Button actionButton = (Button) view.findViewById(R.id.bOrdersProcess);
            Button cancelOrder = (Button) view.findViewById(R.id.bOrdersCancel);

            if(isKitchenOrders()){
                actionButton.setText("FINISHED");

                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = "Order is successfully finished.";
                        StringRequest stringRequest = updateCookedRequest(orders.get(0).getBatchid(), message);
                        stringRequest.setShouldCache(false);
                        requestQueue.add(stringRequest);
                    }
                });
            } else {
                actionButton.setText("PROCESS PAYMENT");

                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = "Order is successfully paid.";
                        StringRequest stringRequest = updateRequest(Constants.ORDER_STATUS_PAID, orders.get(0).getBatchid(), message);
                        stringRequest.setShouldCache(false);
                        requestQueue.add(stringRequest);
                    }
                });
            }

            cancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = "Order is successfully cancelled.";
                    StringRequest stringRequest = updateRequest(Constants.ORDER_STATUS_CANCELLED, orders.get(0).getBatchid(), message);
                    stringRequest.setShouldCache(false);
                    requestQueue.add(stringRequest);
                }
            });
        }

        private void loadForPaidActions(View view, final List<Order> orders) {
            Button reprintOrder = (Button) view.findViewById(R.id.bOrdersReceipt);
            Button voidOrder = (Button) view.findViewById(R.id.bOrdersVoid);

            reprintOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printDialog(orders.get(0).getBatchid());
                }
            });

            voidOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = "Order is successfully voided.";
                    StringRequest stringRequest = updateRequest(Constants.ORDER_STATUS_VOID, orders.get(0).getBatchid(), message);
                    stringRequest.setShouldCache(false);
                    requestQueue.add(stringRequest);
                }
            });
        }
    }

    private boolean isForPending(){
        return getStatus().equals(Constants.ORDER_STATUS_PENDING);
    }

    protected boolean isKitchenOrders(){
        return false;
    }

}
