package com.guruvardaan.ghargharsurvey.welcome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.MarshMallowPermission;
import com.guruvardaan.ghargharsurvey.config.SingleShotLocationProvider;
import com.guruvardaan.ghargharsurvey.model.GooglePlaceModel;

import java.util.ArrayList;

public class LocationAutocomplete extends BaseActivity {
    EditText edit_location;
    ProgressBar prog;
    ImageView back;
    RecyclerView rec_view;
    ArrayList<GooglePlaceModel> placeModels;
    MarshMallowPermission marshMallowPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.location_autocomplete, frameLayout);
        //getSupportActionBar().hide();
        marshMallowPermission = new MarshMallowPermission(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                marshMallowPermission.requestPermissionForAccessFineLocation();
            }
        }
        initializeVars();
        rec_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        prog.setVisibility(View.GONE);
        edit_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // getGooglePlaces(edit_location.getText().toString().trim());
            }
        });
        placeModels = new ArrayList<>();
        placeModels.add(new GooglePlaceModel("Current Location", "Your Current Location", "0"));
        rec_view.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rec_view.setAdapter(new PlaceAdapter(placeModels));
    }

    public void initializeVars() {
        rec_view = (RecyclerView) findViewById(R.id.rec_view);
        prog = (ProgressBar) findViewById(R.id.prog);
        back = (ImageView) findViewById(R.id.back);
        edit_location = (EditText) findViewById(R.id.edit_location);
    }


/*
    public void getGooglePlaces(final String txt) {
        prog.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(LocationAutocomplete.this);
        StringRequest sr = new StringRequest(Request.Method.POST, Config.PLACE_URL1 + txt + Config.PLACE_URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.setVisibility(View.GONE);
                try {
                    System.out.println("Animesh   " + Config.PLACE_URL1 + txt + Config.PLACE_URL2);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("predictions");
                    if (jsonArray.length() > 0) {
                        placeModels = new ArrayList<>();
                        placeModels.add(new GooglePlaceModel("Current Location", "Your Current Location", "0"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            JSONObject strct = object.getJSONObject("structured_formatting");
                            placeModels.add(new GooglePlaceModel(strct.getString("main_text"), strct.getString("secondary_text"), object.getString("place_id")));
                        }
                        rec_view.setVisibility(View.VISIBLE);
                        rec_view.setAdapter(new PlaceAdapter(placeModels));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prog.setVisibility(View.GONE);
                error.printStackTrace();
            }
        });
        queue.add(sr);
    }
*/

    class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {

        private ArrayList<GooglePlaceModel> gpModel;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView main_txt, second_txt;
            public LinearLayout main_lay;

            public MyViewHolder(View view) {
                super(view);
                main_txt = (TextView) view.findViewById(R.id.main_txt);
                second_txt = (TextView) view.findViewById(R.id.second_txt);
                main_lay = (LinearLayout) view.findViewById(R.id.main_lay);
            }
        }


        public PlaceAdapter(ArrayList<GooglePlaceModel> gpModel) {
            this.gpModel = gpModel;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.main_txt.setText(gpModel.get(position).getMainText());
            holder.second_txt.setText(gpModel.get(position).getSecondText());
            holder.main_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Animeshhh ");
                    if (!gpModel.get(position).getPlaceID().equalsIgnoreCase("0")) {
                        //getTotalData(gpModel.get(position).getPlaceID());
                    } else {
                        System.out.println("Animeshhh VIsh");
                        getLocation();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return gpModel.size();
        }
    }

/*
    public void getTotalData(String txt) {
        prog.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(LocationAutocomplete.this);
        StringRequest sr = new StringRequest(Request.Method.POST, Config.TOT_URL1 + txt + Config.TOT_URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("result");
                    JSONObject geometry = result.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    String lats = location.getString("lat");
                    String lng = location.getString("lng");
                    String add = result.getString("formatted_address");
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("LAT", lats);
                    bundle.putString("LNG", lng);
                    bundle.putString("ADD", add);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prog.setVisibility(View.GONE);
                error.printStackTrace();
            }
        });
        queue.add(sr);
    }
*/

    public void getLocation() {
        SingleShotLocationProvider.requestSingleUpdate(LocationAutocomplete.this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        Log.d("Location", "my location is " + location.latitude);
                        prog.setVisibility(View.VISIBLE);
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("LAT", "" + location.latitude);
                        bundle.putString("LNG", "" + location.longitude);
                        bundle.putString("ADD", location.latitude + "," + location.longitude);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        //getCurrentData("" + location.latitude, "" + location.longitude);
                    }
                });

    }

/*
    public void getCurrentData(final String lat, final String lng) {
        RequestQueue queue = Volley.newRequestQueue(LocationAutocomplete.this);
        StringRequest sr = new StringRequest(Request.Method.POST, Config.CURR_URL1 + lat + "," + lng + Config.TOT_URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");
                    JSONObject result = array.getJSONObject(0);
                    String add = result.getString("formatted_address");
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("LAT", lat);
                    bundle.putString("LNG", lng);
                    bundle.putString("ADD", add);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prog.setVisibility(View.GONE);
                error.printStackTrace();
            }
        });
        queue.add(sr);
    }
*/


}
