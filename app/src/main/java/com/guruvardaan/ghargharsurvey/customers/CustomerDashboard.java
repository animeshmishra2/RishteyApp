package com.guruvardaan.ghargharsurvey.customers;

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

import com.google.android.material.navigation.NavigationView;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.agent.CheckDateActivity;
import com.guruvardaan.ghargharsurvey.agent.CustomerDateActivity;
import com.guruvardaan.ghargharsurvey.agent.MyReceiptActivity;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.plots.OurPropertiesActivity;
import com.guruvardaan.ghargharsurvey.welcome.AboutActivity;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.ChangePassword;
import com.guruvardaan.ghargharsurvey.welcome.ComingSoonActivity;
import com.guruvardaan.ghargharsurvey.welcome.ContactUsActivity;
import com.guruvardaan.ghargharsurvey.welcome.FeedbackActivity;
import com.guruvardaan.ghargharsurvey.welcome.GalleryActivity;
import com.guruvardaan.ghargharsurvey.welcome.MainActivity;
import com.guruvardaan.ghargharsurvey.welcome.OffersActivity;
import com.guruvardaan.ghargharsurvey.welcome.PrivacyPolicy;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerDashboard extends BaseActivity {

    FrameLayout frameLayout;
    ImageView menu_list, logout;
    DrawerLayout drawerLayout;
    NavigationView nav_view;
    UserDetails userDetails;
    ImageView home_img, visits_img, plots_img, pass_img;
    TextView home_txt, visits_txt, plots_txt, pass_txt;
    View home_view, visit_view, plot_view, pass_view;
    LinearLayout home_lay, visits_lay, plots_lay, pass_lay;
    LinearLayout change_language, buy_plot, my_passbooks, my_plots, change_pwd_lay, gallery_lay, offers, contact, about_lay, feedback;
    LinearLayout registry_request, pay_cust, tr_video, homes, privacy_policy, check_service;
    CircleImageView profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        home_view = (View) findViewById(R.id.home_view);
        userDetails = new UserDetails(getApplicationContext());
        logout = (ImageView) findViewById(R.id.logout);
        visit_view = (View) findViewById(R.id.visit_view);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        plot_view = (View) findViewById(R.id.plot_view);
        pass_view = (View) findViewById(R.id.pass_view);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        menu_list = (ImageView) findViewById(R.id.menu_list);
        home_lay = (LinearLayout) findViewById(R.id.home_lay);
        visits_lay = (LinearLayout) findViewById(R.id.visits_lay);
        plots_lay = (LinearLayout) findViewById(R.id.plots_lay);
        pass_lay = (LinearLayout) findViewById(R.id.pass_lay);
        home_img = (ImageView) findViewById(R.id.home_img);
        visits_img = (ImageView) findViewById(R.id.visits_img);
        plots_img = (ImageView) findViewById(R.id.plots_img);
        pass_img = (ImageView) findViewById(R.id.pass_img);
        home_txt = (TextView) findViewById(R.id.home_txt);
        plots_txt = (TextView) findViewById(R.id.plots_txt);
        pass_txt = (TextView) findViewById(R.id.pass_txt);
        visits_txt = (TextView) findViewById(R.id.visits_txt);
        drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        check_service = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.check_service);
        change_language = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.change_language);
        buy_plot = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.buy_plot);
        my_passbooks = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.my_passbooks);
        my_plots = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.my_plots);
        profile_pic = (CircleImageView) nav_view.getHeaderView(0).findViewById(R.id.profile_pic);
        change_pwd_lay = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.change_pwd_lay);
        gallery_lay = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.gallery_lay);
        offers = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.offers);
        contact = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.contact);
        about_lay = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.about_lay);
        feedback = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.feedback);
        registry_request = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.registry_request);
        registry_request.setVisibility(View.GONE);
        pay_cust = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.pay_cust);
        tr_video = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.tr_video);
        homes = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.homes);
        tr_video.setVisibility(View.GONE);
        privacy_policy = (LinearLayout) nav_view.getHeaderView(0).findViewById(R.id.privacy_policy);
        menu_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        plots_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickb("3");
            }
        });
        visits_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickb("2");
            }
        });

        pass_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickb("4");
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });

        tr_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ComingSoonActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        homes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
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

        buy_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                clickb("3");
            }
        });
        my_passbooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                clickb("4");
            }
        });
        pay_cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(getApplicationContext(), MyReceiptActivity.class);
                startActivity(intent);

            }
        });

        my_plots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                clickb("2");
            }
        });
        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(getApplicationContext(), OffersActivity.class);
                startActivity(intent);

            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(CustomerDashboard.this, FeedbackActivity.class);
                startActivity(intent);

            }
        });
        gallery_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                startActivity(intent);

            }
        });
        about_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(CustomerDashboard.this, AboutActivity.class);
                startActivity(intent);
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(CustomerDashboard.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(getApplicationContext(), PrivacyPolicy.class);
                startActivity(intent);
            }
        });
        change_pwd_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intent);
            }
        });
        check_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CheckDateActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    public void showLogoutDialog() {
        final Dialog dialog = new Dialog(CustomerDashboard.this);
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
            if (CustomerDashboard.this.getClass().getSimpleName().toString().indexOf("MainCustomerActivity") < 0) {
                startActivity(new Intent(this, MainCustomerActivity.class));
                finish();
            }
        } else if (s.equalsIgnoreCase("2")) {
            visit_view.setVisibility(View.VISIBLE);
            visits_img.setColorFilter(getResources().getColor(R.color.colorBlack), android.graphics.PorterDuff.Mode.SRC_IN);
            visits_txt.setTextColor(getResources().getColor(R.color.colorBlack));
            if (CustomerDashboard.this.getClass().getSimpleName().toString().indexOf("CustomerPlotsActivity") < 0) {
                startActivity(new Intent(this, CustomerPlotsActivity.class));
            }
        } else if (s.equalsIgnoreCase("3")) {
            plot_view.setVisibility(View.VISIBLE);
            plots_txt.setTextColor(getResources().getColor(R.color.colorBlack));
            plots_img.setColorFilter(getResources().getColor(R.color.colorBlack), android.graphics.PorterDuff.Mode.SRC_IN);
            if (CustomerDashboard.this.getClass().getSimpleName().indexOf("PlotsActivity") < 0) {
                startActivity(new Intent(this, OurPropertiesActivity.class));
            }
        } else if (s.equalsIgnoreCase("4")) {
            pass_view.setVisibility(View.VISIBLE);
            pass_img.setColorFilter(getResources().getColor(R.color.colorBlack), android.graphics.PorterDuff.Mode.SRC_IN);
            pass_txt.setTextColor(getResources().getColor(R.color.colorBlack));
            if (CustomerDashboard.this.getClass().getSimpleName().toString().indexOf("PassbookCustmers") < 0) {
                startActivity(new Intent(this, PassbookCustmers.class));
            }

        }

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
}