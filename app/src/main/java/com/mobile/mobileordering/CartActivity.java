package com.mobile.mobileordering;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobile.mobileordering.util.LayoutManager;
import com.mobile.mobileordering.util.PendingItem;
import com.mobile.mobileordering.util.PrefsManager;

import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private int totalAmount;
    private int tableid;

    private final double MINUTES_PER_DISH_MIN = 1.5;
    private final double MINUTES_PER_DISH_MAX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        loadPreferences();
        loadListeners();

        ListView cartListView = (ListView) findViewById(R.id.lvCart);
        cartListView.setAdapter(new OrderAdapter(this));

        computeTotalAmount();
    }

    private void loadPreferences() {
        PrefsManager prefsManager = new PrefsManager(this);
        tableid = prefsManager.getPreferences().getInt(prefsManager.TABLE, 1);

        TextView tableNoTextView = (TextView) findViewById(R.id.tvCartTable);
        tableNoTextView.setText(String.valueOf(tableid));
    }

    private void loadListeners(){
        final Button orderButton = (Button) findViewById(R.id.bCartOrder);
        ImageButton logoutButton = (ImageButton) findViewById(R.id.ibCartLogout);
        final LayoutManager layoutManger = new LayoutManager(CartActivity.this);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = layoutManger.inflate(R.layout.custom_dialog_confirm);

                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Confirm Order")
                        .setView(view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                                StringRequest stringRequest = getRequest();
                                stringRequest.setShouldCache(false);
                                requestQueue.add(stringRequest);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //no function here...
                            }
                        })
                        .show();
            }
        });
    }

    private StringRequest postRequest(final String category, final String name, final int menuid, final int qty, final int price, final int batchid) {
        return new StringRequest(Request.Method.POST, "http://mobileordering-gnjb.rhcloud.com/sendorder.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int size = 0;

                        for(PendingItem item : CategoryActivity.items){
                            size += item.getQty();
                        }

                        Toast.makeText(CartActivity.this,
                                "Order Sent. Please wait " + MINUTES_PER_DISH_MIN * size + " to " + MINUTES_PER_DISH_MAX * size + " minutes for your order.",
                                Toast.LENGTH_LONG).show();

                        CategoryActivity.items.clear();
                        ListView cartListView = (ListView) findViewById(R.id.lvCart);
                        cartListView.setAdapter(new OrderAdapter(CartActivity.this));

                        computeTotalAmount();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("tableid", String.valueOf(tableid));
                        params.put("category", category);
                        params.put("name", name);
                        params.put("menuid", String.valueOf(menuid));
                        params.put("qty", String.valueOf(qty));
                        params.put("batchid", String.valueOf(batchid));
                        params.put("price", String.valueOf(price));
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

    private StringRequest getRequest() {
        return new StringRequest(Request.Method.GET, "http://mobileordering-gnjb.rhcloud.com/latestbatch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                        for (PendingItem item : CategoryActivity.items) {
                            StringRequest stringRequest = postRequest(item.getCategory(), item.getName(), item.getId(), item.getQty(), item.getPrice(), Integer.valueOf(response));
                            stringRequest.setShouldCache(false);
                            requestQueue.add(stringRequest);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    private void computeTotalAmount(){
        totalAmount = 0;

        if(CategoryActivity.items != null){
            for (PendingItem item : CategoryActivity.items) {
                totalAmount += item.getPrice() * item.getQty();
            }
        }

        TextView totalAmountTextView = (TextView) findViewById(R.id.tvCartTotal);
        String totalAmountDisplay = "Php " + String.valueOf(totalAmount) + ".00";
        totalAmountTextView.setText(totalAmountDisplay);
    }

    private class OrderAdapter extends BaseAdapter {

        protected Context context;

        public OrderAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return CategoryActivity.items.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;

            if (convertView == null) {
                LayoutManager function_121412 = new LayoutManager(context);
                view = function_121412.inflate(R.layout.custom_view_cart, parent);
            } else {
                view = convertView;
            }

            ImageButton param_1242141 = (ImageButton) view.findViewById(R.id.ibCartRemove);
            TextView param_423542352 = (TextView) view.findViewById(R.id.tvCartLabel);
            TextView param_3242352214 = (TextView) view.findViewById(R.id.tvCartQty);
            TextView param_4235123 = (TextView) view.findViewById(R.id.tvCartPrice);

            final PendingItem function_43251312312 = CategoryActivity.items.get(position);

            param_1242141.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CartActivity.this, "Removed " + function_43251312312.getName(), Toast.LENGTH_SHORT).show();
                    CategoryActivity.items.remove(position);
                    notifyDataSetChanged();
                    computeTotalAmount();
                }
            });

            param_423542352.setText(String.valueOf(function_43251312312.getName()));
            //modified on february 20, 2016
            param_3242352214.setText(String.valueOf(function_43251312312.getQty()) + " x " + function_43251312312.getPrice() + ".00");

            int subTotal = function_43251312312.getPrice() * function_43251312312.getQty();
            param_4235123.setText("Php " + String.valueOf(subTotal) + ".00");

            return view;
        }
    }
}
