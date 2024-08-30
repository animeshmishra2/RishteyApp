package com.guruvardaan.ghargharsurvey.fragments;

import static com.guruvardaan.ghargharsurvey.config.Config.CUSTOMER_PASSBOOK_URL;

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
import com.guruvardaan.ghargharsurvey.welcome.CustomerPassActivity;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.CustPassModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptFragment extends Fragment {

    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<CustPassModel> custPassModels;
    CreditAdapter adapter;
    LinearLayout no_lay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
        userDetails = new UserDetails(getActivity());
        no_lay = (LinearLayout) view.findViewById(R.id.no_lay);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        custPassModels = new ArrayList<>();
        adapter = new CreditAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getReceipt();
        return view;
    }

    class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.VisitHolder> {

        @Override
        public int getItemCount() {
            return custPassModels.size();

        }

        @Override
        public void onBindViewHolder(VisitHolder holder, final int position) {
            holder.types.setText("Receipt");
            holder.dates.setText("Receipt Date: " + custPassModels.get(position).getDue_date());
            holder.money.setText("\u20b9 " + custPassModels.get(position).getInstallment_amount());
            holder.message.setText("Receipt has been generated for this payment by admin.");
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
        no_lay.setVisibility(View.GONE);
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
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            custPassModels = new ArrayList<>();
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                custPassModels.add(new CustPassModel(object.getString("installment_no"), object.getString("installment_amount"), object.getString("due_date"), object.getString("paid_amount"), object.getString("paid_date"), object.getString("status")));
                            }
                            if (custPassModels.size() <= 0) {
                                no_lay.setVisibility(View.VISIBLE);
                            }
                            JSONArray Table2 = jsonObject.getJSONArray("Table2");
                            if (Table2.length() > 0) {
                                JSONObject object = Table2.getJSONObject(0);
                                CustomerPassActivity.user_name.setText(object.getString("customerFirstName"));
                                CustomerPassActivity.plot_name.setText(object.getString("plotName"));
                            }
                            JSONArray Table3 = jsonObject.getJSONArray("Table3");
                            if (Table3.length() > 0) {
                                JSONObject object = Table3.getJSONObject(0);
                                CustomerPassActivity.total_price.setText("\u20b9" + object.getString("total"));
                                CustomerPassActivity.paid.setText("\u20b9" + object.getString("paid"));
                                CustomerPassActivity.pending.setText("\u20b9" + object.getString("cPending"));
                                CustomerPassActivity.remaining.setText("\u20b9" + object.getString("remaining"));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            no_lay.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        no_lay.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}