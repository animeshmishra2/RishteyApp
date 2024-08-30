package com.guruvardaan.ghargharsurvey.customers;

import static com.guruvardaan.ghargharsurvey.config.Config.CUSTOMER_PLOT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.PlotCustomerModel;
import com.guruvardaan.ghargharsurvey.plots.BlockActivity;
import com.guruvardaan.ghargharsurvey.plots.OurPropertiesActivity;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerPlotsActivity extends BaseActivity {

    ImageView back_button;
    RecyclerView plot_recycler;
    ProgressBar progress;
    LinearLayout no_lay;
    UserDetails userDetails;
    ArrayList<PlotCustomerModel> plotCustomerModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_customer_plots, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        plot_recycler = (RecyclerView) findViewById(R.id.plot_recycler);
        plot_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        progress = (ProgressBar) findViewById(R.id.progress);
        plotCustomerModels = new ArrayList<>();
        no_lay = (LinearLayout) findViewById(R.id.no_lay);
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllPlots();
    }

    private void getAllPlots() {
        progress.setVisibility(View.VISIBLE);
        no_lay.setVisibility(View.GONE);
        plot_recycler.setVisibility(View.GONE);
        String URL_CHECK = CUSTOMER_PLOT + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid();
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        System.out.println(response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                plotCustomerModels.add(new PlotCustomerModel(object.getString("name"), object.getString("plotName"), object.getString("plotRate"), object.getString("sqft"), object.getString("service_duration"), object.getString("mobile1"), object.getString("total"), object.getString("paid"), object.getString("cPending"), object.getString("lastAmount"), object.getString("dt"), object.getString("registeryStatus"), object.getString("remaining")));
                            }
                            if (Table.length() > 0) {
                                no_lay.setVisibility(View.GONE);
                                plot_recycler.setVisibility(View.VISIBLE);
                            } else {
                                no_lay.setVisibility(View.VISIBLE);
                                plot_recycler.setVisibility(View.GONE);
                            }
                            plot_recycler.setAdapter(new MyPlotAdapter());

                        } catch (Exception e) {
                            e.printStackTrace();
                            no_lay.setVisibility(View.VISIBLE);
                            plot_recycler.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        no_lay.setVisibility(View.VISIBLE);
                        plot_recycler.setVisibility(View.GONE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    class MyPlotAdapter extends RecyclerView.Adapter<MyPlotAdapter.PropertyHolder> {

        @Override
        public int getItemCount() {
            return plotCustomerModels.size();
        }

        @Override
        public void onBindViewHolder(MyPlotAdapter.PropertyHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.plot_num.setText(plotCustomerModels.get(position).getPlotName().trim());
            holder.plot_price.setText("\u20b9" + plotCustomerModels.get(position).getTotal());
            holder.owner_name.setText(plotCustomerModels.get(position).getName().trim());
            holder.total_area.setText(plotCustomerModels.get(position).getSqft() + " Sq ft");
            holder.sqprice.setText("\u20b9" + plotCustomerModels.get(position).getPlotRate() + "/ Sq ft");
            holder.emi_month.setText(plotCustomerModels.get(position).getService_duration() + " Months");
            holder.paid_amount.setText("\u20b9" + plotCustomerModels.get(position).getPaid());
            holder.pending_amount.setText("\u20b9" + plotCustomerModels.get(position).getcPending());
            holder.last_amount.setText("\u20b9" + plotCustomerModels.get(position).getLastAmount() + " on " + plotCustomerModels.get(position).getDt());
            holder.remaining.setText("\u20b9" + plotCustomerModels.get(position).getRemaining());
            if (plotCustomerModels.get(position).getRegisteryStatus().equalsIgnoreCase("0")) {
                holder.registry.setText("No");
            } else {
                holder.registry.setText("Done");
            }

        }

        @Override
        public MyPlotAdapter.PropertyHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.cust_prop_row, viewGroup, false);
            MyPlotAdapter.PropertyHolder listHolder = new MyPlotAdapter.PropertyHolder(mainGroup);
            return listHolder;
        }

        public class PropertyHolder extends RecyclerView.ViewHolder {

            TextView plot_num, plot_price, owner_name, total_area, sqprice, emi_month, paid_amount, pending_amount, last_amount, remaining, registry;

            public PropertyHolder(View view) {
                super(view);
                plot_num = (TextView) view.findViewById(R.id.plot_num);
                plot_price = (TextView) view.findViewById(R.id.plot_price);
                owner_name = (TextView) view.findViewById(R.id.owner_name);
                total_area = (TextView) view.findViewById(R.id.total_area);
                sqprice = (TextView) view.findViewById(R.id.sqprice);
                emi_month = (TextView) view.findViewById(R.id.emi_month);
                paid_amount = (TextView) view.findViewById(R.id.paid_amount);
                pending_amount = (TextView) view.findViewById(R.id.pending_amount);
                last_amount = (TextView) view.findViewById(R.id.last_amount);
                remaining = (TextView) view.findViewById(R.id.remaining);
                registry = (TextView) view.findViewById(R.id.registry);
            }
        }
    }


}