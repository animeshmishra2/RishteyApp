package com.guruvardaan.ghargharsurvey.fragments;

import static com.guruvardaan.ghargharsurvey.config.Config.CUSTOMER_PASSBOOK_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.guruvardaan.ghargharsurvey.model.CustPassModel;
import com.guruvardaan.ghargharsurvey.model.PaymentModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerPayment extends Fragment {

    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<PaymentModel> paymentModels;
    CreditAdapter adapter;

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
        paymentModels = new ArrayList<>();
        adapter = new CreditAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getReceipt();
        return view;
    }

    class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.VisitHolder> {

        @Override
        public int getItemCount() {
            return paymentModels.size();

        }

        @Override
        public void onBindViewHolder(VisitHolder holder, final int position) {
            holder.types.setText("Payment Done");
            holder.dates.setText("Payment Date: " + paymentModels.get(position).getTransaction_date());
            holder.money.setText("\u20b9 " + paymentModels.get(position).getTransaction_amount());
            if (getPayData(paymentModels.get(position).getOfferAmount()) > 0) {
                holder.message.setText("Customer paid amount by " + paymentModels.get(position).getTransaction_mode() + ". And " + paymentModels.get(position).getOfferAmount() + " offer was applied.");
            } else {
                holder.message.setText("Customer paid amount by " + paymentModels.get(position).getTransaction_mode() + ". And any offer was not applied.");
            }
        }

        @Override
        public VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.transaction_row, viewGroup, false);
            VisitHolder listHolder = new VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {
            TextView types, money, dates, message;

            public VisitHolder(View view) {
                super(view);
                types = (TextView) view.findViewById(R.id.types);
                money = (TextView) view.findViewById(R.id.money);
                dates = (TextView) view.findViewById(R.id.dates);
                message = (TextView) view.findViewById(R.id.message);
            }
        }
    }

    private void getReceipt() {
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = CUSTOMER_PASSBOOK_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + getActivity().getIntent().getExtras().getString("CID") + "/" + getActivity().getIntent().getExtras().getString("SID") + "/" + getActivity().getIntent().getExtras().getString("DATE");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(URL_CHECK);
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table1");
                            paymentModels = new ArrayList<>();
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                paymentModels.add(new PaymentModel(object.getString("transaction_date"), object.getString("fk_cnf_branch_id"), object.getString("transaction_amount"), object.getString("transaction_mode"), object.getString("offerTaken"), object.getString("offerAmount")));
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

    public float getPayData(String ff) {
        try {
            float f = Float.parseFloat(ff);
            return f;
        } catch (Exception e) {
            return 0.0f;
        }

    }

}