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
import com.guruvardaan.ghargharsurvey.fragments.SelfBusinessFragment;
import com.guruvardaan.ghargharsurvey.fragments.SelfPlotFragment;
import com.guruvardaan.ghargharsurvey.fragments.TeamBusinessFragment;
import com.guruvardaan.ghargharsurvey.fragments.TeamPlotFragment;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

public class TodayBusiness extends BaseActivity {

    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_today_business, frameLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        back_button = (ImageView) findViewById(R.id.back_button);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewPager.setCurrentItem(getIntent().getExtras().getInt("POS"));
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new SelfBusinessFragment();
            } else if (position == 1) {
                //fragment = new TeamBusinessFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = "Self Business";
            } else if (position == 1) {
                title = "Team Business";
            }
            return title;
        }
    }

}