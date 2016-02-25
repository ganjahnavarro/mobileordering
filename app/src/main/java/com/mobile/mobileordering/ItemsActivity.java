package com.mobile.mobileordering;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mobile.mobileordering.util.FontManager;
import com.mobile.mobileordering.util.JSONManager;
import com.mobile.mobileordering.util.LayoutManager;
import com.mobile.mobileordering.util.PendingItem;
import com.mobile.mobileordering.util.PrefsManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class ItemsActivity extends AppCompatActivity {

    private JSONArray data;
    private String category;
    private String categoryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        category = getIntent().getStringExtra("category");
        categoryname = getIntent().getStringExtra("categoryname");

        TextView textView = (TextView) findViewById(R.id.tvItemsHeading);
        textView.setText(categoryname);

        loadPreferences();
        loadListeners();
        loadItems();
    }

    private void loadPreferences() {
        PrefsManager prefsManager = new PrefsManager(this);
        TextView tableNumber = (TextView) findViewById(R.id.tvItemsTable);
        tableNumber.setText(String.valueOf(prefsManager.getPreferences().getInt(prefsManager.TABLE, 1)));
    }

    private void loadListeners() {
        Button viewOrder = (Button) findViewById(R.id.bItemsView);
        ImageButton logout = (ImageButton) findViewById(R.id.ibItemsLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemsActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadItems(){
        String itemCategory = "menu_" + category + ".json";

        GridView gridView = (GridView) findViewById(R.id.gvItems);
        gridView.setNumColumns(3);
        gridView.setAdapter(new ItemsAdapter(this, itemCategory));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String description = "No Description";

                try {
                    description = data.getJSONObject(position).getString("menu_desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final LayoutManager layoutManager = new LayoutManager(ItemsActivity.this);
                View dialogView = layoutManager.inflate(R.layout.custom_dialog_items);

                final TextView quantityTextView = (TextView) dialogView.findViewById(R.id.tvItemsQty);
                TextView descriptionTextView = (TextView) dialogView.findViewById(R.id.tvItemsDesc);
                descriptionTextView.setText(description);
                final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.sbItemsQty);

                final int[] progressBar = new int[1];
                final int MIN_QUANTITY = 1;

                seekBar.setMax(10 - MIN_QUANTITY);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress += MIN_QUANTITY;
                        quantityTextView.setText(String.valueOf(progress));
                        progressBar[0] = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                quantityTextView.setText("1");
                progressBar[0] = 1;

                try {
                    new AlertDialog.Builder(ItemsActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Quantity of " + data.getJSONObject(position).getString("menu_name"))
                            .setView(dialogView)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (progressBar[0] == 0) {
                                            View vv = layoutManager.inflate(R.layout.custom_dialog_note);
                                            new AlertDialog.Builder(ItemsActivity.this)
                                                    .setTitle("Note")
                                                    .setView(vv)
                                                    .show();
                                        }
                                        if (progressBar[0] != 0) {
                                            String menuName = data.getJSONObject(position).getString("menu_name");
                                            int menuPrice = Integer.parseInt(data.getJSONObject(position).getString("menu_price"));
                                            CategoryActivity.items.add(new PendingItem(category, position, menuName, progressBar[0], menuPrice));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class ItemsAdapter extends BaseAdapter {

        private Context context;

        public ItemsAdapter(Context c, String path) {
            context = c;
            JSONManager jsonManager = new JSONManager(c, path);
            data = jsonManager.getData();
            new FontManager(c);
        }

        @Override
        public int getCount() {
            return data.length();
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
                view = layoutManager.inflate(R.layout.custom_view_items, parent);
                view.setLayoutParams(new GridView.LayoutParams(MainActivity.AVAILABLE_WIDTH / 3, MainActivity.AVAILABLE_WIDTH / 2));
            } else {
                view = convertView;
            }

            String name, cost, image;

            // Ui
            ImageView display = (ImageView) view.findViewById(R.id.ivItemsDisplay);
            TextView price = (TextView) view.findViewById(R.id.tvItemsPrice);
            TextView label = (TextView) view.findViewById(R.id.tvItemsLabel);

            try {
                // Fetch data
                name = data.getJSONObject(position).getString("menu_name");
                cost = data.getJSONObject(position).getString("menu_price");
                image = data.getJSONObject(position).getString("menu_image").concat(".jpg");

                String priceFragment = "Php " + cost + ".00";

                label.setTextColor(ColorStateList.valueOf(Color.WHITE));
                label.setTextSize(26);
                label.setTypeface(FontManager.FONT_TIMESB);

                price.setTextColor(ColorStateList.valueOf(Color.WHITE));
                price.setTextSize(20);
                price.setTypeface(FontManager.FONT_TIMES);

                // Set data
                display.setImageDrawable(getDrawableFromAsset(image));
                price.setText(priceFragment);
                label.setText(name);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return view;
        }
    }

    private BitmapDrawable getDrawableFromAsset(String filename) throws IOException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open("menu_image/" + filename);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return new BitmapDrawable(bitmap);
    }
}
