package com.guruvardaan.ghargharsurvey.agent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

public class PayInstallmentActivity extends BaseActivity {
    TextView user_name, user_service, user_id, f_amount;
    LinearLayout full_payment, other_amount, edit_lay;
    RadioButton rg_1, rg_2;
    EditText money;
    TextView proceed;
    View view_1, view2;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_pay_installment, frameLayout);
        user_name = (TextView) findViewById(R.id.user_name);
        back_button = (ImageView) findViewById(R.id.back_button);
        user_service = (TextView) findViewById(R.id.user_service);
        f_amount = (TextView) findViewById(R.id.f_amount);
        user_id = (TextView) findViewById(R.id.user_id);
        view_1 = (View) findViewById(R.id.view_1);
        view2 = (View) findViewById(R.id.view2);
        proceed = (TextView) findViewById(R.id.proceed);
        full_payment = (LinearLayout) findViewById(R.id.full_payment);
        other_amount = (LinearLayout) findViewById(R.id.other_amount);
        edit_lay = (LinearLayout) findViewById(R.id.edit_lay);
        rg_1 = (RadioButton) findViewById(R.id.rg_1);
        rg_2 = (RadioButton) findViewById(R.id.rg_2);
        money = (EditText) findViewById(R.id.money);
        view_1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.INVISIBLE);
        rg_1.setChecked(true);
        rg_2.setChecked(false);
        edit_lay.setVisibility(View.GONE);
        user_name.setText(getIntent().getExtras().getString("NAME"));
        user_service.setText("SERVICE : " + getIntent().getExtras().getString("SID"));
        user_id.setText("Customer Id : " + getIntent().getExtras().getString("CID"));
        f_amount.setText("\u20b9" + getIntent().getExtras().getString("AM"));
        full_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                proceed.setVisibility(View.VISIBLE);
                edit_lay.setVisibility(View.GONE);
                rg_1.setChecked(true);
                rg_2.setChecked(false);
            }
        });
        other_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                proceed.setVisibility(View.VISIBLE);
                edit_lay.setVisibility(View.VISIBLE);
                rg_1.setChecked(false);
                rg_2.setChecked(true);
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rg_2.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), PayDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SID", getIntent().getExtras().getString("SID"));
                    bundle.putString("CID", getIntent().getExtras().getString("CID"));
                    bundle.putString("NAME", getIntent().getExtras().getString("NAME"));
                    bundle.putString("LF", getIntent().getExtras().getString("LF"));
                    bundle.putString("AM", money.getText().toString().trim());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,29);
                    finish();
                } else if (rg_1.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), PayDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SID", getIntent().getExtras().getString("SID"));
                    bundle.putString("CID", getIntent().getExtras().getString("CID"));
                    bundle.putString("NAME", getIntent().getExtras().getString("NAME"));
                    bundle.putString("LF", getIntent().getExtras().getString("LF"));
                    bundle.putString("AM", getIntent().getExtras().getString("AM"));
                    intent.putExtras(bundle);
                    startActivityForResult(intent,29);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Select Any Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}