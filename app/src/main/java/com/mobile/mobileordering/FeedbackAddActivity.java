package com.mobile.mobileordering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FeedbackAddActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText nameEditText;
    private EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_add);
        loadListeners();
    }

    private void loadListeners(){
        final ImageButton logout = (ImageButton) findViewById(R.id.ibFeedbackLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackAddActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ratingBar = (RatingBar) findViewById(R.id.feedbackRatingBar);
        nameEditText = (EditText) findViewById(R.id.feedbackName);
        messageEditText = (EditText) findViewById(R.id.feedbackMessage);

        Button submitButton = (Button) findViewById(R.id.bSubmitFeedback);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = (int) ratingBar.getRating();
                String message = messageEditText.getText().toString();
                String name = nameEditText.getText().toString();

                if(message != null && !message.isEmpty()){
                    RequestQueue requestQueue = Volley.newRequestQueue(FeedbackAddActivity.this);
                    StringRequest request = postRequest(rating, message, name);
                    requestQueue.add(request);
                } else {
                    Toast.makeText(FeedbackAddActivity.this, "Message is required.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private StringRequest postRequest(final int rating, final String message, final String name) {
        return new StringRequest(Request.Method.POST, "http://mobileordering-gnjb.rhcloud.com/sendfeedback.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(FeedbackAddActivity.this,
                                "Feedback submitted. Thank you.",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(FeedbackAddActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FeedbackAddActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rating", String.valueOf(rating));
                params.put("name", name != null ? name : "");
                params.put("message", message);
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
