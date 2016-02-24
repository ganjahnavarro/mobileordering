package com.mobile.mobileordering;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobile.mobileordering.util.InventoryManager;
import com.mobile.mobileordering.util.LayoutManager;
import com.mobile.mobileordering.util.OrderJSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventoryActivity extends AppCompatActivity {

    ArrayList<InventoryManager> function_132141asa31 = new ArrayList<>();
    RequestQueue function_2324124;
    ProgressBar field_232141;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        field_232141 = (ProgressBar) findViewById(R.id.progressBar2);


        function_2324124 = Volley.newRequestQueue(this);

        field_32425425();


        ImageButton bRefresh = (ImageButton) findViewById(R.id.ibInventoryRefresh);
        bRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                field_32425425();
            }
        });
    }

    private void field_32425425() {
        field_232141.setVisibility(View.VISIBLE);
        StringRequest fetchInventory = getRequest("http://opres.heliohost.org/order/getinventory");
        function_2324124.add(fetchInventory);
    }

    private class InventoryAdapter extends BaseAdapter {

        private Context context;

        public InventoryAdapter(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            return function_132141asa31.size();
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
                view = layoutManager.inflate(R.layout.custom_view_inventory, parent);
            } else {
                view = convertView;
            }

            // Ui
            Button edit = (Button) view.findViewById(R.id.bInventoryEdit);
            TextView name = (TextView) view.findViewById(R.id.tvInventoryName);
            TextView qty = (TextView) view.findViewById(R.id.tvInventoryQty);


            // Fetch data
            final InventoryManager inventoryManager = function_132141asa31.get(position);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutManager layoutManager = new LayoutManager(context);
                    View dialogView = layoutManager.inflate(R.layout.custom_dialog_inventory);

                    final EditText editText = (EditText) dialogView.findViewById(R.id.etInventoryEdit);

                    new AlertDialog.Builder(InventoryActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Edit Quantity")
                            .setView(dialogView)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!editText.getText().equals("0")) {
                                        StringRequest stringRequest = updateRequest(inventoryManager.getInventoryID(), editText.getText().toString());
                                        function_2324124.add(stringRequest);
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });

            // Set data
            name.setText(inventoryManager.getName());
            qty.setText(String.valueOf(inventoryManager.getQty()));

            return view;
        }
    }

    private StringRequest getRequest(String uri) {

        return new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        function_132141asa31 = OrderJSONParser.parseFeedInventory(response);
                        field_232141.setVisibility(View.GONE);
                        ListView listView = (ListView) findViewById(R.id.lvInventory);
                        listView.setAdapter(new InventoryAdapter(InventoryActivity.this));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
    }

    private StringRequest updateRequest(final int inventoryid, final String qty) {

        return new StringRequest(Request.Method.POST, "http://opres.heliohost.org/order/updateinventory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        field_32425425();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("inventoryid", String.valueOf(inventoryid));
                params.put("qty", qty);

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
}
