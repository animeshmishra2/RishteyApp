package com.guruvardaan.ghargharsurvey.fragments;

import static com.guruvardaan.ghargharsurvey.config.Config.TEAM_CUST_URL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.agent.AgentBusinessActivity;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.TeamPlotModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TeamPlotFragment extends Fragment {

    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<TeamPlotModel> teamPlotModels;
    TeamAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_team, container, false);
        userDetails = new UserDetails(getActivity());
        progress = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        teamPlotModels = new ArrayList<>();
        adapter = new TeamAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getTeamPlot();
        return view;
    }

    class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.VisitHolder> {

        @Override
        public int getItemCount() {
            return teamPlotModels.size();

        }

        @Override
        public void onBindViewHolder(VisitHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.user_name.setText(teamPlotModels.get(position).getAdvisor_name());
            holder.total_sale_area.setText(teamPlotModels.get(position).getTotalSaleArea() + " sq ft");
            holder.user_rank.setText("\u20b9 " + teamPlotModels.get(position).getcPending());
            holder.plot_amount.setText("\u20b9 " + String.format("%.2f", getWholeNumber(teamPlotModels.get(position).getPlotAmount())));
            holder.sale.setText("\u20b9 " + teamPlotModels.get(position).getPaid());
            holder.last_month_business.setText("\u20b9 " + teamPlotModels.get(position).getLastMonthBusiness());
            if (teamPlotModels.get(position).getAlternate_no().equalsIgnoreCase("null") || teamPlotModels.get(position).getAlternate_no().equalsIgnoreCase("")) {
                holder.customer_mobile.setText(teamPlotModels.get(position).getMobile_no());
            } else {
                holder.customer_mobile.setText(teamPlotModels.get(position).getMobile_no() + " , " + teamPlotModels.get(position).getAlternate_no());
            }
            holder.main_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AgentBusinessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",teamPlotModels.get(position).getFk_acc_adm_advisor_id());
                    bundle.putString("NAME", teamPlotModels.get(position).getAdvisor_name());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }

        @Override
        public VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.team_plot_row, viewGroup, false);
            VisitHolder listHolder = new VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {
            TextView user_name, total_sale_area, user_rank, plot_amount, sale, last_month_business, customer_mobile;
            LinearLayout main_lay;

            public VisitHolder(View view) {
                super(view);
                main_lay = (LinearLayout) view.findViewById(R.id.main_lay);
                user_name = (TextView) view.findViewById(R.id.user_name);
                total_sale_area = (TextView) view.findViewById(R.id.total_sale_area);
                user_rank = (TextView) view.findViewById(R.id.user_rank);
                plot_amount = (TextView) view.findViewById(R.id.plot_amount);
                sale = (TextView) view.findViewById(R.id.sale);
                last_month_business = (TextView) view.findViewById(R.id.last_month_business);
                customer_mobile = (TextView) view.findViewById(R.id.customer_mobile);
            }
        }
    }

    private void getTeamPlot() {
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = TEAM_CUST_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid() + "/" + getCurrentDate() + "/" + getMonthDate(1) + "/" + getMonthDate(2);
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
                            teamPlotModels = new ArrayList<>();
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                teamPlotModels.add(new TeamPlotModel(object.getString("fk_acc_adm_advisor_id"), object.getString("advisor_name"), object.getString("mobile_no"), object.getString("alternate_no"), object.getString("advisor_rank"), object.getString("totalSaleArea"), object.getString("plotAmount"), object.getString("lastMonthBusiness"), object.getString("total"), object.getString("paid"), object.getString("cPending")));
                            }
                            adapter.notifyDataSetChanged();
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public String getMonthDate(int s) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        if (s == 1) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
        } else {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        return format.format(cal.getTime());
    }

    public String getCurrentDate() {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy");
        String newDateStr = postFormater.format(new Date());
        return newDateStr;
    }

    public Double getWholeNumber(String s) {
        try {
            Double d = Double.parseDouble(s);
            return d;
        } catch (Exception e) {
            return 0.0;
        }
    }
}