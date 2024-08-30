package com.guruvardaan.ghargharsurvey.plots;

import static com.guruvardaan.ghargharsurvey.config.Config.ADD_CUSTOMERS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.visits.SelectOccupation;
import com.guruvardaan.ghargharsurvey.visits.SelectProfession;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectPropertyActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PlotRequestForm extends BaseActivity {

    TextView add_customer;
    EditText customer_name, customer_mobile, customer_profession, customer_occupation, customer_address, visit_date, property;
    LinearLayout profession_lay, occupation_lay, status_lay, payment_lay, block_lay, category_lay, visit_lay, property_lay;
    String sel = "";
    String occ = "";
    String blocks = "";
    String pid = "";
    UserDetails userDetails;
    ImageView back_button;
    String duration_array[] = {"1 Year", "2 Year", "3 Year", "4 Year", "5 Year", "8 Year"};
    int dur_a[] = {1, 2, 3, 4, 5, 6};
    EditText status, payment_option, emi_years, plot_size, block, category, amount;
    String[] status_array = new String[]{"Interested", "Decision After Visit"};
    String[] payment_array = new String[]{"EMI Payment", "Full Payment(One Time)"};
    String[] category_array = new String[]{"Normal Plot", "Cornered Plot"};
    ProgressDialog pd;
    int d_p = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_plot_request_form, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        sel = "";
        occ = "";
        blocks = "";
        pid = "";
        property_lay = (LinearLayout) findViewById(R.id.property_lay);
        property = (EditText) findViewById(R.id.property);
        visit_date = (EditText) findViewById(R.id.visit_date);
        visit_lay = (LinearLayout) findViewById(R.id.visit_lay);
        add_customer = (TextView) findViewById(R.id.add_customer);
        profession_lay = (LinearLayout) findViewById(R.id.profession_lay);
        occupation_lay = (LinearLayout) findViewById(R.id.occupation_lay);
        status_lay = (LinearLayout) findViewById(R.id.status_lay);
        payment_lay = (LinearLayout) findViewById(R.id.payment_lay);
        block_lay = (LinearLayout) findViewById(R.id.block_lay);
        category_lay = (LinearLayout) findViewById(R.id.category_lay);
        customer_name = (EditText) findViewById(R.id.customer_name);
        customer_mobile = (EditText) findViewById(R.id.customer_mobile);
        customer_profession = (EditText) findViewById(R.id.customer_profession);
        customer_occupation = (EditText) findViewById(R.id.customer_occupation);
        customer_address = (EditText) findViewById(R.id.customer_address);
        status = (EditText) findViewById(R.id.status);
        payment_option = (EditText) findViewById(R.id.payment_option);
        emi_years = (EditText) findViewById(R.id.emi_years);
        plot_size = (EditText) findViewById(R.id.plot_size);
        block = (EditText) findViewById(R.id.block);
        category = (EditText) findViewById(R.id.category);
        amount = (EditText) findViewById(R.id.amount);

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


        profession_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectProfession.class);
                startActivityForResult(intent, 29);
            }
        });

        occupation_lay.setOnClickListener(new View.OnClickListener() {
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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        status_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlotRequestForm.this);
                mBuilder.setTitle("Select Status");
                mBuilder.setSingleChoiceItems(status_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        status.setText(status_array[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlotRequestForm.this);
                mBuilder.setTitle("Select Status");
                mBuilder.setSingleChoiceItems(status_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        status.setText(status_array[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        payment_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlotRequestForm.this);
                mBuilder.setTitle("Select Payment Option");
                mBuilder.setSingleChoiceItems(payment_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        payment_option.setText(payment_array[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        emi_years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlotRequestForm.this);
                mBuilder.setTitle("Select Duration");
                mBuilder.setSingleChoiceItems(duration_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        emi_years.setText(duration_array[s]);
                        d_p = s;
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        payment_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlotRequestForm.this);
                mBuilder.setTitle("Select Payment Option");
                mBuilder.setSingleChoiceItems(payment_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        payment_option.setText(payment_array[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        category_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlotRequestForm.this);
                mBuilder.setTitle("Select Plot Category");
                mBuilder.setSingleChoiceItems(category_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        category.setText(category_array[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlotRequestForm.this);
                mBuilder.setTitle("Select Plot Category");
                mBuilder.setSingleChoiceItems(category_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        category.setText(category_array[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        property_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectPropertyActivity.class);
                startActivityForResult(intent, 35);
            }
        });
        property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectPropertyActivity.class);
                startActivityForResult(intent, 35);
            }
        });
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pid.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), SelectBlockActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PID", pid);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 34);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Property First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        block_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pid.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), SelectBlockActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PID", pid);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 34);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Property First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        visit_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(PlotRequestForm.this, R.style.MySpinnerDatePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                visit_date.setText(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });
        visit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(PlotRequestForm.this, R.style.MySpinnerDatePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                visit_date.setText(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customer_name.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Name", Toast.LENGTH_SHORT).show();
                } else if (customer_mobile.getText().toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Mobile", Toast.LENGTH_SHORT).show();
                } /*else if (sel.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Profession", Toast.LENGTH_SHORT).show();
                } else if (occ.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Occupation", Toast.LENGTH_SHORT).show();
                }*/ else if (customer_address.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Customer Address", Toast.LENGTH_SHORT).show();
                } else if (status.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Status", Toast.LENGTH_SHORT).show();
                } else if (payment_option.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Payment Option", Toast.LENGTH_SHORT).show();
                } else if (emi_years.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Years for EMI", Toast.LENGTH_SHORT).show();
                } else if (plot_size.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Plot Size", Toast.LENGTH_SHORT).show();
                } else if (pid.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Property", Toast.LENGTH_SHORT).show();
                } else if (blocks.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Block", Toast.LENGTH_SHORT).show();
                } else if (category.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_SHORT).show();
                } else if (visit_date.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Visit Date", Toast.LENGTH_SHORT).show();
                } else if (amount.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Amount to pay on visit", Toast.LENGTH_SHORT).show();
                } else {
                    createJSON();
                }
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

    public void createJSON() {
        try {
            JSONObject main_obj = new JSONObject();
            main_obj.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            main_obj.put("advisorId", userDetails.getuserid());
            main_obj.put("customer_name", customer_name.getText().toString().trim());
            main_obj.put("customer_mobile", customer_mobile.getText().toString().trim());
            main_obj.put("customer_address", customer_address.getText().toString().trim());
            main_obj.put("profession", "3");
            main_obj.put("occupation", "194");
            main_obj.put("status_of_plot_buy", status.getText().toString().trim());
            main_obj.put("payment_option", payment_option.getText().toString().trim());
            main_obj.put("plot_size", plot_size.getText().toString().trim());
            main_obj.put("block", blocks);
            main_obj.put("property", pid);
            main_obj.put("category", category.getText().toString().trim());
            main_obj.put("emiforyear", "" + d_p);
            main_obj.put("visit_date", getCurrentDate());
            main_obj.put("visit_amount", amount.getText().toString().trim());
            System.out.println("Animesh " + main_obj.toString());
            addRequest(main_obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentDate() {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd/MM/yyyy");
        String newDateStr = postFormater.format(new Date());
        return newDateStr;
    }

    public void addRequest(String jsons) {
        pd = ProgressDialog.show(PlotRequestForm.this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                ADD_CUSTOMERS,
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
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Table = object.getJSONArray("Table");
                    if (Table.length() > 0) {
                        JSONObject objects = Table.getJSONObject(0);
                        if (objects.getString("Column1").equalsIgnoreCase("t")) {
                            Intent intent = new Intent(getApplicationContext(), UploadAadharActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("ID", objects.getString("Id"));
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 39);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Mishras " + response);
            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        if (requestCode == 34 && resultCode == RESULT_OK) {
            blocks = data.getExtras().getString("NAME");
            block.setText(data.getExtras().getString("NAME") + " Block");
        }
        if (requestCode == 35 && resultCode == RESULT_OK) {
            pid = data.getExtras().getString("ID");
            property.setText(data.getExtras().getString("NAME"));
        }
        if (requestCode == 39 && resultCode == RESULT_OK) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}