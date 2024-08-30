package com.guruvardaan.ghargharsurvey.welcome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.guruvardaan.ghargharsurvey.agent.SelectAddress;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.DataPart;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.utils.VolleyMultipartRequest;
import com.guruvardaan.ghargharsurvey.visits.SelectOccupation;
import com.guruvardaan.ghargharsurvey.visits.SelectProfession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;

public class AddClientActivity extends BaseActivity {

    LinearLayout layout_1, layout_2, layout_3, layout_4, layout_5, layout_6, layout_7;
    EditText prefix_edit, first_name, last_name, dob, phone_number, alternate_no, relative_name, relation_type, pan_no;
    EditText aadhar_no, email, country, state, city, tehsil, area, address, house_no, landmark, pincode, whatsapp;
    EditText nominee_name, nominee_relation, nominee_age, nominee_mobile, nominee_whatsapp;
    EditText account_holder, father_name, account_number, ifsc, bank_name, branch, customer_profession, customer_occupation, customer_photo, customer_signature;
    TextView prev_1, next_1, prev_2, next_2, prev_3, next_3, prev_4, next_4, prev_5, next_5, prev_6, next_6, prev_7, next_7;
    String prefix_arr[] = {"Mr.", "Mrs.", "Smt.", "Km."};
    String relation_array[] = {"Son", "Daughter", "Husband", "Wife", "Guardian"};
    String state_id, city_id, area_id, tehsil_id, sel, occ = "";
    UserDetails userDetails;
    ProgressDialog pd;
    String cust_id = "";
    ImageView back_button;
    ScrollView mainScroll;
    StepperIndicator step_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_client, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        step_indicator = (StepperIndicator) findViewById(R.id.step_indicator);
        initializeVars();
        prev_1.setText("Back");
        next_7.setText("Submit");
        state_id = "";
        city_id = "";
        area_id = "";
        tehsil_id = "";
        cust_id = "";
        sel = "";
        occ = "";
        checkAndRequestPermissions();
        prev_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefix_edit.getText().toString().length() < 2) {
                    Toast.makeText(getApplicationContext(), "Select Prefix", Toast.LENGTH_SHORT).show();
                } else if (first_name.getText().toString().length() < 2) {
                    Toast.makeText(getApplicationContext(), "Enter Valid First Name", Toast.LENGTH_SHORT).show();
                } else if (last_name.getText().toString().length() < 2) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Last Name", Toast.LENGTH_SHORT).show();
                } else if (dob.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Select Date of Birth", Toast.LENGTH_SHORT).show();
                } else if (phone_number.getText().toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter 10 Digit Mobile", Toast.LENGTH_SHORT).show();
                } else {
                    setLayout(2);
                }
            }
        });
        prev_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout(1);
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
                setLayout(6);
            }
        });
        prev_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout(5);
            }
        });
        next_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createJSON();
            }
        });
        prev_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path1, path2 = "";
                Bitmap bm1, bm2 = null;
                if (!customer_photo.getText().toString().trim().equalsIgnoreCase("")) {
                    path1 = customer_photo.getText().toString().trim().substring(customer_photo.getText().toString().trim().lastIndexOf("/") + 1);
                    bm1 = getBitmapFromPath(customer_photo.getText().toString().trim());
                } else {
                    bm1 = null;
                    path1 = "";
                }
                if (!customer_signature.getText().toString().trim().equalsIgnoreCase("")) {
                    path2 = customer_signature.getText().toString().trim().substring(customer_signature.getText().toString().trim().lastIndexOf("/") + 1);
                    bm2 = getBitmapFromPath(customer_signature.getText().toString().trim());
                } else {
                    bm2 = null;
                    path2 = "";
                }
                if (bm1 == null && bm2 == null) {
                    Toast.makeText(getApplicationContext(), "Customer Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    uploadBitmap(bm1, bm2, path1, path2);
                }
            }
        });
        prefix_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddClientActivity.this);
                mBuilder.setTitle("Select Prefix");
                mBuilder.setSingleChoiceItems(prefix_arr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        prefix_edit.setText(prefix_arr[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(AddClientActivity.this, R.style.MySpinnerDatePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });
        relation_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddClientActivity.this);
                mBuilder.setTitle("Select Relation");
                mBuilder.setSingleChoiceItems(relation_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        relation_type.setText(relation_array[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        nominee_relation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddClientActivity.this);
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
        customer_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    FilePickerBuilder.getInstance()
                            .setMaxCount(1) //optional
                            .setActivityTheme(R.style.LibAppTheme) //optional
                            .pickPhoto(AddClientActivity.this, 201);
                }
            }
        });
        customer_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    FilePickerBuilder.getInstance()
                            .setMaxCount(1) //optional
                            .setActivityTheme(R.style.LibAppTheme) //optional
                            .pickPhoto(AddClientActivity.this, 202);
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
        if (i == 1) {
            step_indicator.setCurrentStep(1);
            layout_1.setVisibility(View.VISIBLE);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.GONE);
            layout_6.setVisibility(View.GONE);
            layout_7.setVisibility(View.GONE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
        if (i == 2) {
            step_indicator.setCurrentStep(2);
            layout_1.setVisibility(View.GONE);
            layout_2.setVisibility(View.VISIBLE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.GONE);
            layout_6.setVisibility(View.GONE);
            layout_7.setVisibility(View.GONE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
        if (i == 3) {
            step_indicator.setCurrentStep(3);
            layout_1.setVisibility(View.GONE);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.VISIBLE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.GONE);
            layout_6.setVisibility(View.GONE);
            layout_7.setVisibility(View.GONE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
        if (i == 4) {
            step_indicator.setCurrentStep(4);
            layout_1.setVisibility(View.GONE);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.VISIBLE);
            layout_5.setVisibility(View.GONE);
            layout_6.setVisibility(View.GONE);
            layout_7.setVisibility(View.GONE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
        if (i == 5) {
            step_indicator.setCurrentStep(5);
            layout_1.setVisibility(View.GONE);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.VISIBLE);
            layout_6.setVisibility(View.GONE);
            layout_7.setVisibility(View.GONE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
        if (i == 6) {
            step_indicator.setCurrentStep(6);
            layout_1.setVisibility(View.GONE);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.GONE);
            layout_6.setVisibility(View.VISIBLE);
            layout_7.setVisibility(View.GONE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
        if (i == 7) {
            step_indicator.setCurrentStep(7);
            layout_1.setVisibility(View.GONE);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.GONE);
            layout_6.setVisibility(View.GONE);
            layout_7.setVisibility(View.VISIBLE);
            mainScroll.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    public void initializeVars() {
        mainScroll = (ScrollView) findViewById(R.id.mainScroll);
        layout_1 = (LinearLayout) findViewById(R.id.layout_1);
        layout_2 = (LinearLayout) findViewById(R.id.layout_2);
        layout_3 = (LinearLayout) findViewById(R.id.layout_3);
        layout_4 = (LinearLayout) findViewById(R.id.layout_4);
        layout_5 = (LinearLayout) findViewById(R.id.layout_5);
        layout_6 = (LinearLayout) findViewById(R.id.layout_6);
        layout_7 = (LinearLayout) findViewById(R.id.layout_7);
        prefix_edit = (EditText) findViewById(R.id.prefix_edit);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        dob = (EditText) findViewById(R.id.dob);
        phone_number = (EditText) findViewById(R.id.phone_number);
        alternate_no = (EditText) findViewById(R.id.alternate_no);
        relative_name = (EditText) findViewById(R.id.relative_name);
        relation_type = (EditText) findViewById(R.id.relation_type);
        pan_no = (EditText) findViewById(R.id.pan_no);
        aadhar_no = (EditText) findViewById(R.id.aadhar_no);
        email = (EditText) findViewById(R.id.email);
        country = (EditText) findViewById(R.id.country);
        state = (EditText) findViewById(R.id.state);
        city = (EditText) findViewById(R.id.city);
        tehsil = (EditText) findViewById(R.id.tehsil);
        area = (EditText) findViewById(R.id.area);
        address = (EditText) findViewById(R.id.address);
        father_name = (EditText) findViewById(R.id.father_name);
        house_no = (EditText) findViewById(R.id.house_no);
        landmark = (EditText) findViewById(R.id.landmark);
        pincode = (EditText) findViewById(R.id.pincode);
        whatsapp = (EditText) findViewById(R.id.whatsapp);
        nominee_name = (EditText) findViewById(R.id.nominee_name);
        nominee_relation = (EditText) findViewById(R.id.nominee_relation);
        nominee_age = (EditText) findViewById(R.id.nominee_age);
        nominee_mobile = (EditText) findViewById(R.id.nominee_mobile);
        nominee_whatsapp = (EditText) findViewById(R.id.nominee_whatsapp);
        account_holder = (EditText) findViewById(R.id.account_holder);
        account_number = (EditText) findViewById(R.id.account_number);
        ifsc = (EditText) findViewById(R.id.ifsc);
        bank_name = (EditText) findViewById(R.id.bank_name);
        branch = (EditText) findViewById(R.id.branch);
        customer_profession = (EditText) findViewById(R.id.customer_profession);
        customer_occupation = (EditText) findViewById(R.id.customer_occupation);
        customer_photo = (EditText) findViewById(R.id.customer_photo);
        customer_signature = (EditText) findViewById(R.id.customer_signature);
        prev_1 = (TextView) findViewById(R.id.prev_1);
        next_1 = (TextView) findViewById(R.id.next_1);
        prev_2 = (TextView) findViewById(R.id.prev_2);
        next_2 = (TextView) findViewById(R.id.next_2);
        prev_3 = (TextView) findViewById(R.id.prev_3);
        next_3 = (TextView) findViewById(R.id.next_3);
        prev_4 = (TextView) findViewById(R.id.prev_4);
        next_4 = (TextView) findViewById(R.id.next_4);
        prev_5 = (TextView) findViewById(R.id.prev_5);
        next_5 = (TextView) findViewById(R.id.next_5);
        prev_6 = (TextView) findViewById(R.id.prev_6);
        next_6 = (TextView) findViewById(R.id.next_6);
        prev_7 = (TextView) findViewById(R.id.prev_7);
        next_7 = (TextView) findViewById(R.id.next_7);
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
        if (requestCode == 201 && resultCode == RESULT_OK) {
            try {
                customer_photo.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 202 && resultCode == RESULT_OK) {
            try {
                customer_signature.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (layout_7.getVisibility() == View.VISIBLE) {
            finish();
        } else if (layout_6.getVisibility() == View.VISIBLE) {
            setLayout(5);
        } else if (layout_5.getVisibility() == View.VISIBLE) {
            setLayout(4);
        } else if (layout_4.getVisibility() == View.VISIBLE) {
            setLayout(3);
        } else if (layout_3.getVisibility() == View.VISIBLE) {
            setLayout(2);
        } else if (layout_2.getVisibility() == View.VISIBLE) {
            setLayout(1);
        } else if (layout_1.getVisibility() == View.VISIBLE) {
            finish();
        }
    }

    public void createJSON() {
        try {
            JSONObject params = new JSONObject();
            params.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            params.put("advisorId", userDetails.getuserid());
            params.put("initials", prefix_edit.getText().toString());
            params.put("customerFirstName", first_name.getText().toString());
            params.put("customerLastName", last_name.getText().toString());
            params.put("flatHouseNo", house_no.getText().toString());
            params.put("landMark", landmark.getText().toString());
            params.put("stateid", state_id);
            params.put("cityId", city_id);
            params.put("area_id", area_id);
            params.put("pin_code", pincode.getText().toString());
            params.put("tehsil", tehsil_id);
            params.put("village", address.getText().toString());
            params.put("mobile1", phone_number.getText().toString());
            params.put("mobile2", alternate_no.getText().toString());
            params.put("whatsapp", whatsapp.getText().toString());
            params.put("email", email.getText().toString());
            params.put("dob", dob.getText().toString());
            params.put("nomineeName", nominee_name.getText().toString());
            params.put("nomineeAge", nominee_age.getText().toString());
            params.put("relation", nominee_relation.getText().toString());
            params.put("nomineemobile", nominee_mobile.getText().toString());
            params.put("acHolder", account_holder.getText().toString());
            params.put("acNo", account_number.getText().toString());
            params.put("IFSC", ifsc.getText().toString());
            params.put("bank", bank_name.getText().toString());
            params.put("branch", branch.getText().toString());
            params.put("occupationCategory", sel);
            params.put("occupation", occ);
            params.put("Father", father_name.getText().toString().trim());
            params.put("panno", pan_no.getText().toString());
            params.put("aadharNo", aadhar_no.getText().toString());
            params.put("relationType", relation_type.getText().toString());
            params.put("relationName", relative_name.getText().toString());
            saveCustomer(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCustomer(String jsons) {
        pd = ProgressDialog.show(AddClientActivity.this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.ADD_CLIENT_URL,
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
                            cust_id = object.getString("Column2");
                            setLayout(7);
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errormsg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Customer couldn't be added", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Customer couldn't be added", Toast.LENGTH_SHORT).show();
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

    private void uploadBitmap(final Bitmap bitmap1, final Bitmap bitmap2, final String name1, final String name2) {
        pd = ProgressDialog.show(AddClientActivity.this);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Config.ADD_CLIENT_IMG + cust_id + "/",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pd.dismiss();
                        System.out.println("Akanksha " + response.data);
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                if (object.getString("Column1").equalsIgnoreCase("T")) {
                                    Toast.makeText(getApplicationContext(), "Customer Added Successfully", Toast.LENGTH_SHORT).show();
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (bitmap1 != null) {
                    params.put("custImg", new DataPart(name1, getFileDataFromDrawable(bitmap1)));
                }
                if (bitmap2 != null) {
                    params.put("custSign", new DataPart(name2, getFileDataFromDrawable(bitmap2)));
                }

                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public Bitmap getBitmapFromPath(String path) {
        File image = new File(path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        return bitmap;
    }
}