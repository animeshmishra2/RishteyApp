package com.guruvardaan.ghargharsurvey.visits;

import static com.guruvardaan.ghargharsurvey.config.Config.COORDINATE_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.START_VISIT_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.STOP_VISIT_URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.MapModel;
import com.guruvardaan.ghargharsurvey.utils.ApiClient;
import com.guruvardaan.ghargharsurvey.utils.DBHelper;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VisitOpenActivity extends BaseActivity {

    DBHelper dbHelper;
    Double source_lat, source_long;
    Double old_lat, old_long;
    TextView start_stop_trip;
    boolean trip_run = false;
    ArrayList<MapModel> mapModels;
    ProgressDialog pd;
    UserDetails userDetails;
    ImageView back_button;
    public static String trip_id = "0";
    public static String status = "0";
    ImageView kebab_menu;
    Dialog prominent_dialog;
    LatAdapter latAdapter;
    private static final long TIMEOUT_IN_MS = 10000; // 10 seconds
    private LocationRequest locationRequest;
    private boolean isRequestingLocationUpdates = false;
    private Handler timeoutHandler = new Handler();
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    TextView visit_tt;
    ProgressBar progress;
    LinearLayout main_layout;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_visit_open, frameLayout);
        mapModels = new ArrayList<>();
        latAdapter = new LatAdapter();
        visit_tt = (TextView) findViewById(R.id.visit_tt);
        progress = (ProgressBar) findViewById(R.id.progress);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(latAdapter);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        FancyToast.makeText(VisitOpenActivity.this, "Location is in progress", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("LocationBroadcast"));
        back_button = (ImageView) findViewById(R.id.back_button);
        kebab_menu = (ImageView) findViewById(R.id.kebab_menu);
        dbHelper = new DBHelper(getApplicationContext());
        userDetails = new UserDetails(getApplicationContext());
        old_lat = 0.00;
        old_long = 0.00;
        trip_run = false;
        start_stop_trip = (TextView) findViewById(R.id.start_stop_trip);
        if (getIntent().getExtras().getString("LATS").contains(",")) {
            source_lat = Double.parseDouble(getIntent().getExtras().getString("LATS").split(",")[0]);
            source_long = Double.parseDouble(getIntent().getExtras().getString("LATS").split(",")[1]);
        } else {
            source_lat = 0.0;
            source_long = 0.0;
        }
        status = getIntent().getExtras().getString("STATUS");
        trip_id = getIntent().getExtras().getString("TID");
        getLocationPermission();
        start_stop_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trip_run == false) {
                    showStartTripDialog();
                } else {
                    //showStopTripDialog();
                }
            }
        });
        start_stop_trip.setVisibility(View.GONE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        kebab_menu.setVisibility(View.GONE);
        PopupMenu popup = new PopupMenu(VisitOpenActivity.this, kebab_menu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.stop_trip) {
                    if (trip_run == false) {
                        //showStartTripDialog();
                    } else {
                        showStopTripDialog();
                    }
                }
                return true;
            }
        });

        kebab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });
        if (getIntent().getExtras().getString("STATUS").equalsIgnoreCase("Visit Started")) {
            visit_tt.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);
            getAllLatLongs();
            visit_tt.setText("आपकी विजिट स्टार्टेड  है , इसी पेज पर आपकी लोकेशन ट्रैक होकर दिखती रहेगी।  इसी पेज पर आप अब विजिट बंद भी कर सकते हैं");
        } else {
            visit_tt.setVisibility(View.VISIBLE);
            visit_tt.setText("आपकी विजिट अभी शुरू नहीं हुई है , विजिट शुरू करने के लिए START TRIP बटन पर क्लिक करे। विजिट स्टार्ट हो जायेगी");
            main_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        // we will receive data updates in onReceive method.
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplicationContext(), "" + dbHelper.getAllLatLong(trip_id), Toast.LENGTH_SHORT).show();
            if (ApiClient.isNetworkAvailable(getApplicationContext())) {

            }
            if (trip_run == true) {
                old_lat = intent.getExtras().getDouble("LT");
                old_long = intent.getExtras().getDouble("LN");
                if (ApiClient.isNetworkAvailable(getApplicationContext())) {
                    //Toast.makeText(getApplicationContext(), ""+dbHelper.getAllLatLong(getIntent().getExtras().getString("TID")).size(), Toast.LENGTH_SHORT).show();
                    createJson();
                } else {
                    //dbHelper.insertTrip(getIntent().getExtras().getString("TID"), "" + old_lat, "" + old_long, getIntent().getExtras().getString("STATUS"), "0");
                }
            }
        }
    };

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 222) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createLocationRequest();
                requestSingleLocationUpdate();
            }
        }
    }

    private void startVisit(String tid, String llong) {
        pd = ProgressDialog.show(VisitOpenActivity.this);
        String URL_CHECK = START_VISIT_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + userDetails.getuserid() + "/" + tid + "/" + llong + "/";
        System.out.println("Mahendra " + URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        System.out.println("AniStop " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                if (object.has("return_type")) {
                                    if (object.getString("return_type").equalsIgnoreCase("f")) {
                                        FancyToast.makeText(VisitOpenActivity.this, "Trip Can Not be Started, Please Contact Support. " + object.getString("errormsg"), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                    }
                                } else {
                                    trip_run = true;
                                    showTripStartDialog();
                                    if (!isMyServiceRunning(LocationUpdateService.class)) {
                                        startService();
                                    }
                                    visit_tt.setText("आपकी विजिट स्टार्टेड  है , इसी पेज पर आपकी लोकेशन ट्रैक होकर दिखती रहेगी।  इसी पेज पर आप अब विजिट बंद भी कर सकते हैं");
                                    getAllLatLongs();
                                    kebab_menu.setVisibility(View.VISIBLE);
                                    start_stop_trip.setVisibility(View.GONE);
                                    //start_stop_trip.setText("Stop Visit");
                                    FancyToast.makeText(VisitOpenActivity.this, "Your Trip Started", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
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
                        error.printStackTrace();
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

    public void sendLatLong(String jsons, String llls) {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.SEND_LATLONG_URL,
                createMyReqSuccessListener(llls),
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
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(myReq);
    }

    private Response.Listener<String> createMyReqSuccessListener(String lls) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    if (Table.length() > 0) {
                        JSONObject object = Table.getJSONObject(0);
                        if (object.has("return_type")) {
                            if (object.getString("return_type").equalsIgnoreCase("t")) {
                                visit_tt.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                //Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                                mapModels.add(new MapModel(lls, "" + System.currentTimeMillis()));
                                dbHelper.deleteRow(getIntent().getExtras().getString("TID"));
                                latAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                // Toast.makeText(getApplicationContext(), "Error Send", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void stopVisit(String tid, String llong) {
        pd = ProgressDialog.show(VisitOpenActivity.this);
        String URL_CHECK = STOP_VISIT_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + userDetails.getuserid() + "/" + tid + "/" + llong + "/";
        System.out.println("Mahendra " + URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        System.out.println("AniStart " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                if (object.has("return_type")) {
                                    if (object.getString("return_type").equalsIgnoreCase("f")) {
                                        FancyToast.makeText(VisitOpenActivity.this, "Trip Can Not be Stopped, Please Contact Support", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                    }
                                } else {
                                    if (isMyServiceRunning(LocationUpdateService.class)) {
                                        stopService();
                                    }
                                    trip_run = false;
                                    dbHelper.deleteRow(getIntent().getExtras().getString("TID"));
                                    showTripStopDialog();
                                    //Toast.makeText(getApplicationContext(), "Trip Stopped", Toast.LENGTH_SHORT).show();

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
                        error.printStackTrace();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    public void showStartTripDialog() {
        final Dialog dialog = new Dialog(VisitOpenActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_membership);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (old_lat != 0.00) {
                    startVisit(getIntent().getExtras().getString("TID"), old_lat + "," + old_long);
                } else {
                    FancyToast.makeText(VisitOpenActivity.this, "Map is loading , please wait", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                }
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void showProminentDialog() {
        prominent_dialog = new Dialog(VisitOpenActivity.this);
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
                ActivityCompat.requestPermissions(VisitOpenActivity.this,
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


    public void showStopTripDialog() {
        final Dialog dialog = new Dialog(VisitOpenActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_stop_membership);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                stopVisit(getIntent().getExtras().getString("TID"), old_lat + "," + old_long);
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void createJson() {
        try {
            JSONObject main_obj = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < dbHelper.getAllLatLong(getIntent().getExtras().getString("TID")).size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lat_lng", dbHelper.getAllLatLong(getIntent().getExtras().getString("TID")).get(i));
                jsonArray.put(jsonObject);
            }
            /*JSONObject jsonObject = new JSONObject();
            jsonObject.put("lat_lng", old_lat + "," + old_long);
            jsonArray.put(jsonObject);*/
            main_obj.put("latlongs", jsonArray);
            main_obj.put("visit_id", getIntent().getExtras().getString("TID"));
            main_obj.put("agent_id", userDetails.getuserid());
            main_obj.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            System.out.println("Animesh " + main_obj.toString());
            if (dbHelper.getAllLatLong(getIntent().getExtras().getString("TID")).size() > 0) {
                sendLatLong(main_obj.toString(), dbHelper.getAllLatLong(getIntent().getExtras().getString("TID")).get(0));
            }
            //Toast.makeText(getApplicationContext(), jsonArray.get(jsonArray.length() - 1).toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, LocationUpdateService.class);
        stopService(serviceIntent);
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, LocationUpdateService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void showTripStartDialog() {
        final Dialog dialog = new Dialog(VisitOpenActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.trip_start_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showTripStopDialog() {
        final Dialog dialog = new Dialog(VisitOpenActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.trip_stop_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        dialog.show();
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
                        old_lat = latitude;
                        old_long = longitude;
                        //Toast.makeText(getApplicationContext(), latitude + "," + longitude, Toast.LENGTH_SHORT).show();
                        if (getIntent().getExtras().getString("STATUS").equalsIgnoreCase("Visit Started")) {
                            kebab_menu.setVisibility(View.VISIBLE);
                            start_stop_trip.setVisibility(View.GONE);
                            //start_stop_trip.setText("Stop Trip");
                            if (!isMyServiceRunning(LocationUpdateService.class)) {
                                startService();
                            }
                            trip_run = true;
                        } else {
                            if (isMyServiceRunning(LocationUpdateService.class)) {
                                stopService();
                            }
                            trip_run = false;
                            start_stop_trip.setVisibility(View.VISIBLE);
                            kebab_menu.setVisibility(View.INVISIBLE);
                        }
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

    private void getAllLatLongs() {
        progress.setVisibility(View.VISIBLE);
        String URL_CHECK = COORDINATE_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + getIntent().getExtras().getString("TID");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                visit_tt.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                mapModels = new ArrayList<>();
                                int l = Table.length();
                                if (l > 25) {
                                    l = 25;
                                }
                                for (int i = 0; i < l; i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    mapModels.add(new MapModel(object.getString("lat"), object.getString("ins_date")));
                                }
                                latAdapter.notifyDataSetChanged();
                            } else {
                                visit_tt.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "No Address Found", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        error.printStackTrace();
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


    class LatAdapter extends RecyclerView.Adapter<LatAdapter.TypeHolder> {

        @Override
        public int getItemCount() {
            return mapModels.size();

        }

        @Override
        public void onBindViewHolder(LatAdapter.TypeHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(mapModels.get(position).getLat());
            holder.prod_time.setText(mapModels.get(position).getIns_date());
        }

        @Override
        public LatAdapter.TypeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.lat_long_row, viewGroup, false);
            LatAdapter.TypeHolder listHolder = new LatAdapter.TypeHolder(mainGroup);
            return listHolder;
        }

        public class TypeHolder extends RecyclerView.ViewHolder {

            TextView prod_name;
            TextView prod_time;
            LinearLayout prod_layout;

            public TypeHolder(View view) {
                super(view);
                prod_layout = (LinearLayout) view.findViewById(R.id.prod_layout);
                prod_name = (TextView) view.findViewById(R.id.prod_name);
                prod_time = (TextView) view.findViewById(R.id.prod_time);
            }
        }
    }


}