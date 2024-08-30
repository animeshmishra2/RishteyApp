package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.UPDATE_BANK_DETAILS;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateDetails extends BaseActivity {

    EditText account_no, select_bank, branch_name, user_name, ifsc;
    LinearLayout select_bank_lay;
    TextView update_details;
    ImageView back_button;
    String bid = "";
    ProgressDialog pd;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_update_details, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        back_button = (ImageView) findViewById(R.id.back_button);
        select_bank = (EditText) findViewById(R.id.select_bank);
        branch_name = (EditText) findViewById(R.id.branch_name);
        user_name = (EditText) findViewById(R.id.user_name);
        ifsc = (EditText) findViewById(R.id.ifsc);
        account_no = (EditText) findViewById(R.id.account_no);
        update_details = (TextView) findViewById(R.id.update_details);
        select_bank_lay = (LinearLayout) findViewById(R.id.select_bank_lay);
        bid = "";
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        select_bank_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectBankActvity.class);
                startActivityForResult(intent, 23);
            }
        });
        select_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectBankActvity.class);
                startActivityForResult(intent, 23);
            }
        });
        account_no.setText(getIntent().getExtras().getString("AC"));
        user_name.setText(getIntent().getExtras().getString("NM"));
        select_bank.setText(getIntent().getExtras().getString("BN"));
        branch_name.setText(getIntent().getExtras().getString("BB"));
        ifsc.setText(getIntent().getExtras().getString("IF"));
        bid = getIntent().getExtras().getString("ID");

        update_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (account_no.getText().toString().trim().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Account Number", Toast.LENGTH_SHORT).show();
                } else if (bid.trim().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please Select Bank", Toast.LENGTH_SHORT).show();
                } else if (select_bank.getText().toString().trim().equalsIgnoreCase("ICICI Bank")) {
                    Toast.makeText(getApplicationContext(), "ICICI Bank Not Supported", Toast.LENGTH_SHORT).show();
                } else if (branch_name.getText().toString().trim().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Branch Name", Toast.LENGTH_SHORT).show();
                } else if (user_name.getText().toString().trim().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Account Holder Name", Toast.LENGTH_SHORT).show();
                } else if (ifsc.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Enter Valid IFSC", Toast.LENGTH_SHORT).show();
                } else {
                    createJSON();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            select_bank.setText(data.getExtras().getString("NAME"));
            bid = data.getExtras().getString("ID");
        }
    }

    public void createJSON() {
        try {
            JSONObject main_obj = new JSONObject();
            main_obj.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            main_obj.put("agent_id", userDetails.getuserid());
            main_obj.put("acholder", user_name.getText().toString());
            main_obj.put("acno", account_no.getText().toString().trim());
            main_obj.put("ifsc", ifsc.getText().toString().trim());
            main_obj.put("bank", bid);
            main_obj.put("branch", branch_name.getText().toString().trim());
            System.out.println("Animesh " + main_obj.toString());
            updateDetails(main_obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDetails(String jsons) {
        pd = ProgressDialog.show(UpdateDetails.this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                UPDATE_BANK_DETAILS,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                return jsons.getBytes();
            }

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(myReq);
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                System.out.println("Mishras " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    if (Table.length() > 0) {
                        JSONObject object = Table.getJSONObject(0);
                        if (object.getString("Column1").equalsIgnoreCase("t")) {
                            Toast.makeText(getApplicationContext(), "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Details Updating Failed", Toast.LENGTH_SHORT).show();
            }
        };
    }
}