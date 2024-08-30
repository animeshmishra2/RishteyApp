package com.guruvardaan.ghargharsurvey.welcome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.RegistryUpload;
import com.guruvardaan.ghargharsurvey.utils.UploaderSingle;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.listener.OnSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadRegistry extends BaseActivity {

    LinearLayout take_photo, upload_gallery;
    UserDetails userDetails;
    ImageView close_button;
    TextView step_txt, upload_txt;
    ImageView aadhar_image;
    View view_1, view_2, view_3, view_4, view_5, view_6;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_upload_registry, frameLayout);
        view_1 = (View) findViewById(R.id.view_1);
        view_2 = (View) findViewById(R.id.view_2);
        view_3 = (View) findViewById(R.id.view_3);
        view_4 = (View) findViewById(R.id.view_4);
        view_5 = (View) findViewById(R.id.view_5);
        view_6 = (View) findViewById(R.id.view_6);
        pDialog = new ProgressDialog(this);
        userDetails = new UserDetails(getApplicationContext());
        step_txt = (TextView) findViewById(R.id.step_txt);
        aadhar_image = (ImageView) findViewById(R.id.aadhar_image);
        upload_txt = (TextView) findViewById(R.id.upload_txt);
        close_button = (ImageView) findViewById(R.id.close_button);
        take_photo = (LinearLayout) findViewById(R.id.take_photo);
        upload_gallery = (LinearLayout) findViewById(R.id.upload_gallery);
        step_txt.setText("Steps 1/6");
        upload_txt.setText("Upload PAN");
        view_1.setBackgroundColor(Color.parseColor("#3700B3"));
        view_2.setBackgroundColor(Color.parseColor("#777777"));
        view_3.setBackgroundColor(Color.parseColor("#777777"));
        view_4.setBackgroundColor(Color.parseColor("#777777"));
        view_5.setBackgroundColor(Color.parseColor("#777777"));
        view_6.setBackgroundColor(Color.parseColor("#777777"));
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        upload_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestGallery(UploadRegistry.this)) {
                    Matisse.from(UploadRegistry.this)
                            .choose(MimeType.ofImage())
                            .countable(true).capture(false)
                            .maxSelectable(1)
                            .gridExpectedSize(300)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine()).setOnSelectedListener(new OnSelectedListener() {
                                @Override
                                public void onSelected(@NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                                    //System.out.println(pathList.get(0));
                                }
                            })
                            .showPreview(false) // Default is `true`
                            .forResult(21);
                }
            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions(UploadRegistry.this)) {
                    ImagePicker.with(UploadRegistry.this)
                            .cameraOnly()   //User can only select image from Gallery
                            .start(22);

                }
            }
        });
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int ExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    101);
            return false;
        }
        return true;
    }

    public static boolean checkAndRequestGallery(final Activity context) {
        int ExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    102);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (ContextCompat.checkSelfPermission(UploadRegistry.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                } else if (ContextCompat.checkSelfPermission(UploadRegistry.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    ImagePicker.with(this)
                            .cameraOnly()    //User can only select image from Gallery
                            .start(22);
                }
                break;
            case 102:
                if (ContextCompat.checkSelfPermission(UploadRegistry.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                } else if (ContextCompat.checkSelfPermission(UploadRegistry.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Matisse.from(UploadRegistry.this)
                            .choose(MimeType.ofImage())
                            .countable(true).capture(false)
                            .maxSelectable(1)
                            .gridExpectedSize(300)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .showPreview(false) // Default is `true`
                            .forResult(21);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("AAkash " + requestCode);
        if (requestCode == 21 && resultCode == RESULT_OK) {
            if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload PAN")) {
                uploadFiles("pan_img", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Aadhar Front")) {
                uploadFiles("aadhar_front", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Aadhar Back")) {
                uploadFiles("aadhar_back", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Owner Pic")) {
                uploadFiles("owner_pic", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Cheque Image 1")) {
                uploadFiles("cheque_1", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Cheque Image 2")) {
                uploadFiles("cheque_2", Matisse.obtainPathResult(data).get(0));
            }
        }
        if (requestCode == 22 && resultCode == RESULT_OK) {
            if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload PAN")) {
                uploadFiles("pan_img", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Aadhar Front")) {
                uploadFiles("aadhar_front", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Aadhar Back")) {
                uploadFiles("aadhar_back", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Owner Pic")) {
                uploadFiles("owner_pic", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Cheque Image 1")) {
                uploadFiles("cheque_1", Matisse.obtainPathResult(data).get(0));
            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Cheque Image 2")) {
                uploadFiles("cheque_2", Matisse.obtainPathResult(data).get(0));
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void uploadFiles(String key, String paths) {
        showProgress("Uploading Profile ...");
        RegistryUpload fileUploader = new RegistryUpload();
        System.out.println("Mishur " + getIntent().getExtras().getString("ID"));
        fileUploader.uploadFiles("requestRegisteryImagesAdd/" + getIntent().getExtras().getString("ID") + "/", key, new File(paths), new RegistryUpload.FileUploaderCallback() {
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
                        if (object.getString("Column1").equalsIgnoreCase("t")) {
                            if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload PAN")) {
                                upload_txt.setText("Upload Aadhar Front");
                                step_txt.setText("Steps 2/6");
                                view_1.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_2.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_3.setBackgroundColor(Color.parseColor("#777777"));
                                view_4.setBackgroundColor(Color.parseColor("#777777"));
                                view_5.setBackgroundColor(Color.parseColor("#777777"));
                                view_6.setBackgroundColor(Color.parseColor("#777777"));
                            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Aadhar Front")) {
                                upload_txt.setText("Upload Aadhar Back");
                                step_txt.setText("Steps 3/6");
                                view_1.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_2.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_3.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_4.setBackgroundColor(Color.parseColor("#777777"));
                                view_5.setBackgroundColor(Color.parseColor("#777777"));
                                view_6.setBackgroundColor(Color.parseColor("#777777"));
                            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Aadhar Back")) {
                                upload_txt.setText("Upload Owner Pic");
                                step_txt.setText("Steps 4/6");
                                view_1.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_2.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_3.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_4.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_5.setBackgroundColor(Color.parseColor("#777777"));
                                view_6.setBackgroundColor(Color.parseColor("#777777"));
                            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Owner Pic")) {
                                step_txt.setText("Steps 5/6");
                                upload_txt.setText("Upload Cheque Image 1");
                                view_1.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_2.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_3.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_4.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_5.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_6.setBackgroundColor(Color.parseColor("#777777"));
                            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Cheque Image 1")) {
                                upload_txt.setText("Upload Cheque Image 2");
                                step_txt.setText("Steps 6/6");
                                view_1.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_2.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_3.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_4.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_5.setBackgroundColor(Color.parseColor("#3700B3"));
                                view_6.setBackgroundColor(Color.parseColor("#3700B3"));
                            } else if (upload_txt.getText().toString().trim().equalsIgnoreCase("Upload Cheque Image 2")) {
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Image Could Not Be Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Image Could Not Be Uploaded", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Image Could Not Be Uploaded", Toast.LENGTH_SHORT).show();
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
            e.printStackTrace();
        }
    }

    public void hideProgress() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}