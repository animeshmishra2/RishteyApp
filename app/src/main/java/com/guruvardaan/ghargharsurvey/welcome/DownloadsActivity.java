package com.guruvardaan.ghargharsurvey.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.agent.AgentReceiptActivity;
import com.guruvardaan.ghargharsurvey.agent.CustomerReceiptActivity;

public class DownloadsActivity extends BaseActivity {

    CardView self_statement, team_statement, customer_registry, agreement_download, get_receipt, customer_receipt;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_downloads, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        self_statement = (CardView) findViewById(R.id.self_statement);
        team_statement = (CardView) findViewById(R.id.team_statement);
        customer_registry = (CardView) findViewById(R.id.customer_registry);
        agreement_download = (CardView) findViewById(R.id.agreement_download);
        get_receipt = (CardView) findViewById(R.id.get_receipt);
        customer_receipt = (CardView) findViewById(R.id.customer_receipt);
        self_statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelfStatement.class);
                startActivity(intent);
            }
        });
        team_statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectDateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("OPEN","1");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        get_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgentReceiptActivity.class);
                startActivity(intent);
            }
        });
        customer_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerReceiptActivity.class);
                startActivity(intent);
            }
        });
    }
}