package com.guruvardaan.ghargharsurvey.agent;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.fragments.AgentBalanceFragment;
import com.guruvardaan.ghargharsurvey.fragments.AgentCreditFragment;
import com.guruvardaan.ghargharsurvey.fragments.AgentDebitFragment;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

public class AllVoucherActivity extends BaseActivity {

    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_all_voucher, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(getIntent().getExtras().getInt("POS"));
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
                fragment = new AgentCreditFragment();
            } else if (position == 1) {
                fragment = new AgentDebitFragment();
            } else if (position == 2) {
                fragment = new AgentBalanceFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = "Credit";
            } else if (position == 1) {
                title = "Debit";
            } else if (position == 2) {
                title = "Balance";
            }
            return title;
        }
    }

}