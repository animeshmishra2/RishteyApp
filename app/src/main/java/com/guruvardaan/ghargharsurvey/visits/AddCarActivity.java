package com.guruvardaan.ghargharsurvey.visits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.FileUploader;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;

public class AddCarActivity extends BaseActivity {

    String vehicle_id = "";
    LinearLayout select_model_lay, select_rc_lay, loan_1_lay, loan_2_lay;
    EditText vehicle_edit, select_rc, loan_1, loan_2, edit_car_num;
    String sel_arr[] = {"Upload Photo", "Upload File"};
    String rc_path = "";
    String loan_1_path = "";
    String loan_2_path = "";
    private ProgressDialog pDialog;
    UserDetails userDetails;
    TextView add_car;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_car, frameLayout);
        pDialog = new ProgressDialog(this);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        vehicle_id = "";
        rc_path = "";
        loan_1_path = "";
        loan_2_path = "";
        select_rc_lay = (LinearLayout) findViewById(R.id.select_rc_lay);
        edit_car_num = (EditText) findViewById(R.id.edit_car_num);
        edit_car_num.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        edit_car_num.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        loan_1_lay = (LinearLayout) findViewById(R.id.loan_1_lay);
        loan_2_lay = (LinearLayout) findViewById(R.id.loan_2_lay);
        select_rc = (EditText) findViewById(R.id.select_rc);
        loan_1 = (EditText) findViewById(R.id.loan_1);
        loan_2 = (EditText) findViewById(R.id.loan_2);
        add_car = (TextView) findViewById(R.id.add_car);
        select_model_lay = (LinearLayout) findViewById(R.id.select_model_lay);
        vehicle_edit = (EditText) findViewById(R.id.vehicle_edit);
        checkAndRequestPermissions();
        select_model_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCarModel.class);
                startActivityForResult(intent, 22);
            }
        });
        vehicle_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCarModel.class);
                startActivityForResult(intent, 22);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        select_rc_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddCarActivity.this);
                alertDialog.setTitle("Choose File Type");
                alertDialog.setSingleChoiceItems(sel_arr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FilePickerBuilder.getInstance()
                                    .setMaxCount(1) //optional
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickPhoto(AddCarActivity.this, 201);
                        } else {
                            FilePickerBuilder.getInstance()
                                    .setMaxCount(1) //optional
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickFile(AddCarActivity.this, 202);
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }
        });
        select_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddCarActivity.this);
                alertDialog.setTitle("Choose File Type");
                alertDialog.setSingleChoiceItems(sel_arr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FilePickerBuilder.getInstance()
                                    .setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickPhoto(AddCarActivity.this, 201);
                        } else {
                            FilePickerBuilder.getInstance()
                                    .setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickFile(AddCarActivity.this, 202);
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }
        });
        loan_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddCarActivity.this);
                alertDialog.setTitle("Choose File Type");
                alertDialog.setSingleChoiceItems(sel_arr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FilePickerBuilder.getInstance().setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickPhoto(AddCarActivity.this, 203);
                        } else {
                            FilePickerBuilder.getInstance().setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickFile(AddCarActivity.this, 204);
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }
        });

        loan_1_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddCarActivity.this);
                alertDialog.setTitle("Choose File Type");
                alertDialog.setSingleChoiceItems(sel_arr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FilePickerBuilder.getInstance().setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickPhoto(AddCarActivity.this, 203);
                        } else {
                            FilePickerBuilder.getInstance().setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickFile(AddCarActivity.this, 204);
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }
        });

        loan_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddCarActivity.this);
                alertDialog.setTitle("Choose File Type");
                alertDialog.setSingleChoiceItems(sel_arr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FilePickerBuilder.getInstance().setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickPhoto(AddCarActivity.this, 205);
                        } else {
                            FilePickerBuilder.getInstance().setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickFile(AddCarActivity.this, 206);
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }
        });
        loan_2_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddCarActivity.this);
                alertDialog.setTitle("Choose File Type");
                alertDialog.setSingleChoiceItems(sel_arr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FilePickerBuilder.getInstance().setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickPhoto(AddCarActivity.this, 205);
                        } else {
                            FilePickerBuilder.getInstance().setMaxCount(1)
                                    .setActivityTheme(R.style.LibAppTheme) //optional
                                    .pickFile(AddCarActivity.this, 206);
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }
        });
        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFiles();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK) {
            vehicle_edit.setText(data.getExtras().getString("NAME"));
            vehicle_id = data.getExtras().getString("ID");
        }
        if (requestCode == 201 && resultCode == RESULT_OK) {
            try {
                rc_path = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0));
                select_rc.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 202 && resultCode == RESULT_OK) {
            try {
                rc_path = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0));
                select_rc.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 203 && resultCode == RESULT_OK) {
            try {
                loan_1_path = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0));
                loan_1.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 204 && resultCode == RESULT_OK) {
            try {
                loan_1_path = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0));
                loan_1.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 205 && resultCode == RESULT_OK) {
            try {
                loan_2_path = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0));
                loan_2.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 206 && resultCode == RESULT_OK) {
            try {
                loan_2_path = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0));
                loan_2.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadFiles() {
        showProgress("Uploading Car data ...");
        FileUploader fileUploader = new FileUploader();
        fileUploader.uploadFiles(userDetails.getuserid() + "/" + vehicle_id + "/" + edit_car_num.getText().toString().trim() + "/", "rcimage", new File(rc_path), "loandocument1", new File(loan_1_path), "loandocument2", new File(loan_2_path), new FileUploader.FileUploaderCallback() {
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
                            Toast.makeText(getApplicationContext(), "Car Added Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errormsg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to Send Car Details", Toast.LENGTH_SHORT).show();
                    }
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
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
}