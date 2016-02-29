package com.mobile.mobileordering;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.mobileordering.util.Cache;
import com.mobile.mobileordering.util.LayoutManager;
import com.mobile.mobileordering.util.PendingItem;
import com.mobile.mobileordering.util.PrefsManager;

public class SubmittedOrdersActivity extends AppCompatActivity {

    private int totalAmount;
    private int tableid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_orders);

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
        ImageButton logoutButton = (ImageButton) findViewById(R.id.ibCartLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubmittedOrdersActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void computeTotalAmount(){
        totalAmount = 0;

        if(Cache.get() != null){
            for (PendingItem item : Cache.get()) {
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
            return Cache.get().size();
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
                LayoutManager layout = new LayoutManager(context);
                view = layout.inflate(R.layout.custom_view_cart, parent);
            } else {
                view = convertView;
            }

            ImageButton imageButton = (ImageButton) view.findViewById(R.id.ibCartRemove);
            imageButton.setVisibility(View.INVISIBLE);

            TextView cartLabel = (TextView) view.findViewById(R.id.tvCartLabel);
            TextView cartQty = (TextView) view.findViewById(R.id.tvCartQty);
            TextView cartPrice = (TextView) view.findViewById(R.id.tvCartPrice);

            final PendingItem item = Cache.get().get(position);

            cartLabel.setText(String.valueOf(item.getName()));
            cartQty.setText(String.valueOf(item.getQty()) + " x " + item.getPrice() + ".00");

            int subTotal = item.getPrice() * item.getQty();
            cartPrice.setText("Php " + String.valueOf(subTotal) + ".00");

            return view;
        }
    }
}
