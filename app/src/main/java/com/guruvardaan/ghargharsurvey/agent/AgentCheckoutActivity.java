package com.guruvardaan.ghargharsurvey.agent;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.PlotModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AgentCheckoutActivity extends BaseActivity {

    RecyclerView plot_recycler;
    ImageView back_button;
    ArrayList<PlotModel> plotModels;
    UserDetails userDetails;
    TextView total_qty, item_price, coup_txt, subtotal, charge_amount;
    String c_id = "";
    Button proceed;
    LinearLayout card_lay, wall_lay, net_lay, upi_lay;
    ImageView card_img, net_img, wall_img, upi_img;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.agent_checkout, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        c_id = "";
        card_img = (ImageView) findViewById(R.id.card_img);
        net_img = (ImageView) findViewById(R.id.net_img);
        wall_img = (ImageView) findViewById(R.id.wall_img);
        upi_img = (ImageView) findViewById(R.id.upi_img);
        card_lay = (LinearLayout) findViewById(R.id.card_lay);
        wall_lay = (LinearLayout) findViewById(R.id.wall_lay);
        net_lay = (LinearLayout) findViewById(R.id.net_lay);
        upi_lay = (LinearLayout) findViewById(R.id.upi_lay);
        total_qty = (TextView) findViewById(R.id.total_qty);
        item_price = (TextView) findViewById(R.id.item_price);
        coup_txt = (TextView) findViewById(R.id.coup_txt);
        subtotal = (TextView) findViewById(R.id.subtotal);
        charge_amount = (TextView) findViewById(R.id.charge_amount);
        proceed = (Button) findViewById(R.id.proceed);
        plot_recycler = (RecyclerView) findViewById(R.id.plot_recycler);
        plot_recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        plotModels = (ArrayList<PlotModel>) getIntent().getExtras().getSerializable("PM");
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        plot_recycler.setAdapter(new PlotAdapter());

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Payment Gateway", Toast.LENGTH_SHORT).show();
            }
        });
        total_qty.setText(plotModels.size() + " Plots");
        item_price.setText("\u20b9 " + findAllTotal());
        coup_txt.setText("\u20b9 " + ((findAllTotal() * 10)) / 100);
        subtotal.setText("\u20b9 " + df.format((((findAllTotal() * 10)) / 100) + 59500));
        charge_amount.setText("\u20b9 " + df.format((((findAllTotal() * 10)) / 100) + 59500));
        setData("card");
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
        final Dialog dialog = new Dialog(AgentCheckoutActivity.this);
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

    public double findAllTotal() {
        double d = 0;
        for (int i = 0; i < plotModels.size(); i++) {
            d = d + ((parseData((plotModels.get(i).getDefault_area()))) * parseData((plotModels.get(i).getDefault_amount())));
        }
        return d;
    }

    public double parseData(String dd) {
        try {
            double ds = Double.parseDouble(dd);
            return ds;
        } catch (Exception e) {
            return 0;
        }
    }

    public void setData(String data) {
        if (data.equalsIgnoreCase("card")) {
            card_img.setImageResource(R.drawable.tick_green);
            wall_img.setImageResource(R.drawable.tick_grey);
            upi_img.setImageResource(R.drawable.tick_grey);
            net_img.setImageResource(R.drawable.tick_grey);
        } else if (data.equalsIgnoreCase("wallet")) {
            card_img.setImageResource(R.drawable.tick_grey);
            wall_img.setImageResource(R.drawable.tick_green);
            upi_img.setImageResource(R.drawable.tick_grey);
            net_img.setImageResource(R.drawable.tick_grey);
        } else if (data.equalsIgnoreCase("upi")) {
            card_img.setImageResource(R.drawable.tick_grey);
            wall_img.setImageResource(R.drawable.tick_grey);
            upi_img.setImageResource(R.drawable.tick_green);
            net_img.setImageResource(R.drawable.tick_grey);
        } else if (data.equalsIgnoreCase("netbanking")) {
            card_img.setImageResource(R.drawable.tick_grey);
            wall_img.setImageResource(R.drawable.tick_grey);
            upi_img.setImageResource(R.drawable.tick_grey);
            net_img.setImageResource(R.drawable.tick_green);
        } else {
            card_img.setImageResource(R.drawable.tick_green);
            wall_img.setImageResource(R.drawable.tick_grey);
            upi_img.setImageResource(R.drawable.tick_grey);
            net_img.setImageResource(R.drawable.tick_grey);
        }
    }

}