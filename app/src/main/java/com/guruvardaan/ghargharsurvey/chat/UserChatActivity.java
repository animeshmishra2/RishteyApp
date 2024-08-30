package com.guruvardaan.ghargharsurvey.chat;

import static com.guruvardaan.ghargharsurvey.config.Config.ALL_USER_CHAT;
import static com.guruvardaan.ghargharsurvey.config.Config.CHAT_HEADERS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.HeaderModel;
import com.guruvardaan.ghargharsurvey.model.NewChatModel;
import com.guruvardaan.ghargharsurvey.model.UserChatModel;
import com.guruvardaan.ghargharsurvey.welcome.TeamList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UserChatActivity extends AppCompatActivity {

    ImageView goBack;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    UserDetails userDetails;
    ArrayList<UserChatModel> userChatModels;
    TextView chat_with_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        userChatModels = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        chat_with_us = (TextView) findViewById(R.id.chat_with_us);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        userDetails = new UserDetails(getApplicationContext());
        goBack = (ImageView) findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        chat_with_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("NEW", "1");
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            }
        });
        getUserChats("1");
    }

    private void getUserChats(String s) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_USER_CHAT + userDetails.getuserid(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        userChatModels.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            userChatModels.add(new UserChatModel(object.getString("id"), object.getString("name"), object.getString("userid"), object.getString("firebase_token"), object.getString("msg"), object.getString("description"), object.getString("header_id"), object.getString("image_url"), object.getString("status"), object.getString("header_name"), object.getString("chat_time")));
                        }
                        if (data.length() <= 0) {
                            if (s.equalsIgnoreCase("1")) {
                                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("NEW", "1");
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 662);
                            }
                            recyclerView.setAdapter(new UserChatAdapter());
                        } else {
                            recyclerView.setAdapter(new UserChatAdapter());
                        }
                    } else {
                        if (s.equalsIgnoreCase("1")) {
                            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("NEW", "1");
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 662);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView voucher_header, chat_desc, status_txt, current_date, complaint_id;
            ImageView status_img;
            RelativeLayout chat_layout;

            public MyViewHolder(View view) {
                super(view);
                voucher_header = (TextView) view.findViewById(R.id.voucher_header);
                chat_desc = (TextView) view.findViewById(R.id.chat_desc);
                status_txt = (TextView) view.findViewById(R.id.status_txt);
                status_img = (ImageView) view.findViewById(R.id.status_img);
                current_date = (TextView) view.findViewById(R.id.current_date);
                chat_layout = (RelativeLayout) view.findViewById(R.id.chat_layout);
                complaint_id = (TextView) view.findViewById(R.id.complaint_id);
            }
        }

        @Override
        public UserChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_row, parent, false);
            return new UserChatAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserChatAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.voucher_header.setText(userChatModels.get(position).getHeader_name());
            holder.chat_desc.setText(userChatModels.get(position).getMsg());
            holder.complaint_id.setText("RSTP000" + userChatModels.get(position).getId());
            holder.current_date.setText(convertMillisToDateString(getMills(userChatModels.get(position).getChat_time())));
            if (userChatModels.get(position).getStatus().equalsIgnoreCase("0")) {
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
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("NEW", "0");
                    bundle.putString("CID", userChatModels.get(position).getId());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 662);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userChatModels.size();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 662 && resultCode == RESULT_OK) {
            getUserChats("0");
        }
    }
}