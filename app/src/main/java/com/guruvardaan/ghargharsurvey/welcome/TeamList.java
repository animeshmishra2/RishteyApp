package com.guruvardaan.ghargharsurvey.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rishtey.agentapp.R;

public class TeamList extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getSupportActionBar().hide();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new TeamAdapter());
    }

    class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {


            public MyViewHolder(View view) {
                super(view);

            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
           /* holder.user_img.setImageURI(Uri.parse("27dps://raw.githubusercontent.com/ArjunAtlast/Profile-Card/master/assets/john-doe.png"));
            if (position % 2 == 0) {
                holder.main_lay.setBackgroundColor(Color.parseColor("#E4F7FA"));
            }*/
        }

        @Override
        public int getItemCount() {
            return 12;
        }
    }
}