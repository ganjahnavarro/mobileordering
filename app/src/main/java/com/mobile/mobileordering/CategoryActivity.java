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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    protected JSONArray data;
    private CategoryAdapter adapter;
    public static final ArrayList<PendingItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        loadPreferences();
        loadListAndListeners();
    }

    private void loadPreferences(){
        PrefsManager prefsManager = new PrefsManager(this);
        TextView textView = (TextView) findViewById(R.id.tvCategoryTable);
        textView.setText(String.valueOf(prefsManager.getPreferences().getInt(prefsManager.TABLE, 1)));
    }

    private void loadListAndListeners(){
        ImageButton logout = (ImageButton) findViewById(R.id.ibCategoryLogout);
        ListView listView = (ListView) findViewById(R.id.lvCategory);
        EditText inputSearch = (EditText) findViewById(R.id.inputSearch);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        adapter = new CategoryAdapter(this, "menu_category.json");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, ItemsActivity.class);
                try {
                    String category = data.getJSONObject(position).getString("menu_name");
                    String categoryname = data.getJSONObject(position).getString("menu_label");

                    System.out.println("Category: " + category);
                    System.out.println("Category Name: " + categoryname);

                    intent.putExtra("category", category);
                    intent.putExtra("categoryname", categoryname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    private class CategoryAdapter extends BaseAdapter implements Filterable {

        private Context context;
        private JSONArray filteredData;
        private ValueFilter valueFilter;

        public CategoryAdapter(Context c, String path) {
            context = c;
            JSONManager jsonManager = new JSONManager(c, path);
            data = jsonManager.getData();
            this.filteredData = data;
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

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {
                    JSONArray jsonArray = new JSONArray();

                    for (int i = 0; i < filteredData.length(); i++) {
                        String label;
                        try {
                            label = filteredData.getJSONObject(i).getString("menu_label");

                            if(label != null && label.toUpperCase().contains(constraint.toString().toUpperCase())){
                                jsonArray.put(filteredData.getJSONObject(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    results.count = jsonArray.length();
                    results.values = jsonArray;
                } else {
                    results.count = filteredData.length();
                    results.values = filteredData;
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data = (JSONArray) results.values;
                notifyDataSetChanged();
            }

        }
    }

    private BitmapDrawable getDrawableFromAsset(String filename) throws IOException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;

        inputStream = assetManager.open("menu_image/" + filename);

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return new BitmapDrawable(bitmap);
    }


}
