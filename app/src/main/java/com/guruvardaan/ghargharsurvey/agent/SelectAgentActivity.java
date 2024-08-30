package com.guruvardaan.ghargharsurvey.agent;

import static com.guruvardaan.ghargharsurvey.config.Config.SELF_AGENT_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.DirectTeamModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectAgentActivity extends BaseActivity {

    RecyclerView passbook_recycler;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<DirectTeamModel> selAgentModels;
    ArrayList<DirectTeamModel> newAgentModels;
    EditText search_edit;
    ImageView micro;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_select_agent, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        progress = (ProgressBar) findViewById(R.id.progress);
        micro = (ImageView) findViewById(R.id.micro);
        search_edit = (EditText) findViewById(R.id.search_edit);
        back_button = (ImageView) findViewById(R.id.back_button);
        passbook_recycler = (RecyclerView) findViewById(R.id.passbook_recycler);
        passbook_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        selAgentModels = new ArrayList<>();
        newAgentModels = new ArrayList<>();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_edit.setSelectAllOnFocus(false);
                startVoiceRecognitionActivity();
            }
        });
        getSelfTeam();
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (search_edit.getText().toString().trim().length() > 0) {
                    newAgentModels.clear();
                    for (int i = 0; i < selAgentModels.size(); i++) {
                        if (selAgentModels.get(i).getAgentName().toLowerCase().contains(search_edit.getText().toString().toLowerCase()) || selAgentModels.get(i).getAgentId().toLowerCase().contains(search_edit.getText().toString().toLowerCase())) {
                            newAgentModels.add(selAgentModels.get(i));
                        }
                    }
                    passbook_recycler.setAdapter(new AgentAdapter(newAgentModels));
                } else {
                    passbook_recycler.setAdapter(new AgentAdapter(selAgentModels));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) {
                search_edit.setText(matches.get(0));
            }
        }
    }

    private void getSelfTeam() {
        progress.setVisibility(View.VISIBLE);
        String URL_CHECK = SELF_AGENT_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid() + "/3";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            selAgentModels = new ArrayList<>();
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                selAgentModels.add(new DirectTeamModel(object.getString("agentId"), object.getString("agentName"), object.getString("agentMobile"), object.getString("agentRank"), object.getString("agentPassword"), object.getString("int_agentId"), object.getString("int_agentName"), object.getString("int_agentMobile"), object.getString("int_agentRank"),""));
                            }
                            //selAgentModels.add(0,new DirectTeamModel(userDetails.getuserid(),userDetails.getName(),userDetails.getMobile(),"","","","","",""));
                            passbook_recycler.setAdapter(new AgentAdapter(selAgentModels));
                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.TypeHolder> {

        ArrayList<DirectTeamModel> teamModels;

        AgentAdapter(ArrayList<DirectTeamModel> teamModels) {
            this.teamModels = teamModels;
        }

        @Override
        public int getItemCount() {
            return teamModels.size();

        }

        @Override
        public void onBindViewHolder(AgentAdapter.TypeHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(teamModels.get(position).getAgentName());
            holder.pr_image.setImageResource(R.drawable.add_cust);
            holder.id_of_agent.setText(teamModels.get(position).getAgentId());
            holder.prod_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", teamModels.get(position).getAgentName());
                    bundle.putString("ID", teamModels.get(position).getAgentId());
                    bundle.putString("RANK", teamModels.get(position).getAgentRank());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public AgentAdapter.TypeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.agent_sel_row, viewGroup, false);
            AgentAdapter.TypeHolder listHolder = new AgentAdapter.TypeHolder(mainGroup);
            return listHolder;
        }

        public class TypeHolder extends RecyclerView.ViewHolder {

            TextView prod_name, id_of_agent;
            ImageView pr_image;
            LinearLayout prod_layout;

            public TypeHolder(View view) {
                super(view);
                prod_layout = (LinearLayout) view.findViewById(R.id.prod_layout);
                id_of_agent = (TextView) view.findViewById(R.id.id_of_agent);
                prod_name = (TextView) view.findViewById(R.id.prod_name);
                pr_image = (ImageView) view.findViewById(R.id.pr_image);
            }
        }
    }


}