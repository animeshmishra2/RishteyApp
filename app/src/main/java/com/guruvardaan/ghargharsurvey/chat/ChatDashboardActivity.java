package com.guruvardaan.ghargharsurvey.chat;

import static com.guruvardaan.ghargharsurvey.config.Config.CHAT_HEADERS;
import static com.guruvardaan.ghargharsurvey.config.Config.DASHBOARD;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.guruvardaan.ghargharsurvey.model.HeaderModel;
import com.guruvardaan.ghargharsurvey.model.NewChatModel;
import com.guruvardaan.ghargharsurvey.welcome.SplashActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatDashboardActivity extends AppCompatActivity {

    TextView user_name, mobile, department;
    ImageView back_button, logout;
    UserDetails userDetails;
    ProgressBar progressBar;
    LinearLayout main_layout;
    TextView today_resolved, today_team_resolved, today_unresolved, total_resolved, total_team_resolved, total_unresolved, tr_today_resolved, tr_today_team_resolved, tr_today_unresolved, tr_total_resolved, tr_total_team_resolved, tr_total_unresolved;

    TextView direct_today, direct_total, transferred_today, transferred_total, view_all_complaints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dashboard);
        userDetails = new UserDetails(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        direct_today = (TextView) findViewById(R.id.direct_today);
        direct_total = (TextView) findViewById(R.id.direct_total);
        view_all_complaints = (TextView) findViewById(R.id.view_all_complaints);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        logout = (ImageView) findViewById(R.id.logout);
        back_button = (ImageView) findViewById(R.id.back_button);
        user_name = (TextView) findViewById(R.id.user_name);
        mobile = (TextView) findViewById(R.id.mobile);
        department = (TextView) findViewById(R.id.department);
        today_resolved = (TextView) findViewById(R.id.today_resolved);
        today_team_resolved = (TextView) findViewById(R.id.today_team_resolved);
        today_unresolved = (TextView) findViewById(R.id.today_unresolved);
        total_resolved = (TextView) findViewById(R.id.total_resolved);
        total_team_resolved = (TextView) findViewById(R.id.total_team_resolved);
        total_unresolved = (TextView) findViewById(R.id.total_unresolved);
        tr_today_resolved = (TextView) findViewById(R.id.tr_today_resolved);
        tr_today_team_resolved = (TextView) findViewById(R.id.tr_today_team_resolved);
        tr_today_unresolved = (TextView) findViewById(R.id.tr_today_unresolved);
        tr_total_team_resolved = (TextView) findViewById(R.id.tr_total_team_resolved);
        tr_total_resolved = (TextView) findViewById(R.id.tr_total_resolved);
        tr_total_unresolved = (TextView) findViewById(R.id.tr_total_unresolved);
        transferred_today = (TextView) findViewById(R.id.transferred_today);
        transferred_total = (TextView) findViewById(R.id.transferred_total);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDetails.clearAll();
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });
        String tl = "";
        if (userDetails.getIsTL().equalsIgnoreCase("1")) {
            tl = "Team Leader";
        } else {
            tl = "Employee";
        }
        user_name.setText(userDetails.getName().toUpperCase() + " (" + tl + ")");
        mobile.setText("Mobile : " + userDetails.getMobile_no());
        department.setText("Department : " + userDetails.getDep_name());
        today_resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodayChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            }
        });
        if (userDetails.getuserid().equalsIgnoreCase("23")) {
            view_all_complaints.setVisibility(View.VISIBLE);
        } else {
            view_all_complaints.setVisibility(View.GONE);
        }

        view_all_complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminComplaintsActivity.class);
                startActivity(intent);
            }
        });
        today_unresolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodayChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            }
        });
        today_team_resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodayChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);

            }
        });


        total_resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TotalChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            }
        });
        total_unresolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TotalChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            }
        });
        total_team_resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TotalChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);

            }
        });

        tr_today_resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodayTransferredActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            }
        });
        tr_today_unresolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodayTransferredActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            }
        });
        tr_today_team_resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodayTransferredActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);

            }
        });

        tr_total_resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TotalTransferredActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            }
        });
        tr_total_unresolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TotalTransferredActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);
            }
        });
        tr_total_team_resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TotalTransferredActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 662);

            }
        });

        getDashboard();
    }

    private void getDashboard() {
        progressBar.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DASHBOARD + userDetails.getuserid(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        progressBar.setVisibility(View.GONE);
                        main_layout.setVisibility(View.VISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                total_resolved.setText(jsonObject.getString("direct_solved"));
                                total_unresolved.setText(jsonObject.getString("direct_unsolved"));
                                tr_total_resolved.setText(jsonObject.getString("trans_solved"));
                                tr_total_unresolved.setText(jsonObject.getString("trans_unsolved"));
                                total_team_resolved.setText(jsonObject.getString("direct_team_solved"));
                                tr_total_team_resolved.setText(jsonObject.getString("trans_team_solved"));
                                today_unresolved.setText(jsonObject.getString("direct_unsolved_today"));
                                today_team_resolved.setText(jsonObject.getString("direct_team_solved_today"));
                                today_resolved.setText(jsonObject.getString("direct_solved_today"));
                                direct_total.setText("Total (" + jsonObject.getString("direct_total") + ")");
                                direct_today.setText("Today (" + jsonObject.getString("direct_total_today") + ")");
                                tr_today_resolved.setText(jsonObject.getString("trans_today_solved"));
                                tr_today_team_resolved.setText(jsonObject.getString("trans_today_team_solved"));
                                tr_today_unresolved.setText(jsonObject.getString("trans_today_unsolved"));
                                transferred_total.setText("Total (" + jsonObject.getString("trans_total") + ")");
                                transferred_today.setText("Today (" + jsonObject.getString("trans_today_total") + ")");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        main_layout.setVisibility(View.VISIBLE);
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 662 && resultCode == RESULT_OK) {
            getDashboard();
        }
    }
}