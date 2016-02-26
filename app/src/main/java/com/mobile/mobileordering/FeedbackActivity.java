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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobile.mobileordering.util.Feedback;
import com.mobile.mobileordering.util.JSONParser;
import com.mobile.mobileordering.util.LayoutManager;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity{

    public static ArrayList<Feedback> feedbackList = new ArrayList<>();
    private ListView feedbackListView;

    private ProgressBar progressBar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        requestQueue = Volley.newRequestQueue(this);
        progressBar = (ProgressBar) findViewById(R.id.feedbackProgressBar);

        refreshListView();
        loadListeners();
    }

    private void loadListeners(){
        ImageButton logout = (ImageButton) findViewById(R.id.ibFeedbackLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ImageButton refresh = (ImageButton) findViewById(R.id.ibFeedbackRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshListView();
            }
        });
    }

    private void refreshListView() {
        feedbackList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = request("http://mobileordering-gnjb.rhcloud.com/feedback.php");
        requestQueue.add(stringRequest);
    }

    private void updateOverallRating(){
        float totalRating = 0f;

        for(Feedback feedback : feedbackList){
            totalRating += (float) feedback.getRating();
        }

        float average = totalRating / (float) feedbackList.size();
        RatingBar overallRatingBar = (RatingBar) findViewById(R.id.overallRatingBar);
        overallRatingBar.setRating(average);

        TextView overallRatingLabel = (TextView) findViewById(R.id.tvOverallRating);
        overallRatingLabel.setText("Overall Rating: " + String.format("%.1f", average));
    }

    private StringRequest request(String uri) {
        return new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        feedbackList = JSONParser.parseFeedFeedback(response);
                        feedbackListView = (ListView) findViewById(R.id.lvFeedback);
                        feedbackListView.setAdapter(new FeedbackAdapter(FeedbackActivity.this));
                        updateOverallRating();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
    }

    private class FeedbackAdapter extends BaseAdapter {
        private Context context;

        public FeedbackAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return feedbackList.size();
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
                view = layoutManager.inflate(R.layout.custom_view_feedback, parent);
            } else {
                view = convertView;
            }

            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.feedbackRatingBar);
            TextView nameTextView = (TextView) view.findViewById(R.id.tvFeedbackBy);
            TextView messageTextView = (TextView) view.findViewById(R.id.tvFeedbackMessage);

            final Feedback feedback = feedbackList.get(position);
            ratingBar.setProgress(feedback.getRating());

            String name = feedback.getName();
            nameTextView.setText(name != null && !name.isEmpty() ? name : "Anonymous");
            messageTextView.setText(feedback.getMessage());
            return view;
        }
    }

}
