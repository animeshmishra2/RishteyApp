package com.guruvardaan.ghargharsurvey.fragments;

import static com.guruvardaan.ghargharsurvey.config.Config.TODAY_BUSINESS;

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
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.TodayBusinessModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TeamBusinessFragment extends Fragment {

    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<TodayBusinessModel> todayBusinessModels;
    TeamAdapter adapter;
    LinearLayout no_transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_self_business, container, false);
        userDetails = new UserDetails(getActivity());
        progress = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        no_transaction = (LinearLayout) view.findViewById(R.id.no_transaction);
        no_transaction.setVisibility(View.GONE);
        todayBusinessModels = new ArrayList<>();
        adapter = new TeamAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getSelfTeam();
        return view;
    }

    class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.VisitHolder> {

        @Override
        public int getItemCount() {
            return todayBusinessModels.size();

        }

        @Override
        public void onBindViewHolder(VisitHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.user_name.setText(todayBusinessModels.get(position).getCustomerFirstName());
            holder.plot_name.setText(todayBusinessModels.get(position).getPlotNo());
            holder.user_mobile.setText(todayBusinessModels.get(position).getMobile1());
            holder.transaction_amount.setText("\u20b9 " + todayBusinessModels.get(position).getTransaction_amount());
            holder.tran_date.setText(todayBusinessModels.get(position).getTransaction_date());
            holder.tran_mode.setText(todayBusinessModels.get(position).getTransaction_mode());
        }

        @Override
        public VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.today_business_row, viewGroup, false);
            VisitHolder listHolder = new VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {
            TextView user_name, user_mobile, plot_name, transaction_amount, tran_date, tran_mode;
            LinearLayout main_lay;

            public VisitHolder(View view) {
                super(view);
                main_lay = (LinearLayout) view.findViewById(R.id.main_lay);
                user_name = (TextView) view.findViewById(R.id.user_name);
                plot_name = (TextView) view.findViewById(R.id.plot_name);
                user_mobile = (TextView) view.findViewById(R.id.user_mobile);
                transaction_amount = (TextView) view.findViewById(R.id.transaction_amount);
                tran_date = (TextView) view.findViewById(R.id.tran_date);
                tran_mode = (TextView) view.findViewById(R.id.tran_mode);
            }
        }
    }

    private void getSelfTeam() {
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = TODAY_BUSINESS + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid() + "/" + getCurrentDate() + "/" + getCurrentDate() + "/2";
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        System.out.println("Animesh " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            todayBusinessModels = new ArrayList<>();
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                todayBusinessModels.add(new TodayBusinessModel(object.getString("fk_acc_adm_advisor_id"), object.getString("transaction_date"), object.getString("cid"), object.getString("customerFirstName"), object.getString("fk_acc_cor_service_id"), object.getString("mobile1"), object.getString("plotNo"), object.getString("transaction_mode"), object.getString("transaction_amount")));
                            }
                            adapter.notifyDataSetChanged();
                            if (todayBusinessModels.size() > 0) {
                                no_transaction.setVisibility(View.GONE);
                            } else {
                                no_transaction.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {

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

    public String getCurrentDate() {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy");
        String newDateStr = postFormater.format(new Date());
        return newDateStr;
    }

}