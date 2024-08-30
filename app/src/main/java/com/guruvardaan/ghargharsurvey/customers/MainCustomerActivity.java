package com.guruvardaan.ghargharsurvey.customers;

import static com.guruvardaan.ghargharsurvey.config.Config.CUSTOMER_PLOT;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.ViewPagerCustomDuration;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.BannerModel;
import com.guruvardaan.ghargharsurvey.plots.OurPropertiesActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainCustomerActivity extends CustomerDashboard {

    ViewPagerCustomDuration view_pager;
    TextView marqueeText;
    CardView search_card;
    LinearLayout plot_req, pay_amount, plot_availability;
    TextView submitted_payment, remaining_payment, total_plots, total_area, submitted, pending, remaining;
    ProgressBar progress;
    LinearLayout lin_layout;
    UserDetails userDetails;
    CustomPagerAdapter customPagerAdapter;
    ArrayList<BannerModel> bannerModels;
    Timer timer;
    double pen, tot, rem, paids;
    int ar;
    int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_dash, frameLayout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        view_pager = findViewById(R.id.view_pager);
        view_pager.getLayoutParams().height = (int) (height / 3.2);
        bannerModels = new ArrayList<>();
        pen = 0;
        tot = 0;
        rem = 0;
        paids = 0;
        ar = 0;
        clickb("1");
        customPagerAdapter = new CustomPagerAdapter();
        view_pager.setAdapter(customPagerAdapter);
        userDetails = new UserDetails(getApplicationContext());
        marqueeText = (TextView) findViewById(R.id.marqueeText);
        search_card = (CardView) findViewById(R.id.search_card);
        plot_req = (LinearLayout) findViewById(R.id.plot_req);
        pay_amount = (LinearLayout) findViewById(R.id.pay_amount);
        plot_availability = (LinearLayout) findViewById(R.id.plot_availability);
        submitted_payment = (TextView) findViewById(R.id.submitted_payment);
        remaining_payment = (TextView) findViewById(R.id.remaining_payment);
        total_plots = (TextView) findViewById(R.id.total_plots);
        total_area = (TextView) findViewById(R.id.total_area);
        submitted = (TextView) findViewById(R.id.submitted);
        pending = (TextView) findViewById(R.id.pending);
        remaining = (TextView) findViewById(R.id.remaining);
        progress = (ProgressBar) findViewById(R.id.progress);
        lin_layout = (LinearLayout) findViewById(R.id.lin_layout);
        progress.setVisibility(View.GONE);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                view_pager.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bannerModels.size() > 0) {
                            view_pager.setCurrentItem((view_pager.getCurrentItem() + 1) % bannerModels.size(), true);
                        }
                    }
                });
            }
        };
        if (timer == null) {
            timer = new Timer();
            timer.schedule(timerTask, 4000, 4000);
        }
        getData();
        plot_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OurPropertiesActivity.class);
                startActivity(intent);
            }
        });
        plot_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPlotRequestActivity.class);
                startActivity(intent);
            }
        });
        pay_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PayCustomerActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getData() {
        try {
            JSONObject jsonObject = new JSONObject(userDetails.getCust_json());
            JSONArray banners = jsonObject.getJSONArray("Table1");
            bannerModels = new ArrayList<>();
            for (int i = 0; i < banners.length(); i++) {
                JSONObject object = banners.getJSONObject(i);
                bannerModels.add(new BannerModel(object.getString("Id"), object.getString("type"), object.getString("img_name"), object.getString("description")));
            }
            customPagerAdapter.notifyDataSetChanged();
            JSONArray Table2 = jsonObject.getJSONArray("Table2");
            String news = "";
            for (int i = 0; i < Table2.length(); i++) {
                JSONObject object = Table2.getJSONObject(i);
                news = news + " " + object.getString("news");
            }
            marqueeText.setText(news);
            marqueeText.setSelected(true);
            getDashboardData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class CustomPagerAdapter extends PagerAdapter {


        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.banner_item, collection, false);
            ImageView banner_view = (ImageView) layout.findViewById(R.id.banner_view);
            CardView main_card = (CardView) layout.findViewById(R.id.main_card);
            main_card.getLayoutParams().height = (int) (height / 3.2);
            Glide.with(getApplicationContext())
                    .load(bannerModels.get(position).getImg_name())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(12))).placeholder(R.drawable.no_req)
                    .into(banner_view);
            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return bannerModels.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            BannerModel customPagerEnum = bannerModels.get(position);
            return customPagerEnum.getDescription();
        }

    }

    private void getDashboardData() {
        progress.setVisibility(View.VISIBLE);
        String URL_CHECK = CUSTOMER_PLOT + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid();
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                rem = rem + getDatas(object.getString("remaining").trim());
                                pen = pen + getDatas(object.getString("cPending").trim());
                                tot = tot + getDatas(object.getString("total").trim());
                                paids = paids + getDatas(object.getString("paid").trim());
                                ar = ar + getAreas(object.getString("sqft").trim());
                            }
                            total_plots.setText("" + Table.length());
                            remaining.setText("\u20b9" + rem);
                            submitted.setText("\u20b9" + paids);
                            submitted_payment.setText("\u20b9" + paids);
                            remaining.setText("\u20b9" + rem);
                            pending.setText("\u20b9" + pen);
                            remaining_payment.setText("\u20b9" + rem);
                            total_area.setText("" + ar);

                        } catch (Exception e) {
                            e.printStackTrace();
                            total_plots.setText("0");
                            remaining.setText("\u20b9" + rem);
                            submitted.setText("\u20b9" + paids);
                            submitted_payment.setText("\u20b9" + paids);
                            remaining.setText("\u20b9" + rem);
                            pending.setText("\u20b9" + pen);
                            remaining_payment.setText("\u20b9" + rem);
                            total_area.setText("" + ar);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        total_plots.setText("0");
                        remaining.setText("\u20b9" + rem);
                        submitted.setText("\u20b9" + paids);
                        submitted_payment.setText("\u20b9" + paids);
                        remaining.setText("\u20b9" + rem);
                        pending.setText("\u20b9" + pen);
                        remaining_payment.setText("\u20b9" + rem);
                        total_area.setText("" + ar);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public double getDatas(String ss) {
        try {
            double d = Double.parseDouble(ss);
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getAreas(String ss) {
        try {
            Integer d = Integer.parseInt(ss);
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        clickb("1");
    }
}