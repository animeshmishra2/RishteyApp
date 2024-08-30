package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.CUSTOMER_LOGIN_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.LOGIN_TEAM;
import static com.guruvardaan.ghargharsurvey.config.Config.LOGIN_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.chat.ChatDashboardActivity;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.customers.MainCustomerActivity;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView signin;
    EditText email, password;
    UserDetails userDetails;
    ProgressDialog pd;
    RelativeLayout agent_lay, cust_lay, admin_lay;
    TextView agent_login, customer_login, admin_login;
    ImageView agent_tick, customer_tick, admin_tick;
    TextView terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        terms = (TextView) findViewById(R.id.terms);
        admin_lay = (RelativeLayout) findViewById(R.id.admin_lay);
        admin_tick = (ImageView) findViewById(R.id.admin_tick);
        agent_lay = (RelativeLayout) findViewById(R.id.agent_lay);
        customer_tick = (ImageView) findViewById(R.id.customer_tick);
        agent_tick = (ImageView) findViewById(R.id.agent_tick);
        cust_lay = (RelativeLayout) findViewById(R.id.cust_lay);
        customer_login = (TextView) findViewById(R.id.customer_login);
        admin_login = (TextView) findViewById(R.id.admin_login);
        agent_login = (TextView) findViewById(R.id.agent_login);
        userDetails = new UserDetails(getApplicationContext());
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        userDetails.setFcm_msg(token);
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("TAG", msg);
                    }
                });

        if (!userDetails.getuserid().equalsIgnoreCase("0")) {
            if (userDetails.getUser_type().equalsIgnoreCase("1")) {
                Intent intent = new Intent(getApplicationContext(), AgentActivity.class);
                startActivity(intent);
                finish();
            } else if (userDetails.getUser_type().equalsIgnoreCase("2")) {
                Intent intent = new Intent(getApplicationContext(), MainCustomerActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), ChatDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(intent);
            }
        });
        signin = (TextView) findViewById(R.id.signin);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Mobile", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (agent_tick.getVisibility() == View.VISIBLE) {
                        loginUser();
                        userDetails.setTypess("1");
                    } else if (admin_tick.getVisibility() == View.VISIBLE) {
                        //loginUser();
                        chatAdminLogin();
                        //userDetails.setTypess("2");
                    } else if (customer_tick.getVisibility() == View.VISIBLE) {
                        userDetails.setTypess("3");
                        customerLoginUser();
                    }

                }
            }
        });

        agent_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLoginLay(0);
            }
        });
        cust_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLoginLay(1);
            }
        });
        admin_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLoginLay(2);
            }
        });
        setLoginLay(0);
    }

    public void setLoginLay(int i) {
        if (i == 0) {
            agent_tick.setVisibility(View.VISIBLE);
            customer_tick.setVisibility(View.INVISIBLE);
            admin_tick.setVisibility(View.INVISIBLE);
            agent_login.setTextColor(getResources().getColor(R.color.purple_700));
            customer_login.setTextColor(getResources().getColor(R.color.colorBlack));
            admin_login.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (i == 1) {
            agent_tick.setVisibility(View.INVISIBLE);
            customer_tick.setVisibility(View.VISIBLE);
            admin_tick.setVisibility(View.INVISIBLE);
            agent_login.setTextColor(getResources().getColor(R.color.colorBlack));
            customer_login.setTextColor(getResources().getColor(R.color.purple_700));
            admin_login.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (i == 2) {
            agent_tick.setVisibility(View.INVISIBLE);
            customer_tick.setVisibility(View.INVISIBLE);
            admin_tick.setVisibility(View.VISIBLE);
            agent_login.setTextColor(getResources().getColor(R.color.colorBlack));
            customer_login.setTextColor(getResources().getColor(R.color.colorBlack));
            admin_login.setTextColor(getResources().getColor(R.color.purple_700));
        }
    }

    private void loginUser() {
        pd = ProgressDialog.show(MainActivity.this);
        String URL_CHECK = LOGIN_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getFcm_msg() + "/" + email.getText().toString().trim() + "/" + password.getText().toString().trim();
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                System.out.println(response);
                try {
                    JSONObject jsonObjects = new JSONObject(response);
                    JSONArray Table = jsonObjects.getJSONArray("Table");
                    if (Table.length() > 0) {
                        JSONObject jsonObject = Table.getJSONObject(0);
                        if (jsonObject.getString("return_type").equalsIgnoreCase("t")) {
                            userDetails.setuserid(jsonObject.getString("loginId"));
                            userDetails.setName(jsonObject.getString("name"));
                            userDetails.setprofilef(jsonObject.getString("agentImage"));
                            userDetails.setFIREBASE(jsonObject.getString("firebaseID"));
                            userDetails.setUser_type("1");
                            JSONArray Table1 = jsonObjects.getJSONArray("Table1");
                            for (int i = 0; i < Table1.length(); i++) {
                                JSONObject object = Table1.getJSONObject(i);
                                userDetails.setAdvisor_rank(object.getString("advisor_rank"));
                                userDetails.setMobile_no(object.getString("mobile_no"));
                                userDetails.setAlternate_no(object.getString("alternate_no"));
                                userDetails.setWhatsapp(object.getString("whatsapp"));
                                userDetails.setAdvisor_email(object.getString("advisor_email"));
                                userDetails.setFlatHouseNo(object.getString("flatHouseNo"));
                                userDetails.setLandMark(object.getString("landMark"));
                                userDetails.setCountry_id(object.getString("country_id"));
                                userDetails.setCity_id(object.getString("city_id"));
                                userDetails.setState_id(object.getString("state_id"));
                                userDetails.setArea_id(object.getString("area_id"));
                                userDetails.setTehsil(object.getString("tehsil"));
                                userDetails.setPin_code(object.getString("pin_code"));
                                userDetails.setVillage(object.getString("village"));
                                userDetails.setAdvisordob(object.getString("advisordob"));
                                userDetails.setAcHolder(object.getString("acHolder"));
                                userDetails.setAcNo(object.getString("acNo"));
                                userDetails.setIFSC(object.getString("IFSC"));
                                userDetails.setBank(object.getString("bank"));
                                userDetails.setBranch(object.getString("branch"));
                                userDetails.setNomineeName(object.getString("nomineeName"));
                                userDetails.setNomineeAge(object.getString("nomineeAge"));
                                userDetails.setNomineemobile(object.getString("nomineemobile"));
                                userDetails.setRelation(object.getString("relation"));
                                userDetails.setFather(object.getString("Father"));
                                userDetails.setOccupationCategory(object.getString("occupationCategory"));
                                userDetails.setOccupation(object.getString("occupation"));
                                userDetails.setAdvisor_image(object.getString("advisor_image"));
                                userDetails.setAadharImage(object.getString("aadharImage"));
                                userDetails.setPanImage(object.getString("panImage"));
                                userDetails.setAgentSign(object.getString("agentSign"));
                                userDetails.setAadharNo(object.getString("aadharNo"));
                                userDetails.setPanNo(object.getString("panNo"));
                                userDetails.setRelationType(object.getString("relationType"));
                                userDetails.setRelationName(object.getString("relationName"));
                            }
                            Intent intent = new Intent(getApplicationContext(), AgentActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed, Please Check that You are Agent", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Failed, Please Check that You are Agent", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(URL_CHECK);
                }
            }
        }, new Response.ErrorListener() {
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
        //VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void customerLoginUser() {
        pd = ProgressDialog.show(MainActivity.this);
        String URL_CHECK = CUSTOMER_LOGIN_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getFcm_msg() + "/" + email.getText().toString().trim() + "/" + password.getText().toString().trim();
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    if (Table.length() > 0) {
                        jsonObject = Table.getJSONObject(0);
                        if (jsonObject.getString("return_type").equalsIgnoreCase("t")) {
                            userDetails.setuserid(jsonObject.getString("loginId"));
                            userDetails.setName(jsonObject.getString("name"));
                            userDetails.setprofilef(jsonObject.getString("memberImage"));
                            userDetails.setFIREBASE(jsonObject.getString("firebaseID"));
                            userDetails.setAgentId(jsonObject.getString("agentId"));
                            userDetails.setCust_json(response);
                            userDetails.setUser_type("2");
                            Intent intent = new Intent(getApplicationContext(), MainCustomerActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed, Please Check that You are Customer", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Failed, Please Check that You are Customer", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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
        //VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void chatAdminLogin() {
        pd = ProgressDialog.show(MainActivity.this);
        String URL_CHECK = LOGIN_TEAM + "mobile=" + email.getText().toString().trim() + "&password=" + password.getText().toString().trim() + "&fb_token=" + userDetails.getFcm_msg();
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONObject user = jsonObject.getJSONObject("user");
                        userDetails.setuserid(user.getString("id"));
                        userDetails.setUser_type("3");
                        userDetails.setName(user.getString("FirstName"));
                        userDetails.setAdvisor_email(user.getString("EmailId"));
                        userDetails.setGender(user.getString("Gender"));
                        userDetails.setDob(user.getString("Dob"));
                        userDetails.setDep_name(user.getString("DepartmentName"));
                        userDetails.setDep_id(user.getString("Department"));
                        userDetails.setIsTL(user.getString("is_teamlead"));
                        userDetails.setMobile_no(email.getText().toString().trim());
                        Intent intent = new Intent(getApplicationContext(), ChatDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        //VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


}