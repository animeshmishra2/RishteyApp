package com.guruvardaan.ghargharsurvey.chat;

import static com.guruvardaan.ghargharsurvey.config.Config.TRANSFERRED_TODAY_COMPLAINTS;
import static com.guruvardaan.ghargharsurvey.config.Config.TRANSFERRED_TOTAL_COMPLAINTS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.TodayTeamModel;
import com.rishtey.agentapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransferredTotalUnsolved extends Fragment {
    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    LinearLayout no_transaction;
    ArrayList<TodayTeamModel> teamChatModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_direct_solved, container, false);
        userDetails = new UserDetails(getActivity());
        teamChatModels = new ArrayList<>();
        no_transaction = (LinearLayout) view.findViewById(R.id.no_transaction);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getDirectComplaints();
        return view;
    }

    private void getDirectComplaints() {
        no_transaction.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = TRANSFERRED_TOTAL_COMPLAINTS + userDetails.getuserid();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        teamChatModels.clear();
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            if (object.getString("team_status").equalsIgnoreCase("0") && object.getString("user_status").equalsIgnoreCase("0")) {
                                teamChatModels.add(new TodayTeamModel(object.getString("id"), object.getString("name"), object.getString("userid"), object.getString("firebase_token"), object.getString("msg"), object.getString("description"), object.getString("header_id"), object.getString("image_url"), object.getString("user_status"), object.getString("team_status"), object.getString("status"), object.getString("chat_time"), object.getString("resolve_time"), object.getString("header_name"), object.getString("res_type"), object.getString("ttype"), object.getString("new_msg"), object.getString("image")));
                            }
                        }
                        if (teamChatModels.size() <= 0) {
                            no_transaction.setVisibility(View.VISIBLE);
                        } else {
                            no_transaction.setVisibility(View.GONE);
                            recyclerView.setAdapter(new TeamChatAdapter());
                        }
                    } else {
                        no_transaction.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    no_transaction.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
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

    class TeamChatAdapter extends RecyclerView.Adapter<TeamChatAdapter.VisitHolder> {


        @Override
        public int getItemCount() {
            return teamChatModels.size();
        }

        @Override
        public void onBindViewHolder(VisitHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.voucher_header.setText(teamChatModels.get(position).getName());
            holder.complaint_id.setText("RSTP000" + teamChatModels.get(position).getId());
            holder.voucher_head.setText(teamChatModels.get(position).getHeader_name());
            holder.chat_desc.setText(teamChatModels.get(position).getHeader_name());
            if (teamChatModels.get(position).getTtype().equalsIgnoreCase("0")) {
                if (teamChatModels.get(position).getRes_type().equalsIgnoreCase("1")) {
                    if (teamChatModels.get(position).getImage().length() >= 6) {
                        holder.chat_desc.setText("नयी फोटो प्राप्त हुई है |");
                    } else {
                        holder.chat_desc.setText(teamChatModels.get(position).getNew_msg());
                    }
                } else if (teamChatModels.get(position).getRes_type().equalsIgnoreCase("2")) {
                    if (teamChatModels.get(position).getImage().length() >= 6) {
                        holder.chat_desc.setText("आपने नयी फोटो भेजी है |");
                    } else {
                        holder.chat_desc.setText("Rishtey Team : - " + teamChatModels.get(position).getNew_msg());
                    }
                } else {
                    holder.chat_desc.setText(teamChatModels.get(position).getNew_msg());
                }
            } else {
                holder.chat_desc.setText(teamChatModels.get(position).getNew_msg());
            }

            holder.current_date.setText(convertMillisToDateString(getMills(teamChatModels.get(position).getChat_time())));
            if (teamChatModels.get(position).getStatus().equalsIgnoreCase("0")) {
                holder.status_img.setImageResource(R.drawable.pending);
                holder.status_txt.setText("Running");
                holder.status_txt.setTextColor(Color.parseColor("#FFAF49"));
            } else {
                holder.status_img.setImageResource(R.drawable.approved);
                holder.status_txt.setText("Resolved");
                holder.status_txt.setTextColor(Color.parseColor("#05c505"));
            }
            holder.chat_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("UNAME", teamChatModels.get(position).getName());
                    bundle.putString("CID", teamChatModels.get(position).getId());
                    bundle.putString("STATUS", teamChatModels.get(position).getStatus());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 662);
                }
            });
        }

        @Override
        public VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.user_chat_row, viewGroup, false);
            VisitHolder listHolder = new VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {
            TextView voucher_header, chat_desc, status_txt, current_date, complaint_id, voucher_head;
            ImageView status_img;
            RelativeLayout chat_layout;

            public VisitHolder(View view) {
                super(view);
                voucher_header = (TextView) view.findViewById(R.id.voucher_header);
                voucher_head = (TextView) view.findViewById(R.id.voucher_head);
                complaint_id = (TextView) view.findViewById(R.id.complaint_id);
                chat_desc = (TextView) view.findViewById(R.id.chat_desc);
                status_txt = (TextView) view.findViewById(R.id.status_txt);
                status_img = (ImageView) view.findViewById(R.id.status_img);
                current_date = (TextView) view.findViewById(R.id.current_date);
                chat_layout = (RelativeLayout) view.findViewById(R.id.chat_layout);
            }
        }
    }

    private String convertMillisToDateString(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy hh:mm a", Locale.getDefault());
        Date date = new Date(millis);
        return dateFormat.format(date);
    }

    public long getMills(String s) {
        try {
            long l = Long.parseLong(s);
            return l;
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 662 && resultCode == Activity.RESULT_OK) {
            getDirectComplaints();
        }
    }
}