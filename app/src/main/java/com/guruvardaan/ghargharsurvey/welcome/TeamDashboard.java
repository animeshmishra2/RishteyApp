package com.guruvardaan.ghargharsurvey.welcome;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.guruvardaan.ghargharsurvey.voucher.VoucherAccountActivity;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.agent.AddAgentActivity;
import com.guruvardaan.ghargharsurvey.agent.AddNewClientActivity;
import com.guruvardaan.ghargharsurvey.agent.AgentPassbook;
import com.guruvardaan.ghargharsurvey.agent.CustomerDateActivity;
import com.guruvardaan.ghargharsurvey.agent.MyCustomers;
import com.guruvardaan.ghargharsurvey.agent.MyDownlines;
import com.guruvardaan.ghargharsurvey.agent.PayForCustomerActivity;
import com.guruvardaan.ghargharsurvey.agent.SettlementActivity;
import com.guruvardaan.ghargharsurvey.agent.TeamVoucherActivity;
import com.guruvardaan.ghargharsurvey.agent.ViewSettlementActivity;
import com.guruvardaan.ghargharsurvey.chat.ChatActivity;
import com.guruvardaan.ghargharsurvey.chat.UserChatActivity;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.customers.FindTextActivity;
import com.guruvardaan.ghargharsurvey.plots.AllRegistryActivity;
import com.guruvardaan.ghargharsurvey.plots.OurPropertiesActivity;
import com.guruvardaan.ghargharsurvey.visits.AddCarActivity;
import com.guruvardaan.ghargharsurvey.visits.AllTripCustomers;
import com.guruvardaan.ghargharsurvey.visits.MyVisitsActivity;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamDashboard extends BaseActivity {
    FrameLayout frameLayout;
    LinearLayout home_lay, visits_lay, plots_lay, pass_lay, change_language, user_login, cust_pass;
    ImageView home_img, visits_img, plots_img, pass_img;
    TextView home_txt, visits_txt, plots_txt, pass_txt;
    View home_view, visit_view, plot_view, pass_view;
    NavigationView nav_view;
    UserDetails userDetails;
    ImageView menu_list;
    CircleImageView profile_pic;
    DrawerLayout my_drawer_layout;
    ImageView logout;
    LinearLayout change_pwd_lay, add_new_customer, add_new_agent, bank_detail, gallery_lay, offers, my_downloads, downlines, plot_request, plot_sales, voucher, contact, about_lay, feedback;
    LinearLayout registry_request, registry_approval, pay_cust, pay_status, tr_video, homes, privacy_policy, refund_policy, terms_conditions, add_new_car, team_voucher, check_service, full_settlement, full_settlements, withdraw_voucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_dashboard);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // Request the overlay permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 112);
        }*/
        userDetails = new UserDetails(getApplicationContext());
        logout = (ImageView) findViewById(R.id.logout);
        menu_list = (ImageView) findViewById(R.id.menu_list);
        my_drawer_layout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        home_view = (View) findViewById(R.id.home_view);
        visit_view = (View) findViewById(R.id.visit_view);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        plot_view = (View) findViewById(R.id.plot_view);
        pass_view = (View) findViewById(R.id.pass_view);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        home_lay = (LinearLayout) findViewById(R.id.home_lay);
        visits_lay = (LinearLayout) findViewById(R.id.visits_lay);
        plots_lay = (LinearLayout) findViewById(R.id.plots_lay);
        pass_lay = (LinearLayout) findViewById(R.id.pass_lay);
        home_img = (ImageView) findViewById(R.id.home_img);
        visits_img = (ImageView) findViewById(R.id.visits_img);
        plots_img = (ImageView) findViewById(R.id.plots_img);
        pass_img = (ImageView) findViewById(R.id.pass_img);
        home_txt = (TextView) findViewById(R.id.home_txt);
        visits_txt = (TextView) findViewById(R.id.visits_txt);
        full_settlements = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.full_settlements);
        change_language = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.change_language);
        user_login = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.user_login);
        cust_pass = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.cust_pass);
        profile_pic = (CircleImageView) nav_view.getHeaderView(0).findViewById(R.id.profile_pic);
        change_pwd_lay = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.change_pwd_lay);
        add_new_customer = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.add_new_customer);
        add_new_car = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.add_new_car);
        add_new_agent = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.add_new_agent);
        bank_detail = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.bank_detail);
        gallery_lay = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.gallery_lay);
        offers = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.offers);
        team_voucher = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.team_voucher);
        my_downloads = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.my_downloads);
        plot_request = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.plot_request);
        downlines = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.downlines);
        plot_sales = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.plot_sales);
        voucher = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.voucher);
        contact = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.contact);
        about_lay = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.about_lay);
        feedback = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.feedback);
        registry_request = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.registry_request);
        registry_approval = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.registry_approval);
        pay_cust = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.pay_cust);
        pay_status = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.pay_status);
        tr_video = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.tr_video);
        homes = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.homes);
        privacy_policy = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.privacy_policy);
        refund_policy = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.refund_policy);
        terms_conditions = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.terms_conditions);
        check_service = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.check_service);
        full_settlement = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.full_settlement);
        withdraw_voucher = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.withdraw_voucher);
        plots_txt = (TextView) findViewById(R.id.plots_txt);
        pass_txt = (TextView) findViewById(R.id.pass_txt);
       /* Glide.with(getApplicationContext())
                .load(userDetails.getprofilef())
                .circleCrop().placeholder(R.drawable.man).dontAnimate()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(profile_pic);
*/
        visits_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickb("2");
            }
        });
        plots_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickb("3");
            }
        });
        homes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_drawer_layout.closeDrawer(Gravity.LEFT);
//                Intent intent = new Intent(getApplicationContext(), FindTextActivity.class);
//                startActivity(intent);
            }
        });

        cust_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerPassbook.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        add_new_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        team_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TeamVoucherActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        menu_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (my_drawer_layout.isDrawerOpen(Gravity.LEFT)) {
                    my_drawer_layout.closeDrawer(Gravity.LEFT);
                } else {
                    my_drawer_layout.openDrawer(Gravity.LEFT);
                    Glide.with(getApplicationContext())
                            .load(userDetails.getprofilef())
                            .circleCrop().placeholder(R.drawable.man).dontAnimate()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(profile_pic);
                }
            }
        });
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        change_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language_code = "en";
                if (userDetails.getLanguage().equalsIgnoreCase("en")) {
                    language_code = "hi";
                    userDetails.setLanguage("hi");
                } else {
                    userDetails.setLanguage("en");
                    language_code = "en";
                }
                setLocale(language_code);
            }
        });
        change_pwd_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_drawer_layout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intent);
            }
        });
        add_new_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewClientActivity.class);
                startActivityForResult(intent, 90);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        bank_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BankDetailsActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        gallery_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getApplicationContext(), OffersActivity.class);*/
                Intent intent = new Intent(getApplicationContext(), NewOfferActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        add_new_agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAgentActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        plot_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllTripCustomers.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        registry_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllRegistryActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        registry_approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistryRequest.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        pass_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickb("4");
            }
        });
        my_downloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DownloadsActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        downlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamDashboard.this, MyDownlines.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        plot_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamDashboard.this, MyCustomers.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamDashboard.this, SelectDateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POS", 0);
                bundle.putString("OPEN", "2");
                intent.putExtras(bundle);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamDashboard.this, ContactUsActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        about_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamDashboard.this, AboutActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamDashboard.this, UserChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("NEW", "1");
                intent.putExtras(bundle);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        pay_status.setVisibility(View.GONE);

        pay_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ComingSoonActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        pay_cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PayForCustomerActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        tr_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ComingSoonActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrivacyPolicy.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        refund_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RefundActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });

        check_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerDateActivity.class);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        full_settlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewSettlementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("OLD", "1");
                intent.putExtras(bundle);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        full_settlements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewSettlementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("OLD", "2");
                intent.putExtras(bundle);
                startActivity(intent);
                my_drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        withdraw_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_drawer_layout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(getApplicationContext(), VoucherAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    public void clickb(String s) {
        home_img.setColorFilter(getResources().getColor(R.color.boticon), android.graphics.PorterDuff.Mode.SRC_IN);
        visits_img.setColorFilter(getResources().getColor(R.color.boticon), android.graphics.PorterDuff.Mode.SRC_IN);
        plots_img.setColorFilter(getResources().getColor(R.color.boticon), android.graphics.PorterDuff.Mode.SRC_IN);
        pass_img.setColorFilter(getResources().getColor(R.color.boticon), android.graphics.PorterDuff.Mode.SRC_IN);
        home_view.setVisibility(View.INVISIBLE);
        plot_view.setVisibility(View.INVISIBLE);
        visit_view.setVisibility(View.INVISIBLE);
        pass_view.setVisibility(View.INVISIBLE);
        visits_txt.setTextColor(getResources().getColor(R.color.boticon));
        home_txt.setTextColor(getResources().getColor(R.color.boticon));
        plots_txt.setTextColor(getResources().getColor(R.color.boticon));
        pass_txt.setTextColor(getResources().getColor(R.color.boticon));
        if (s.equalsIgnoreCase("1")) {
            home_view.setVisibility(View.VISIBLE);
            home_img.setColorFilter(getResources().getColor(R.color.colorBlack), android.graphics.PorterDuff.Mode.SRC_IN);
            home_txt.setTextColor(getResources().getColor(R.color.colorBlack));
            if (TeamDashboard.this.getClass().getSimpleName().toString().indexOf("AgentActivity") < 0) {
                startActivity(new Intent(this, AgentActivity.class));
                finish();
            }
        } else if (s.equalsIgnoreCase("2")) {
            visit_view.setVisibility(View.VISIBLE);
            visits_img.setColorFilter(getResources().getColor(R.color.colorBlack), android.graphics.PorterDuff.Mode.SRC_IN);
            visits_txt.setTextColor(getResources().getColor(R.color.colorBlack));
            if (TeamDashboard.this.getClass().getSimpleName().toString().indexOf("MyVisitsActivity") < 0) {
                startActivity(new Intent(this, MyVisitsActivity.class));
                finish();
            }
        } else if (s.equalsIgnoreCase("3")) {
            plot_view.setVisibility(View.VISIBLE);
            plots_txt.setTextColor(getResources().getColor(R.color.colorBlack));
            plots_img.setColorFilter(getResources().getColor(R.color.colorBlack), android.graphics.PorterDuff.Mode.SRC_IN);
            if (TeamDashboard.this.getClass().getSimpleName().indexOf("PlotsActivity") < 0) {
                startActivity(new Intent(this, OurPropertiesActivity.class));
            }
        } else if (s.equalsIgnoreCase("4")) {
            pass_view.setVisibility(View.VISIBLE);
            pass_img.setColorFilter(getResources().getColor(R.color.colorBlack), android.graphics.PorterDuff.Mode.SRC_IN);
            pass_txt.setTextColor(getResources().getColor(R.color.colorBlack));
            if (TeamDashboard.this.getClass().getSimpleName().toString().indexOf("PassBookActivity") < 0) {
                startActivity(new Intent(this, AgentPassbook.class));
            }

        }

    }

    public void showLogoutDialog() {
        final Dialog dialog = new Dialog(TeamDashboard.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                userDetails.clearAll();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showCloseApp() {
        final Dialog dialog = new Dialog(TeamDashboard.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.close_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (my_drawer_layout.isDrawerOpen(Gravity.LEFT)) {
            my_drawer_layout.closeDrawer(Gravity.LEFT);
        } else {
            showCloseApp();
        }
    }

}