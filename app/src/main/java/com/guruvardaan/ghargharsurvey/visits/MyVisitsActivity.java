package com.guruvardaan.ghargharsurvey.visits;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_ALL_VISITS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import com.guruvardaan.ghargharsurvey.model.PlotRequestModel;
import com.guruvardaan.ghargharsurvey.model.VisitModel;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.welcome.AgentActivity;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyVisitsActivity extends BaseActivity {

    RecyclerView visit_recycler;
    LinearLayout request_new_trip;
    ArrayList<VisitModel> visitModels;
    ArrayList<PlotRequestModel> plotRequestModels;
    ProgressDialog pd;
    UserDetails userDetails;
    ImageView back_button;
    Animation anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_my_visits, frameLayout);
        visitModels = new ArrayList<>();
        anim = new AlphaAnimation(0.4f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        plotRequestModels = new ArrayList<>();
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        request_new_trip = (LinearLayout) findViewById(R.id.request_new_trip);
        visit_recycler = (RecyclerView) findViewById(R.id.visit_recycler);
        visit_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        visit_recycler.setAdapter(new VisitAdapter());
        request_new_trip.startAnimation(anim);
        request_new_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTripActivity.class);
                startActivityForResult(intent, 234);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getAllVisits();

    }


    class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.VisitHolder> {

        @Override
        public int getItemCount() {
            return visitModels.size();
        }

        @Override
        public void onBindViewHolder(VisitHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.source_txt.setText(visitModels.get(position).getStartingAddress().split(",")[0]);
            holder.date_txt.setText(getForDate(visitModels.get(position).getDtOfVisit()));
            if (getForDate(visitModels.get(position).getDtOfVisit()).equalsIgnoreCase(getMainDate())) {
                holder.main_lay.setBackgroundResource(R.drawable.top_bottom_shadow2);
            } else {
                holder.main_lay.setBackgroundResource(R.drawable.top_bottom_shadow);
            }
            //holder.expense_txt.setText("Probable Expense : \u20b9 " + visitModels.get(position).getTotalFare());
            holder.customers.setText(visitModels.get(position).getTotalCustomers() + " Customers Added");
            holder.status_txt.setText(visitModels.get(position).getStatus());
            if (visitModels.get(position).getStatus().equalsIgnoreCase("Rejected")) {
                holder.status_img.setImageResource(R.drawable.rejected);
                holder.status_txt.setTextColor(Color.parseColor("#ff0000"));
                holder.trips.setVisibility(View.INVISIBLE);
            } else if (visitModels.get(position).getStatus().equalsIgnoreCase("Under Process")) {
                holder.status_img.setImageResource(R.drawable.pending);
                holder.status_txt.setTextColor(Color.parseColor("#FFAF49"));
                holder.status_txt.setText("Pending");
                holder.trips.setVisibility(View.INVISIBLE);
            } else if (visitModels.get(position).getStatus().equalsIgnoreCase("Approved")) {
                holder.status_img.setImageResource(R.drawable.approved);
                holder.status_txt.setTextColor(Color.parseColor("#05c505"));
                //holder.trips.setVisibility(View.VISIBLE);
                if ((System.currentTimeMillis() - getMilliseconds(visitModels.get(position).getDtOfVisit() + " 00:00:01")) >= 0) {
                    holder.trips.setVisibility(View.VISIBLE);
                    holder.trips.setText("Start Trip");
                } else {
                    holder.trips.setVisibility(View.INVISIBLE);
                }
            } else if (visitModels.get(position).getStatus().equalsIgnoreCase("Visit Started")) {
                holder.status_img.setImageResource(R.drawable.approved);
                holder.status_txt.setTextColor(Color.parseColor("#05c505"));
                holder.status_txt.setText(visitModels.get(position).getStatus());
                holder.trips.setVisibility(View.VISIBLE);
                holder.trips.setText("Stop Trip");
            } else if (visitModels.get(position).getStatus().equalsIgnoreCase("Visit Completed")) {
                holder.status_img.setImageResource(R.drawable.completed);
                holder.status_txt.setText("Completed");
                holder.status_txt.setTextColor(Color.parseColor("#FF8C19"));
                holder.trips.setVisibility(View.INVISIBLE);
            }
            if (getIDs(visitModels.get(position).getAct_customers()) > getIDs(visitModels.get(position).getTotalCustomers())) {
                holder.add_new_cst.setVisibility(View.VISIBLE);
            } else {
                holder.add_new_cst.setVisibility(View.INVISIBLE);
            }
          /*  if ((getMilliseconds(visitModels.get(position).getDtOfVisit() + " 23:59:00") - System.currentTimeMillis()) <= 0) {
                holder.status_img.setImageResource(R.drawable.completed);
                holder.status_txt.setText("Completed");
                holder.status_txt.setTextColor(Color.parseColor("#FF8C19"));
                holder.trips.setVisibility(View.INVISIBLE);
            }*/
            holder.add_new_cst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CreateCustomersActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("VID", visitModels.get(position).getId());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 220);
                }
            });
            holder.trips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), VisitOpenActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("LATS", visitModels.get(position).getStarting_latlong());
                    bundle.putString("TID", visitModels.get(position).getId());
                    bundle.putString("STATUS", visitModels.get(position).getStatus());
                    bundle.putString("ACT", visitModels.get(position).getAct_customers());
                    bundle.putInt("TOT", getIDs(visitModels.get(position).getTotalCustomers()));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 309);
                }
            });
            holder.view_visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ViewVisitActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TID", visitModels.get(position).getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.visit_row, viewGroup, false);
            VisitHolder listHolder = new VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {

            TextView status_txt, trips, source_txt, customers, view_visit, date_txt, add_new_cst;
            ImageView status_img;
            RelativeLayout main_lay;

            public VisitHolder(View view) {
                super(view);
                main_lay = (RelativeLayout) view.findViewById(R.id.main_lay);
                trips = (TextView) view.findViewById(R.id.trips);
                status_txt = (TextView) view.findViewById(R.id.status_txt);
                status_img = (ImageView) view.findViewById(R.id.status_img);
                source_txt = (TextView) view.findViewById(R.id.source_txt);
                customers = (TextView) view.findViewById(R.id.customers);
                view_visit = (TextView) view.findViewById(R.id.view_visit);
                date_txt = (TextView) view.findViewById(R.id.date_txt);
                add_new_cst = (TextView) view.findViewById(R.id.add_new_cst);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AgentActivity.class);
        startActivity(intent);
        finish();
    }

    private void getAllVisits() {
        pd = ProgressDialog.show(MyVisitsActivity.this);
        String URL_CHECK = GET_ALL_VISITS + "K9154289-68a1-80c7-e009-2asdccf7b0d/1/" + userDetails.getuserid();
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
                                visitModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    visitModels.add(new VisitModel(object.getString("id"), object.getString("vehicleType"), object.getString("carNumber"), object.getString("carName"), object.getString("startingAddress"), object.getString("dtOfVisit"), object.getString("submittedDt"), object.getString("remark"), object.getString("starting_latlong"), object.getString("token_amount"), object.getString("km"), object.getString("amountPerKm"), object.getString("totalFare"), object.getString("totalCustomers"), object.getString("status"), object.getString("act_customers")));
                                }
                                Collections.sort(visitModels, new Comparator<VisitModel>() {
                                    @Override
                                    public int compare(VisitModel o1, VisitModel o2) {
                                        return getIDs(o1.getId()) - getIDs(o2.getId());
                                    }
                                });
                                Collections.reverse(visitModels);
                                visit_recycler.setAdapter(new VisitAdapter());
                            } else {
                                Toast.makeText(getApplicationContext(), "No Visits Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 234 && resultCode == RESULT_OK) {
            getAllVisits();
        }
        if (requestCode == 309 && resultCode == RESULT_OK) {
            getAllVisits();
        }
        if (requestCode == 220 && resultCode == RESULT_OK) {
            plotRequestModels.clear();
            plotRequestModels = (ArrayList<PlotRequestModel>) data.getExtras().getSerializable("SM");
            if (plotRequestModels.size() > 0) {
                createJSON(data.getExtras().getString("VID"));
            }
        }
    }

    public String getForDate(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd MMM, yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

    public long getMilliseconds(String dates) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = format1.parse(dates);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public void createJSON(String visitId) {
        try {
            JSONObject main_obj = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < plotRequestModels.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cust_id", plotRequestModels.get(i).getId());
                jsonArray.put(jsonObject);
            }
            main_obj.put("customers", jsonArray);
            main_obj.put("agent_id", userDetails.getuserid());
            main_obj.put("visitId", visitId);
            main_obj.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            System.out.println("Animesh " + main_obj.toString());
            addCustomers(main_obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCustomers(String jsons) {
        pd = ProgressDialog.show(MyVisitsActivity.this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.ADD_VISIT_CST,
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
                Toast.makeText(getApplicationContext(), "Customers Added Successfully", Toast.LENGTH_SHORT).show();
                getAllVisits();
            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Visit Adding Failed", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public String getMainDate() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");
        System.out.println(formatter.format(date));
        return formatter.format(date);
    }
}