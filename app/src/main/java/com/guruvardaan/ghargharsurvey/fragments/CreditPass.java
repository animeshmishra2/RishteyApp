package com.guruvardaan.ghargharsurvey.fragments;

import static com.guruvardaan.ghargharsurvey.config.Config.AGENT_PASSBOOK_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.SELF_AGENT_URL;

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
import com.guruvardaan.ghargharsurvey.model.AgentModel;
import com.guruvardaan.ghargharsurvey.model.PassbookModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreditPass extends Fragment {

    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<PassbookModel> passbookModels;
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
        passbookModels = new ArrayList<>();
        adapter = new CreditAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getCreditPassbook();
        return view;
    }

    class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.VisitHolder> {

        @Override
        public int getItemCount() {
            return passbookModels.size();

        }

        @Override
        public void onBindViewHolder(VisitHolder holder, final int position) {
            holder.types.setText(passbookModels.get(position).getCreditType());
            holder.dates.setText(passbookModels.get(position).getDt());
            holder.money.setText("\u20b9 " + passbookModels.get(position).getAmount());
            holder.message.setText(passbookModels.get(position).getDetails());
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

    private void getCreditPassbook() {
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = AGENT_PASSBOOK_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid() + "/" + getCurrentDate() + "/" + getActivity().getIntent().getExtras().getString("TYPE") + "/" + getActivity().getIntent().getExtras().getString("SD") + "/" + getActivity().getIntent().getExtras().getString("ED");
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
                            passbookModels = new ArrayList<>();
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                passbookModels.add(new PassbookModel(object.getString("id"), object.getString("dt"), object.getString("amount"), object.getString("details"), object.getString("creditType"), object.getString("pay")));
                            }
                            adapter.notifyDataSetChanged();
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