package com.guruvardaan.ghargharsurvey.plots;

import static com.guruvardaan.ghargharsurvey.config.Config.MAP_URL;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.barteksc.pdfviewer.PDFView;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.jsibbold.zoomage.ZoomageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewMapActivity extends BaseActivity {

    ImageView back_button;
    ZoomageView myZoomageView;
    TextView view_map, main_txt;
    ProgressBar progress;
    String map = "";
    PDFView pdfview;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_view_map, frameLayout);
        map = "";
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(1);
        pdfview = (PDFView) findViewById(R.id.pdfview);
        progress = (ProgressBar) findViewById(R.id.progress);
        back_button = (ImageView) findViewById(R.id.back_button);
        view_map = (TextView) findViewById(R.id.view_map);
        main_txt = (TextView) findViewById(R.id.main_txt);
        myZoomageView = (ZoomageView) findViewById(R.id.myZoomageView);


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        main_txt.setText(getIntent().getExtras().getString("TITLE"));
        getAllImages();
        view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!map.equalsIgnoreCase("")) {
                    DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(map);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle("Downloading Map")
                            .setMimeType("image/jpeg")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    dm.enqueue(request);
                } else {
                    Toast.makeText(getApplicationContext(), "Map Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getAllImages() {
        progress.setVisibility(View.VISIBLE);
        myZoomageView.setVisibility(View.GONE);
        String URL_CHECK = MAP_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + getIntent().getExtras().getString("PR") + "/" + getIntent().getExtras().getString("BL");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        myZoomageView.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                map = object.getString("upload_pdf");
                                if (map.contains(".pdf")) {
                                    pdfview.setVisibility(View.VISIBLE);
                                    webView.setVisibility(View.GONE);
                                    myZoomageView.setVisibility(View.GONE);
                                    pdfview.fromUri(Uri.parse(map)).load();
                                } else {
                                    pdfview.setVisibility(View.GONE);
                                    myZoomageView.setVisibility(View.GONE);
                                    webView.setVisibility(View.VISIBLE);
                                    webView.loadUrl(map);
                                   /* Glide.with(myZoomageView.getContext())
                                            .load(map).placeholder(R.drawable.albums)
                                            .into(myZoomageView);*/
                                    /*Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .load(map)
                                            .into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(Bitmap bitmap,
                                                                            Transition<? super Bitmap> transition) {
                                                    int w = bitmap.getWidth();
                                                    int h = bitmap.getHeight();
                                                    myZoomageView.setImageBitmap(bitmap);
                                                }
                                            });*/

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "No Map Found", Toast.LENGTH_SHORT).show();
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