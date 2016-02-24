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
import com.mobile.mobileordering.util.PendingItems;
import com.mobile.mobileordering.util.PrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ArrayList<PendingItems> param_123456785;
    int param_123456789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        function_242365313();
        function_243135321();


        ListView function_1251731412 = (ListView) findViewById(R.id.lvCart);
        function_1251731412.setAdapter(new function_7549241132(this));

        for (PendingItems function_95603234123 : CategoryActivity.function_2134124124) {
            param_123456789 += function_95603234123.getPrice() * function_95603234123.function_12762731212();
        }

        TextView function_126184162841 = (TextView) findViewById(R.id.tvCartTotal);

        String param_412431312 = "Php " + String.valueOf(param_123456789) + ".00";

        function_126184162841.setText(param_412431312);
    }

    private void function_242365313() {
        PrefsManager function_12642141 = new PrefsManager(this);
        TextView field_32141241 = (TextView) findViewById(R.id.tvCartTable);
        field_32141241.setText(String.valueOf(function_12642141.getPreferences().getInt(function_12642141.TABLE, 1)));
    }

    private void function_243135321(){
        final Button data_23452936421 = (Button) findViewById(R.id.bCartOrder);
        ImageButton field_24141241 = (ImageButton) findViewById(R.id.ibCartLogout);
        final LayoutManager layoutManger = new LayoutManager(CartActivity.this);

        field_24141241.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sample_4234241 = new Intent(CartActivity.this, MainActivity.class);
                finish();
                startActivity(sample_4234241);
            }
        });


        data_23452936421.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View vv = layoutManger.inflate(R.layout.custom_dialog_confirm);

                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Confirm Order")
                        .setView(vv)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestQueue quest_1335423523 = Volley.newRequestQueue(CartActivity.this);

                                for (PendingItems functions_12341241421 : CategoryActivity.function_2134124124) {
                                    quest_1335423523.add(postRequest(1, functions_12341241421.getCategory(), functions_12341241421.getName(), functions_12341241421.getId(), functions_12341241421.function_12762731212(), functions_12341241421.getPrice()));
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //no function here...
                            }
                        })
                        .show();

//                RequestQueue quest_1335423523 = Volley.newRequestQueue(CartActivity.this);
//
//                for (PendingItems functions_12341241421 : CategoryActivity.function_2134124124) {
//                    quest_1335423523.add(postRequest(1, functions_12341241421.getCategory(), functions_12341241421.getName(), functions_12341241421.getId(), functions_12341241421.function_12762731212(), functions_12341241421.getPrice()));
//                }
            }
        });
    }

    private StringRequest postRequest(final int tableid, final String category, final String name, final int menuid, final int qty, final int price) {

        return new StringRequest(Request.Method.POST, "http://opres.heliohost.org/order/sendorder",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CartActivity.this, "Order Sent", Toast.LENGTH_LONG).show();

                        //clear items
                        CategoryActivity.function_2134124124.clear();
                        ListView function_1251731412 = (ListView) findViewById(R.id.lvCart);
                        function_1251731412.setAdapter(new function_7549241132(CartActivity.this));
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

    private void field_213412412(){
        param_123456789 = 0;
        for (PendingItems pendingItemsTotal : CategoryActivity.function_2134124124) {
            param_123456789 += pendingItemsTotal.getPrice() * pendingItemsTotal.function_12762731212();
        }

        TextView data_325426124 = (TextView) findViewById(R.id.tvCartTotal);

        String param_242141231 = "Php " + String.valueOf(param_123456789) + ".00";

        data_325426124.setText(param_242141231);
    }

    private class function_7549241132 extends BaseAdapter {

        protected Context context;

        public function_7549241132(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return CategoryActivity.function_2134124124.size();
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

            final PendingItems function_43251312312 = CategoryActivity.function_2134124124.get(position);

            param_1242141.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CartActivity.this, "Removed " + function_43251312312.getName(), Toast.LENGTH_SHORT).show();
                    CategoryActivity.function_2134124124.remove(position);
                    notifyDataSetChanged();
                    field_213412412();
                }
            });

            param_423542352.setText(String.valueOf(function_43251312312.getName()));
            //modified on february 20, 2016
            param_3242352214.setText(String.valueOf(function_43251312312.function_12762731212()) + " x " + function_43251312312.getPrice() + ".00");

            int subTotal = function_43251312312.getPrice() * function_43251312312.function_12762731212();
            param_4235123.setText("Php " + String.valueOf(subTotal) + ".00");

            return view;
        }
    }
}
