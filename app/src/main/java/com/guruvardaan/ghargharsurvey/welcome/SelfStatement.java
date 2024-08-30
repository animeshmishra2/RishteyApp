package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.SELF_STATEMENT_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.TOTAL_BUSINESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.fragments.DebitFragment;
import com.guruvardaan.ghargharsurvey.model.MultiCreditModel;
import com.guruvardaan.ghargharsurvey.model.SelfStatementModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelfStatement extends BaseActivity {

    LinearLayout no_transaction;
    RecyclerView recyclerView;
    ProgressBar progress;
    ImageView back_button;
    UserDetails userDetails;
    ArrayList<SelfStatementModel> selfStatementModels;
    int mDay, mMonth, mYear;
    TextView total;
    SelfStatementAdapter adapter;
    float t_am = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_self_statement, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        no_transaction = (LinearLayout) findViewById(R.id.no_transaction);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progress = (ProgressBar) findViewById(R.id.progress);
        total = (TextView) findViewById(R.id.total);
        userDetails = new UserDetails(getApplicationContext());
        selfStatementModels = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        adapter = new SelfStatementAdapter();
        recyclerView.setAdapter(adapter);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSelfStatement(getPastDate(), getCurrentDate());
    }

    private void getSelfStatement(String start_date, String end_date) {
        no_transaction.setVisibility(View.GONE);
        total.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = SELF_STATEMENT_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid() + "/" + start_date + "/" + end_date;
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
                            selfStatementModels.clear();
                            t_am = 0;
                            selfStatementModels.add(new SelfStatementModel(1, start_date, end_date, "", "", "", "", "", "", "", "", "", "", ""));
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                t_am += getTotalAmount(object.getString("transaction_amount"));
                                selfStatementModels.add(new SelfStatementModel(2, start_date, end_date, object.getString("transaction_date"), object.getString("cid"), object.getString("customerFirstName"), object.getString("fk_acc_cor_service_id"), object.getString("mobile1"), object.getString("mobile2"), object.getString("whatsapp"), object.getString("plotNo"), object.getString("transaction_mode"), object.getString("transaction_amount"), object.getString("empcomment")));
                            }
                            if (selfStatementModels.size() > 1) {
                                total.setVisibility(View.VISIBLE);
                                total.setText("Total Amount : \u20b9 " + t_am);
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


    class SelfStatementAdapter extends RecyclerView.Adapter {

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

            TextView user_name, mobile, date, transaction_amount, transaction_mode, plot_no, service;

            public Row2Holder(View view) {
                super(view);
                user_name = (TextView) view.findViewById(R.id.user_name);
                mobile = (TextView) view.findViewById(R.id.mobile);
                date = (TextView) view.findViewById(R.id.date);
                transaction_amount = (TextView) view.findViewById(R.id.transaction_amount);
                transaction_mode = (TextView) view.findViewById(R.id.transaction_mode);
                plot_no = (TextView) view.findViewById(R.id.plot_no);
                service = (TextView) view.findViewById(R.id.service);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;
            switch (viewType) {
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_row_1, parent, false);
                    return new SelfStatementAdapter.Row1Holder(view);
                case 2:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_statement_row, parent, false);
                    return new SelfStatementAdapter.Row2Holder(view);
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {

            switch (selfStatementModels.get(position).getType()) {
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
            final SelfStatementModel object = selfStatementModels.get(listPosition);
            if (object != null) {
                switch (object.getType()) {
                    case 1:
                        ((SelfStatementAdapter.Row1Holder) holder).start_date.setText(selfStatementModels.get(listPosition).getStart_date());
                        ((SelfStatementAdapter.Row1Holder) holder).end_date.setText(selfStatementModels.get(listPosition).getEnd_date());
                        ((SelfStatementAdapter.Row1Holder) holder).submit_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getSelfStatement(((SelfStatementAdapter.Row1Holder) holder).start_date.getText().toString(), ((SelfStatementAdapter.Row1Holder) holder).end_date.getText().toString());
                            }
                        });
                        ((SelfStatementAdapter.Row1Holder) holder).start_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog datePickerDialog = new DatePickerDialog(SelfStatement.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                ((SelfStatementAdapter.Row1Holder) holder).start_date.setText(formatDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                                            }
                                        }, mYear, mMonth, mDay);
                                datePickerDialog.show();
                            }
                        });
                        ((SelfStatementAdapter.Row1Holder) holder).end_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog datePickerDialog = new DatePickerDialog(SelfStatement.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                ((SelfStatementAdapter.Row1Holder) holder).end_date.setText(formatDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                                            }
                                        }, mYear, mMonth, mDay);
                                datePickerDialog.show();
                            }
                        });

                        break;
                    case 2:
                        ((Row2Holder) holder).user_name.setText(selfStatementModels.get(listPosition).getCustomerFirstName());
                        ((Row2Holder) holder).mobile.setText(selfStatementModels.get(listPosition).getMobile1());
                        ((Row2Holder) holder).date.setText(selfStatementModels.get(listPosition).getTransaction_date());
                        ((Row2Holder) holder).transaction_amount.setText("\u20b9 " + selfStatementModels.get(listPosition).getTransaction_amount());
                        ((Row2Holder) holder).transaction_mode.setText(selfStatementModels.get(listPosition).getTransaction_mode());
                        ((Row2Holder) holder).plot_no.setText(selfStatementModels.get(listPosition).getPlotNo());
                        ((Row2Holder) holder).service.setText(selfStatementModels.get(listPosition).getFk_acc_cor_service_id());
                        break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return selfStatementModels.size();
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