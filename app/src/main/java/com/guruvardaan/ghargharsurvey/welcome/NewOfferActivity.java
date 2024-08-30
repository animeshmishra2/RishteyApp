package com.guruvardaan.ghargharsurvey.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.guruvardaan.ghargharsurvey.agent.ViewSettlementActivity;
import com.rishtey.agentapp.R;

public class NewOfferActivity extends AppCompatActivity {

    ImageView back_button;
    CardView old_gold1, old_gold2, old_gold3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);
        old_gold1 = (CardView) findViewById(R.id.old_gold1);
        old_gold2 = (CardView) findViewById(R.id.old_gold2);
        old_gold3 = (CardView) findViewById(R.id.old_gold3);
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        old_gold1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewSettlementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("OLD", "2");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        old_gold2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewSettlementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("OLD", "1");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        old_gold3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewSettlementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("OLD", "3");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}