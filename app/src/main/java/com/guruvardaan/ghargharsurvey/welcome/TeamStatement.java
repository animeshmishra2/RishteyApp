package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.SELF_STATEMENT_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.TEAM_STATEMENT_URL;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.guruvardaan.ghargharsurvey.model.SelfStatementModel;
import com.guruvardaan.ghargharsurvey.model.TeamStatementModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TeamStatement extends BaseActivity {

    LinearLayout no_transaction;
    RecyclerView recyclerView;
    ProgressBar progress;
    ImageView back_button;
    UserDetails userDetails;
    ArrayList<TeamStatementModel> teamStatementModels;
    int mDay, mMonth, mYear;
    TextView total;
    TeamStatementAdapter adapter;
    float t_am = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_team_statement, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        no_transaction = (LinearLayout) findViewById(R.id.no_transaction);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progress = (ProgressBar) findViewById(R.id.progress);
        total = (TextView) findViewById(R.id.total);
        userDetails = new UserDetails(getApplicationContext());
        teamStatementModels = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        adapter = new TeamStatementAdapter();
        recyclerView.setAdapter(adapter);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getTeamStatement(getIntent().getExtras().getString("ST"), getIntent().getExtras().getString("ED"));
    }

    private void getTeamStatement(String start_date, String end_date) {
        no_transaction.setVisibility(View.GONE);
        total.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = TEAM_STATEMENT_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid()+"/"+start_date+"/"+end_date;
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        System.out.println("Animeshwa " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            teamStatementModels.clear();
                            t_am = 0;
                            teamStatementModels.add(new TeamStatementModel(1, start_date, end_date, "", "", "", "", "", "", ""));
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                t_am += getTotalAmount(object.getString("selfBusiness"));
                                teamStatementModels.add(new TeamStatementModel(2, start_date, end_date, object.getString("pk_acc_adm_advisor_id"), object.getString("advisor_name"), object.getString("mobile_no"), object.getString("whatsapp"), object.getString("advisor_rank"), object.getString("selfBusiness"), object.getString("selfCount")));
                            }
                            if (teamStatementModels.size() > 1) {
                                total.setVisibility(View.VISIBLE);
                                total.setText("Total Amount : \u20b9 " + String.format("%.0f", t_am));
                                recyclerView.setVisibility(View.VISIBLE);
                                no_transaction.setVisibility(View.GONE);
                            } else {

                                recyclerView.setVisibility(View.VISIBLE);
                                no_transaction.setVisibility(View.VISIBLE);
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    class TeamStatementAdapter extends RecyclerView.Adapter {

        class Row1Holder extends RecyclerView.ViewHolder {

            EditText start_date, end_date;
            TextView submit_date;

            public Row1Holder(View itemView) {
                super(itemView);
                start_date = (EditText) itemView.findViewById(R.id.start_date);
                end_date = (EditText) itemView.findViewById(R.id.end_date);
                submit_date = (TextView) itemView.findViewById(R.id.submit_date);
            }
        }

        class Row2Holder extends RecyclerView.ViewHolder {

            TextView user_name, mobile, rank, transaction_amount, self_count;

            public Row2Holder(View view) {
                super(view);
                user_name = (TextView) view.findViewById(R.id.user_name);
                mobile = (TextView) view.findViewById(R.id.mobile);
                rank = (TextView) view.findViewById(R.id.rank);
                transaction_amount = (TextView) view.findViewById(R.id.transaction_amount);
                self_count = (TextView) view.findViewById(R.id.self_count);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;
            switch (viewType) {
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_row_1, parent, false);
                    return new Row1Holder(view);
                case 2:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_statement_row, parent, false);
                    return new Row2Holder(view);
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {

            switch (teamStatementModels.get(position).getType()) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                default:
                    return -1;
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int listPosition) {
            final TeamStatementModel object = teamStatementModels.get(listPosition);
            if (object != null) {
                switch (object.getType()) {
                    case 1:
                        ((Row1Holder) holder).start_date.setText(teamStatementModels.get(listPosition).getStart_date());
                        ((Row1Holder) holder).end_date.setText(teamStatementModels.get(listPosition).getEnd_date());
                        ((Row1Holder) holder).submit_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getTeamStatement(((Row1Holder) holder).start_date.getText().toString(), ((Row1Holder) holder).end_date.getText().toString());
                            }
                        });
                        ((Row1Holder) holder).start_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog datePickerDialog = new DatePickerDialog(TeamStatement.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                ((Row1Holder) holder).start_date.setText(formatDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                                            }
                                        }, mYear, mMonth, mDay);
                                datePickerDialog.show();
                            }
                        });
                        ((Row1Holder) holder).end_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog datePickerDialog = new DatePickerDialog(TeamStatement.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                ((Row1Holder) holder).end_date.setText(formatDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                                            }
                                        }, mYear, mMonth, mDay);
                                datePickerDialog.show();
                            }
                        });

                        break;
                    case 2:
                        ((Row2Holder) holder).user_name.setText(teamStatementModels.get(listPosition).getAdvisor_name());
                        ((Row2Holder) holder).mobile.setText(teamStatementModels.get(listPosition).getMobile_no());
                        ((Row2Holder) holder).rank.setText(teamStatementModels.get(listPosition).getAdvisor_rank());
                        ((Row2Holder) holder).transaction_amount.setText("\u20b9 " + teamStatementModels.get(listPosition).getSelfBusiness());
                        ((Row2Holder) holder).self_count.setText(teamStatementModels.get(listPosition).getSelfCount());
                        break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return teamStatementModels.size();
        }
    }


    public String getCurrentDate() {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy");
        String newDateStr = postFormater.format(new Date());
        return newDateStr;
    }

    public String getPastDate() {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy");
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.add(Calendar.MONTH, -1);
        aCalendar.set(Calendar.DATE, 1);
        Date firstDateOfPreviousMonth = aCalendar.getTime();
        String newDateStr = postFormater.format(firstDateOfPreviousMonth);
        return newDateStr;
    }

    public String formatDate(String dates) {
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date date = format.parse(dates);
            System.out.println(date);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dates;
        }
    }

    public float getTotalAmount(String tt) {
        try {
            float f = Float.parseFloat(tt);
            return f;
        } catch (Exception e) {
            return 0;
        }

    }

}