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
import com.mobile.mobileordering.util.PendingItems;
import com.mobile.mobileordering.util.PrefsManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class ItemsActivity extends AppCompatActivity {

    private JSONArray data;
    private String param_1433895645;
    private String param_3407635235;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        param_1433895645 = getIntent().getStringExtra("category");
        param_3407635235 = getIntent().getStringExtra("categoryname");

        TextView textView = (TextView) findViewById(R.id.tvItemsHeading);
        textView.setText(param_3407635235);

        field_123641231();
        field_3423523();
        field_98765234();
    }

    private void field_123641231() {
        PrefsManager prefsManager = new PrefsManager(this);
        TextView tableNumber = (TextView) findViewById(R.id.tvItemsTable);
        tableNumber.setText(String.valueOf(prefsManager.getPreferences().getInt(prefsManager.TABLE, 1)));
    }

    private void field_3423523() {
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

    private void field_98765234(){
        String itemCategory = "menu_" + param_1433895645 + ".json";

        GridView gridView = (GridView) findViewById(R.id.gvItems);
        gridView.setNumColumns(3);
        gridView.setAdapter(new ItemsAdapter(this, itemCategory));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String description = "No Description";

                try {
                    description = data.getJSONObject(position).getString("menu_desc");
//                    description = data.getJSONObject(position).getString("menu_desc") + "\n\n\nIngredient:\n\n" + data.getJSONObject(position).getString("menu_ingredient") + "\n\n\nProcedure:\n\n" + data.getJSONObject(position).getString("menu_procedure");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final LayoutManager layoutManager = new LayoutManager(ItemsActivity.this);
                View dialogView = layoutManager.inflate(R.layout.custom_dialog_items);

                final TextView textView = (TextView) dialogView.findViewById(R.id.tvItemsQty);
                TextView textView1 = (TextView) dialogView.findViewById(R.id.tvItemsDesc);
                textView1.setText(description);
                final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.sbItemsQty);

                final int[] progressBar = new int[1];

                seekBar.setMax(10);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        textView.setText(String.valueOf(progress));
                        progressBar[0] = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

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
                                            CategoryActivity.function_2134124124.add(new PendingItems(param_1433895645, position, menuName, progressBar[0], menuPrice));
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
        InputStream inputStream = null;

        inputStream = assetManager.open("menu_image/" + filename);

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // TODO Change the return value
        return new BitmapDrawable(bitmap);
    }
}
