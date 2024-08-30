package com.guruvardaan.ghargharsurvey.customers;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.agent.SelectRank;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.MyPlotRequestModel;
import com.guruvardaan.ghargharsurvey.utils.DataPart;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.utils.VolleyMultipartRequest;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
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

public class CustomerPaymentActivity extends BaseActivity {

    EditText property, block, property_type, plot, term, duration, downpayment, payment_mode, bank, branch, cheque_amount, cheque_number, cheque_date, remarks, cheque_image, payment_slip;
    TextView buy_plot, plot_amount, paid_amount, balance_amount, installments, installment_amount;
    ImageView back_button;
    MyPlotRequestModel myPlotRequestModel;
    LinearLayout bank_layout, branch_layout, ch_am_layout, ch_num_layout, ch_date_layout, cheque_im_layout;
    String term_array[] = {"Monthly", "Quarterly", "Half Yearly", "Yearly"};
    String payment_m[] = {"Online", "Bank Transfer", "Cash", "Cheque"};
    int term_a[] = {1, 3, 6, 12};
    int term_l[] = {12, 4, 2, 1};
    String duration_array[] = {"1 Year", "2 Year", "3 Year", "4 Year", "5 Year", "6 Year", "7 Year", "8 Year"};
    int dur_a[] = {1, 2, 3, 4, 5, 6, 7, 8};
    int t_p = -1;
    int d_p = -1;
    DecimalFormat df;
    String bank_is;
    ProgressDialog pd;
    UserDetails userDetails;
    String total_ins = "";
    String ins_amount = "";
    String bal_amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_customer_payment, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        df = new DecimalFormat("0.00");
        total_ins = "0";
        bank_is = "0";
        ins_amount = "0";
        bal_amount = "0";
        myPlotRequestModel = (MyPlotRequestModel) getIntent().getExtras().getSerializable("PM");
        initializeVars();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        property.setText(myPlotRequestModel.getAllotedPropertyName());
        block.setText(myPlotRequestModel.getAllotedBlockName());
        property_type.setText(myPlotRequestModel.getProperty_type_name());
        plot.setText(myPlotRequestModel.getAllotedPlotName());
        plot_amount.setText("\u20b9 " + myPlotRequestModel.getAmount());
        paid_amount.setText("\u20b9 0");
        balance_amount.setText("\u20b9 " + myPlotRequestModel.getAmount());
        installments.setText("0");
        installment_amount.setText("\u20b9 0");
        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CustomerPaymentActivity.this);
                mBuilder.setTitle("Select Term");
                mBuilder.setSingleChoiceItems(term_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        term.setText(term_array[s]);
                        t_p = s;
                        dialogInterface.dismiss();
                        findAllData();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CustomerPaymentActivity.this);
                mBuilder.setTitle("Select Duration");
                mBuilder.setSingleChoiceItems(duration_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        duration.setText(duration_array[s]);
                        d_p = s;
                        dialogInterface.dismiss();
                        findAllData();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        payment_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CustomerPaymentActivity.this);
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
                DatePickerDialog dpd = new DatePickerDialog(CustomerPaymentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cheque_date.setText(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });


        downpayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                findAllData();
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
                if (checkAndRequestPermissions()) {
                    FilePickerBuilder.getInstance()
                            .setMaxCount(1) //optional
                            .setActivityTheme(R.style.LibAppTheme) //optional
                            .pickPhoto(CustomerPaymentActivity.this, 205);
                }
            }
        });

        cheque_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    FilePickerBuilder.getInstance()
                            .setMaxCount(1) //optional
                            .setActivityTheme(R.style.LibAppTheme) //optional
                            .pickPhoto(CustomerPaymentActivity.this, 206);
                }
            }
        });

        buy_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t_p == -1) {
                    Toast.makeText(getApplicationContext(), "Please Select Term", Toast.LENGTH_SHORT).show();
                } else if (d_p == -1) {
                    Toast.makeText(getApplicationContext(), "Please Select Duration", Toast.LENGTH_SHORT).show();
                } else if (d_p == -1) {
                    Toast.makeText(getApplicationContext(), "Please Select Duration", Toast.LENGTH_SHORT).show();
                } else if (getParsing(downpayment.getText().toString().trim()) < 1) {
                    Toast.makeText(getApplicationContext(), "Minimum Down payment Should Be 1 to book", Toast.LENGTH_SHORT).show();
                } else if (payment_mode.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                } else {
                    if (payment_mode.getText().toString().trim().equalsIgnoreCase("Cash") || payment_mode.getText().toString().trim().equalsIgnoreCase("Online")) {
                        if (remarks.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter Remark", Toast.LENGTH_SHORT).show();
                        } else if (payment_slip.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Upload Payment Slip", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "ready", Toast.LENGTH_SHORT).show();
                            readyData();
                        }
                    } else if (payment_mode.getText().toString().trim().equalsIgnoreCase("Bank Transfer")) {
                        if (bank.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Select Bank", Toast.LENGTH_SHORT).show();
                        } else if (branch.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter Branch Name", Toast.LENGTH_SHORT).show();
                        } else if (remarks.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter Remark", Toast.LENGTH_SHORT).show();
                        } else if (payment_slip.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Upload Payment Slip", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "ready", Toast.LENGTH_SHORT).show();
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
                        } else if (remarks.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter Remark", Toast.LENGTH_SHORT).show();
                        } else if (cheque_image.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Upload Cheque Image", Toast.LENGTH_SHORT).show();
                        } else if (payment_slip.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Upload Payment Slip", Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(getApplicationContext(), "ready", Toast.LENGTH_SHORT).show();
                            readyData();
                        }
                    }
                }

            }
        });
        findAllData();

    }

    public void readyData() {
        String path1, path2 = "";
        Bitmap bm1, bm2 = null;
        if (!cheque_image.getText().toString().trim().equalsIgnoreCase("")) {
            path1 = cheque_image.getText().toString().trim().substring(cheque_image.getText().toString().trim().lastIndexOf("/") + 1);
            bm1 = getBitmapFromPath(cheque_image.getText().toString().trim());
        } else {
            bm1 = null;
            path1 = "";
        }
        if (!payment_slip.getText().toString().trim().equalsIgnoreCase("")) {
            path2 = payment_slip.getText().toString().trim().substring(payment_slip.getText().toString().trim().lastIndexOf("/") + 1);
            bm2 = getBitmapFromPath(payment_slip.getText().toString().trim());
        } else {
            bm2 = null;
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
        if (downpayment.getText().toString().trim().length() > 0) {
            downpayt = downpayment.getText().toString().trim();
        }
        String brr = "0";
        if (branch.getText().toString().trim().length() > 0) {
            brr = branch.getText().toString().trim();
        }
        uploadBitmap(bm1, bm2, path1, path2, cd, cm, cn, downpayt, brr);
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
        if (requestCode == 113 && resultCode == RESULT_OK) {
            bank_is = data.getExtras().getString("ID");
            bank.setText(data.getExtras().getString("NAME"));
        }
        if (requestCode == 205 && resultCode == RESULT_OK) {
            try {
                payment_slip.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 206 && resultCode == RESULT_OK) {
            try {
                cheque_image.setText(ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void findAllData() {
        int tterm = 0;
        int dur = 0;
        int total_inst = 0;
        double total = getParsing(myPlotRequestModel.getAmount());
        double downp = getParsing(downpayment.getText().toString().trim());
        if (t_p != -1) {
            tterm = term_l[t_p];
        }
        if (d_p != -1) {
            dur = dur_a[d_p];
        }
        total_inst = tterm * dur;
        total_ins = "" + total_inst;
        installments.setText("" + total_inst);
        paid_amount.setText("\u20b9 " + downp);
        double balance = total - downp;
        balance_amount.setText("\u20b9 " + balance);
        bal_amount = "" + balance;
        if (total_inst > 0) {
            double inst_am = balance / (total_inst);
            ins_amount = "" + df.format(inst_am);
            installment_amount.setText("\u20b9 " + df.format(inst_am));
        }
    }

    public void setLayout(int pos) {
        if (pos == 0 || pos == 2) {
            bank_layout.setVisibility(View.GONE);
            branch_layout.setVisibility(View.GONE);
            ch_am_layout.setVisibility(View.GONE);
            ch_num_layout.setVisibility(View.GONE);
            ch_date_layout.setVisibility(View.GONE);
            cheque_im_layout.setVisibility(View.GONE);
        } else if (pos == 1) {
            bank_layout.setVisibility(View.VISIBLE);
            branch_layout.setVisibility(View.VISIBLE);
            ch_am_layout.setVisibility(View.GONE);
            ch_num_layout.setVisibility(View.GONE);
            ch_date_layout.setVisibility(View.GONE);
            cheque_im_layout.setVisibility(View.GONE);
        } else {
            bank_layout.setVisibility(View.VISIBLE);
            branch_layout.setVisibility(View.VISIBLE);
            ch_am_layout.setVisibility(View.VISIBLE);
            ch_num_layout.setVisibility(View.VISIBLE);
            ch_date_layout.setVisibility(View.VISIBLE);
            cheque_im_layout.setVisibility(View.VISIBLE);
        }
    }

    public void initializeVars() {
        bank_layout = (LinearLayout) findViewById(R.id.bank_layout);
        bank_layout.setVisibility(View.GONE);
        branch_layout = (LinearLayout) findViewById(R.id.branch_layout);
        branch_layout.setVisibility(View.GONE);
        ch_am_layout = (LinearLayout) findViewById(R.id.ch_am_layout);
        ch_am_layout.setVisibility(View.GONE);
        ch_num_layout = (LinearLayout) findViewById(R.id.ch_num_layout);
        ch_num_layout.setVisibility(View.GONE);
        ch_date_layout = (LinearLayout) findViewById(R.id.ch_date_layout);
        ch_date_layout.setVisibility(View.GONE);
        cheque_im_layout = (LinearLayout) findViewById(R.id.cheque_im_layout);
        cheque_im_layout.setVisibility(View.GONE);
        back_button = (ImageView) findViewById(R.id.back_button);
        property = (EditText) findViewById(R.id.property);
        block = (EditText) findViewById(R.id.block);
        property_type = (EditText) findViewById(R.id.property_type);
        plot = (EditText) findViewById(R.id.plot);
        term = (EditText) findViewById(R.id.term);
        duration = (EditText) findViewById(R.id.duration);
        downpayment = (EditText) findViewById(R.id.downpayment);
        payment_mode = (EditText) findViewById(R.id.payment_mode);
        bank = (EditText) findViewById(R.id.bank);
        branch = (EditText) findViewById(R.id.branch);
        cheque_amount = (EditText) findViewById(R.id.cheque_amount);
        cheque_number = (EditText) findViewById(R.id.cheque_number);
        cheque_date = (EditText) findViewById(R.id.cheque_date);
        remarks = (EditText) findViewById(R.id.remarks);
        cheque_image = (EditText) findViewById(R.id.cheque_image);
        payment_slip = (EditText) findViewById(R.id.payment_slip);
        buy_plot = (TextView) findViewById(R.id.buy_plot);
        plot_amount = (TextView) findViewById(R.id.plot_amount);
        paid_amount = (TextView) findViewById(R.id.paid_amount);
        balance_amount = (TextView) findViewById(R.id.balance_amount);
        installments = (TextView) findViewById(R.id.installments);
        installment_amount = (TextView) findViewById(R.id.installment_amount);
    }

    public double getParsing(String ss) {
        try {
            double dd = Double.parseDouble(ss);
            return dd;
        } catch (Exception e) {
            return 0;
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

    private void uploadBitmap(final Bitmap bitmap1, final Bitmap bitmap2, String name1, String name2, String cheque_date, String check_am, String check_num, String dpay, String br) {
        pd = ProgressDialog.show(CustomerPaymentActivity.this);
        String URL_CHECK = Config.CUSTOMER_PAYMENT + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + userDetails.getAgentId() + "/" + userDetails.getuserid() + "/" + myPlotRequestModel.getPk_prm_property_id() + "/" + myPlotRequestModel.getPk_prm_block_id() + "/" + myPlotRequestModel.getPk_prm_property_type() + "/" + myPlotRequestModel.getPk_prm_define_property_id() + "/" +
                myPlotRequestModel.getId() + "/" + term_a[t_p] + "/" + dur_a[d_p] + "/" + total_ins + "/" + ins_amount + "/" + dpay + "/" + bal_amount + "/" + payment_mode.getText().toString().trim() + "/" + cheque_date + "/" + check_am + "/" + check_num + "/"
                + bank_is + "/" + br + "/" + remarks.getText().toString().trim();
        System.out.println(URL_CHECK);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_CHECK,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pd.dismiss();
                        System.out.println("Akanksha " + response.toString());
                        //7388601892
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            System.out.println(json);
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                if (object.getString("Column1").equalsIgnoreCase("T")) {
                                    Toast.makeText(getApplicationContext(), "Successfully Submitted For Approval", Toast.LENGTH_SHORT).show();
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
                    params.put("chequeimage", new DataPart(name1, getFileDataFromDrawable(bitmap1)));
                }
                if (bitmap2 != null) {
                    params.put("paymentSlip", new DataPart(name2, getFileDataFromDrawable(bitmap2)));
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