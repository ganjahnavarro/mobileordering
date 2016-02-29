package com.mobile.mobileordering;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        calendar = Calendar.getInstance();

        displayDate();

        ImageButton logout = (ImageButton) findViewById(R.id.ibReportLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        Button bReportDaily = (Button) findViewById(R.id.bReportDaily);
        bReportDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = String.valueOf(calendar.get(Calendar.YEAR)) + "-" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-" + String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));

                StringRequest stringRequest = request("http://mobileordering-gnjb.rhcloud.com/sendreport.php", "DAILY", day);
                RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
                stringRequest.setShouldCache(false);
                requestQueue.add(stringRequest);
            }
        });

        Button bReportMonthly = (Button) findViewById(R.id.bReportMonthly);
        bReportMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = String.valueOf(calendar.get(Calendar.MONTH)+1);

                StringRequest stringRequest = request("http://mobileordering-gnjb.rhcloud.com/sendreport.php", "MONTHLY", month);
                RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
                stringRequest.setShouldCache(false);
                requestQueue.add(stringRequest);
            }
        });

        Button bReportYearly = (Button) findViewById(R.id.bReportYearly);
        bReportYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = String.valueOf(calendar.get(Calendar.YEAR));

                StringRequest stringRequest = request("http://mobileordering-gnjb.rhcloud.com/sendreport.php", "YEARLY", year);
                RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
                stringRequest.setShouldCache(false);
                requestQueue.add(stringRequest);
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    private StringRequest request(String uri, final String type, final String param) {
        return new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ReportActivity.this, "Report successfully sent to printer.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("type", type);
                params.put("param", param);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        displayDate();
    }

    public void displayDate() {
        TextView tvReportCDate = (TextView) findViewById(R.id.tvReportCDate);
        String day = String.valueOf(calendar.get(Calendar.YEAR)) + "-" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-" + String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));
        tvReportCDate.setText(day);
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (ReportActivity) getActivity(), year, month, day);
        }
    }

}
