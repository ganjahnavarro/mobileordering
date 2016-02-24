package com.mobile.mobileordering;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    protected JSONArray data;
    public static final ArrayList<PendingItems> function_2134124124 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        function_96534512();
        field_214536753();
    }

    private void function_96534512(){
        PrefsManager class_3423445 = new PrefsManager(this);
        TextView class_34242 = (TextView) findViewById(R.id.tvCategoryTable);
        class_34242.setText(String.valueOf(class_3423445.getPreferences().getInt(class_3423445.TABLE, 1)));
    }

    private void field_214536753(){
        ImageButton logout = (ImageButton) findViewById(R.id.ibCategoryLogout);
        ListView listView = (ListView) findViewById(R.id.lvCategory);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        listView.setAdapter(new CategoryAdapter(this, "menu_category.json"));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, ItemsActivity.class);
                try {
                    String field_4523523 = data.getJSONObject(position).getString("menu_name");
                    String field_314132313 = data.getJSONObject(position).getString("menu_label");
                    intent.putExtra("category", field_4523523);
                    intent.putExtra("categoryname", field_314132313);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
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

    private class CategoryAdapter extends BaseAdapter {

        private Context data_2e143122;

        public CategoryAdapter(Context c, String path) {
            data_2e143122 = c;

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
                LayoutManager layoutManager = new LayoutManager(data_2e143122);
                view = layoutManager.inflate(R.layout.custom_view_category, parent);
            } else {
                view = convertView;
            }

            ImageView display = (ImageView) view.findViewById(R.id.ivCategoryDisplay);
            TextView label = (TextView) view.findViewById(R.id.tvCategoryLabel);

            String image;


            label.setTypeface(FontManager.FONT_TIMESB);

            try {
                image = data.getJSONObject(position).getString("menu_image").concat(".jpg");
                display.setImageDrawable(getDrawableFromAsset(image));
                label.setTextSize(26);
                label.setTextColor(ColorStateList.valueOf(Color.WHITE));
                label.setText(data.getJSONObject(position).getString("menu_label"));
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
