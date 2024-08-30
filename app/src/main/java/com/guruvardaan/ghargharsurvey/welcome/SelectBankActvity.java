package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_CUSTOMER_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.SELECT_BANK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.BankModel;
import com.guruvardaan.ghargharsurvey.model.SelCusModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectBankActvity extends BaseActivity {
    RecyclerView passbook_recycler;
    UserDetails userDetails;
    ProgressBar progress;
    TextView header;
    ImageView back_button;
    EditText search_edit;
    ArrayList<BankModel> bankModels;
    ArrayList<BankModel> newBankModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_select_customer, frameLayout);
        header = (TextView) findViewById(R.id.header);
        search_edit = (EditText)findViewById(R.id.search_edit);
        search_edit.setHint("Search Bank");
        back_button = (ImageView) findViewById(R.id.back_button);
        header.setText(getString(R.string.select_bank));
        userDetails = new UserDetails(getApplicationContext());
        progress = (ProgressBar) findViewById(R.id.progress);
        passbook_recycler = (RecyclerView) findViewById(R.id.passbook_recycler);
        passbook_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        bankModels = new ArrayList<>();
        newBankModels = new ArrayList<>();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllBanks();

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
                    newBankModels.clear();
                    for (int i = 0; i < bankModels.size(); i++) {
                        if (bankModels.get(i).getName().toLowerCase().contains(search_edit.getText().toString().toLowerCase())) {
                            newBankModels.add(bankModels.get(i));
                        }
                    }
                    passbook_recycler.setAdapter(new CustomerAdapter(newBankModels));
                } else {
                    passbook_recycler.setAdapter(new CustomerAdapter(bankModels));
                }
            }
        });

    }

    private void getAllBanks() {
        progress.setVisibility(View.VISIBLE);
        String URL_CHECK = SELECT_BANK + "K9154289-68a1-80c7-e009-2asdccf7b0d/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                bankModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    bankModels.add(new BankModel(object.getString("pk_acc_cnf_bankname_id"), object.getString("bank_name")));
                                }
                                passbook_recycler.setAdapter(new CustomerAdapter(bankModels));
                            } else {
                                Toast.makeText(getApplicationContext(), "No Bank Found", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
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

    class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.TypeHolder> {
        ArrayList<BankModel> bModels;
        CustomerAdapter(ArrayList<BankModel> bModels)
        {
            this.bModels = bModels;
        }

        @Override
        public int getItemCount() {
            return bModels.size();

        }

        @Override
        public void onBindViewHolder(TypeHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(bModels.get(position).getName());
            holder.pr_image.setImageResource(R.drawable.bank_details);
            holder.prod_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", bModels.get(position).getName());
                    bundle.putString("ID", bModels.get(position).getId());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public TypeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.vehicle_row, viewGroup, false);
            TypeHolder listHolder = new TypeHolder(mainGroup);
            return listHolder;
        }

        public class TypeHolder extends RecyclerView.ViewHolder {

            TextView prod_name;
            ImageView pr_image;
            LinearLayout prod_layout;

            public TypeHolder(View view) {
                super(view);
                prod_layout = (LinearLayout) view.findViewById(R.id.prod_layout);
                prod_name = (TextView) view.findViewById(R.id.prod_name);
                pr_image = (ImageView) view.findViewById(R.id.pr_image);
            }
        }
    }
}