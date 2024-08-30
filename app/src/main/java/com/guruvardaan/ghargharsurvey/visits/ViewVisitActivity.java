package com.guruvardaan.ghargharsurvey.visits;


import static com.guruvardaan.ghargharsurvey.config.Config.VISIT_STATUS_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.AgentModel;
import com.guruvardaan.ghargharsurvey.model.CustomerModel;
import com.guruvardaan.ghargharsurvey.model.PlotRequestModel;
import com.guruvardaan.ghargharsurvey.utils.ApiClient;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewVisitActivity extends BaseActivity {

    TextView source_txt, dest_txt, visit_date, status_txt, distance_tr, total_txt, distance_travelled, expense_txt, request_date, token_amount;
    ImageView status_img;
    RecyclerView recyclerView;
    ScrollView scroll_l;
    ProgressBar progress;
    UserDetails userDetails;
    ArrayList<PlotRequestModel> plotRequestModels;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_view_visit, frameLayout);
        plotRequestModels = new ArrayList<>();
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progress = (ProgressBar) findViewById(R.id.progress);
        scroll_l = (ScrollView) findViewById(R.id.scroll_l);
        status_img = (ImageView) findViewById(R.id.status_img);
        source_txt = (TextView) findViewById(R.id.source_txt);
        dest_txt = (TextView) findViewById(R.id.dest_txt);
        visit_date = (TextView) findViewById(R.id.visit_date);
        status_txt = (TextView) findViewById(R.id.status_txt);
        distance_tr = (TextView) findViewById(R.id.distance_tr);
        request_date = (TextView) findViewById(R.id.request_date);
        token_amount = (TextView) findViewById(R.id.token_amount);
        total_txt = (TextView) findViewById(R.id.total_txt);
        distance_travelled = (TextView) findViewById(R.id.distance_travelled);
        expense_txt = (TextView) findViewById(R.id.expense_txt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getThisVisit();
    }

    private void getThisVisit() {
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = VISIT_STATUS_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid() + "/" + getIntent().getExtras().getString("TID");
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                source_txt.setText(object.getString("startingAddress"));
                                visit_date.setText(getForDate(object.getString("dtOfVisit")));
                                status_txt.setText(object.getString("status"));
                                token_amount.setText("\u20b9 " + object.getString("token_amount"));
                                request_date.setText(getRisDate(object.getString("submittedDt")));
                                if (object.getString("status").equalsIgnoreCase("Rejected")) {
                                    status_img.setImageResource(R.drawable.rejected);
                                    status_txt.setTextColor(Color.parseColor("#ff0000"));
                                    distance_travelled.setText("0 Km");
                                    expense_txt.setText("\u20b9 0.00");
                                } else if (object.getString("status").equalsIgnoreCase("Under Process")) {
                                    status_img.setImageResource(R.drawable.pending);
                                    status_txt.setTextColor(Color.parseColor("#FFAF49"));
                                    status_txt.setText("Pending");
                                    if (object.getString("starting_latlong").contains(",")) {
                                        //checkDistance(object.getString("amountPerKm"), getDoubles(object.getString("starting_latlong").split(",")[0]), getDoubles(object.getString("starting_latlong").split(",")[1]), 26.781212, 80.8987);
                                    }
                                   /* distance_travelled.setText(getPrices(object.getString("")) + " Km");
                                    expense_txt.setText("\u20b9 " + object.getString("amount"));*/
                                } else if (object.getString("status").equalsIgnoreCase("Approved")) {
                                    status_img.setImageResource(R.drawable.approved);
                                    status_txt.setTextColor(Color.parseColor("#05c505"));
                                    if (object.getString("starting_latlong").contains(",")) {
                                        //checkDistance(object.getString("amountPerKm"), getDoubles(object.getString("starting_latlong").split(",")[0]), getDoubles(object.getString("starting_latlong").split(",")[1]), 26.781212, 80.8987);
                                    }
                                    //holder.trips.setVisibility(View.VISIBLE);
                                } else if (object.getString("status").equalsIgnoreCase("Visit Started")) {
                                    status_img.setImageResource(R.drawable.approved);
                                    status_txt.setTextColor(Color.parseColor("#05c505"));
                                    status_txt.setText(object.getString("status"));
                                    if (object.getString("starting_latlong").contains(",")) {
                                        //checkDistance(object.getString("amountPerKm"), getDoubles(object.getString("starting_latlong").split(",")[0]), getDoubles(object.getString("starting_latlong").split(",")[1]), 26.781212, 80.8987);
                                    }
                                } else if (object.getString("status").equalsIgnoreCase("Visit Completed")) {
                                    status_img.setImageResource(R.drawable.completed);
                                    status_txt.setText("Completed");
                                    status_txt.setTextColor(Color.parseColor("#FF8C19"));
                                    distance_travelled.setText(object.getString("km") + " Km");
                                    expense_txt.setText("\u20b9 " + object.getString("amount"));
                                }
                            }
                            JSONArray Table1 = jsonObject.getJSONArray("Table1");
                            plotRequestModels = new ArrayList<>();
                            for (int i = 0; i < Table1.length(); i++) {
                                JSONObject object = Table1.getJSONObject(i);
                                plotRequestModels.add(new PlotRequestModel("", "", object.getString("customer_name"), object.getString("customer_mobile"), object.getString("customer_address"), object.getString("professionId"), object.getString("professionName"), object.getString("occupationId"), object.getString("occupationName"), object.getString("status_of_plot_buy"), object.getString("payment_option"), object.getString("plot_size"), object.getString("block"), object.getString("category"), object.getString("emiforyear"), object.getString("visit_date"), object.getString("visit_amount"), object.getString("pan"), object.getString("aadhar_front"), object.getString("aadhar_back"), object.getString("meeting_pic")));
                            }
                            recyclerView.setAdapter(new CustomerAdapter());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public String getForDate(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd MMM, yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

    public String getRisDate(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd MMM, yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

    public float getPrices(String s) {
        try {
            float f = Float.parseFloat(s);
            return f;
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public double getDoubles(String s) {
        try {
            double f = Double.parseDouble(s);
            return f;
        } catch (Exception e) {
            return 0.0d;
        }
    }

/*
    public void checkDistance(final String perkm, final double lat1, final double lon1, final double lat2, final double lon2) {
        if (ApiClient.isNetworkAvailable(ViewVisitActivity.this)) {
            String URL_CHECK = Config.DIS_URL1 + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + Config.DIS_URL2;
            System.out.println(URL_CHECK);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("routes");
                                JSONObject routes = jsonArray.getJSONObject(0);
                                JSONArray legs = routes.getJSONArray("legs");
                                JSONObject legObj = legs.getJSONObject(0);
                                JSONObject distance = legObj.getJSONObject("distance");
                                JSONObject duration = legObj.getJSONObject("duration");
                                distance_tr.setText("Probable Distance");
                                total_txt.setText("Probable Expense");
                                expense_txt.setText("\u20b9 " + (2 * ((getPrices(distance.getString("value")) / 1000) * getPrices(perkm))));
                                distance_travelled.setText((2 * (getPrices(distance.getString("value"))) / 1000) + " Km");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(ViewVisitActivity.this).addToRequestQueue(stringRequest);
        } else {

        }
    }
*/

    class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {

        @Override
        public int getItemCount() {
            return plotRequestModels.size();
        }

        @Override
        public void onBindViewHolder(CustomerAdapter.CustomerHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.name.setText(plotRequestModels.get(position).getCustomer_name());
            holder.mobile.setText(plotRequestModels.get(position).getOccupationName() + " / " + plotRequestModels.get(position).getProfessionName());
        }

        @Override
        public CustomerAdapter.CustomerHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.visit_cust_row, viewGroup, false);
            CustomerAdapter.CustomerHolder listHolder = new CustomerAdapter.CustomerHolder(mainGroup);
            return listHolder;
        }

        public class CustomerHolder extends RecyclerView.ViewHolder {

            TextView name, mobile;

            public CustomerHolder(View view) {
                super(view);
                mobile = (TextView) view.findViewById(R.id.mobile);
                name = (TextView) view.findViewById(R.id.name);
            }
        }
    }




}