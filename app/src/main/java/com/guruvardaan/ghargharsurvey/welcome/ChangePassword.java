package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.CHANGE_PASSWORD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends BaseActivity {

    EditText old_password, new_password, confirm_password;
    TextView old_show, new_show, confirm_show, submit;
    ImageView back_button;
    ProgressDialog pd;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_change_password, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        old_password = (EditText) findViewById(R.id.old_password);
        back_button = (ImageView) findViewById(R.id.back_button);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        old_show = (TextView) findViewById(R.id.old_show);
        new_show = (TextView) findViewById(R.id.new_show);
        confirm_show = (TextView) findViewById(R.id.confirm_show);
        submit = (TextView) findViewById(R.id.submit);

        old_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (old_show.getText().toString().trim().equalsIgnoreCase("Show")) {
                    old_password.setTransformationMethod(null);
                    old_show.setText("Hide");
                } else {
                    old_password.setTransformationMethod(new PasswordTransformationMethod());
                    old_show.setText("Show");
                }
            }
        });

        new_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new_show.getText().toString().trim().equalsIgnoreCase("Show")) {
                    new_password.setTransformationMethod(null);
                    new_show.setText("Hide");
                } else {
                    new_password.setTransformationMethod(new PasswordTransformationMethod());
                    new_show.setText("Show");
                }
            }
        });
        confirm_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirm_show.getText().toString().trim().equalsIgnoreCase("Show")) {
                    confirm_password.setTransformationMethod(null);
                    confirm_show.setText("Hide");
                } else {
                    confirm_password.setTransformationMethod(new PasswordTransformationMethod());
                    confirm_show.setText("Show");
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (old_password.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Old Password", Toast.LENGTH_SHORT).show();
                } else if (new_password.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter New Password", Toast.LENGTH_SHORT).show();
                } else if (confirm_password.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!new_password.getText().toString().equalsIgnoreCase(confirm_password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "New Password and Confirm Password doesn't match", Toast.LENGTH_SHORT).show();
                } else {
                    changePassword();
                }
            }
        });

    }

    private void changePassword() {
        pd = ProgressDialog.show(ChangePassword.this);
        String URL_CHECK = CHANGE_PASSWORD + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + userDetails.getuserid() + "/" + old_password.getText().toString().trim() + "/" + new_password.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                if (object.getString("return_type").equalsIgnoreCase("t")) {
                                    Toast.makeText(getApplicationContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Old Password is wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Password Not Updated", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        error.printStackTrace();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content_Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

}