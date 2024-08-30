package com.guruvardaan.ghargharsurvey.customers;

import static com.guruvardaan.ghargharsurvey.config.Config.PLOT_REQUEST;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.PlotModel;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectCustomerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlotRemarkActivity extends BaseActivity {

    RecyclerView plot_recycler;
    EditText remarks, customer_name;
    ImageView back_button;
    TextView submit_request;
    ProgressBar progress;
    LinearLayout lin_lay, customer_lay;
    ArrayList<PlotModel> plotModels;
    UserDetails userDetails;
    ProgressDialog pd;
    String cid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_plot_remark, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        customer_name = (EditText) findViewById(R.id.customer_name);
        customer_lay = (LinearLayout) findViewById(R.id.customer_lay);
        plot_recycler = (RecyclerView) findViewById(R.id.plot_recycler);
        cid = "";
        plot_recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        remarks = (EditText) findViewById(R.id.remarks);
        plotModels = (ArrayList<PlotModel>) getIntent().getExtras().getSerializable("PM");
        lin_lay = (LinearLayout) findViewById(R.id.lin_lay);
        progress = (ProgressBar) findViewById(R.id.progress);
        back_button = (ImageView) findViewById(R.id.back_button);
        submit_request = (TextView) findViewById(R.id.submit_request);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        plot_recycler.setAdapter(new PlotAdapter());
        submit_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (remarks.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter Remarks", Toast.LENGTH_SHORT).show();
                } else {
                    if (userDetails.getUser_type().equalsIgnoreCase("2")) {
                        createJSON();
                    } else {
                        if (cid.equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "Select Customer", Toast.LENGTH_SHORT).show();
                        } else {
                            createJSON();
                        }
                    }
                }
            }
        });
        if (userDetails.getUser_type().equalsIgnoreCase("1")) {
            customer_lay.setVisibility(View.VISIBLE);
        } else {
            customer_lay.setVisibility(View.GONE);
        }
        customer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCustomerActivity.class);
                startActivityForResult(intent, 22);
            }
        });
    }

    public void createJSON() {
        try {
            JSONObject main_obj = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < plotModels.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("plot_id", plotModels.get(i).getId());
                jsonArray.put(jsonObject);
            }
            main_obj.put("plots", jsonArray);
            main_obj.put("remark", remarks.getText().toString().trim());
            if (userDetails.getUser_type().equalsIgnoreCase("2")) {
                main_obj.put("advisorId", userDetails.getAgentId());
                main_obj.put("userid", userDetails.getuserid());
            } else {
                main_obj.put("advisorId", userDetails.getuserid());
                main_obj.put("userid", cid);
            }
            main_obj.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            System.out.println("Animesh " + main_obj.toString());
            addPlotRequest(main_obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addPlotRequest(String jsons) {
        pd = ProgressDialog.show(PlotRemarkActivity.this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                PLOT_REQUEST,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {
            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                return jsons.getBytes();
            }

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(myReq);
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                System.out.println("Mishras " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    if (Table.length() > 0) {
                        JSONObject object = Table.getJSONObject(0);
                        if (object.getString("return_type").equalsIgnoreCase("t")) {
                            Toast.makeText(getApplicationContext(), "Plot Request Sent Successfully", Toast.LENGTH_SHORT).show();
                            successPlotDialog();
                        } else {
                            Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
            }
        };
    }


    class PlotAdapter extends RecyclerView.Adapter<PlotAdapter.PlotHolder> {

        @Override
        public int getItemCount() {
            return plotModels.size();
        }

        @Override
        public void onBindViewHolder(PlotAdapter.PlotHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.plot_area.setText(plotModels.get(position).getDefault_area() + " Sq ft");
            holder.plot_name.setText(plotModels.get(position).getUniquename());
            if (plotModels.get(position).getSel().equalsIgnoreCase("0")) {
                if (plotModels.get(position).getStatus().equalsIgnoreCase("Booked")) {
                    holder.main_lay.setBackgroundResource(R.drawable.booked_border);
                    holder.plot_name.setTextColor(Color.parseColor("#ffffff"));
                    holder.plot_area.setTextColor(Color.parseColor("#ffffff"));
                    holder.plot_price.setVisibility(View.INVISIBLE);
                } else if (plotModels.get(position).getStatus().equalsIgnoreCase("Vacant")) {
                    holder.main_lay.setBackgroundResource(R.drawable.vacant_border);
                    holder.plot_name.setTextColor(Color.parseColor("#000000"));
                    holder.plot_area.setTextColor(Color.parseColor("#000000"));
                    holder.plot_price.setVisibility(View.INVISIBLE);
                } else {
                    holder.plot_name.setTextColor(Color.parseColor("#000000"));
                    holder.plot_price.setVisibility(View.VISIBLE);
                    holder.plot_price.setText("Hold");
                    holder.plot_area.setTextColor(Color.parseColor("#000000"));
                    holder.main_lay.setBackgroundResource(R.drawable.black_borders);
                }
            } else {
                holder.main_lay.setBackgroundResource(R.drawable.selected_border);
                holder.plot_name.setTextColor(Color.parseColor("#ffffff"));
                holder.plot_area.setTextColor(Color.parseColor("#ffffff"));
                holder.plot_price.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public PlotAdapter.PlotHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.plots_row, viewGroup, false);
            PlotAdapter.PlotHolder listHolder = new PlotAdapter.PlotHolder(mainGroup);
            return listHolder;
        }

        public class PlotHolder extends RecyclerView.ViewHolder {

            TextView plot_name, plot_area, plot_price;
            LinearLayout main_lay;

            public PlotHolder(View view) {
                super(view);
                plot_price = (TextView) view.findViewById(R.id.plot_price);
                plot_area = (TextView) view.findViewById(R.id.plot_area);
                plot_name = (TextView) view.findViewById(R.id.plot_name);
                main_lay = (LinearLayout) view.findViewById(R.id.main_lay);
            }
        }
    }

    public void successPlotDialog() {
        final Dialog dialog = new Dialog(PlotRemarkActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_plot_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK) {
            cid = data.getExtras().getString("ID");
            customer_name.setText(data.getExtras().getString("NAME"));
        }
    }
}