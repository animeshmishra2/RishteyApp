package com.guruvardaan.ghargharsurvey.agent;

import static com.guruvardaan.ghargharsurvey.config.Config.AGENT_PLOT_REQUEST;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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

public class AgentPlotRequest extends BaseActivity {

    RecyclerView plot_recycler;
    EditText remarks, select_customer;
    ImageView back_button;
    TextView submit_request;
    ProgressBar progress;
    LinearLayout lin_lay;
    ArrayList<PlotModel> plotModels;
    UserDetails userDetails;
    ProgressDialog pd;
    ImageView microphone;
    String c_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.agent_plot_remark, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        c_id = "";
        microphone = (ImageView) findViewById(R.id.microphone);
        plot_recycler = (RecyclerView) findViewById(R.id.plot_recycler);
        plot_recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        remarks = (EditText) findViewById(R.id.remarks);
        plotModels = (ArrayList<PlotModel>) getIntent().getExtras().getSerializable("PM");
        lin_lay = (LinearLayout) findViewById(R.id.lin_lay);
        progress = (ProgressBar) findViewById(R.id.progress);
        back_button = (ImageView) findViewById(R.id.back_button);
        select_customer = (EditText) findViewById(R.id.select_customer);
        submit_request = (TextView) findViewById(R.id.submit_request);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        plot_recycler.setAdapter(new PlotAdapter());
        select_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerSelectActivity.class);
                startActivityForResult(intent, 22);
            }
        });
        submit_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c_id.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Customer For Whom You Want To Buy Plot", Toast.LENGTH_SHORT).show();
                } else if (remarks.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter Remarks", Toast.LENGTH_SHORT).show();
                } else {
                    createJSON();
                }
            }
        });
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceRecognitionActivity();
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
            main_obj.put("advisorId", userDetails.getuserid());
            main_obj.put("userid", c_id);
            main_obj.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            System.out.println("Animesh " + main_obj.toString());
            addPlotRequest(main_obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addPlotRequest(String jsons) {
        pd = ProgressDialog.show(AgentPlotRequest.this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                AGENT_PLOT_REQUEST,
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
        public void onBindViewHolder(PlotHolder holder, @SuppressLint("RecyclerView") final int position) {
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
        public PlotHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.plots_row, viewGroup, false);
            PlotHolder listHolder = new PlotHolder(mainGroup);
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
        final Dialog dialog = new Dialog(AgentPlotRequest.this);
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
            c_id = data.getExtras().getString("ID");
            select_customer.setText(data.getExtras().getString("NAME"));
        }
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) {
                remarks.setText(matches.get(0));
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
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }

}