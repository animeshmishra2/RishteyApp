package com.guruvardaan.ghargharsurvey.agent;

import static com.guruvardaan.ghargharsurvey.config.Config.SELF_CUST_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.TODAY_BUSINESS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.fragments.SelfPlotFragment;
import com.guruvardaan.ghargharsurvey.model.PlotCustomer;
import com.guruvardaan.ghargharsurvey.model.TodayBusinessModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AgentBusinessActivity extends BaseActivity {

    TextView header_txt;
    ImageView back_button;
    RecyclerView recyclerView;
    LinearLayout no_transaction;
    ProgressBar progress;
    ArrayList<PlotCustomer> plotCustomers;
    TeamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_agent_business, frameLayout);
        progress = (ProgressBar) findViewById(R.id.progress);
        header_txt = (TextView) findViewById(R.id.header_txt);
        header_txt.setText(getIntent().getExtras().getString("NAME")+"'s Business");
        back_button = (ImageView) findViewById(R.id.back_button);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        no_transaction = (LinearLayout) findViewById(R.id.no_transaction);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        plotCustomers = new ArrayList<>();
        adapter = new TeamAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getSelfTeam();
    }

    private void getSelfTeam() {
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        no_transaction.setVisibility(View.GONE);
        String URL_CHECK = SELF_CUST_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + getIntent().getExtras().getString("ID");
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
                            plotCustomers = new ArrayList<>();
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                plotCustomers.add(new PlotCustomer(object.getString("name"), object.getString("plotName"), object.getString("plotRate"), object.getString("sqft"), object.getString("service_duration"), object.getString("mobile1"), object.getString("total"), object.getString("paid"), object.getString("cPending"), object.getString("lastAmount"), object.getString("dt"), object.getString("registeryStatus"), object.getString("remaining")));
                            }
                            adapter.notifyDataSetChanged();
                            if(plotCustomers.size()<=0)
                            {
                                no_transaction.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            no_transaction.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        no_transaction.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.VisitHolder> {

        @Override
        public int getItemCount() {
            return plotCustomers.size();

        }

        @Override
        public void onBindViewHolder(TeamAdapter.VisitHolder holder, final int position) {
            holder.user_name.setText(plotCustomers.get(position).getName());
            holder.plot_name.setText(plotCustomers.get(position).getPlotName());
            holder.plot_area.setText(plotCustomers.get(position).getSqft() + " sq. ft");
            holder.plot_rate.setText(plotCustomers.get(position).getPlotRate() + " / sq. ft");
            holder.duration.setText(plotCustomers.get(position).getService_duration() + " Months");
            holder.total_amount.setText("\u20b9 " + plotCustomers.get(position).getTotal());
            holder.total_paid.setText("\u20b9 " + plotCustomers.get(position).getPaid());
            holder.customer_mobile.setText(plotCustomers.get(position).getMobile1());
            holder.last_paid.setText("\u20b9 " + plotCustomers.get(position).getLastAmount());
            holder.last_date.setText(plotCustomers.get(position).getDt());
            holder.pending_amount.setText("\u20b9 " + plotCustomers.get(position).getcPending());
        }

        @Override
        public TeamAdapter.VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.plot_cust_row, viewGroup, false);
            TeamAdapter.VisitHolder listHolder = new TeamAdapter.VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {
            TextView user_name, plot_name, plot_area, plot_rate, duration, total_amount, total_paid, customer_mobile, last_paid, last_date, pending_amount;

            public VisitHolder(View view) {
                super(view);
                user_name = (TextView) view.findViewById(R.id.user_name);
                plot_name = (TextView) view.findViewById(R.id.plot_name);
                plot_area = (TextView) view.findViewById(R.id.plot_area);
                plot_rate = (TextView) view.findViewById(R.id.plot_rate);
                duration = (TextView) view.findViewById(R.id.duration);
                total_amount = (TextView) view.findViewById(R.id.total_amount);
                total_paid = (TextView) view.findViewById(R.id.total_paid);
                customer_mobile = (TextView) view.findViewById(R.id.customer_mobile);
                last_paid = (TextView) view.findViewById(R.id.last_paid);
                last_date = (TextView) view.findViewById(R.id.last_date);
                pending_amount = (TextView) view.findViewById(R.id.pending_amount);
            }
        }
    }

    public String getCurrentDate() {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy");
        String newDateStr = postFormater.format(new Date());
        return newDateStr;
    }

}