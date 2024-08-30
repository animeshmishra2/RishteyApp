package com.guruvardaan.ghargharsurvey.agent;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.SearchModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    EditText search_edit;
    RecyclerView recyclerView;
    UserDetails userDetails;
    ProgressBar progress;
    RelativeLayout cats_lay;
    LinearLayout custom_lay;
    ArrayList<SearchModel> searchModels;
    SearchAdapter searchAdapter;
    ImageView drawer_img;
    ImageView micro, close;
    LinearLayout filter_lay;
    String tpe;
    TextView filter_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_search, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        progress = (ProgressBar) findViewById(R.id.progress);
        // getSupportActionBar().hide();
        cats_lay = (RelativeLayout) findViewById(R.id.cats_lay);
        custom_lay = (LinearLayout) findViewById(R.id.custom_lay);
        micro = (ImageView) findViewById(R.id.micro);
        filter_lay = (LinearLayout) findViewById(R.id.filter_lay);
        filter_lay.setVisibility(View.GONE);
        close = (ImageView) findViewById(R.id.close);
        filter_result = (TextView) findViewById(R.id.filter_result);
        drawer_img = (ImageView) findViewById(R.id.drawer_img);
        drawer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progress.setVisibility(View.GONE);
        tpe = "";
        search_edit = (EditText) findViewById(R.id.search_edit);
        searchModels = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        searchAdapter = new SearchAdapter();
        custom_lay.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(searchAdapter);
        filter_lay.setVisibility(View.GONE);
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tpe.equalsIgnoreCase("")) {
                    search_edit.setSelectAllOnFocus(false);
                    if (search_edit.getText().toString().trim().length() > 0) {
                        close.setVisibility(View.VISIBLE);
                        micro.setVisibility(View.GONE);
                    }
                    if (search_edit.getText().toString().trim().length() > 0) {
                        getSearch(search_edit.getText().toString());
                    } else if (search_edit.getText().toString().trim().length() < 1) {
                        searchModels.clear();
                        searchModels = new ArrayList<>();
                        close.setVisibility(View.GONE);
                        micro.setVisibility(View.VISIBLE);
                        searchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tpe = "";
                search_edit.setSelectAllOnFocus(false);
                startVoiceRecognitionActivity();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tpe = "";
                search_edit.setSelectAllOnFocus(false);
                search_edit.setText("");
            }
        });
        search_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tpe = "";
                    System.out.println("Focus Started");
                    if (search_edit.getText().toString().trim().length() > 0) {
                        searchModels.clear();
                        searchAdapter.notifyDataSetChanged();
                        getSearch(search_edit.getText().toString());
                    } else {
                        searchModels.clear();
                        close.setVisibility(View.GONE);
                        micro.setVisibility(View.VISIBLE);
                        searchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void startVoiceRecognitionActivity() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "Speech recognition demo");
            startActivityForResult(intent, 1234);
        } catch (ActivityNotFoundException e) {
            String appPackageName = "com.google.android.googlequicksearchbox";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }


    public void getSearch(final String keyword) {
        recyclerView.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET, Config.SEARCH_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + userDetails.getuserid() + "/" + keyword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    searchModels.clear();
                    for (int i = 0; i < Table.length(); i++) {
                        JSONObject jobj = Table.getJSONObject(i);
                        searchModels.add(new SearchModel(jobj.getString("name"), jobj.getString("UserRank"), jobj.getString("userId"), jobj.getString("mobile1"), jobj.getString("mobile2"), jobj.getString("userImage"), jobj.getString("type"), keyword));
                    }
                    searchAdapter.notifyDataSetChanged();
                    if (searchModels.size() > 0) {
                        custom_lay.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        custom_lay.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    searchModels = new ArrayList<>();
                    searchAdapter.notifyDataSetChanged();
                    custom_lay.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                System.out.println("Response  " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                searchModels = new ArrayList<>();
                recyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                searchAdapter.notifyDataSetChanged();
                cats_lay.setVisibility(View.VISIBLE);
            }
        });
        sr.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }


    class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView user_name, user_mobile, rank_txt, user_rank, customer_type;

            public MyViewHolder(View view) {
                super(view);
                user_name = (TextView) view.findViewById(R.id.user_name);
                user_mobile = (TextView) view.findViewById(R.id.user_mobile);
                rank_txt = (TextView) view.findViewById(R.id.rank_txt);
                user_rank = (TextView) view.findViewById(R.id.user_rank);
                customer_type = (TextView) view.findViewById(R.id.customer_type);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_customer_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.customer_type.setText(searchModels.get(position).getType());
            holder.user_name.setText(searchModels.get(position).getName());
            holder.user_mobile.setText(searchModels.get(position).getMobile1());
            holder.user_rank.setText(searchModels.get(position).getUserRank());
            if (searchModels.get(position).getMobile2().length() == 10) {
                holder.user_mobile.setText(searchModels.get(position).getMobile1() + "/" + searchModels.get(position).getMobile1());
            }
            if (searchModels.get(position).getUserRank().equalsIgnoreCase("null")) {
                holder.rank_txt.setVisibility(View.GONE);
                holder.user_rank.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return searchModels.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) {
                search_edit.setText(matches.get(0));
            }
        }
        if (requestCode == 28 && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
