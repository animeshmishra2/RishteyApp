package com.guruvardaan.ghargharsurvey.agent;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_CHEQUE_STATUS;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.guruvardaan.ghargharsurvey.utils.UriToBitmapConverter;
import com.guruvardaan.ghargharsurvey.utils.UriToPathConverter;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.DataPart;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.utils.VolleyMultipartRequest;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectOfferActivity;

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
import pl.droidsonroids.gif.GifImageView;

public class PayDetailsActivity extends BaseActivity {

    TextView service_id, late_fee, installment_amount, otp_sms, submit;
    EditText customer_name, payment_mode, bank, branch, cheque_amount, cheque_number, cheque_date, offers, payment_slip, cheque_image;
    LinearLayout bank_layout, branch_layout, ch_am_layout, ch_num_layout, ch_date_layout, cheque_im_layout, pay_layout, otp_layout, pay_slip;
    String payment_m[] = {"Online", "Bank Transfer", "Cash", "Cheque"};
    String bank_is = "";
    TextView pay_amount;
    ProgressDialog pd;
    UserDetails userDetails;
    String otpp = "";
    EditText otp_1, otp_2, otp_3, otp_4, otp_5, otp_6;
    ImageView back_button;
    String off_id = "0", off_am = "";
    GifImageView newgif;
    int height;
    int width;
    Bitmap chequeBms, slipBms;
    Uri slipUri, chequeUri;
    DisplayMetrics displayMetrics = new DisplayMetrics();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_pay_details, frameLayout);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels / 2;
        width = displayMetrics.widthPixels / 2;
        slipUri = null;
        chequeUri = null;
        slipBms = null;
        chequeBms = null;
        back_button = (ImageView) findViewById(R.id.back_button);
        newgif = (GifImageView) findViewById(R.id.newgif);
        pay_slip = (LinearLayout) findViewById(R.id.pay_slip);
        newgif.setVisibility(View.GONE);
        service_id = (TextView) findViewById(R.id.service_id);
        userDetails = new UserDetails(getApplicationContext());
        otpp = "";
        bank_is = "0";
        off_id = "0";
        off_am = "0";
        late_fee = (TextView) findViewById(R.id.late_fee);
        installment_amount = (TextView) findViewById(R.id.installment_amount);
        customer_name = (EditText) findViewById(R.id.customer_name);
        payment_mode = (EditText) findViewById(R.id.payment_mode);
        otp_sms = (TextView) findViewById(R.id.otp_sms);
        submit = (TextView) findViewById(R.id.submit);
        otp_1 = (EditText) findViewById(R.id.otp_1);
        otp_2 = (EditText) findViewById(R.id.otp_2);
        otp_3 = (EditText) findViewById(R.id.otp_3);
        otp_4 = (EditText) findViewById(R.id.otp_4);
        otp_5 = (EditText) findViewById(R.id.otp_5);
        otp_6 = (EditText) findViewById(R.id.otp_6);
        bank = (EditText) findViewById(R.id.bank);
        pay_amount = (TextView) findViewById(R.id.pay_amount);
        otp_layout = (LinearLayout) findViewById(R.id.otp_layout);
        pay_layout = (LinearLayout) findViewById(R.id.pay_layout);
        cheque_image = (EditText) findViewById(R.id.cheque_image);
        payment_slip = (EditText) findViewById(R.id.payment_slip);
        branch = (EditText) findViewById(R.id.branch);
        cheque_amount = (EditText) findViewById(R.id.cheque_amount);
        cheque_number = (EditText) findViewById(R.id.cheque_number);
        cheque_date = (EditText) findViewById(R.id.cheque_date);
        offers = (EditText) findViewById(R.id.offers);
        bank_layout = (LinearLayout) findViewById(R.id.bank_layout);
        branch_layout = (LinearLayout) findViewById(R.id.branch_layout);
        ch_am_layout = (LinearLayout) findViewById(R.id.ch_am_layout);
        ch_num_layout = (LinearLayout) findViewById(R.id.ch_num_layout);
        ch_date_layout = (LinearLayout) findViewById(R.id.ch_date_layout);
        cheque_im_layout = (LinearLayout) findViewById(R.id.cheque_im_layout);
        service_id.setText(getIntent().getExtras().getString("SID"));
        late_fee.setText(getIntent().getExtras().getString("LF"));
        installment_amount.setText("\u20b9" + getIntent().getExtras().getString("AM"));
        customer_name.setText(getIntent().getExtras().getString("NAME"));
        payment_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PayDetailsActivity.this);
                mBuilder.setTitle("Select Payment Mode");
                mBuilder.setSingleChoiceItems(payment_m, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        payment_mode.setText(payment_m[s]);
                        setLayout(s);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        cheque_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(PayDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cheque_date.setText(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectRank.class);
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", "SELECT BANK");
                bundle.putString("ID", "0");
                intent.putExtras(bundle);
                startActivityForResult(intent, 113);
            }
        });
        payment_slip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (checkAndRequestPermissions()) {
                        pickMedia.launch(new PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                .build());
                    }
                } else {
                    if (checkAndRequestPermissionsStorage()) {
                        FilePickerBuilder.getInstance()
                                .setMaxCount(1) //optional
                                .setActivityTheme(R.style.LibAppTheme) //optional
                                .pickPhoto(PayDetailsActivity.this, 205);
                    }
                }
            }
        });

        cheque_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (checkAndRequestPermissions()) {
                            pickMedia1.launch(new PickVisualMediaRequest.Builder()
                                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                    .build());
                        }
                    } else {
                        if (checkAndRequestPermissionsStorage()) {
                            FilePickerBuilder.getInstance()
                                    .setMaxCount(1) //optional
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickPhoto(PayDetailsActivity.this, 206);
                        }
                    }
                }
            }
        });

        pay_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payment_mode.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                } else {
                    if (payment_mode.getText().toString().trim().equalsIgnoreCase("Cash") || payment_mode.getText().toString().trim().equalsIgnoreCase("Online")) {
                        if (!userDetails.getTypess().equalsIgnoreCase("2")) {
                            /*if (payment_slip.getText().toString().trim().length() <= 0) {
                                Toast.makeText(getApplicationContext(), "Upload Payment Slip", Toast.LENGTH_SHORT).show();
                            } else {*/
                            readyData();
                            // }
                        } else {
                            readyData();
                        }
                    } else if (payment_mode.getText().toString().trim().equalsIgnoreCase("Bank Transfer")) {
                        if (bank.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Select Bank", Toast.LENGTH_SHORT).show();
                        } else if (branch.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter Branch Name", Toast.LENGTH_SHORT).show();
                        }
                        if (!userDetails.getTypess().equalsIgnoreCase("2")) {
                            /*if (payment_slip.getText().toString().trim().length() <= 0) {
                                Toast.makeText(getApplicationContext(), "Upload Payment Slip", Toast.LENGTH_SHORT).show();
                            } else {*/
                            readyData();
                            // }
                        } else {
                            readyData();
                        }
                    } else if (payment_mode.getText().toString().trim().equalsIgnoreCase("Cheque")) {
                        if (bank.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Select Bank", Toast.LENGTH_SHORT).show();
                        } else if (branch.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter Branch Name", Toast.LENGTH_SHORT).show();
                        } else if (cheque_amount.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter Cheque Amount", Toast.LENGTH_SHORT).show();
                        } else if (cheque_number.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter Cheque Number", Toast.LENGTH_SHORT).show();
                        } else if (cheque_date.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Select Cheque Date", Toast.LENGTH_SHORT).show();
                        } else {
                            if (userDetails.getTypess().equalsIgnoreCase("2")) {
                                // Toast.makeText(getApplicationContext(), "ready", Toast.LENGTH_SHORT).show();
                                getChequeStatus();
                            } else {
                                /*if (cheque_image.getText().toString().trim().length() <= 0) {
                                    Toast.makeText(getApplicationContext(), "Upload Cheque Image", Toast.LENGTH_SHORT).show();
                                } else {*/
                                getChequeStatus();
                                //}
                            }
                        }
                    }
                }
            }
        });
        otp_1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkOtp();
                if (otp_1.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_2.requestFocus();
                } else if (otp_1.getText().toString().length() == 0) {
                    otp_1.requestFocus();
                }
            }

        });
        otp_2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkOtp();
                if (otp_2.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_3.requestFocus();
                } else if (otp_2.getText().toString().length() == 0) {
                    otp_2.clearFocus();
                    otp_1.requestFocus();
                }
            }

        });
        otp_3.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkOtp();
                if (otp_3.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_4.requestFocus();
                } else if (otp_3.getText().toString().length() == 0) {
                    otp_3.clearFocus();
                    otp_2.requestFocus();
                }
            }

        });
        otp_4.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkOtp();
                if (otp_4.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_5.requestFocus();
                } else if (otp_4.getText().toString().length() == 0) {
                    otp_4.clearFocus();
                    otp_3.requestFocus();
                }
            }

        });
        otp_5.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkOtp();
                if (otp_5.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_6.requestFocus();
                } else if (otp_5.getText().toString().length() == 0) {
                    otp_5.clearFocus();
                    otp_4.requestFocus();
                }
            }

        });

        otp_6.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkOtp();
                if (otp_6.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_6.requestFocus();
                } else if (otp_6.getText().toString().length() == 0) {
                    otp_6.clearFocus();
                    otp_5.requestFocus();
                }
            }

        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpp.equalsIgnoreCase((otp_1.getText().toString().trim() + otp_2.getText().toString().trim() + otp_3.getText().toString().trim() + otp_4.getText().toString().trim() + otp_5.getText().toString().trim() + otp_6.getText().toString().trim()))) {
                    readyData();
                } else {
                    Toast.makeText(getApplicationContext(), "You Entered Wrong OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectOfferActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("OFFER", getIntent().getExtras().getString("AM"));
                intent.putExtras(bundle);
                startActivityForResult(intent, 34);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    payment_slip.setText(UriToPathConverter.getPathFromUri(getApplicationContext(), uri));
                    slipUri = uri;
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(UriToPathConverter.getPathFromUri(getApplicationContext(), uri))
                            .apply(new RequestOptions().override(width, height))
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    slipBms = resource;
                                }
                            });
                    //slipBms = UriToBitmapConverter.uriToBitmap(getApplicationContext(), slipUri);
                } else {
                    Toast.makeText(getApplicationContext(), "No Valid Image Selected", Toast.LENGTH_SHORT).show();
                }
            });

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia1 =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    cheque_image.setText(UriToPathConverter.getPathFromUri(getApplicationContext(), uri));
                    chequeUri = uri;
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(UriToPathConverter.getPathFromUri(getApplicationContext(), uri))
                            .apply(new RequestOptions().override(width, height))
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    chequeBms = resource;
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "No Valid Image Selected", Toast.LENGTH_SHORT).show();
                }
            });

    public void readyData() {
        String path1, path2 = "";
        if (!cheque_image.getText().toString().trim().equalsIgnoreCase("")) {
            path1 = cheque_image.getText().toString().trim().substring(cheque_image.getText().toString().trim().lastIndexOf("/") + 1);
        } else {
            chequeBms = null;
            path1 = "";
        }
        if (!payment_slip.getText().toString().trim().equalsIgnoreCase("")) {
            path2 = payment_slip.getText().toString().trim().substring(payment_slip.getText().toString().trim().lastIndexOf("/") + 1);
        } else {
            slipBms = null;
            path2 = "";
        }
        String cd = "0";
        String cm = "0";
        String cn = "0";
        if (cheque_date.getText().toString().trim().length() > 0) {
            cd = cheque_date.getText().toString().trim();
        }
        if (cheque_amount.getText().toString().trim().length() > 0) {
            cm = cheque_amount.getText().toString().trim();
        }
        if (cheque_number.getText().toString().trim().length() > 0) {
            cn = cheque_number.getText().toString().trim();
        }
        String downpayt = "0";
        if (getIntent().getExtras().getString("AM").length() > 0) {
            downpayt = getIntent().getExtras().getString("AM").trim();
        }
        String brr = "0";
        if (branch.getText().toString().trim().length() > 0) {
            brr = branch.getText().toString().trim();
        }
        //System.out.println("Animesh Mishra " + bms);
        uploadBitmap(path1, path2, cd, cm, cn, downpayt, brr, off_id, off_am);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        int quality = 100;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public Bitmap getBitmapFromPath(String path) {
        File image = new File(path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return bitmap;
    }

    public void checkOtp() {
        String otp = otp_1.getText().toString().trim() + otp_2.getText().toString().trim() + otp_3.getText().toString().trim() + otp_4.getText().toString().trim() + otp_5.getText().toString().trim() + otp_6.getText().toString().trim();
        if (otp.length() == 6) {
            submit.setEnabled(true);
            submit.setBackgroundResource(R.drawable.button);
            submit.setTextColor(Color.parseColor("#ffffff"));
        } else {
            submit.setBackgroundResource(R.drawable.button_disable);
            submit.setTextColor(Color.parseColor("#000000"));
            submit.setEnabled(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 113 && resultCode == RESULT_OK) {
            bank_is = data.getExtras().getString("ID");
            bank.setText(data.getExtras().getString("NAME"));
        }
        if (requestCode == 34 && resultCode == RESULT_OK) {
            off_id = data.getExtras().getString("ID");
            off_am = data.getExtras().getString("AMOUNT");
            offers.setText(off_am);
        }
        if (requestCode == 205 && resultCode == RESULT_OK) {
            try {
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)))
                        .apply(new RequestOptions().override(width, height))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                slipBms = resource;
                            }
                        });
                payment_slip.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
                System.out.println("Mishra " + getBitmapFromPath(payment_slip.getText().toString().trim()).getWidth());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 206 && resultCode == RESULT_OK) {
            try {
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)))
                        .apply(new RequestOptions().override(width, height))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                chequeBms = resource;
                            }
                        });
                cheque_image.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getChequeStatus() {
        pd = ProgressDialog.show(PayDetailsActivity.this, "Please Wait..");
        String URL_CHECK = GET_CHEQUE_STATUS + userDetails.getuserid() + "/" + cheque_number.getText().toString().trim() + "/" + bank_is + "/" + getIntent().getExtras().getString("AM") + "/" + cheque_amount.getText().toString().trim();
        System.out.println(URL_CHECK);
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
                                if (object.getString("Column1").equalsIgnoreCase("t")) {
                                    if (object.has("sentsms")) {
                                        otpp = "";
                                        if (object.getString("sentsms").equalsIgnoreCase("1")) {
                                            otp_layout.setVisibility(View.VISIBLE);
                                            pay_layout.setVisibility(View.GONE);
                                            otpp = object.getString("otpNumber");
                                            otp_sms.setText(getString(R.string.otp_name) + " Mobile - " + object.getString("mobileNo"));
                                        } else {
                                            readyData();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "This Cheque has been exhausted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please Check Cheque Details", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Check Cheque Details", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Please Check Cheque Details", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Please Check Cheque Details", Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    private boolean checkAndRequestPermissionsStorage() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (listPermissionsNeeded.size() > 0) {
            System.out.println("Animesh " + listPermissionsNeeded.get(0));
            ActivityCompat.requestPermissions(PayDetailsActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2011);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES);
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_MEDIA_IMAGES);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (listPermissionsNeeded.size() > 0) {
            System.out.println("Animesh " + listPermissionsNeeded.get(0));
            ActivityCompat.requestPermissions(PayDetailsActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2011);
            return false;
        } else {
            return true;
        }
    }

    public void setLayout(int pos) {
        if (pos == 0 || pos == 2) {
            if (pos == 0) {
                pay_slip.setVisibility(View.VISIBLE);
            } else {
                pay_slip.setVisibility(View.GONE);
            }
            bank_layout.setVisibility(View.GONE);
            branch_layout.setVisibility(View.GONE);
            ch_am_layout.setVisibility(View.GONE);
            ch_num_layout.setVisibility(View.GONE);
            ch_date_layout.setVisibility(View.GONE);
            cheque_im_layout.setVisibility(View.GONE);
        } else if (pos == 1) {
            pay_slip.setVisibility(View.VISIBLE);
            bank_layout.setVisibility(View.VISIBLE);
            branch_layout.setVisibility(View.VISIBLE);
            ch_am_layout.setVisibility(View.GONE);
            ch_num_layout.setVisibility(View.GONE);
            ch_date_layout.setVisibility(View.GONE);
            cheque_im_layout.setVisibility(View.GONE);
        } else {
            pay_slip.setVisibility(View.GONE);
            bank_layout.setVisibility(View.VISIBLE);
            branch_layout.setVisibility(View.VISIBLE);
            ch_am_layout.setVisibility(View.VISIBLE);
            ch_num_layout.setVisibility(View.VISIBLE);
            ch_date_layout.setVisibility(View.VISIBLE);
            cheque_im_layout.setVisibility(View.VISIBLE);
        }
    }

    public String getForDate(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

    private void uploadBitmap(String name1, String name2, String cheque_date, String check_am, String check_num, String amounts, String brr, String offer_id, String offer_am) {
        pd = ProgressDialog.show(PayDetailsActivity.this);
        String URL_CHECK = Config.INSTALLMENT_PAYMENT + getIntent().getExtras().getString("SID") + "/" + userDetails.getuserid() + "/" + getIntent().getExtras().getString("LF") + "/" + payment_mode.getText().toString().trim() + "/" + bank_is + "/" + brr + "/" + check_num + "/" +
                cheque_date + "/" + check_am + "/" + amounts + "/" + getIntent().getExtras().getString("CID") + "/";
        System.out.println(URL_CHECK);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_CHECK,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pd.dismiss();
                        System.out.println("Akanksha " + response.toString());

                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            System.out.println(json);
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                if (object.getString("Column1").equalsIgnoreCase("T")) {
                                    Toast.makeText(getApplicationContext(), "Successfully Submitted For Approval", Toast.LENGTH_SHORT).show();
                                    pay_layout.setVisibility(View.GONE);
                                    otp_layout.setVisibility(View.GONE);
                                    newgif.setVisibility(View.VISIBLE);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            /* Create an Intent that will start the Menu-Activity. */
                                            Intent returnIntent = new Intent();
                                            setResult(Activity.RESULT_OK, returnIntent);
                                            finish();
                                        }
                                    }, 3000);
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
                        Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (chequeBms != null) {
                    params.put("chequeimage", new DataPart(name1, getFileDataFromDrawable(chequeBms)));
                }
                if (slipBms != null) {
                    params.put("paymentSlip", new DataPart(name2, getFileDataFromDrawable(slipBms)));
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
}