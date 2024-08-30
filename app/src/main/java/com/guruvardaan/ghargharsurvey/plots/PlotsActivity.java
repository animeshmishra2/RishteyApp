package com.guruvardaan.ghargharsurvey.plots;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_PLOT_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.agent.DirectPropertySaleActivity;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.customers.PlotRemarkActivity;
import com.guruvardaan.ghargharsurvey.model.PlotModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlotsActivity extends BaseActivity {

    RecyclerView plot_recyclerView;
    ProgressBar progress;
    HorizontalScrollView myHorizontalScrollView;
    UserDetails userDetails;
    ArrayList<PlotModel> plotModels;
    ArrayList<PlotModel> newPlotModels;
    TextView header_txt, view_map;
    ImageView back_button;
    ArrayList<PlotModel> sels_p;
    TextView t_plot, sel_plot, booked_plots, free_plots;
    LinearLayout lay_st;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_plots, frameLayout);
        lay_st = (LinearLayout) findViewById(R.id.lay_st);
        anim = new AlphaAnimation(0.4f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        back_button = (ImageView) findViewById(R.id.back_button);
        view_map = (TextView) findViewById(R.id.view_map);
        booked_plots = (TextView) findViewById(R.id.booked_plots);
        free_plots = (TextView) findViewById(R.id.free_plots);
        t_plot = (TextView) findViewById(R.id.t_plot);
        sel_plot = (TextView) findViewById(R.id.sel_plot);
        sels_p = new ArrayList<>();
        userDetails = new UserDetails(getApplicationContext());
        header_txt = (TextView) findViewById(R.id.header_txt);
        myHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.myHorizontalScrollView);
        plot_recyclerView = (RecyclerView) findViewById(R.id.plot_recyclerView);
        progress = (ProgressBar) findViewById(R.id.progress);
        plotModels = new ArrayList<>();
        newPlotModels = new ArrayList<>();
        header_txt.setText(getIntent().getExtras().getString("BID") + " Block Plots");
        getAllPlots();
        if (userDetails.getUser_type().equalsIgnoreCase("1")) {
            t_plot.setText("0 Plots");
            sel_plot.setText("Book Plot");
        } else {
            t_plot.setText("0 Plots");
            sel_plot.setText("Book Plot");
        }
        view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("PR", getIntent().getExtras().getString("PR"));
                bundle.putString("BL", getIntent().getExtras().getString("BID"));
                bundle.putString("TITLE", getIntent().getExtras().getString("PR") + " - " + getIntent().getExtras().getString("BID"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lay_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sels_p.size() > 0 && sels_p.size() == 1) {
                    if (userDetails.getUser_type().equalsIgnoreCase("1")) {
                        Intent intent = new Intent(getApplicationContext(), DirectPropertySaleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("PROPERTYNAME", getIntent().getExtras().getString("PRN"));
                        bundle.putString("BLOCKNAME", getIntent().getExtras().getString("BID"));
                        bundle.putString("PLOTNAME", sels_p.get(0).getUniquename());
                        bundle.putString("AMOUNT", sels_p.get(0).getPlotAmount());
                        bundle.putString("PROPERTYID", getIntent().getExtras().getString("PR"));
                        bundle.putString("BLOCKID", getIntent().getExtras().getString("BLOCK"));
                        bundle.putString("DEFINE_PID", sels_p.get(0).getPk_prm_define_property_id());
                        bundle.putString("PLOTID", sels_p.get(0).getId());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 29);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), PlotRemarkActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("PM", sels_p);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 29);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select One Plot", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lay_st.startAnimation(anim);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 29 && resultCode == RESULT_OK) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    private void getAllPlots() {
        progress.setVisibility(View.VISIBLE);
        myHorizontalScrollView.setVisibility(View.GONE);
        String URL_CHECK = GET_PLOT_URL + userDetails.getuserid() + "/" + getIntent().getExtras().getString("PR") + "/" + getIntent().getExtras().getString("BID") + "/1";
        System.out.println("Mahendra " + URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        myHorizontalScrollView.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                plot_recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), getRow(object.getString("totalcolumns"))));
                            }
                            JSONArray Table1 = jsonObject.getJSONArray("Table1");
                            plotModels = new ArrayList<>();
                            int free = 0, book = 0;
                            for (int i = 0; i < Table1.length(); i++) {
                                JSONObject object = Table1.getJSONObject(i);
                                if (object.getString("status").equalsIgnoreCase("Booked")) {
                                    book = book + 1;
                                } else if (object.getString("status").equalsIgnoreCase("Vacant")) {
                                    free = free + 1;
                                }
                                plotModels.add(new PlotModel(object.getString("id"), object.getString("pk_prm_define_property_id"), object.getString("cellcount"), object.getString("uniquename"), object.getString("default_amount"), object.getString("default_area"), object.getString("customerID"), object.getString("option2"), object.getString("status"), "0", object.getString("plotAmount")));
                                newPlotModels.add(plotModels.get(i));
                            }
                            header_txt.setText(getIntent().getExtras().getString("BID") + " Block Total Plots (" + Table1.length() + ")");
                            booked_plots.setText("" + book);
                            free_plots.setText("" + free);
                           /* for (int i = 0; i < plotModels.size(); i++) {
                                if (searchCell((i + 1)) == null) {
                                    newPlotModels.add(new PlotModel("0", "0", "0", "NA", "0", "0", "0", "0", "0"));
                                } else {
                                    newPlotModels.add(searchCell((i + 1)));
                                }
                            }*/
                            plot_recyclerView.setAdapter(new PlotAdapter());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.setVisibility(View.GONE);
                        myHorizontalScrollView.setVisibility(View.VISIBLE);
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

    public int getRow(String ss) {
        try {
            int i = Integer.parseInt(ss);
            return i;
        } catch (Exception e) {
            return 0;
        }
    }

    class PlotAdapter extends RecyclerView.Adapter<PlotAdapter.PlotHolder> {

        @Override
        public int getItemCount() {
            return newPlotModels.size();
        }

        @Override
        public void onBindViewHolder(PlotAdapter.PlotHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.plot_area.setText(newPlotModels.get(position).getDefault_area() + " Sq ft");
            holder.plot_price.setText("\u20b9 " + (findPrice(newPlotModels.get(position).getDefault_amount()) * (findPrice(newPlotModels.get(position).getDefault_area()))));
            holder.plot_name.setText(newPlotModels.get(position).getUniquename());
            if (newPlotModels.get(position).getSel().equalsIgnoreCase("0")) {
                if (newPlotModels.get(position).getStatus().equalsIgnoreCase("Booked")) {
                    holder.main_lay.setBackgroundResource(R.drawable.booked_border);
                    holder.plot_name.setTextColor(Color.parseColor("#ffffff"));
                    holder.plot_area.setTextColor(Color.parseColor("#ffffff"));
                    holder.plot_price.setText("Booked");
                    holder.plot_area.setText("");
                    holder.plot_price.setTextColor(Color.parseColor("#ffffff"));
                    holder.plot_price.setVisibility(View.VISIBLE);
                } else if (newPlotModels.get(position).getStatus().equalsIgnoreCase("Vacant")) {
                    holder.main_lay.setBackgroundResource(R.drawable.vacant_border);
                    holder.plot_name.setTextColor(Color.parseColor("#000000"));
                    holder.plot_area.setTextColor(Color.parseColor("#000000"));
                    holder.plot_price.setTextColor(Color.parseColor("#000000"));
                    holder.plot_price.setText("Vacant");
                    holder.plot_price.setVisibility(View.VISIBLE);
                } else if (newPlotModels.get(position).getStatus().equalsIgnoreCase("Hold")) {
                    holder.main_lay.setBackgroundResource(R.drawable.hold_border);
                    holder.plot_name.setTextColor(Color.parseColor("#000000"));
                    holder.plot_area.setTextColor(Color.parseColor("#000000"));
                    holder.plot_area.setText("");
                    holder.plot_price.setTextColor(Color.parseColor("#000000"));
                    holder.plot_price.setText("Hold");
                    holder.plot_price.setVisibility(View.VISIBLE);
                } else {
                    holder.plot_name.setTextColor(Color.parseColor("#000000"));
                    holder.plot_price.setVisibility(View.VISIBLE);
                    holder.plot_area.setTextColor(Color.parseColor("#000000"));
                    holder.plot_price.setTextColor(Color.parseColor("#000000"));
                    holder.main_lay.setBackgroundResource(R.drawable.black_borders);
                }
            } else {
                holder.main_lay.setBackgroundResource(R.drawable.selected_border);
                holder.plot_name.setTextColor(Color.parseColor("#ffffff"));
                holder.plot_area.setTextColor(Color.parseColor("#ffffff"));
                holder.plot_price.setVisibility(View.VISIBLE);
                holder.plot_price.setText("Selected");
                holder.plot_price.setTextColor(Color.parseColor("#ffffff"));
            }
            //holder.plot_price.setTextColor(Color.parseColor("#000000"));
            // holder.plot_price.setVisibility(View.VISIBLE);
            holder.main_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (newPlotModels.get(position).getStatus().equalsIgnoreCase("Booked")) {
                        Toast.makeText(getApplicationContext(), "This plot is booked already", Toast.LENGTH_SHORT).show();
                    } else if (newPlotModels.get(position).getStatus().equalsIgnoreCase("Hold")) {
                        Toast.makeText(getApplicationContext(), "This plot is on Hold", Toast.LENGTH_SHORT).show();
                    } else if (newPlotModels.get(position).getStatus().equalsIgnoreCase("Vacant")) {
                        if (newPlotModels.get(position).getSel().equalsIgnoreCase("0")) {
                            newPlotModels.get(position).setSel("1");
                            if (!sels_p.contains(newPlotModels.get(position))) {
                                sels_p.add(newPlotModels.get(position));
                            }
                        } else {
                            if (sels_p.contains(newPlotModels.get(position))) {
                                sels_p.remove(newPlotModels.get(position));
                            }
                            newPlotModels.get(position).setSel("0");
                        }
                        t_plot.setText(sels_p.size() + " Plots");
                        notifyDataSetChanged();
                        // Toast.makeText(getApplicationContext(), "This plot is vacant", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "This plot is on hold", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

    public PlotModel searchCell(int k) {
        PlotModel plotModel = null;
        for (int i = 0; i < plotModels.size(); i++) {
            if (k == getRow(plotModels.get(i).getCellcount())) {
                plotModel = plotModels.get(i);
                break;
            }
        }
        return plotModel;
    }

    public float findPrice(String s) {
        try {
            float f = Float.parseFloat(s);
            return f;
        } catch (Exception e) {
            return 0.0f;
        }
    }

}