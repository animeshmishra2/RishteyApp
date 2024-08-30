package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_PROFILE_URL;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brkckr.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.SingleFileUploader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;

public class ProfileActivity extends BaseActivity {

    TextView user_name, user_mobile, joining_date, my_id, my_rank, total, paid, balance, plot_sales, visits, commission, introducer_name, introducer_mobile, introducer_rank, my_email;
    ImageView profile_image;
    LinearLayout main_lay, second_lay;
    ProgressBar progress;
    UserDetails userDetails;
    ImageView back_img;
    private ProgressDialog pDialog;
    String rc_path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, frameLayout);
        checkAndRequestPermissions();
        pDialog = new ProgressDialog(this);
        rc_path = "";
        back_img = (ImageView) findViewById(R.id.back_img);
        userDetails = new UserDetails(getApplicationContext());
        progress = (ProgressBar) findViewById(R.id.progress);
        main_lay = (LinearLayout) findViewById(R.id.main_lay);
        second_lay = (LinearLayout) findViewById(R.id.second_lay);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        user_name = (TextView) findViewById(R.id.user_name);
        user_mobile = (TextView) findViewById(R.id.user_mobile);
        joining_date = (TextView) findViewById(R.id.joining_date);
        my_id = (TextView) findViewById(R.id.my_id);
        my_rank = (TextView) findViewById(R.id.my_rank);
        total = (TextView) findViewById(R.id.total);
        paid = (TextView) findViewById(R.id.paid);
        balance = (TextView) findViewById(R.id.balance);
        plot_sales = (TextView) findViewById(R.id.plot_sales);
        visits = (TextView) findViewById(R.id.visits);
        commission = (TextView) findViewById(R.id.commission);
        introducer_name = (TextView) findViewById(R.id.introducer_name);
        introducer_mobile = (TextView) findViewById(R.id.introducer_mobile);
        introducer_rank = (TextView) findViewById(R.id.introducer_rank);
        my_email = (TextView) findViewById(R.id.my_email);
        getUserProfile();

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.getInstance()
                        .setMaxCount(1) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickPhoto(ProfileActivity.this, 201);
            }
        });
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

    private void getUserProfile() {
        progress.setVisibility(View.VISIBLE);
        second_lay.setVisibility(View.GONE);
        main_lay.setVisibility(View.GONE);
        String URL_CHECK = GET_PROFILE_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + userDetails.getuserid();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        second_lay.setVisibility(View.VISIBLE);
                        main_lay.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                user_name.setText(object.getString("advisor_name"));
                                if (object.getString("alternate_no").length() < 10) {
                                    user_mobile.setText(getString(R.string.mobile_no) + object.getString("mobile_no"));
                                } else {
                                    user_mobile.setText(getString(R.string.mobile_no) + object.getString("mobile_no") + " / " + object.getString("alternate_no"));
                                }
                                joining_date.setText(getForDate(object.getString("joining_date")));
                                my_id.setText(object.getString("pk_acc_adm_advisor_id"));
                                my_rank.setText(object.getString("advisor_rank"));
                                total.setText("\u20b9" + object.getString("totalcomissionearned"));
                                paid.setText("\u20b9" + object.getString("totalcomissionPaid"));
                                balance.setText("\u20b9" + object.getString("balanceComission"));
                                plot_sales.setText(object.getString("totalplotsalesbyuser"));
                                visits.setText(object.getString("totalvisits"));
                                commission.setText("\u20b9" + object.getString("totalcomissionearned"));
                                if (!object.getString("introducerName").equalsIgnoreCase("null")) {
                                    introducer_name.setText(object.getString("introducerName"));
                                }
                                if (!object.getString("introducerMobile").equalsIgnoreCase("null")) {
                                    introducer_mobile.setText(object.getString("introducerMobile"));
                                }
                                if (!object.getString("introducerRank").equalsIgnoreCase("null")) {
                                    introducer_rank.setText(object.getString("introducerRank"));
                                }
                                if (!object.getString("advisor_email").equalsIgnoreCase("null")) {
                                    my_email.setText(object.getString("advisor_email"));
                                }
                                userDetails.setprofilef(object.getString("advisorImage"));
                                Glide.with(getApplicationContext())
                                        .load(object.getString("advisorImage"))
                                        .circleCrop().placeholder(R.drawable.man).dontAnimate().skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .into(profile_image);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.setVisibility(View.GONE);
                        second_lay.setVisibility(View.VISIBLE);
                        main_lay.setVisibility(View.VISIBLE);
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

    public String getForDate(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd MMM, yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

    public void uploadFiles() {
        showProgress("Uploading Profile ...");
        SingleFileUploader fileUploader = new SingleFileUploader();
        fileUploader.uploadFiles(userDetails.getuserid() + "/", "1", new File(rc_path), new SingleFileUploader.FileUploaderCallback() {
            @Override
            public void onError() {
                System.out.println("Vikas ki Error ");
                hideProgress();
            }

            @Override
            public void onFinish(String responses) {
                hideProgress();
                System.out.println("Vickey " + responses);
                try {
                    JSONObject jsonObject = new JSONObject(responses);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    if (Table.length() > 0) {
                        JSONObject object = Table.getJSONObject(0);
                        if (object.getString("return_type").equalsIgnoreCase("t")) {
                            Toast.makeText(getApplicationContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Profile Could Not Be Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Profile Could Not Be Uploaded", Toast.LENGTH_SHORT).show();
                    }
                    getUserProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed to Send Car Details", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onProgressUpdate(int currentpercent, int totalpercent, int filenumber) {
                updateProgress(totalpercent, "Uploading file " + filenumber, "");
                Log.e("Progress Status", currentpercent + " " + totalpercent + " " + filenumber);
            }
        });
    }

    public void updateProgress(int val, String title, String msg) {
        pDialog.setTitle(title);
        pDialog.setMessage(msg);
        pDialog.setProgress(val);
    }

    public void showProgress(String str) {
        try {
            pDialog.setCancelable(false);
            pDialog.setTitle("Please wait");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setMax(100); // Progress Dialog Max Value
            pDialog.setMessage(str);
            if (pDialog.isShowing())
                pDialog.dismiss();
            pDialog.show();
        } catch (Exception e) {

        }
    }

    public void hideProgress() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK) {
            try {
                rc_path = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0));
                uploadFiles();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}