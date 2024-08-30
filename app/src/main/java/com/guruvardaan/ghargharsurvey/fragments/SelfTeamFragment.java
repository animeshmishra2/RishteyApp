package com.guruvardaan.ghargharsurvey.fragments;

import static com.guruvardaan.ghargharsurvey.config.Config.SELF_AGENT_URL;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.guruvardaan.ghargharsurvey.model.AgentModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelfTeamFragment extends Fragment {

    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<AgentModel> agentModels;
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
        agentModels = new ArrayList<>();
        adapter = new TeamAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getSelfTeam();
        return view;
    }

    class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.VisitHolder> {

        @Override
        public int getItemCount() {
            return agentModels.size();

        }

        @Override
        public void onBindViewHolder(TeamAdapter.VisitHolder holder, final int position) {
            holder.user_name.setText(agentModels.get(position).getAdvisor_name());
            holder.user_mobile.setText("**********");
            holder.user_rank.setText(agentModels.get(position).getAdvisor_rank());
            holder.introducer_name.setText(agentModels.get(position).getIadvisor_name());
            holder.introducer_mobile.setText("**********");
            holder.introducer_rank.setText("**");
            holder.agent_id.setText(agentModels.get(position).getPk_acc_adm_advisor_id());
        }

        @Override
        public TeamAdapter.VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.team_row, viewGroup, false);
            TeamAdapter.VisitHolder listHolder = new TeamAdapter.VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {
            TextView user_name, user_mobile, user_rank, introducer_name, introducer_mobile, introducer_rank, agent_id;

            public VisitHolder(View view) {
                super(view);
                agent_id = (TextView) view.findViewById(R.id.agent_id);
                user_name = (TextView) view.findViewById(R.id.user_name);
                user_mobile = (TextView) view.findViewById(R.id.user_mobile);
                user_rank = (TextView) view.findViewById(R.id.user_rank);
                introducer_name = (TextView) view.findViewById(R.id.introducer_name);
                introducer_mobile = (TextView) view.findViewById(R.id.introducer_mobile);
                introducer_rank = (TextView) view.findViewById(R.id.introducer_rank);
            }
        }
    }

    private void getSelfTeam() {
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = SELF_AGENT_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid() + "/1";
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
                            agentModels = new ArrayList<>();
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                agentModels.add(new AgentModel(object.getString("advisor_name"), object.getString("pk_acc_adm_advisor_id"), object.getString("mobile_no"), object.getString("advisor_rank"), object.getString("iadvisor_name"), object.getString("ipk_acc_adm_advisor_id"), object.getString("imobile_no"), object.getString("iadvisor_rank"),""));
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

}