package com.guruvardaan.ghargharsurvey.visits;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_ALL_REQUEST;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.guruvardaan.ghargharsurvey.model.PlotRequestModel;
import com.guruvardaan.ghargharsurvey.plots.PlotRequestForm;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SelectCustomers extends BaseActivity {

    ImageView back_button;
    RecyclerView recyclerView;
    LinearLayout no_lay, plot_request;
    ArrayList<PlotRequestModel> plotRequestModels;
    ArrayList<PlotRequestModel> newReqModels;
    UserDetails userDetails;
    TextView header;
    ProgressDialog pd;
    EditText search_edit;
    ImageView micro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_all_trip_customers, frameLayout);
        no_lay = (LinearLayout) findViewById(R.id.no_lay);
        header = (TextView) findViewById(R.id.header);
        micro = (ImageView) findViewById(R.id.micro);
        search_edit = (EditText) findViewById(R.id.search_edit);
        header.setText("Select Customer");
        plot_request = (LinearLayout) findViewById(R.id.plot_request);
        userDetails = new UserDetails(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        back_button = (ImageView) findViewById(R.id.back_button);
        plotRequestModels = new ArrayList<>();
        newReqModels = new ArrayList<>();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllRequest();

        plot_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlotRequestForm.class);
                startActivityForResult(intent, 23);
            }
        });
        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_edit.setSelectAllOnFocus(false);
                startVoiceRecognitionActivity();
            }
        });
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
                    newReqModels.clear();
                    for (int i = 0; i < plotRequestModels.size(); i++) {
                        if (plotRequestModels.get(i).getCustomer_name().toLowerCase().contains(search_edit.getText().toString().toLowerCase())) {
                            newReqModels.add(plotRequestModels.get(i));
                        }
                    }
                    recyclerView.setAdapter(new RequestAdapter(newReqModels));
                } else {
                    recyclerView.setAdapter(new RequestAdapter(plotRequestModels));
                }
            }
        });

    }

    private void getAllRequest() {
        no_lay.setVisibility(View.GONE);
        pd = ProgressDialog.show(SelectCustomers.this);
        String URL_CHECK = GET_ALL_REQUEST + userDetails.getuserid();
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
                                no_lay.setVisibility(View.GONE);
                                plotRequestModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    plotRequestModels.add(new PlotRequestModel(object.getString("id"), object.getString("agent_id"), object.getString("customer_name"), object.getString("customer_mobile"), object.getString("customer_address"), object.getString("professionId"), object.getString("professionName"), object.getString("occupationId"), object.getString("occupationName"), object.getString("status_of_plot_buy"), object.getString("payment_option"), object.getString("plot_size"), object.getString("block"), object.getString("category"), object.getString("emiforyear"), object.getString("visit_date"), object.getString("visit_amount"), object.getString("pan"), object.getString("aadhar_front"), object.getString("aadhar_back"), object.getString("meeting_pic")));
                                    newReqModels.add(new PlotRequestModel(object.getString("id"), object.getString("agent_id"), object.getString("customer_name"), object.getString("customer_mobile"), object.getString("customer_address"), object.getString("professionId"), object.getString("professionName"), object.getString("occupationId"), object.getString("occupationName"), object.getString("status_of_plot_buy"), object.getString("payment_option"), object.getString("plot_size"), object.getString("block"), object.getString("category"), object.getString("emiforyear"), object.getString("visit_date"), object.getString("visit_amount"), object.getString("pan"), object.getString("aadhar_front"), object.getString("aadhar_back"), object.getString("meeting_pic")));
                                }
                                Collections.sort(plotRequestModels, new Comparator<PlotRequestModel>() {
                                    @Override
                                    public int compare(PlotRequestModel o1, PlotRequestModel o2) {
                                        return getIDs(o1.getId()) - getIDs(o2.getId());
                                    }
                                });
                                Collections.reverse(plotRequestModels);
                                recyclerView.setAdapter(new RequestAdapter(plotRequestModels));

                            } else {
                                no_lay.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "No Plot Request Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            no_lay.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
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

    public int getIDs(String s) {
        try {
            int n = Integer.parseInt(s);
            return n;
        } catch (Exception e) {
            return 0;
        }
    }

    class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.VisitHolder> {

        ArrayList<PlotRequestModel> p_req;

        RequestAdapter(ArrayList<PlotRequestModel> p_req) {
            this.p_req = p_req;
        }

        @Override
        public int getItemCount() {
            return p_req.size();
        }

        @Override
        public void onBindViewHolder(VisitHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.user_name.setText(p_req.get(position).getCustomer_name());
            holder.user_mobile.setText(p_req.get(position).getCustomer_mobile());
            holder.block.setText(p_req.get(position).getBlock());
            holder.address.setText(p_req.get(position).getCustomer_address());
            holder.amount.setText("\u20b9 " + p_req.get(position).getVisit_amount());
            holder.status.setText(p_req.get(position).getStatus_of_plot_buy());
            holder.payment_mode.setText(p_req.get(position).getPayment_option());
            holder.visit_date.setText(p_req.get(position).getVisit_date());
            holder.main_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CM", p_req.get(position));
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.cust_pl_row, viewGroup, false);
            VisitHolder listHolder = new VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {

            TextView user_name, user_mobile, block, address, amount, status, payment_mode, visit_date;
            LinearLayout main_lay;

            public VisitHolder(View view) {
                super(view);
                main_lay = (LinearLayout) view.findViewById(R.id.main_lay);
                user_name = (TextView) view.findViewById(R.id.user_name);
                user_mobile = (TextView) view.findViewById(R.id.user_mobile);
                block = (TextView) view.findViewById(R.id.block);
                address = (TextView) view.findViewById(R.id.address);
                amount = (TextView) view.findViewById(R.id.amount);
                status = (TextView) view.findViewById(R.id.status);
                payment_mode = (TextView) view.findViewById(R.id.payment_mode);
                visit_date = (TextView) view.findViewById(R.id.visit_date);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            getAllRequest();
        }
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) {
                search_edit.setText(matches.get(0));
            }
        }
    }

    public void startVoiceRecognitionActivity() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "Speech recognition demo");
            startActivityForResult(intent, 1234);
        } catch (ActivityNotFoundException e) {
            String appPackageName = "com.google.android.googlequicksearchbox";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }
}