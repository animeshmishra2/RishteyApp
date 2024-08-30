package com.guruvardaan.ghargharsurvey.agent;

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
import com.guruvardaan.ghargharsurvey.fragments.CreditPass;
import com.guruvardaan.ghargharsurvey.fragments.DebitFragment;
import com.guruvardaan.ghargharsurvey.fragments.DebitPass;
import com.guruvardaan.ghargharsurvey.fragments.SelfTeamFragment;
import com.guruvardaan.ghargharsurvey.fragments.TeamTeamFragment;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

public class AgentPassActivity extends BaseActivity {

    TextView credit, debit, balance;
    TabLayout tabs;
    ViewPager viewPager;
    ImageView back_button;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_agent_pass, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        credit = (TextView) findViewById(R.id.credit);
        debit = (TextView) findViewById(R.id.debit);
        balance = (TextView) findViewById(R.id.balance);
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
                fragment = new CreditPass();
            } else if (position == 1) {
                fragment = new DebitPass();
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
                title = "Credit";
            } else if (position == 1) {
                title = "Debit";
            }
            return title;
        }
    }

}