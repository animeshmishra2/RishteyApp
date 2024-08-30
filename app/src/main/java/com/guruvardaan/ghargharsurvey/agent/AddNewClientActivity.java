package com.guruvardaan.ghargharsurvey.agent;

import android.Manifest;
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
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.DataPart;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.utils.VolleyMultipartRequest;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

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

public class AddNewClientActivity extends BaseActivity {

    EditText prefix_edit, first_name, last_name, dob, phone_number, alternate_no, whatsapp, relation, state, city, tehsil, area, address, house_no, landmark, pincode;
    TextView next_1, father_txt, prev_4, next_4;
    LinearLayout layout_1, layout_3;
    ImageView back_button;
    UserDetails userDetails;
    ScrollView mainScroll;
    String prefix_arr[] = {"Mr.", "Mrs.", "Smt.", "Km."};
    String relation_array[] = {"Son of", "Daughter of", "Husband of", "Wife of", "Care of"};
    ProgressDialog pd;
    String cust_id = "";
    String state_id, city_id, area_id, tehsil_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_new_client, frameLayout);
        initializeVars();
        cust_id = "";
        state_id = "";
        city_id = "";
        area_id = "";
        tehsil_id = "";
        father_txt = (TextView) findViewById(R.id.father_txt);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        next_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefix_edit.getText().toString().length() < 2) {
                    Toast.makeText(getApplicationContext(), "Select Prefix", Toast.LENGTH_SHORT).show();
                } else if (first_name.getText().toString().length() < 2) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Full Name", Toast.LENGTH_SHORT).show();
                } else if (relation.getText().toString().trim().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Select Relation", Toast.LENGTH_SHORT).show();
                } else if (last_name.getText().toString().length() < 2) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Relation Name", Toast.LENGTH_SHORT).show();
                } else if (dob.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Select Date of Birth", Toast.LENGTH_SHORT).show();
                } else if (phone_number.getText().toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter 10 Digit Mobile", Toast.LENGTH_SHORT).show();
                } else {
                    layout_1.setVisibility(View.GONE);
                    layout_3.setVisibility(View.VISIBLE);
                    mainScroll.fullScroll(ScrollView.FOCUS_UP);
                }
            }
        });

        prefix_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddNewClientActivity.this);
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
                DatePickerDialog dpd = new DatePickerDialog(AddNewClientActivity.this, R.style.MySpinnerDatePickerStyle,
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


        relation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddNewClientActivity.this);
                mBuilder.setTitle("Select Relation");
                mBuilder.setSingleChoiceItems(relation_array, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        relation.setText(relation_array[s]);
                        if (s == 0 || s == 1) {
                            father_txt.setText("Father Name");
                            last_name.setHint("Enter Father Name");
                        } else if (s == 2) {
                            father_txt.setText("Wife Name");
                            last_name.setHint("Enter Wife Name");
                        } else if (s == 3) {
                            father_txt.setText("Husband Name");
                            last_name.setHint("Enter Husband Name");
                        } else if (s == 4) {
                            father_txt.setText("Guardian Name");
                            last_name.setHint("Enter Guardian Name");
                        } else {
                            father_txt.setText("Father Name");
                            last_name.setHint("Enter Father Name");
                        }
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
        prev_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_3.setVisibility(View.GONE);
                layout_1.setVisibility(View.VISIBLE);
                mainScroll.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        next_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createJSON();
            }
        });

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

    public String getMidName() {
        String mid_name = "";
        if (relation.getText().toString().trim().equalsIgnoreCase("Son of")) {
            mid_name = " S/O ";
        } else if (relation.getText().toString().trim().equalsIgnoreCase("Daughter of")) {
            mid_name = " D/O ";
        } else if (relation.getText().toString().trim().equalsIgnoreCase("Wife of")) {
            mid_name = " W/O ";
        } else if (relation.getText().toString().trim().equalsIgnoreCase("Husband of")) {
            mid_name = " H/O ";
        } else if (relation.getText().toString().trim().equalsIgnoreCase("Care of")) {
            mid_name = " C/O ";
        } else {
            mid_name = " S/O ";
        }
        return mid_name;
    }


    public void createJSON() {
        try {
            JSONObject params = new JSONObject();
            params.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            params.put("advisorId", userDetails.getuserid());
            params.put("initials", prefix_edit.getText().toString());
            params.put("customerFirstName", first_name.getText().toString());
            params.put("customerLastName", getMidName() + last_name.getText().toString());
            params.put("flatHouseNo", house_no.getText().toString());
            params.put("landMark", landmark.getText().toString());
            params.put("stateid", state_id);
            params.put("cityId", city_id);
            params.put("area_id", area_id);
            params.put("pin_code", pincode.getText().toString().trim());
            params.put("tehsil", tehsil_id);
            params.put("village", address.getText().toString().trim());
            params.put("mobile1", phone_number.getText().toString());
            params.put("mobile2", alternate_no.getText().toString());
            params.put("whatsapp", whatsapp.getText().toString());
            params.put("email", "");
            params.put("dob", dob.getText().toString());
            params.put("nomineeName", "");
            params.put("nomineeAge", "");
            params.put("relation", "");
            params.put("nomineemobile", "");
            params.put("acHolder", "");
            params.put("acNo", "");
            params.put("IFSC", "");
            params.put("bank", "");
            params.put("branch", "");
            params.put("occupationCategory", "");
            params.put("occupation", "");
            params.put("Father", "");
            params.put("panno", "");
            params.put("aadharNo", "");
            params.put("relationType", "");
            params.put("relationName", "");
            saveCustomer(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initializeVars() {
        relation = (EditText) findViewById(R.id.relation);
        mainScroll = (ScrollView) findViewById(R.id.mainScroll);
        layout_1 = (LinearLayout) findViewById(R.id.layout_1);
        layout_3 = (LinearLayout) findViewById(R.id.layout_3);
        prefix_edit = (EditText) findViewById(R.id.prefix_edit);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        dob = (EditText) findViewById(R.id.dob);
        state = (EditText) findViewById(R.id.state);
        city = (EditText) findViewById(R.id.city);
        tehsil = (EditText) findViewById(R.id.tehsil);
        area = (EditText) findViewById(R.id.area);
        address = (EditText) findViewById(R.id.address);
        house_no = (EditText) findViewById(R.id.house_no);
        landmark = (EditText) findViewById(R.id.landmark);
        pincode = (EditText) findViewById(R.id.pincode);
        phone_number = (EditText) findViewById(R.id.phone_number);
        alternate_no = (EditText) findViewById(R.id.alternate_no);
        whatsapp = (EditText) findViewById(R.id.whatsapp);

        next_1 = (TextView) findViewById(R.id.next_1);
        prev_4 = (TextView) findViewById(R.id.prev_3);
        next_4 = (TextView) findViewById(R.id.next_3);
    }

    public void saveCustomer(String jsons) {
        pd = ProgressDialog.show(AddNewClientActivity.this);
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
                            Toast.makeText(getApplicationContext(), "Customer Added Successfully", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
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
        pd = ProgressDialog.show(AddNewClientActivity.this);
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK) {
            try {
                //customer_photo.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 202 && resultCode == RESULT_OK) {
            try {
                //customer_signature.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    }
}