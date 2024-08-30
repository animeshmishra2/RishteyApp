package com.guruvardaan.ghargharsurvey.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.fragments.CustomerPayment;
import com.guruvardaan.ghargharsurvey.fragments.DebitPass;
import com.guruvardaan.ghargharsurvey.fragments.ReceiptFragment;

public class CustomerPassActivity extends BaseActivity {

    public static TextView user_name, plot_name, total_price, paid, pending, remaining;
    TabLayout tabs;
    ViewPager viewPager;
    ImageView back_button;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_customer_pass, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        user_name = (TextView) findViewById(R.id.user_name);
        plot_name = (TextView) findViewById(R.id.plot_name);
        total_price = (TextView) findViewById(R.id.total_price);
        paid = (TextView) findViewById(R.id.paid);
        pending = (TextView) findViewById(R.id.pending);
        remaining = (TextView) findViewById(R.id.remaining);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(viewPager);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new ReceiptFragment();
            } else if (position == 1) {
                fragment = new CustomerPayment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = "Receipt";
            } else if (position == 1) {
                title = "Payment";
            }
            return title;
        }
    }

}