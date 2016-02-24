package com.mobile.mobileordering;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobile.mobileordering.util.OrderJSONParser;
import com.mobile.mobileordering.util.SalesManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    public static ArrayList<SalesManager> simple_2312312das = new ArrayList<>();
    private static String dateNow;

    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        c = Calendar.getInstance();

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
                StringRequest stringRequest = request("http://opres.heliohost.org/order/getsalesdaily", "" + String.valueOf(c.get(Calendar.YEAR)) + "-" + String.valueOf(c.get(Calendar.MONTH) + 1) + "-" + String.valueOf(c.get(Calendar.DAY_OF_YEAR) + ""));

                RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        Button bReportMonthly = (Button) findViewById(R.id.bReportMonthly);
        bReportMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = String.valueOf(c.get(Calendar.MONTH)+1);
                if (month.length() < 2)
                    month = "0" + month;

                StringRequest stringRequest = request("http://opres.heliohost.org/order/getsalesmonthly", month);
                RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        Button bReportYearly = (Button) findViewById(R.id.bReportYearly);
        bReportYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = String.valueOf(c.get(Calendar.YEAR));

                StringRequest stringRequest = request("http://opres.heliohost.org/order/getsalesyearly", year);

                RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
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

    private void printDialog() {


        Intent printIntent = new Intent(ReportActivity.this, PrintDialogActivity.class);
        File tempReceipt;

        String title = "Sales Report for Papi's Grill and Restaurant";

        int leftSpace = 60 - title.length();

        int centerSpace = (int) ((Math.floor(leftSpace/2)) + title.length());

        try {
            String tempName = "sales.txt";
            tempReceipt = File.createTempFile("sales", "txt");

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempReceipt));
            bufferedWriter.write(field_sdsadada(title, centerSpace, false));


            bufferedWriter.newLine();
            bufferedWriter.newLine();

            int nameWidth = 40;
            int qtyWidth = 5;
            int priceWidth = 15;
            int grossTotal = 0;

            bufferedWriter.write(field_sdsadada("Name", nameWidth, true));
            bufferedWriter.write(field_sdsadada("Qty", qtyWidth, false));
            bufferedWriter.write(field_sdsadada("Sub", priceWidth, false));
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            for (SalesManager salesManager : simple_2312312das) {
                String name = salesManager.getName();
                int qty = salesManager.getQty();
                int price = salesManager.getPrice();

                int total = qty * price;

                grossTotal += total;

                bufferedWriter.write(field_sdsadada(name, nameWidth, true));
                bufferedWriter.write(field_sdsadada(qty, qtyWidth, false));
                bufferedWriter.write(field_sdsadada("Php " + String.valueOf(total) + ".00", priceWidth, false));
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();
            bufferedWriter.write(field_sdsadada("Total:", 10, true));
            bufferedWriter.write(field_sdsadada("Php " + String.valueOf(grossTotal) + ".00", 50, false));

            bufferedWriter.close();

            printIntent.setDataAndType(Uri.fromFile(tempReceipt), getMimeType(tempName));
            printIntent.putExtra("title", tempName);
            startActivity(printIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public String field_sdsadada(String s, int size, boolean left) {
        String temp;

        if (left) {
            temp = String.format("%1$-" + size + "s", s);
        } else {
            temp = String.format("%1$" + size + "s", s);
        }

        return temp.substring(0, size);
    }

    public String field_sdsadada(int s, int size, boolean left) {
        String formatInt = String.valueOf(s);
        String temp;
        if (left) {
            temp = String.format("%1$-" + size + "s", formatInt);
        } else {
            temp = String.format("%1$" + size + "s", formatInt);
        }
        return temp.substring(0, size);
    }

    private StringRequest request(String uri, final String param) {
        return new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        simple_2312312das = OrderJSONParser.parseFeedSales(response);
                        printDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("param", param);

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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        c.set(year, monthOfYear, dayOfMonth);
        displayDate();
    }

    public void displayDate() {
        TextView tvReportCDate = (TextView) findViewById(R.id.tvReportCDate);
        tvReportCDate.setText(String.valueOf(c.get(Calendar.MONTH)) + "-" + String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + "-" + String.valueOf(c.get(Calendar.YEAR)));
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (ReportActivity) getActivity(), year, month, day);

            // Create a newlogo instance of DatePickerDialog and return it
            return datePickerDialog;
        }
    }

}
