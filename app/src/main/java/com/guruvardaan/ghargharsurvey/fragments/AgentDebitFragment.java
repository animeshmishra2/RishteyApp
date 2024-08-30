package com.guruvardaan.ghargharsurvey.fragments;

import static com.guruvardaan.ghargharsurvey.config.Config.TOTAL_BUSINESS;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import com.guruvardaan.ghargharsurvey.model.MultiCreditModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AgentDebitFragment extends Fragment {

    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    MultipleAdapter adapter;
    LinearLayout no_transaction;
    ArrayList<MultiCreditModel> multiCreditModels;
    int mDay, mMonth, mYear;
    float total = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_self_business, container, false);
        no_transaction = (LinearLayout) view.findViewById(R.id.no_transaction);
        no_transaction.setVisibility(View.GONE);
        total = 0;
        userDetails = new UserDetails(getActivity());
        progress = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        multiCreditModels = new ArrayList<>();
        adapter = new MultipleAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        getCreditBusiness(getActivity().getIntent().getExtras().getString("ST"), getActivity().getIntent().getExtras().getString("ED"));
        return view;
    }

    private void getCreditBusiness(String start_date, String end_date) {
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = TOTAL_BUSINESS + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + getActivity().getIntent().getExtras().getString("ID") + "/" + start_date + "/" + end_date + "/12";
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        total = 0;
                        System.out.println("Animeshwa " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            multiCreditModels.clear();
                            multiCreditModels.add(new MultiCreditModel(1, start_date, end_date, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                total = total + getValues(object.getString("payableAmount"));
                                multiCreditModels.add(new MultiCreditModel(2, "", "", object.getString("id"), object.getString("pk_acc_com_voucher_id"), object.getString("voucher_dateFrom"), object.getString("voucher_dateTo"), object.getString("voucher_generated_date"), object.getString("fk_acc_adm_advisor_id"), object.getString("direct_comission"), object.getString("gap_comission"), object.getString("total_comission"), object.getString("tdsPer"), object.getString("serviceChargePEr"), object.getString("tdsAmount"), object.getString("serviceChargeAmount"), object.getString("totalDeduction"), object.getString("payableAmount"), object.getString("voucher_status"), object.getString("paymentBy"), object.getString("payment_date"), object.getString("paymentTime"), object.getString("payment_transaction_id"), object.getString("paymentMode"), object.getString("utr"), object.getString("option1"), object.getString("option2"), object.getString("option3"), object.getString("ins_system"), object.getString("ins_by"), object.getString("ins_date"), object.getString("ins_time"), object.getString("advisor_name"), object.getString("status")));
                            }
                            if (multiCreditModels.size() > 1) {
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

    class MultipleAdapter extends RecyclerView.Adapter {

        class Row1Holder extends RecyclerView.ViewHolder {

            EditText start_date, end_date;
            TextView submit_date, txt, amount;

            public Row1Holder(View itemView) {
                super(itemView);
                start_date = (EditText) itemView.findViewById(R.id.start_date);
                end_date = (EditText) itemView.findViewById(R.id.end_date);
                submit_date = (TextView) itemView.findViewById(R.id.submit_date);
                txt = (TextView) itemView.findViewById(R.id.txt);
                amount = (TextView) itemView.findViewById(R.id.amount);
            }
        }

        class Row2Holder extends RecyclerView.ViewHolder {

            TextView user_name, total_commission, tds, service_charge, total_deduction, payable_amount, payment_mode, payment_date, payment_status, ins_date;

            public Row2Holder(View view) {
                super(view);
                user_name = (TextView) view.findViewById(R.id.user_name);
                total_commission = (TextView) view.findViewById(R.id.total_commission);
                tds = (TextView) view.findViewById(R.id.tds);
                service_charge = (TextView) view.findViewById(R.id.service_charge);
                total_deduction = (TextView) view.findViewById(R.id.total_deduction);
                payable_amount = (TextView) view.findViewById(R.id.payable_amount);
                payment_mode = (TextView) view.findViewById(R.id.payment_mode);
                payment_date = (TextView) view.findViewById(R.id.payment_date);
                payment_status = (TextView) view.findViewById(R.id.payment_status);
                ins_date = (TextView) view.findViewById(R.id.ins_date);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;
            switch (viewType) {
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bb_row, parent, false);
                    return new Row1Holder(view);
                case 2:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_row_2, parent, false);
                    return new Row2Holder(view);
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {

            switch (multiCreditModels.get(position).getType()) {
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
            final MultiCreditModel object = multiCreditModels.get(listPosition);
            if (object != null) {
                switch (object.getType()) {
                    case 1:
                        ((Row1Holder) holder).txt.setText("Total Debit");
                        ((Row1Holder) holder).amount.setText("\u20b9" + total);
                        ((Row1Holder) holder).start_date.setText(multiCreditModels.get(listPosition).getStart_date());
                        ((Row1Holder) holder).end_date.setText(multiCreditModels.get(listPosition).getEnd_date());
                        ((Row1Holder) holder).submit_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getCreditBusiness(((Row1Holder) holder).start_date.getText().toString(), ((Row1Holder) holder).end_date.getText().toString());
                            }
                        });
                        ((Row1Holder) holder).start_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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
                                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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
                        ((Row2Holder) holder).user_name.setText(multiCreditModels.get(listPosition).getAdvisor_name());
                        ((Row2Holder) holder).total_commission.setText("\u20b9 " + multiCreditModels.get(listPosition).getTotal_comission());
                        ((Row2Holder) holder).tds.setText("\u20b9 " + multiCreditModels.get(listPosition).getTdsAmount());
                        ((Row2Holder) holder).service_charge.setText("\u20b9 " + multiCreditModels.get(listPosition).getServiceChargeAmount());
                        ((Row2Holder) holder).total_deduction.setText("\u20b9 " + multiCreditModels.get(listPosition).getTotalDeduction());
                        ((Row2Holder) holder).payable_amount.setText("\u20b9 " + multiCreditModels.get(listPosition).getPayableAmount());
                        if (multiCreditModels.get(listPosition).getPayment_date().equalsIgnoreCase("null")) {
                            ((Row2Holder) holder).payment_date.setText("Unpaid");
                        } else {
                            ((Row2Holder) holder).payment_date.setText(multiCreditModels.get(listPosition).getPayment_date());
                        }
                        if (multiCreditModels.get(listPosition).getStatus().equalsIgnoreCase("null")) {
                            ((Row2Holder) holder).payment_status.setText("Unpaid");
                        } else {
                            ((Row2Holder) holder).payment_status.setText(multiCreditModels.get(listPosition).getStatus());
                        }
                        if (multiCreditModels.get(listPosition).getPaymentMode().equalsIgnoreCase("null")) {
                            ((Row2Holder) holder).payment_mode.setText("Unpaid");
                        } else {
                            ((Row2Holder) holder).payment_mode.setText(multiCreditModels.get(listPosition).getPaymentMode());
                        }
                        ((Row2Holder) holder).ins_date.setText(multiCreditModels.get(listPosition).getIns_date());
                        break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return multiCreditModels.size();
        }
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

    public float getValues(String s) {
        try {
            float f = Float.parseFloat(s);
            return f;
        } catch (Exception e) {
            return 0;
        }
    }

}