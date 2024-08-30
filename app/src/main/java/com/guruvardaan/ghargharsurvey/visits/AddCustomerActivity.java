package com.guruvardaan.ghargharsurvey.visits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.model.CustomerModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

public class AddCustomerActivity extends BaseActivity {

    TextView add_customer;
    EditText customer_name, customer_mobile, customer_profession, customer_occupation, customer_address;
    LinearLayout profession_lay, occupation_lay;
    String sel = "";
    String occ = "";
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_customer, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        sel = "";
        occ = "";
        add_customer = (TextView) findViewById(R.id.add_customer);
        profession_lay = (LinearLayout) findViewById(R.id.profession_lay);
        occupation_lay = (LinearLayout) findViewById(R.id.occupation_lay);
        customer_name = (EditText) findViewById(R.id.customer_name);
        customer_mobile = (EditText) findViewById(R.id.customer_mobile);
        customer_profession = (EditText) findViewById(R.id.customer_profession);
        customer_occupation = (EditText) findViewById(R.id.customer_occupation);
        customer_address = (EditText) findViewById(R.id.customer_address);
        if (getIntent().getExtras().getString("TYPE").equalsIgnoreCase("E")) {
            CustomerModel customerModel = (CustomerModel) getIntent().getExtras().getSerializable("CM");
            sel = customerModel.getPr_id();
            occ = customerModel.getOcc_id();
            customer_name.setText(customerModel.getName());
            customer_profession.setText(customerModel.getProfession());
            customer_occupation.setText(customerModel.getOccupation());
            customer_mobile.setText(customerModel.getMobile());
            customer_address.setText(customerModel.getAddress());
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        customer_profession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectProfession.class);
                startActivityForResult(intent, 29);
            }
        });

        customer_occupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sel.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), SelectOccupation.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", sel);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 31);
                } else {
                    Toast.makeText(getApplicationContext(), "Select Profession First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customer_name.getText().toString().trim().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (customer_occupation.getText().toString().trim().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Please Enter Occupation", Toast.LENGTH_SHORT).show();
                } else if (customer_profession.getText().toString().trim().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Please Enter Profession", Toast.LENGTH_SHORT).show();
                } else {
                    CustomerModel customerModel = new CustomerModel(customer_name.getText().toString().trim(), customer_mobile.getText().toString().trim(), customer_profession.getText().toString().trim(), customer_occupation.getText().toString().trim(), customer_address.getText().toString().trim(), sel, occ);
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CM", customerModel);
                    bundle.putInt("POS", getIntent().getExtras().getInt("POS"));
                    bundle.putString("TYPE", getIntent().getExtras().getString("TYPE"));
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 29 && resultCode == RESULT_OK) {
            sel = data.getExtras().getString("ID");
            customer_profession.setText(data.getExtras().getString("NAME"));
            occ = "";
            customer_occupation.setText("");
        }
        if (requestCode == 31 && resultCode == RESULT_OK) {
            occ = data.getExtras().getString("ID");
            customer_occupation.setText(data.getExtras().getString("NAME"));
        }
    }
}