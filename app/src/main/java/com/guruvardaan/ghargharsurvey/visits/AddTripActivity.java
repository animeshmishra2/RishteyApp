package com.guruvardaan.ghargharsurvey.visits;

import static com.guruvardaan.ghargharsurvey.config.Config.ADD_VISIT_URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.CustomerModel;
import com.guruvardaan.ghargharsurvey.model.PlotRequestModel;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.LocationAutocomplete;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTripActivity extends BaseActivity {

    LinearLayout car_layout, comment_layout, location_layout, visit_date_layout, vehicle_type_layout;
    EditText vehicle_edit, edit_comment, edit_car, date_edit, agent_location, token_amount, total_customers;
    RecyclerView customer_recyclerview;
    TextView add_customer, total_customer, add_visit;
    ArrayList<PlotRequestModel> customerModels;
    CustomerAdapter adapter;
    private int mYear, mMonth, mDay;
    String latLongs = "";
    UserDetails userDetails;
    ProgressDialog pd;
    String car_id = "";
    ImageView back_button;
    String token_am;
    Animation anim;
    TextView comment_txt;
    private static final long TIMEOUT_IN_MS = 10000; // 10 seconds
    private LocationRequest locationRequest;
    private boolean isRequestingLocationUpdates = false;
    private Handler timeoutHandler = new Handler();
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    Dialog prominent_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_trip, frameLayout);
        token_am = "";
        back_button = (ImageView) findViewById(R.id.back_button);
        car_id = "";
        customerModels = new ArrayList<>();
        total_customers = (EditText) findViewById(R.id.total_customers);
        userDetails = new UserDetails(getApplicationContext());
        add_visit = (TextView) findViewById(R.id.add_visit);
        comment_txt = (TextView) findViewById(R.id.comment_txt);
        car_layout = (LinearLayout) findViewById(R.id.car_layout);
        token_amount = (EditText) findViewById(R.id.token_amount);
        comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
        location_layout = (LinearLayout) findViewById(R.id.location_layout);
        visit_date_layout = (LinearLayout) findViewById(R.id.visit_date_layout);
        vehicle_type_layout = (LinearLayout) findViewById(R.id.vehicle_type_layout);
        vehicle_edit = (EditText) findViewById(R.id.vehicle_edit);
        edit_comment = (EditText) findViewById(R.id.edit_comment);
        edit_car = (EditText) findViewById(R.id.edit_car);
        date_edit = (EditText) findViewById(R.id.date_edit);
        agent_location = (EditText) findViewById(R.id.agent_location);
        customer_recyclerview = (RecyclerView) findViewById(R.id.customer_recyclerview);
        add_customer = (TextView) findViewById(R.id.add_customer);
        total_customer = (TextView) findViewById(R.id.total_customer);
        customer_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CustomerAdapter();
        customer_recyclerview.setAdapter(adapter);
        total_customer.setText(customerModels.size() + " Customers");
        getLocationPermission();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTripActivity.this, SelectCustomers.class);
                startActivityForResult(intent, 22);
            }
        });
        vehicle_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTripActivity.this, VehicleType.class);
                startActivityForResult(intent, 23);
            }
        });
        vehicle_type_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTripActivity.this, VehicleType.class);
                startActivityForResult(intent, 23);
            }
        });
        edit_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!vehicle_edit.getText().toString().trim().equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), SelectCarActivity.class);
                    startActivityForResult(intent, 271);
                } else {
                    Toast.makeText(getApplicationContext(), "Select Vehicle Type", Toast.LENGTH_SHORT).show();
                }
            }
        });
        car_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!vehicle_edit.getText().toString().trim().equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), SelectCarActivity.class);
                    startActivityForResult(intent, 271);
                } else {
                    Toast.makeText(getApplicationContext(), "Select Vehicle Type", Toast.LENGTH_SHORT).show();
                }
            }
        });

        agent_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FancyToast.makeText(AddTripActivity.this, "We are getting location, please wait", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                requestSingleLocationUpdate();
            }
        });
        location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FancyToast.makeText(AddTripActivity.this, "We are getting location, please wait", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                requestSingleLocationUpdate();
            }
        });

        date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(AddTripActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_edit.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                //dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });
        visit_date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(AddTripActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_edit.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        add_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vehicle_edit.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Vehicle Type", Toast.LENGTH_SHORT).show();
                } else if (vehicle_edit.getText().toString().trim().equalsIgnoreCase("Company Car") && edit_car.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Car", Toast.LENGTH_SHORT).show();
                } else if ((!vehicle_edit.getText().toString().trim().equalsIgnoreCase("Company Car")) && edit_comment.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Vehicle Number/Comments", Toast.LENGTH_SHORT).show();
                } else if (date_edit.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Visit Date", Toast.LENGTH_SHORT).show();
                } else if (agent_location.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Agent Location", Toast.LENGTH_SHORT).show();
                } else if (total_customers.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Total Customers", Toast.LENGTH_SHORT).show();
                }
                else if (customerModels.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please Add Customers", Toast.LENGTH_SHORT).show();
                }
                else {
                    createJSON();
                }
            }
        });

        getLocationPermission();
        anim = new AlphaAnimation(0.4f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        add_customer.startAnimation(anim);
    }

    class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {

        @Override
        public int getItemCount() {
            return customerModels.size();
        }

        @Override
        public void onBindViewHolder(CustomerAdapter.CustomerHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.name.setText(customerModels.get(position).getCustomer_name());
            holder.edit.setVisibility(View.GONE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddTripActivity.this, AddCustomerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("POS", position);
                    bundle.putString("TYPE", "E");
                    bundle.putSerializable("CM", customerModels.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 22);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customerModels.remove(position);
                    total_customer.setText(customerModels.size() + " Customers");
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public CustomerAdapter.CustomerHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.customer_row, viewGroup, false);
            CustomerAdapter.CustomerHolder listHolder = new CustomerAdapter.CustomerHolder(mainGroup);
            return listHolder;
        }

        public class CustomerHolder extends RecyclerView.ViewHolder {

            TextView name;
            ImageView edit, delete;

            public CustomerHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                edit = (ImageView) view.findViewById(R.id.edit);
                delete = (ImageView) view.findViewById(R.id.delete);
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            createLocationRequest();
            requestSingleLocationUpdate();
        } else {
            if (prominent_dialog == null || (!prominent_dialog.isShowing())) {
                showProminentDialog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK) {

            customerModels.add((PlotRequestModel) data.getExtras().getSerializable("CM"));
            adapter.notifyDataSetChanged();
            total_customer.setText(customerModels.size() + " Customers");
        }
        if (requestCode == 23 && resultCode == RESULT_OK) {
            vehicle_edit.setText(data.getExtras().getString("TYPE"));
            car_id = "";
            edit_car.setText("");
            if (data.getExtras().getString("TYPE").equalsIgnoreCase("Company Car")) {
                car_layout.setVisibility(View.VISIBLE);
                comment_layout.setVisibility(View.GONE);
            } else {
                if (data.getExtras().getString("TYPE").equalsIgnoreCase("Rented Car")) {
                    comment_txt.setText(getString(R.string.car_number));
                    edit_comment.setHint(getString(R.string.car_number));
                } else {
                    comment_txt.setText(getString(R.string.comment));
                    edit_comment.setHint(getString(R.string.enter_comment));
                }
                car_layout.setVisibility(View.GONE);
                comment_layout.setVisibility(View.VISIBLE);
            }
        }
        if (requestCode == 112 && resultCode == RESULT_OK) {
            agent_location.setText(data.getExtras().getString("ADD"));
            latLongs = data.getExtras().getString("LAT") + "," + data.getExtras().getString("LNG");
        }
        if (requestCode == 271 && resultCode == RESULT_OK) {
            edit_car.setText(data.getExtras().getString("NAME"));
            car_id = data.getExtras().getString("ID");
        }
    }

    public void createJSON() {
        try {
            JSONObject main_obj = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            int t_am = 0;
            for (int i = 0; i < customerModels.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cust_id", customerModels.get(i).getId());
                jsonArray.put(jsonObject);
                t_am = t_am + getTotalAmount(customerModels.get(i).getVisit_amount());
            }
            main_obj.put("customers", jsonArray);
            main_obj.put("vehicle_type", vehicle_edit.getText().toString().trim());
            main_obj.put("vehicle_id", car_id);
            main_obj.put("remarks", edit_comment.getText().toString().trim());
            main_obj.put("dateofvisit", date_edit.getText().toString().trim());
            main_obj.put("starting_location", agent_location.getText().toString().trim());
            main_obj.put("starting_latlong", agent_location.getText().toString().trim());
            main_obj.put("token_amount", "10000");
            main_obj.put("totalCustomers", total_customers.getText().toString().trim());
            main_obj.put("agent_id", userDetails.getuserid());
            main_obj.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            System.out.println("Animesh " + main_obj.toString());
            addTrip(main_obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalAmount(String ss) {
        try {
            int n = Integer.parseInt(ss);
            return n;
        } catch (Exception e) {
            return 0;
        }
    }

    public void addTrip(String jsons) {
        pd = ProgressDialog.show(AddTripActivity.this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                ADD_VISIT_URL,
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
                Toast.makeText(getApplicationContext(), "Visit Added Successfully", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Visit Adding Failed", Toast.LENGTH_SHORT).show();
            }
        };
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 222) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createLocationRequest();
                requestSingleLocationUpdate();
            }
        }
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void requestSingleLocationUpdate() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        agent_location.setText(latitude + "," + longitude);
                        //Toast.makeText(getApplicationContext(), latitude + "," + longitude, Toast.LENGTH_SHORT).show();
                        // Do something with the latitude and longitude
                    }
                    stopLocationUpdates();
                }
            };

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

            timeoutHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopLocationUpdates();
                }
            }, TIMEOUT_IN_MS);

            isRequestingLocationUpdates = true;
        }
    }

    private void stopLocationUpdates() {
        if (isRequestingLocationUpdates) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            timeoutHandler.removeCallbacksAndMessages(null);
            isRequestingLocationUpdates = false;
            //Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
        }
    }

    public void showProminentDialog() {
        prominent_dialog = new Dialog(AddTripActivity.this);
        prominent_dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        prominent_dialog.setContentView(R.layout.prominent_dialog);
        prominent_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        prominent_dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prominent_dialog.dismiss();
                finish();
            }
        });
        prominent_dialog.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prominent_dialog.dismiss();
                ActivityCompat.requestPermissions(AddTripActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        222);
            }
        });
        prominent_dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prominent_dialog.dismiss();
                finish();
            }
        });
        prominent_dialog.show();
    }

}