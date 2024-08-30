package com.guruvardaan.ghargharsurvey.welcome;


import static com.guruvardaan.ghargharsurvey.config.Config.DASHBOARD_URL;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.brkckr.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.agent.AddNewAgentActivity;
import com.guruvardaan.ghargharsurvey.agent.AddNewClientActivity;
import com.guruvardaan.ghargharsurvey.agent.CompleteAgentActivity;
import com.guruvardaan.ghargharsurvey.agent.DirectAgentActivity;
import com.guruvardaan.ghargharsurvey.agent.MyCustomers;
import com.guruvardaan.ghargharsurvey.agent.PayForCustomerActivity;
import com.guruvardaan.ghargharsurvey.agent.SearchActivity;
import com.guruvardaan.ghargharsurvey.agent.TodayBusiness;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.ViewPagerCustomDuration;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.BannerModel;
import com.guruvardaan.ghargharsurvey.plots.PlotRequestForm;
import com.guruvardaan.ghargharsurvey.plots.ViewMapActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AgentActivity extends TeamDashboard {
    ArrayList<BannerModel> bannerModels;
    Timer timer;
    ViewPagerCustomDuration view_pager;
    LinearLayout team_count, self_team, self_plot, team_plot, add_agents, add_clients, p_request, view_map, add_agent, add_customer;
    TextView marqueeText;
    UserDetails userDetails;
    ProgressBar progress;
    LinearLayout lin_layout;
    CardView search_card;

    SwipeRefreshLayout refresh_layout;
    CustomPagerAdapter customPagerAdapter;
    TextView self_cust, team_cust, self_agent, team_agent, self_business, team_business, credit, debit, balance, self_total_business, team_total_business, user_name, user_id;
    int height = 0;
    TextView profile_percentage;
    ImageView profile_image;
    CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.member_dash, frameLayout);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //getSupportActionBar().hide();
        profile_percentage = (TextView) findViewById(R.id.profile_percentage);
        add_agents = (LinearLayout) findViewById(R.id.add_agents);
        add_clients = (LinearLayout) findViewById(R.id.add_clients);
        user_id = (TextView) findViewById(R.id.user_id);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        user_name = (TextView) findViewById(R.id.user_name);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        p_request = (LinearLayout) findViewById(R.id.p_request);
        self_total_business = (TextView) findViewById(R.id.self_total_business);
        team_total_business = (TextView) findViewById(R.id.team_total_business);
        search_card = (CardView) findViewById(R.id.search_card);
        refresh_layout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        self_cust = (TextView) findViewById(R.id.self_cust);
        team_cust = (TextView) findViewById(R.id.team_cust);
        marqueeText = (TextView) findViewById(R.id.marqueeText);
        self_agent = (TextView) findViewById(R.id.self_agent);
        view_map = (LinearLayout) findViewById(R.id.view_map);
        add_agent = (LinearLayout) findViewById(R.id.add_agent);
        add_customer = (LinearLayout) findViewById(R.id.add_customer);
        team_agent = (TextView) findViewById(R.id.team_agent);
        self_plot = (LinearLayout) findViewById(R.id.self_plot);
        team_plot = (LinearLayout) findViewById(R.id.team_plot);
        self_business = (TextView) findViewById(R.id.self_business);
        team_business = (TextView) findViewById(R.id.team_business);
        credit = (TextView) findViewById(R.id.credit);
        debit = (TextView) findViewById(R.id.debit);
        balance = (TextView) findViewById(R.id.balance);
        progress = (ProgressBar) findViewById(R.id.progress);
        lin_layout = (LinearLayout) findViewById(R.id.lin_layout);
        userDetails = new UserDetails(getApplicationContext());
        bannerModels = new ArrayList<>();
        team_count = (LinearLayout) findViewById(R.id.team_count);
        self_team = (LinearLayout) findViewById(R.id.self_team);
        view_pager = (ViewPagerCustomDuration) findViewById(R.id.view_pager);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        view_pager.getLayoutParams().height = (int) (height / 3.2);
        customPagerAdapter = new CustomPagerAdapter();
        view_pager.setAdapter(customPagerAdapter);
        getDashboardData();
        marqueeText.setSelected(true);
        user_name.setText(userDetails.getName().toUpperCase());
        user_id.setText("Mobile : " + userDetails.getMobile_no() + "    ID : " + userDetails.getuserid().toUpperCase());
        Glide.with(getApplicationContext())
                .load("http://erp.rishteytownship.com/Upload/Profile/" + userDetails.getAdvisor_image())
                .circleCrop().placeholder(R.drawable.man).dontAnimate().skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(profile_image);
        view_pager.setScrollDurationFactor(5);
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
        team_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, DirectAgentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        self_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, DirectAgentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        self_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, MyCustomers.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        search_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        team_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, MyCustomers.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        self_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, TodayBusiness.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        team_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, TodayBusiness.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, SelectDateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                bundle.putString("OPEN", "2");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, SelectDateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 1);
                bundle.putString("OPEN", "2");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, SelectDateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 2);
                bundle.putString("OPEN", "2");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        add_agents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccountDetailsActivity.class);
                startActivity(intent);
                /*Intent intent = new Intent(getApplicationContext(), AddAgentActivity.class);
                startActivity(intent);*/
            }
        });
        add_clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PayForCustomerActivity.class);
                startActivity(intent);
            }
        });
        p_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlotRequestForm.class);
                startActivity(intent);
            }
        });
        add_agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewAgentActivity.class);
                startActivity(intent);
            }
        });
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewClientActivity.class);
                startActivity(intent);
            }
        });
        team_total_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, MyCustomers.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        self_total_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentActivity.this, MyCustomers.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", "Rishtey MAP");
                bundle.putString("PR", "PR00001");
                bundle.putString("BL", "A");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_layout.setRefreshing(false);
                getDashboardData();
            }
        });
        profile_percentage.setText(getFillDetails() + "%");
        circularProgressBar.setProgressValue(getFillDetails());
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CompleteAgentActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        clickb("1");
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
        lin_layout.setVisibility(View.GONE);
        String URL_CHECK = DASHBOARD_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid() + "/" + getCurrentDate() + "/Agent";
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        lin_layout.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("Table");
                            if (jsonArray.length() > 0) {
                                JSONObject object = jsonArray.getJSONObject(0);
                                self_cust.setText(object.getString("directPlotSaleCustomer"));
                                team_cust.setText(object.getString("teamPlotSaleCustomer"));
                                self_agent.setText(object.getString("directAgentJoining"));
                                team_agent.setText(object.getString("teamAgentJoining"));
                                self_business.setText("\u20b9" + formatAmount(object.getString("todayBusinessSelf")));
                                team_business.setText("\u20b9" + formatAmount(object.getString("todayBusinessTeam")));
                                credit.setText("\u20b9" + formatAmount(object.getString("totalCredit")));
                                debit.setText("\u20b9" + formatAmount(object.getString("totalDebit")));
                                balance.setText("\u20b9" + formatAmount(object.getString("totalLeft")));
                                self_total_business.setText("\u20b9" + formatAmount(object.getString("totalSelfBusiness")));
                                team_total_business.setText("\u20b9" + formatAmount(object.getString("totalTeamBusiness")));
                            } else {
                                self_cust.setText("0");
                                team_cust.setText("0");
                                self_agent.setText("0");
                                team_agent.setText("0");
                                self_business.setText("\u20b90");
                                team_business.setText("\u20b90");
                                credit.setText("\u20b90");
                                debit.setText("\u20b90");
                                balance.setText("\u20b90");
                                self_total_business.setText("\u20b90");
                                team_total_business.setText("\u20b90");
                            }

                            JSONArray banners = jsonObject.getJSONArray("Table1");
                            bannerModels = new ArrayList<>();
                            for (int i = 0; i < banners.length(); i++) {
                                JSONObject object = banners.getJSONObject(i);
                                bannerModels.add(new BannerModel(object.getString("Id"), object.getString("type"), object.getString("img_name"), object.getString("description")));
                            }
                            if (bannerModels.size() <= 0) {
                                view_pager.setVisibility(View.GONE);
                            }
                            customPagerAdapter.notifyDataSetChanged();
                            JSONArray Table2 = jsonObject.getJSONArray("Table2");
                            String news = "";
                            for (int i = 0; i < Table2.length(); i++) {
                                JSONObject object = Table2.getJSONObject(i);
                                news = news + " " + object.getString("news");
                            }
                            marqueeText.setText(news);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        lin_layout.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(System.currentTimeMillis());
        return formattedDate;
    }

    public int getFillDetails() {
        int n = 0;
        if (userDetails.getName().equalsIgnoreCase("") || userDetails.getName().equalsIgnoreCase("null") || userDetails.getName().equalsIgnoreCase("0") || userDetails.getName() == null) {
            n = n + 1;
        }
        if (userDetails.getAdvisordob().equalsIgnoreCase("") || userDetails.getAdvisordob().equalsIgnoreCase("null") || userDetails.getAdvisordob().equalsIgnoreCase("0") || userDetails.getAdvisordob() == null) {
            n = n + 1;
        }
        if (userDetails.getMobile_no().equalsIgnoreCase("") || userDetails.getMobile_no().equalsIgnoreCase("null") || userDetails.getMobile_no().equalsIgnoreCase("0") || userDetails.getMobile_no() == null) {
            n = n + 1;
        }
        if (userDetails.getAlternate_no().equalsIgnoreCase("") || userDetails.getAlternate_no().equalsIgnoreCase("null") || userDetails.getAlternate_no().equalsIgnoreCase("0") || userDetails.getAlternate_no() == null) {
            n = n + 1;
        }
        if (userDetails.getAdvisor_rank().equalsIgnoreCase("") || userDetails.getAdvisor_rank().equalsIgnoreCase("null") || userDetails.getAdvisor_rank().equalsIgnoreCase("0") || userDetails.getAdvisor_rank() == null) {
            n = n + 1;
        }
        if (userDetails.getWhatsapp().equalsIgnoreCase("") || userDetails.getWhatsapp().equalsIgnoreCase("null") || userDetails.getWhatsapp().equalsIgnoreCase("0") || userDetails.getWhatsapp() == null) {
            n = n + 1;
        }
        if (userDetails.getAdvisor_email().equalsIgnoreCase("") || userDetails.getAdvisor_email().equalsIgnoreCase("null") || userDetails.getAdvisor_email().equalsIgnoreCase("0") || userDetails.getAdvisor_email() == null) {
            n = n + 1;
        }
        if (userDetails.getFlatHouseNo().equalsIgnoreCase("") || userDetails.getFlatHouseNo().equalsIgnoreCase("null") || userDetails.getFlatHouseNo().equalsIgnoreCase("0") || userDetails.getFlatHouseNo() == null) {
            n = n + 1;
        }
        if (userDetails.getLandMark().equalsIgnoreCase("") || userDetails.getLandMark().equalsIgnoreCase("null") || userDetails.getLandMark().equalsIgnoreCase("0") || userDetails.getLandMark() == null) {
            n = n + 1;
        }
        if (userDetails.getCountry_id().equalsIgnoreCase("") || userDetails.getCountry_id().equalsIgnoreCase("null") || userDetails.getCountry_id().equalsIgnoreCase("0") || userDetails.getCountry_id() == null) {
            n = n + 1;
        }
        if (userDetails.getState_id().equalsIgnoreCase("") || userDetails.getState_id().equalsIgnoreCase("null") || userDetails.getState_id().equalsIgnoreCase("0") || userDetails.getState_id() == null) {
            n = n + 1;
        }
        if (userDetails.getCity_id().equalsIgnoreCase("") || userDetails.getCity_id().equalsIgnoreCase("null") || userDetails.getCity_id().equalsIgnoreCase("0") || userDetails.getCity_id() == null) {
            n = n + 1;
        }
        if (userDetails.getArea_id().equalsIgnoreCase("") || userDetails.getArea_id().equalsIgnoreCase("null") || userDetails.getArea_id().equalsIgnoreCase("0") || userDetails.getArea_id() == null) {
            n = n + 1;
        }
        if (userDetails.getTehsil().equalsIgnoreCase("") || userDetails.getTehsil().equalsIgnoreCase("null") || userDetails.getTehsil().equalsIgnoreCase("0") || userDetails.getTehsil() == null) {
            n = n + 1;
        }
        if (userDetails.getPin_code().equalsIgnoreCase("") || userDetails.getPin_code().equalsIgnoreCase("null") || userDetails.getPin_code().equalsIgnoreCase("0") || userDetails.getPin_code() == null) {
            n = n + 1;
        }
        if (userDetails.getVillage().equalsIgnoreCase("") || userDetails.getVillage().equalsIgnoreCase("null") || userDetails.getVillage().equalsIgnoreCase("0") || userDetails.getVillage() == null) {
            n = n + 1;
        }
        if (userDetails.getAcHolder().equalsIgnoreCase("") || userDetails.getAcHolder().equalsIgnoreCase("null") || userDetails.getAcHolder().equalsIgnoreCase("0") || userDetails.getAcHolder() == null) {
            n = n + 1;
        }
        if (userDetails.getAcNo().equalsIgnoreCase("") || userDetails.getAcNo().equalsIgnoreCase("null") || userDetails.getAcNo().equalsIgnoreCase("0") || userDetails.getAcNo() == null) {
            n = n + 1;
        }
        if (userDetails.getIFSC().equalsIgnoreCase("") || userDetails.getIFSC().equalsIgnoreCase("null") || userDetails.getIFSC().equalsIgnoreCase("0") || userDetails.getIFSC() == null) {
            n = n + 1;
        }
        if (userDetails.getBank().equalsIgnoreCase("") || userDetails.getBank().equalsIgnoreCase("null") || userDetails.getBank().equalsIgnoreCase("0") || userDetails.getBank() == null) {
            n = n + 1;
        }
        if (userDetails.getBranch().equalsIgnoreCase("") || userDetails.getBranch().equalsIgnoreCase("null") || userDetails.getBranch().equalsIgnoreCase("0") || userDetails.getBranch() == null) {
            n = n + 1;
        }
        if (userDetails.getNomineeName().equalsIgnoreCase("") || userDetails.getNomineeName().equalsIgnoreCase("null") || userDetails.getNomineeName().equalsIgnoreCase("0") || userDetails.getNomineeName() == null) {
            n = n + 1;
        }
        if (userDetails.getNomineeAge().equalsIgnoreCase("") || userDetails.getNomineeAge().equalsIgnoreCase("null") || userDetails.getNomineeAge().equalsIgnoreCase("0") || userDetails.getNomineeAge() == null) {
            n = n + 1;
        }
        if (userDetails.getNomineemobile().equalsIgnoreCase("") || userDetails.getNomineemobile().equalsIgnoreCase("null") || userDetails.getNomineemobile().equalsIgnoreCase("0") || userDetails.getNomineemobile() == null) {
            n = n + 1;
        }
        if (userDetails.getRelation().equalsIgnoreCase("") || userDetails.getRelation().equalsIgnoreCase("null") || userDetails.getRelation().equalsIgnoreCase("0") || userDetails.getRelation() == null) {
            n = n + 1;
        }
        if (userDetails.getFather().equalsIgnoreCase("") || userDetails.getFather().equalsIgnoreCase("null") || userDetails.getFather().equalsIgnoreCase("0") || userDetails.getFather() == null) {
            n = n + 1;
        }
        if (userDetails.getOccupationCategory().equalsIgnoreCase("") || userDetails.getOccupationCategory().equalsIgnoreCase("null") || userDetails.getOccupationCategory().equalsIgnoreCase("0") || userDetails.getOccupationCategory() == null) {
            n = n + 1;
        }
        if (userDetails.getOccupation().equalsIgnoreCase("") || userDetails.getOccupation().equalsIgnoreCase("null") || userDetails.getOccupation().equalsIgnoreCase("0") || userDetails.getOccupation() == null) {
            n = n + 1;
        }
        if (userDetails.getAdvisor_image().equalsIgnoreCase("") || userDetails.getAdvisor_image().equalsIgnoreCase("null") || userDetails.getAdvisor_image().equalsIgnoreCase("0") || userDetails.getAdvisor_image() == null) {
            n = n + 1;
        }
        if (userDetails.getAadharImage().equalsIgnoreCase("") || userDetails.getAadharImage().equalsIgnoreCase("null") || userDetails.getAadharImage().equalsIgnoreCase("0") || userDetails.getAadharImage() == null) {
            n = n + 1;
        }
        if (userDetails.getPanImage().equalsIgnoreCase("") || userDetails.getPanImage().equalsIgnoreCase("null") || userDetails.getPanImage().equalsIgnoreCase("0") || userDetails.getPanImage() == null) {
            n = n + 1;
        }
        if (userDetails.getAgentSign().equalsIgnoreCase("") || userDetails.getAgentSign().equalsIgnoreCase("null") || userDetails.getAgentSign().equalsIgnoreCase("0") || userDetails.getAgentSign() == null) {
            n = n + 1;
        }
        if (userDetails.getAadharNo().equalsIgnoreCase("") || userDetails.getAadharNo().equalsIgnoreCase("null") || userDetails.getAadharNo().equalsIgnoreCase("0") || userDetails.getAadharNo() == null) {
            n = n + 1;
        }
        if (userDetails.getPanNo().equalsIgnoreCase("") || userDetails.getPanNo().equalsIgnoreCase("null") || userDetails.getPanNo().equalsIgnoreCase("0") || userDetails.getPanNo() == null) {
            n = n + 1;
        }
        int perc = ((34 - n) * 100 / 34);
        return perc;
    }

    public String formatAmount(String amount) {
        try {
            double d = Double.parseDouble(amount);
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            String formattedAmount = decimalFormat.format(d);
            return formattedAmount;
        } catch (Exception e) {
            return "0";
        }
    }
}