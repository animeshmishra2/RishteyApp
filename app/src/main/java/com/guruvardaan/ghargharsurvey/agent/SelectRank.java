package com.guruvardaan.ghargharsurvey.agent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.AddressModel;
import com.guruvardaan.ghargharsurvey.model.BankModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectRank extends BaseActivity {

    TextView header_txt;
    ImageView back_button;
    ArrayList<BankModel> bankModels;
    RecyclerView recyclerView;
    ProgressBar progress;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_select_rank, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progress = (ProgressBar) findViewById(R.id.progress);
        header_txt = (TextView) findViewById(R.id.header_txt);
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        header_txt.setText(getIntent().getExtras().getString("TITLE"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        getRank();
    }

    public void getRank() {
        recyclerView.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "";
        if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT ADVISOR RANK")) {
            URL = Config.SELECT_RANK + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + getIntent().getExtras().getString("RANK");
        } else if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT BANK")) {
            URL = Config.SELECT_BANK + "K9154289-68a1-80c7-e009-2asdccf7b0d/";
        }
        System.out.println(URL);
        StringRequest sr = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    bankModels = new ArrayList<>();
                    for (int i = 0; i < Table.length(); i++) {
                        JSONObject object = Table.getJSONObject(i);
                        if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT BANK")) {
                            bankModels.add(new BankModel(object.getString("pk_acc_cnf_bankname_id"), object.getString("bank_name")));
                        } else if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT ADVISOR RANK")) {
                            if (getValue(object.getString("level_rank")) <= 14) {
                                bankModels.add(new BankModel(object.getString("level_rank"), object.getString("level_name")));
                            }
                        }
                    }
                    recyclerView.setAdapter(new AddressAdapter());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Response  " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                recyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }
        });
        sr.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.VehicleHolder> {

        @Override
        public int getItemCount() {
            return bankModels.size();

        }

        @Override
        public void onBindViewHolder(VehicleHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(bankModels.get(position).getName());
            holder.pr_image.setImageResource(R.drawable.view_manager);
            holder.prod_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", bankModels.get(position).getName());
                    bundle.putString("ID", bankModels.get(position).getId());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public VehicleHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.vehicle_row, viewGroup, false);
            VehicleHolder listHolder = new VehicleHolder(mainGroup);
            return listHolder;
        }

        public class VehicleHolder extends RecyclerView.ViewHolder {

            TextView prod_name;
            ImageView pr_image;
            LinearLayout prod_layout;

            public VehicleHolder(View view) {
                super(view);
                prod_layout = (LinearLayout) view.findViewById(R.id.prod_layout);
                prod_name = (TextView) view.findViewById(R.id.prod_name);
                pr_image = (ImageView) view.findViewById(R.id.pr_image);
            }
        }
    }

    public int getValue(String s) {
        try {
            int n = Integer.parseInt(s);
            return n;
        } catch (Exception e) {
            return 0;
        }
    }

}