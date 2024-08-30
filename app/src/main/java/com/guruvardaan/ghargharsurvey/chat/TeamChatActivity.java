package com.guruvardaan.ghargharsurvey.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.rishtey.agentapp.R;

public class TeamChatActivity extends AppCompatActivity {

    TextView header_txt;
    TabLayout tabs;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_chat);
        header_txt = (TextView) findViewById(R.id.header_txt);
        back_button = (ImageView) findViewById(R.id.back_button);
        if (getIntent().getExtras().getString("TYPE").equalsIgnoreCase("1")) {
            header_txt.setText("Direct Complaints");
        } else {
            header_txt.setText("Transferred Complaints");
        }
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(getIntent().getExtras().getInt("POS"));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
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
            if (getIntent().getExtras().getString("TYPE").equalsIgnoreCase("1")) {
                if (position == 0) {
                    fragment = new DirectSolved();
                } else if (position == 1) {
                    fragment = new DirectUnSolved();
                }
            } else {
                if (position == 0) {
                    fragment = new TransferredSolved();
                } else if (position == 1) {
                    fragment = new TransferredUnSolved();
                }
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
                title = "Resolved";
            } else if (position == 1) {
                title = "Unresolved";
            }
            return title;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}