package com.guruvardaan.ghargharsurvey.voucher;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_ALL_REQUEST;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_VOUCHER_ACCOUNT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.PlotRequestModel;
import com.guruvardaan.ghargharsurvey.model.VoucherAccountModel;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.visits.SelectCustomers;
import com.guruvardaan.ghargharsurvey.welcome.GalleryActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectBankActvity;
import com.guruvardaan.ghargharsurvey.welcome.ViewImageActivity;
import com.rishtey.agentapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class VoucherAccountActivity extends AppCompatActivity {

    LinearLayout bank_lay, select_bank_lay;
    ImageView back_button;
    RecyclerView recyclerView;
    ProgressDialog pd;
    UserDetails userDetails;
    EditText account_no, select_bank, branch_name, user_name, ifsc;
    TextView update_details;
    int tst = 0;
    String bid = "";
    ArrayList<VoucherAccountModel> voucherAccountModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_account);
        userDetails = new UserDetails(getApplicationContext());
        tst = 0;
        bid = "";
        back_button = (ImageView) findViewById(R.id.back_button);
        bank_lay = (LinearLayout) findViewById(R.id.bank_lay);
        select_bank_lay = (LinearLayout) findViewById(R.id.select_bank_lay);
        branch_name = (EditText) findViewById(R.id.branch_name);
        user_name = (EditText) findViewById(R.id.user_name);
        account_no = (EditText) findViewById(R.id.account_no);
        select_bank = (EditText) findViewById(R.id.select_bank);
        ifsc = (EditText) findViewById(R.id.ifsc);
        update_details = (TextView) findViewById(R.id.update_details);
        select_bank_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectBankActvity.class);
                startActivityForResult(intent, 23);
            }
        });
        select_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectBankActvity.class);
                startActivityForResult(intent, 23);
            }
        });
        voucherAccountModels = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (account_no.getText().toString().trim().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Account Number", Toast.LENGTH_SHORT).show();
                } else if (bid.trim().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please Select Bank", Toast.LENGTH_SHORT).show();
                } else if (select_bank.getText().toString().trim().equalsIgnoreCase("ICICI Bank")) {
                    Toast.makeText(getApplicationContext(), "ICICI Bank Not Supported", Toast.LENGTH_SHORT).show();
                } else if (branch_name.getText().toString().trim().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Branch Name", Toast.LENGTH_SHORT).show();
                } else if (user_name.getText().toString().trim().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Account Holder Name", Toast.LENGTH_SHORT).show();
                } else if (ifsc.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Enter Valid IFSC", Toast.LENGTH_SHORT).show();
                } else {
                    updateBankAccount();
                }
            }
        });
        getVoucherAccounts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            select_bank.setText(data.getExtras().getString("NAME"));
            bid = data.getExtras().getString("ID");
        }
    }

    private void getVoucherAccounts() {
        bank_lay.setVisibility(View.GONE);
        pd = ProgressDialog.show(VoucherAccountActivity.this, "Please Wait...");
        String URL_CHECK = GET_VOUCHER_ACCOUNT + userDetails.getuserid();
        System.out.println("Mahendra " + URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                voucherAccountModels.clear();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    tst = 1;
                                    voucherAccountModels.add(new VoucherAccountModel(object.getString("id"), object.getString("acHolder"), object.getString("acNo"), object.getString("IFSC"), object.getString("bankId"), object.getString("bank_name"), object.getString("branch"), "1"));
                                }
                                recyclerView.setAdapter(new VoucherAccountAdapter());
                            } else {
                                bank_lay.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "No Bank Account Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            bank_lay.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        bank_lay.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "No Bank Account Found, Add New Bank", Toast.LENGTH_SHORT).show();
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

    class VoucherAccountAdapter extends RecyclerView.Adapter<VoucherAccountAdapter.MyViewHolder> {

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
        public VoucherAccountAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_account_row, parent, false);
            return new VoucherAccountAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(VoucherAccountAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.account_no.setText(voucherAccountModels.get(position).getAcNo().trim());
            holder.account_holder.setText(voucherAccountModels.get(position).getAcHolder().trim().toUpperCase());
            holder.ifsc.setText(voucherAccountModels.get(position).getIFSC().trim().toUpperCase());
            holder.bank_name.setText(voucherAccountModels.get(position).getBank_name());
            holder.branch_name.setText(voucherAccountModels.get(position).getBranch());
            if (tst == 1) {
                if (voucherAccountModels.get(position).getSelected().equalsIgnoreCase("1")) {
                    holder.account_type.setVisibility(View.VISIBLE);
                    holder.account_type.setText("Default");
                    holder.withdraw_am.setVisibility(View.VISIBLE);
                    holder.start.setText("Withdraw");
                } else {
                    holder.withdraw_am.setVisibility(View.GONE);
                    holder.account_type.setVisibility(View.GONE);
                }
            } else {
                holder.withdraw_am.setVisibility(View.VISIBLE);
                holder.start.setText("Make Default");
                holder.account_type.setVisibility(View.GONE);
            }
            holder.withdraw_am.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), VoucherWithdrawActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("IFSC", voucherAccountModels.get(position).getIFSC().trim().toUpperCase());
                    bundle.putString("ACHOLDER", voucherAccountModels.get(position).getAcHolder().trim().toUpperCase());
                    bundle.putString("ACNO", voucherAccountModels.get(position).getAcNo().trim());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return voucherAccountModels.size();
        }

    }

    public void updateBankAccount() {
        pd = ProgressDialog.show(VoucherAccountActivity.this, "Please Wait...");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, Config.MAKE_DEFAULT_ACCOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    if (Table.length() > 0) {
                        JSONObject object = Table.getJSONObject(0);
                        if (object.has("Column1")) {
                            if (object.getString("Column1").equalsIgnoreCase("t")) {
                                Toast.makeText(getApplicationContext(), "Default Account has been set", Toast.LENGTH_SHORT).show();
                                getVoucherAccounts();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error in Setting Default Account", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "You have already one default account", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error in Setting Default Account", Toast.LENGTH_SHORT).show();
                    }
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
                params2.put("acHolder", user_name.getText().toString().trim().toUpperCase());
                params2.put("acNo", account_no.getText().toString().trim());
                params2.put("IFSC", ifsc.getText().toString().trim().toUpperCase());
                params2.put("bank", bid);
                params2.put("branch", branch_name.getText().toString().trim());
                params2.put("agent_id", userDetails.getuserid());
                params2.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
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