package com.guruvardaan.ghargharsurvey.visits;

import static com.guruvardaan.ghargharsurvey.config.Config.START_VISIT_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.STOP_VISIT_URL;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.ApiClient;
import com.guruvardaan.ghargharsurvey.utils.DBHelper;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class VisitMapActivity extends BaseActivity implements OnMapReadyCallback {
    SupportMapFragment mapFrag;
    GoogleMap mMap;
    DBHelper dbHelper;
    ArrayList<LatLng> points;
    Double source_lat, source_long;
    Double old_lat, old_long;
    Marker marker;
    private float v;
    PolylineOptions lineOptions = null;
    TextView start_stop_trip;
    boolean trip_run = false;
    ProgressDialog pd;
    UserDetails userDetails;
    ImageView back_button;
    public static String trip_id = "0";
    public static String status = "0";
    ImageView kebab_menu;
    Dialog prominent_dialog;
    KonfettiView konfettiView;
    private Shape.DrawableShape drawableShape = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_visit_map, frameLayout);
        konfettiView = (KonfettiView) findViewById(R.id.konfettiView);
        FancyToast.makeText(VisitMapActivity.this, "Map is loading , please wait", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
        drawableShape = new Shape.DrawableShape(drawable, true);
        points = new ArrayList<>();
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
        mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
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
        PopupMenu popup = new PopupMenu(VisitMapActivity.this, kebab_menu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.stop_trip) {
                    if (trip_run == false) {
                        //showStartTripDialog();
                    } else {
                        showStopTripDialog();
                       /* if (getIntent().getExtras().getInt("TOT") > 0) {
                            showStopTripDialog();
                        } else {
                            showNoCustomers();
                        }*/

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
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        // we will receive data updates in onReceive method.
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplicationContext(), "" + dbHelper.getAllLatLong(trip_id), Toast.LENGTH_SHORT).show();
            if (ApiClient.isNetworkAvailable(getApplicationContext())) {
               /* String url1 = getDirectionsUrl(new LatLng(old_lat, old_long), new LatLng(intent.getExtras().getDouble("LT"), intent.getExtras().getDouble("LN")));
                DownloadTask1 downloadTask1 = new DownloadTask1();
                downloadTask1.execute(url1);*/
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(intent.getExtras().getDouble("LT"), intent.getExtras().getDouble("LN")))
                        .title("Location").draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.rrl)));
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
            showUpdates();
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
                showUpdates();
            }
        }
    }

    private void startVisit(String tid, String llong) {
        pd = ProgressDialog.show(VisitMapActivity.this);
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
                                        FancyToast.makeText(VisitMapActivity.this, "Trip Can Not be Started, Please Contact Support. " + object.getString("errormsg"), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                    }
                                } else {
                                    trip_run = true;
                                    showTripStartDialog();
                                    if (!isMyServiceRunning(LocationUpdateService.class)) {
                                        startService();
                                    }
                                    kebab_menu.setVisibility(View.VISIBLE);
                                    start_stop_trip.setVisibility(View.GONE);
                                    //start_stop_trip.setText("Stop Visit");
                                    FancyToast.makeText(VisitMapActivity.this, "Your Trip Started", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
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

    public void sendLatLong(String jsons) {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.SEND_LATLONG_URL,
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
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(myReq);
    }

    private Response.Listener<String> createMyReqSuccessListener() {
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
                                //Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                                dbHelper.deleteRow(getIntent().getExtras().getString("TID"));
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
        pd = ProgressDialog.show(VisitMapActivity.this);
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
                                        FancyToast.makeText(VisitMapActivity.this, "Trip Can Not be Stopped, Please Contact Support", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
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


    public void showUpdates() {
        LocationParams locationParams = new LocationParams.Builder().setAccuracy(LocationAccuracy.HIGH).setInterval(5000).setDistance(0).build();
        SmartLocation.with(getApplicationContext()).location().config(locationParams).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (old_lat == 0.00) {
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .title("Location").draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.rrl)));
                    //  Toast.makeText(getApplicationContext(), "Hello Dada " + old_lat + " - " + old_long + " --- " + location.getLatitude() + " - " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    old_lat = location.getLatitude();
                    old_long = location.getLongitude();
                    start_stop_trip.setVisibility(View.VISIBLE);
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
                }
                //  Toast.makeText(getApplicationContext(), "" + location.getLongitude() + " --- " + location.getLatitude(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showStartTripDialog() {
        final Dialog dialog = new Dialog(VisitMapActivity.this);
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
                    FancyToast.makeText(VisitMapActivity.this, "Map is loading , please wait", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
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
        prominent_dialog = new Dialog(VisitMapActivity.this);
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
                ActivityCompat.requestPermissions(VisitMapActivity.this,
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
        final Dialog dialog = new Dialog(VisitMapActivity.this);
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
            sendLatLong(main_obj.toString());
            //Toast.makeText(getApplicationContext(), jsonArray.get(jsonArray.length() - 1).toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();

        if (source_lat != 0.0) {
            if (ApiClient.isNetworkAvailable(getApplicationContext())) {
                String url1 = getDirectionsUrl(new LatLng(source_lat, source_long), new LatLng(26.781212, 80.8987));
                DownloadTask downloadTask1 = new DownloadTask();
                downloadTask1.execute(url1);
            }
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                    (new CameraPosition.Builder().target(new LatLng(source_lat, source_long))
                            .zoom(14.0f).build()), 100, null);
        }
/*
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (ApiClient.isNetworkAvailable(getApplicationContext())) {
                    String url1 = getDirectionsUrl(new LatLng(old_lat, old_long), latLng);
                    DownloadTask1 downloadTask1 = new DownloadTask1();
                    downloadTask1.execute(url1);
                }
            }
        });
*/

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyBqgkuHg7SujUIkb6kA5o4XZyh5LXiD2ws";
        System.out.println("TotalUrl" + url);
        return url;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception while do", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                MapDirectionsParser parser = new MapDirectionsParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);

            }
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                // Toast.makeText(getApplicationContext(), "Document verification is in Progress", Toast.LENGTH_SHORT).show();
                //finish();
            }

        }


    }

    private class DownloadTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask1 parserTask1 = new ParserTask1();
            parserTask1.execute(result);


        }
    }

    private class ParserTask1 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                MapDirectionsParser parser = new MapDirectionsParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = result.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.GREEN);
                    animateCarOnMap(points);

                }
            }
            if (lineOptions != null) {
                //mMap.addPolyline(lineOptions);
            } else {
                // Toast.makeText(getApplicationContext(), "Document verification is in Progress", Toast.LENGTH_SHORT).show();
                //finish();
            }

        }
    }

    private void animateCarOnMap(final List<LatLng> latLngs) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
        mMap.animateCamera(mCameraUpdate);
        System.out.println("ANi1");
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                System.out.println("ANi2");
                v = valueAnimator.getAnimatedFraction();
                double lng = v * latLngs.get(latLngs.size() - 1).longitude + (1 - v)
                        * latLngs.get(0).longitude;
                double lat = v * latLngs.get(latLngs.size() - 1).latitude + (1 - v)
                        * latLngs.get(0).latitude;
                LatLng newPos = new LatLng(lat, lng);
                marker.setPosition(newPos);
                marker.setAnchor(0.5f, 0.5f);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                        (new CameraPosition.Builder().target(newPos)
                                .zoom(16.0f).build()), 100, null);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();
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
        final Dialog dialog = new Dialog(VisitMapActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.trip_start_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EmitterConfig emitterConfig = new Emitter(2000, TimeUnit.MILLISECONDS).max(100);
                konfettiView.start(
                        new PartyFactory(emitterConfig)
                                .spread(360)
                                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                                .setSpeedBetween(0f, 30f)
                                .position(new Position.Relative(0.5, 0.3))
                                .build()
                );
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EmitterConfig emitterConfig = new Emitter(2000, TimeUnit.MILLISECONDS).max(100);
                konfettiView.start(
                        new PartyFactory(emitterConfig)
                                .spread(360)
                                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                                .setSpeedBetween(0f, 30f)
                                .position(new Position.Relative(0.5, 0.3))
                                .build()
                );
            }
        });
        dialog.show();
    }

    public void showTripStopDialog() {
        final Dialog dialog = new Dialog(VisitMapActivity.this);
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


    public void showNoCustomers() {
        final Dialog dialog = new Dialog(VisitMapActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_sel_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }


}