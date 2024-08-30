package com.guruvardaan.ghargharsurvey.welcome;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;

import org.json.JSONObject;

public class UpdateAgentActivity extends BaseActivity {

    ImageView back_button;
    EditText state, city, tehsil, area, customer_profession, customer_occupation;
    TextView next_3;
    String state_id, city_id, area_id, tehsil_id, sel, occ = "";
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_update_agent, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        state_id = "";
        city_id = "";
        area_id = "";
        tehsil_id = "";
        sel = "";
        occ = "";
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        state = (EditText) findViewById(R.id.state);
        city = (EditText) findViewById(R.id.city);
        tehsil = (EditText) findViewById(R.id.tehsil);
        area = (EditText) findViewById(R.id.area);
        customer_profession = (EditText) findViewById(R.id.customer_profession);
        customer_occupation = (EditText) findViewById(R.id.customer_occupation);
        next_3 = (TextView) findViewById(R.id.next_3);
    }


}