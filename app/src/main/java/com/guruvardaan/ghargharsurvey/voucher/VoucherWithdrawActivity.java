package com.guruvardaan.ghargharsurvey.voucher;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_VOUCHER_ACCOUNT;
import static com.guruvardaan.ghargharsurvey.config.Config.UNPAID_VOUCHERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.UnpaidVoucherModel;
import com.guruvardaan.ghargharsurvey.model.VoucherAccountModel;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.rishtey.agentapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VoucherWithdrawActivity extends AppCompatActivity {

    ImageView back_button;
    UserDetails userDetails;
    RecyclerView recyclerView;
    LinearLayout no_lay;
    ProgressDialog pd;
    ArrayList<UnpaidVoucherModel> unpaidVoucherModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_withdraw);
        unpaidVoucherModels = new ArrayList<>();
        userDetails = new UserDetails(getApplicationContext());
        back_button = (ImageView) findViewById(R.id.back_button);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        no_lay = (LinearLayout) findViewById(R.id.no_lay);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getUnpaidVouchers();
    }


    private void getUnpaidVouchers() {
        pd = ProgressDialog.show(VoucherWithdrawActivity.this, "Please Wait...");
        no_lay.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = UNPAID_VOUCHERS + userDetails.getuserid();
        System.out.println("Mahendra " + URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    unpaidVoucherModels.clear();
                    if (Table.length() > 0) {
                        for (int i = 0; i < Table.length(); i++) {
                            JSONObject object = Table.getJSONObject(i);
                            unpaidVoucherModels.add(new UnpaidVoucherModel(object.getString("pk_acc_com_voucher_id"), object.getString("fk_acc_adm_advisor_id"), object.getString("voucher_dateFrom"), object.getString("voucher_dateTo"), object.getString("voucher_generated_date"), object.getString("payableAmount")));
                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new UnpaidAdapter());
                    } else {
                        no_lay.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "No Unpaid Voucher Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    no_lay.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "No Unpaid Voucher Found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                no_lay.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "No Unpaid Voucher Found", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content_Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    class UnpaidAdapter extends RecyclerView.Adapter<UnpaidAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout withdraw_am;
            TextView bank_name, account_type, account_no, ifsc, branch_name, account_holder, start;

            public MyViewHolder(View view) {
                super(view);
                bank_name = (TextView) view.findViewById(R.id.bank_name);
                account_type = (TextView) view.findViewById(R.id.account_type);
                account_no = (TextView) view.findViewById(R.id.account_no);
                start = (TextView) view.findViewById(R.id.start);
                ifsc = (TextView) view.findViewById(R.id.ifsc);
                branch_name = (TextView) view.findViewById(R.id.branch_name);
                account_holder = (TextView) view.findViewById(R.id.account_holder);
                withdraw_am = (LinearLayout) view.findViewById(R.id.withdraw_am);
            }
        }

        @Override
        public UnpaidAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_account_row, parent, false);
            return new UnpaidAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UnpaidAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.account_no.setText("\u20b9" + unpaidVoucherModels.get(position).getPayableAmount().trim());
            holder.account_holder.setText("Generated Date: " + unpaidVoucherModels.get(position).getVoucher_generated_date().trim().toUpperCase());
            holder.ifsc.setText("From : " + unpaidVoucherModels.get(position).getVoucher_dateFrom().trim());
            holder.bank_name.setText(unpaidVoucherModels.get(position).getFk_acc_adm_advisor_id());
            holder.branch_name.setText("To : " + unpaidVoucherModels.get(position).getVoucher_dateTo());
            holder.account_type.setVisibility(View.VISIBLE);
            holder.account_type.setText("Unpaid");
            holder.withdraw_am.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    withdrawVoucher(unpaidVoucherModels.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
            return unpaidVoucherModels.size();
        }

    }

    public void withdrawVoucher(UnpaidVoucherModel voucherModel) {
        pd = ProgressDialog.show(VoucherWithdrawActivity.this, "Please Wait...");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, Config.VOUCHER_WITHDRAW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                pd.dismiss();
                try {
                    if (response.trim().contains("Payment Done")) {
                        Toast.makeText(getApplicationContext(), "Voucher Payment Done", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Voucher Payment Failed", Toast.LENGTH_SHORT).show();
                    }
                    getUnpaidVouchers();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error in Setting Default Account", Toast.LENGTH_SHORT).show();
                }
                System.out.println("Response  " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in Setting Default Account", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                params2.put("acholderName", getIntent().getExtras().getString("ACHOLDER"));
                params2.put("acno", getIntent().getExtras().getString("ACNO"));
                params2.put("ifsc", getIntent().getExtras().getString("IFSC"));
                params2.put("amount", voucherModel.getPayableAmount());
                params2.put("agentId", userDetails.getuserid());
                params2.put("voucherNo", voucherModel.getPk_acc_adm_advisor_id());
                params2.put("voucher_generated_date", voucherModel.getVoucher_generated_date());
                params2.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
                params2.put("TXNTYPE", "IFS");
                return new JSONObject(params2).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }


}