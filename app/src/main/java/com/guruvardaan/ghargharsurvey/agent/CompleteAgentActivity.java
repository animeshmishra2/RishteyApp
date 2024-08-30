package com.guruvardaan.ghargharsurvey.agent;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.badoualy.stepperindicator.StepperIndicator;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.DataPart;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.utils.VolleyMultipartRequest;
import com.guruvardaan.ghargharsurvey.visits.SelectOccupation;
import com.guruvardaan.ghargharsurvey.visits.SelectProfession;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteAgentActivity extends BaseActivity {
    LinearLayout layout_2, layout_3, layout_4, layout_5;
    EditText alternate_no, relative_name, pan_no;
    EditText aadhar_no, email, country, state, city, tehsil, area, address, house_no, landmark, pincode, whatsapp;
    EditText nominee_name, nominee_relation, nominee_age, nominee_mobile, nominee_whatsapp;
    EditText customer_profession, customer_occupation;
    TextView prev_2, next_2, prev_3, next_3, prev_4, next_4, prev_5, next_5;
    String state_id, city_id, area_id, tehsil_id, sel, occ = "";
    UserDetails userDetails;
    String relation_array[] = {"Son", "Daughter", "Husband", "Wife", "Guardian"};
    ProgressDialog pd;
    String agent_id = "";
    String tr_id = "";
    ImageView back_button;
    StepperIndicator step_indicator;
    TextView cheque_txt, c_date_txt, c_am_txt, ref_txt;
    CardView num_card, ref_card, am_card, date_card;
    ScrollView mainScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_complete_agent, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        step_indicator = (StepperIndicator) findViewById(R.id.step_indicator);
        initializeVars();
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        state_id = "";
        city_id = "";
        area_id = "";
        tehsil_id = "";
        agent_id = "";
        tr_id = "";
        sel = "";
        occ = "";
        checkAndRequestPermissions();
        setData();
        prev_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout(3);
            }
        });
        prev_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout(2);
            }
        });
        next_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout(4);
            }
        });
        prev_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout(3);
            }
        });
        next_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout(5);
            }
        });
        prev_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout(4);
            }
        });
        next_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createJSON();
            }
        });

        nominee_relation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CompleteAgentActivity.this);
                mBuilder.setTitle("Select Nominee Relation");
                mBuilder.setSingleChoiceItems(relation_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        nominee_relation.setText(relation_array[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectAddress.class);
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", "SELECT STATE");
                bundle.putString("ID", "0");
                intent.putExtras(bundle);
                startActivityForResult(intent, 101);
            }
        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!state_id.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), SelectAddress.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TITLE", "SELECT CITY");
                    bundle.putString("ID", state_id);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 102);
                } else {
                    Toast.makeText(getApplicationContext(), "Select State First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tehsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!city_id.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), SelectAddress.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TITLE", "SELECT TEHSIL");
                    bundle.putString("ID", city_id);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 103);
                } else {
                    Toast.makeText(getApplicationContext(), "Select City First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tehsil_id.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), SelectAddress.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TITLE", "SELECT AREA");
                    bundle.putString("ID", tehsil_id);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 104);
                } else {
                    Toast.makeText(getApplicationContext(), "Select Tehsil First", Toast.LENGTH_SHORT).show();
                }
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
        setLayout(1);
    }

    private boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2011);
            return false;
        }
        return true;
    }

    public void setLayout(int i) {
        if (i == 2) {
            step_indicator.setCurrentStep(1);
            layout_2.setVisibility(View.VISIBLE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.GONE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
        if (i == 3) {
            step_indicator.setCurrentStep(2);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.VISIBLE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.GONE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
        if (i == 4) {
            step_indicator.setCurrentStep(3);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.VISIBLE);
            layout_5.setVisibility(View.GONE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
        if (i == 5) {
            step_indicator.setCurrentStep(4);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.VISIBLE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    public void initializeVars() {
        mainScroll = (ScrollView) findViewById(R.id.mainScroll);
        num_card = (CardView) findViewById(R.id.num_card);
        date_card = (CardView) findViewById(R.id.date_card);
        ref_card = (CardView) findViewById(R.id.ref_card);
        am_card = (CardView) findViewById(R.id.am_card);
        layout_2 = (LinearLayout) findViewById(R.id.layout_2);
        layout_3 = (LinearLayout) findViewById(R.id.layout_3);
        layout_4 = (LinearLayout) findViewById(R.id.layout_4);
        layout_5 = (LinearLayout) findViewById(R.id.layout_5);
        alternate_no = (EditText) findViewById(R.id.alternate_no);
        relative_name = (EditText) findViewById(R.id.relative_name);
        pan_no = (EditText) findViewById(R.id.pan_no);
        aadhar_no = (EditText) findViewById(R.id.aadhar_no);
        email = (EditText) findViewById(R.id.email);
        country = (EditText) findViewById(R.id.country);
        state = (EditText) findViewById(R.id.state);
        city = (EditText) findViewById(R.id.city);
        tehsil = (EditText) findViewById(R.id.tehsil);
        area = (EditText) findViewById(R.id.area);
        address = (EditText) findViewById(R.id.address);
        house_no = (EditText) findViewById(R.id.house_no);
        landmark = (EditText) findViewById(R.id.landmark);
        pincode = (EditText) findViewById(R.id.pincode);
        whatsapp = (EditText) findViewById(R.id.whatsapp);
        nominee_name = (EditText) findViewById(R.id.nominee_name);
        nominee_relation = (EditText) findViewById(R.id.nominee_relation);
        nominee_age = (EditText) findViewById(R.id.nominee_age);
        nominee_mobile = (EditText) findViewById(R.id.nominee_mobile);
        nominee_whatsapp = (EditText) findViewById(R.id.nominee_whatsapp);
        customer_profession = (EditText) findViewById(R.id.customer_profession);
        customer_occupation = (EditText) findViewById(R.id.customer_occupation);
        cheque_txt = (TextView) findViewById(R.id.cheque_txt);
        c_date_txt = (TextView) findViewById(R.id.c_date_txt);
        c_am_txt = (TextView) findViewById(R.id.c_am_txt);
        ref_txt = (TextView) findViewById(R.id.ref_txt);
        prev_2 = (TextView) findViewById(R.id.prev_2);
        next_2 = (TextView) findViewById(R.id.next_2);
        prev_3 = (TextView) findViewById(R.id.prev_3);
        next_3 = (TextView) findViewById(R.id.next_3);
        prev_4 = (TextView) findViewById(R.id.prev_4);
        next_4 = (TextView) findViewById(R.id.next_4);
        prev_5 = (TextView) findViewById(R.id.prev_5);
        next_5 = (TextView) findViewById(R.id.next_5);
    }

    public String getForDate(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            state_id = data.getExtras().getString("ID");
            state.setText(data.getExtras().getString("NAME"));
            city_id = "";
            tehsil_id = "";
            area_id = "";
            city.setText("");
            tehsil.setText("");
            area.setText("");
        }
        if (requestCode == 102 && resultCode == RESULT_OK) {
            city_id = data.getExtras().getString("ID");
            city.setText(data.getExtras().getString("NAME"));
            tehsil_id = "";
            area_id = "";
            tehsil.setText("");
            area.setText("");
        }
        if (requestCode == 103 && resultCode == RESULT_OK) {
            tehsil_id = data.getExtras().getString("ID");
            tehsil.setText(data.getExtras().getString("NAME"));
            area_id = "";
            area.setText("");
        }
        if (requestCode == 104 && resultCode == RESULT_OK) {
            area_id = data.getExtras().getString("ID");
            area.setText(data.getExtras().getString("NAME"));
        }
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

    @Override
    public void onBackPressed() {
        if (layout_5.getVisibility() == View.VISIBLE) {
            setLayout(4);
        } else if (layout_4.getVisibility() == View.VISIBLE) {
            setLayout(3);
        } else if (layout_3.getVisibility() == View.VISIBLE) {
            setLayout(2);
        } else if (layout_2.getVisibility() == View.VISIBLE) {
            finish();
        }
    }

    public void createJSON() {
        try {
            JSONObject params = new JSONObject();
            params.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            params.put("pk_advisorID", userDetails.getuserid());
            params.put("mobile2", alternate_no.getText().toString());
            params.put("whatsapp", whatsapp.getText().toString());
            params.put("email", email.getText().toString());
            params.put("panno", pan_no.getText().toString());
            params.put("aadharNo", aadhar_no.getText().toString());
            params.put("flatHouseNo", house_no.getText().toString());
            params.put("landMark", landmark.getText().toString());
            params.put("country", "+91");
            params.put("stateid", state_id);
            params.put("cityId", city_id);
            params.put("tehsil", tehsil_id);
            params.put("area_id", area_id);
            params.put("pin_code", pincode.getText().toString());
            params.put("village", address.getText().toString());
            params.put("occupationCategory", sel);
            params.put("occupation", occ);
            params.put("nomineeName", nominee_name.getText().toString());
            params.put("nomineeAge", nominee_age.getText().toString());
            params.put("relation", nominee_relation.getText().toString());
            params.put("nomineemobile", nominee_mobile.getText().toString());
            saveAgent(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAgent(String jsons) {
        pd = ProgressDialog.show(CompleteAgentActivity.this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.UPDATE_ADVISOR_URL,
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
                        if (!object.has("return_type")) {
                            Toast.makeText(getApplicationContext(), "Agent Updated Successfully", Toast.LENGTH_SHORT).show();
                            userDetails.setAlternate_no(alternate_no.getText().toString());
                            userDetails.setWhatsapp(whatsapp.getText().toString());
                            userDetails.setAdvisor_email(email.getText().toString());
                            userDetails.setAadharNo(aadhar_no.getText().toString());
                            userDetails.setPanNo(pan_no.getText().toString());
                            userDetails.setFlatHouseNo(house_no.getText().toString());
                            userDetails.setLandMark(landmark.getText().toString());
                            userDetails.setCountry_id("+91");
                            userDetails.setState_id(state_id);
                            userDetails.setArea_id(area_id);
                            userDetails.setTehsil(tehsil_id);
                            userDetails.setCity_id(city_id);
                            userDetails.setPin_code(pincode.getText().toString());
                            userDetails.setVillage(address.getText().toString());
                            userDetails.setOccupation(customer_occupation.getText().toString().trim());
                            userDetails.setOccupationCategory(customer_profession.getText().toString().trim());
                            userDetails.setNomineeName(nominee_name.getText().toString());
                            userDetails.setNomineemobile(nominee_mobile.getText().toString());
                            userDetails.setNomineeAge(nominee_age.getText().toString());
                            userDetails.setRelation(nominee_relation.getText().toString());
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errormsg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Agent couldn't be updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Agent couldn't be added", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Customer couldn't be added", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void setData() {
        if (userDetails.getAlternate_no().equalsIgnoreCase("") || userDetails.getAlternate_no().equalsIgnoreCase("null") || userDetails.getAlternate_no().equalsIgnoreCase("0") || userDetails.getAlternate_no() == null || userDetails.getAlternate_no().equalsIgnoreCase("0")) {
            alternate_no.setText("");
        } else {
            alternate_no.setText(userDetails.getAlternate_no());
        }
        if (userDetails.getWhatsapp().equalsIgnoreCase("") || userDetails.getWhatsapp().equalsIgnoreCase("null") || userDetails.getWhatsapp().equalsIgnoreCase("0") || userDetails.getWhatsapp() == null || userDetails.getWhatsapp().equalsIgnoreCase("0")) {
            whatsapp.setText("");
        } else {
            whatsapp.setText(userDetails.getWhatsapp());
        }
        if (userDetails.getAdvisor_email().equalsIgnoreCase("") || userDetails.getAdvisor_email().equalsIgnoreCase("null") || userDetails.getAdvisor_email().equalsIgnoreCase("0") || userDetails.getAdvisor_email() == null || userDetails.getAdvisor_email().equalsIgnoreCase("0")) {
            email.setText("");
        } else {
            email.setText(userDetails.getAdvisor_email());
        }
        if (userDetails.getFlatHouseNo().equalsIgnoreCase("") || userDetails.getFlatHouseNo().equalsIgnoreCase("null") || userDetails.getFlatHouseNo().equalsIgnoreCase("0") || userDetails.getFlatHouseNo() == null || userDetails.getFlatHouseNo().equalsIgnoreCase("0")) {
            house_no.setText("");
        } else {
            house_no.setText(userDetails.getFlatHouseNo());
        }
        if (userDetails.getLandMark().equalsIgnoreCase("") || userDetails.getLandMark().equalsIgnoreCase("null") || userDetails.getLandMark().equalsIgnoreCase("0") || userDetails.getLandMark() == null || userDetails.getLandMark().equalsIgnoreCase("0")) {
            landmark.setText("");
        } else {
            landmark.setText(userDetails.getLandMark());
        }
        if (userDetails.getCountry_id().equalsIgnoreCase("") || userDetails.getCountry_id().equalsIgnoreCase("null") || userDetails.getCountry_id().equalsIgnoreCase("0") || userDetails.getCountry_id() == null || userDetails.getCountry_id().equalsIgnoreCase("0")) {
            //country.setText("");
        } else {
            //country.setText(userDetails.getCountry_id());
        }
        if (userDetails.getState_id().equalsIgnoreCase("") || userDetails.getState_id().equalsIgnoreCase("null") || userDetails.getState_id().equalsIgnoreCase("0") || userDetails.getState_id() == null || userDetails.getState_id().equalsIgnoreCase("0")) {
            state.setText("");
        } else {
            state.setText(userDetails.getState_id());
        }
        if (userDetails.getCity_id().equalsIgnoreCase("") || userDetails.getCity_id().equalsIgnoreCase("null") || userDetails.getCity_id().equalsIgnoreCase("0") || userDetails.getCity_id() == null || userDetails.getCity_id().equalsIgnoreCase("0")) {
            city.setText("");
        } else {
            city.setText(userDetails.getCity_id());
        }
        if (userDetails.getArea_id().equalsIgnoreCase("") || userDetails.getArea_id().equalsIgnoreCase("null") || userDetails.getArea_id().equalsIgnoreCase("0") || userDetails.getArea_id() == null || userDetails.getArea_id().equalsIgnoreCase("0")) {
            area.setText("");
        } else {
            area.setText(userDetails.getArea_id());
        }
        if (userDetails.getTehsil().equalsIgnoreCase("") || userDetails.getTehsil().equalsIgnoreCase("null") || userDetails.getTehsil().equalsIgnoreCase("0") || userDetails.getTehsil() == null || userDetails.getTehsil().equalsIgnoreCase("0")) {
            tehsil.setText("");
        } else {
            tehsil.setText(userDetails.getTehsil());
        }
        if (userDetails.getPin_code().equalsIgnoreCase("") || userDetails.getPin_code().equalsIgnoreCase("null") || userDetails.getPin_code().equalsIgnoreCase("0") || userDetails.getPin_code() == null || userDetails.getPin_code().equalsIgnoreCase("0")) {
            pincode.setText("");
        } else {
            pincode.setText(userDetails.getPin_code());
        }
        if (userDetails.getVillage().equalsIgnoreCase("") || userDetails.getVillage().equalsIgnoreCase("null") || userDetails.getVillage().equalsIgnoreCase("0") || userDetails.getVillage() == null || userDetails.getVillage().equalsIgnoreCase("0")) {
            address.setText("");
        } else {
            address.setText(userDetails.getVillage());
        }
        if (userDetails.getNomineeName().equalsIgnoreCase("") || userDetails.getNomineeName().equalsIgnoreCase("null") || userDetails.getNomineeName().equalsIgnoreCase("0") || userDetails.getNomineeName() == null || userDetails.getNomineeName().equalsIgnoreCase("0")) {
            nominee_name.setText("");
        } else {
            nominee_name.setText(userDetails.getNomineeName());
        }
        if (userDetails.getNomineeAge().equalsIgnoreCase("") || userDetails.getNomineeAge().equalsIgnoreCase("null") || userDetails.getNomineeAge().equalsIgnoreCase("0") || userDetails.getNomineeAge() == null || userDetails.getNomineeAge().equalsIgnoreCase("0")) {
            nominee_age.setText("");
        } else {
            nominee_age.setText(userDetails.getNomineeAge());
        }
        if (userDetails.getNomineemobile().equalsIgnoreCase("") || userDetails.getNomineemobile().equalsIgnoreCase("null") || userDetails.getNomineemobile().equalsIgnoreCase("0") || userDetails.getNomineemobile() == null || userDetails.getNomineemobile().equalsIgnoreCase("0")) {
            nominee_mobile.setText("");
        } else {
            nominee_mobile.setText(userDetails.getNomineemobile());
        }
        if (userDetails.getOccupationCategory().equalsIgnoreCase("") || userDetails.getOccupationCategory().equalsIgnoreCase("null") || userDetails.getOccupationCategory().equalsIgnoreCase("0") || userDetails.getOccupationCategory() == null || userDetails.getOccupationCategory().equalsIgnoreCase("0")) {
            customer_profession.setText("");
        } else {
            customer_profession.setText(userDetails.getOccupationCategory());
        }
        if (userDetails.getOccupation().equalsIgnoreCase("") || userDetails.getOccupation().equalsIgnoreCase("null") || userDetails.getOccupation().equalsIgnoreCase("0") || userDetails.getOccupation() == null || userDetails.getOccupation().equalsIgnoreCase("0")) {
            customer_occupation.setText("");
        } else {
            customer_occupation.setText(userDetails.getOccupation());
        }
        if (userDetails.getAadharNo().equalsIgnoreCase("") || userDetails.getAadharNo().equalsIgnoreCase("null") || userDetails.getAadharNo().equalsIgnoreCase("0") || userDetails.getAadharNo() == null || userDetails.getAadharNo().equalsIgnoreCase("0")) {
            aadhar_no.setText("");
        } else {
            aadhar_no.setText(userDetails.getAadharNo());
        }
        if (userDetails.getPanNo().equalsIgnoreCase("") || userDetails.getPanNo().equalsIgnoreCase("null") || userDetails.getPanNo().equalsIgnoreCase("0") || userDetails.getPanNo() == null || userDetails.getPanNo().equalsIgnoreCase("0")) {
            pan_no.setText("");
        } else {
            pan_no.setText(userDetails.getPanNo());
        }
        if (userDetails.getRelationName().equalsIgnoreCase("") || userDetails.getRelationName().equalsIgnoreCase("null") || userDetails.getRelationName().equalsIgnoreCase("0") || userDetails.getRelationName() == null || userDetails.getRelationName().equalsIgnoreCase("0")) {
            nominee_relation.setText("");
        } else {
            nominee_relation.setText(userDetails.getRelationName());
        }
    }

}