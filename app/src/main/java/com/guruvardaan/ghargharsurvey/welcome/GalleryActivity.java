package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.GALLERY_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.SELECT_BANK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.model.GalleryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GalleryActivity extends BaseActivity {

    RecyclerView gallery_recycler;
    ImageView back_button;
    ArrayList<GalleryModel> galleryModels;
    ProgressBar progress;
    LinearLayout scroll;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getLayoutInflater().inflate(R.layout.activity_update_agent, frameLayout);
        scroll = (LinearLayout) findViewById(R.id.scroll);
        progress = (ProgressBar) findViewById(R.id.progress);
        gallery_recycler = (RecyclerView) findViewById(R.id.gallery_recycler);
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        galleryModels = new ArrayList<>();
        gallery_recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        getAllImages();
    }

    class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView gal_img;
            LinearLayout gal_lay;
            TextView main_txt;

            public MyViewHolder(View view) {
                super(view);
                main_txt = (TextView) view.findViewById(R.id.main_txt);
                gal_img = (ImageView) view.findViewById(R.id.gal_img);
                gal_lay = (LinearLayout) view.findViewById(R.id.gal_lay);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.gal_lay.getLayoutParams().height = (int) (width / 2.2);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(8));
            Glide.with(holder.gal_img.getContext())
                    .load(galleryModels.get(position).getImgLoc())
                    .apply(requestOptions).placeholder(R.drawable.albums)
                    .into(holder.gal_img);
            holder.gal_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("IMG", galleryModels.get(position).getImgLoc());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.main_txt.setText(galleryModels.get(position).getDescription().trim());
        }

        @Override
        public int getItemCount() {
            return galleryModels.size();
        }

    }

    private void getAllImages() {
        progress.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.GONE);
        String URL_CHECK = GALLERY_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/Agent/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        scroll.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                galleryModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    galleryModels.add(new GalleryModel(object.getString("id"), object.getString("title"), object.getString("description"), object.getString("imgLoc")));
                                }
                                gallery_recycler.setAdapter(new GalleryAdapter());
                            } else {
                                Toast.makeText(getApplicationContext(), "No Images Found", Toast.LENGTH_SHORT).show();

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

}