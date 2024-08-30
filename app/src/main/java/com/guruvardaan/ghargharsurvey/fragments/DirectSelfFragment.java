package com.guruvardaan.ghargharsurvey.fragments;

import static com.guruvardaan.ghargharsurvey.config.Config.SELF_AGENT_URL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.guruvardaan.ghargharsurvey.model.DirectTeamModel;
import com.guruvardaan.ghargharsurvey.welcome.SelectBankActvity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DirectSelfFragment extends Fragment {
    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<DirectTeamModel> directTeamModels;
    ArrayList<DirectTeamModel> newTeamModels;
    EditText search_edit;
    LinearLayout no_transaction;
    TeamAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_direct_self, container, false);
        userDetails = new UserDetails(getActivity());
        no_transaction = (LinearLayout) view.findViewById(R.id.no_transaction);
        search_edit = (EditText) view.findViewById(R.id.search_edit);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        directTeamModels = new ArrayList<>();
        newTeamModels = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getSelfTeam();
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (search_edit.getText().toString().trim().length() > 0) {
                    newTeamModels.clear();
                    for (int i = 0; i < directTeamModels.size(); i++) {
                        if (directTeamModels.get(i).getAgentName().toLowerCase().contains(search_edit.getText().toString().toLowerCase())) {
                            newTeamModels.add(directTeamModels.get(i));
                        }
                    }
                    recyclerView.setAdapter(new TeamAdapter(newTeamModels));
                } else {
                    recyclerView.setAdapter(new TeamAdapter(directTeamModels));
                }
            }
        });
        return view;
    }

    private void getSelfTeam() {
        no_transaction.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = SELF_AGENT_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid() + "/4";
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
                            directTeamModels = new ArrayList<>();
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                directTeamModels.add(new DirectTeamModel(object.getString("agentId"), object.getString("agentName"), object.getString("agentMobile"), object.getString("agentRank"), object.getString("agentPassword"), object.getString("int_agentId"), object.getString("int_agentName"), object.getString("int_agentMobile"), object.getString("int_agentRank"),""));
                                newTeamModels.add(new DirectTeamModel(object.getString("agentId"), object.getString("agentName"), object.getString("agentMobile"), object.getString("agentRank"), object.getString("agentPassword"), object.getString("int_agentId"), object.getString("int_agentName"), object.getString("int_agentMobile"), object.getString("int_agentRank"),""));
                            }
                            adapter = new TeamAdapter(directTeamModels);
                            recyclerView.setAdapter(adapter);
                            if (directTeamModels.size() <= 0) {
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
                        no_transaction.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.VisitHolder> {

        ArrayList<DirectTeamModel> teamModels;

        TeamAdapter(ArrayList<DirectTeamModel> teamModels) {
            this.teamModels = teamModels;
        }

        @Override
        public int getItemCount() {
            return teamModels.size();
        }

        @Override
        public void onBindViewHolder(TeamAdapter.VisitHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.user_name.setText(teamModels.get(position).getAgentName());
            holder.user_mobile.setText(teamModels.get(position).getAgentMobile());
            holder.user_rank.setText(teamModels.get(position).getAgentRank());
            holder.introducer_name.setText(teamModels.get(position).getInt_agentName());
            holder.introducer_mobile.setText(teamModels.get(position).getInt_agentMobile());
            holder.introducer_rank.setText("**");
            holder.agent_id.setText(teamModels.get(position).getAgentId());
            holder.share_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareApp("Agent Request has been sent to admin. It will take 24-48 hours to respond your request.You can login with these details.\nName-" + directTeamModels.get(position).getAgentName() + "\nMobile - " + directTeamModels.get(position).getAgentMobile() + "\nPassword- " + directTeamModels.get(position).getAgentPassword() + "\nAgent Id- " + directTeamModels.get(position).getAgentId());
                }
            });
        }

        @Override
        public TeamAdapter.VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.team_row_1, viewGroup, false);
            TeamAdapter.VisitHolder listHolder = new TeamAdapter.VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {
            TextView user_name, user_mobile, user_rank, introducer_name, introducer_mobile, introducer_rank, agent_id, share_login;

            public VisitHolder(View view) {
                super(view);
                agent_id = (TextView) view.findViewById(R.id.agent_id);
                user_name = (TextView) view.findViewById(R.id.user_name);
                share_login = (TextView) view.findViewById(R.id.share_login);
                user_mobile = (TextView) view.findViewById(R.id.user_mobile);
                user_rank = (TextView) view.findViewById(R.id.user_rank);
                introducer_name = (TextView) view.findViewById(R.id.introducer_name);
                introducer_mobile = (TextView) view.findViewById(R.id.introducer_mobile);
                introducer_rank = (TextView) view.findViewById(R.id.introducer_rank);
            }
        }
    }

    public void shareApp(String msg) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Rishtey Township Agent");
            String shareMessage = msg + "\n\n";
            shareMessage = shareMessage + "\n" + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share Via..."));
        } catch (Exception e) {
            //e.toString();
        }
    }

}