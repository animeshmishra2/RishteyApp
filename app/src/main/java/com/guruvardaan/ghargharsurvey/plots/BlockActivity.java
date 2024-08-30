package com.guruvardaan.ghargharsurvey.plots;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_BLOCK_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_PROPERTY_URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.model.BlockModel;
import com.guruvardaan.ghargharsurvey.model.PropertyModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockActivity extends BaseActivity {

    TextView header_txt;
    RecyclerView block_recycler;
    ProgressBar progress;
    ImageView back_button;
    ArrayList<BlockModel> blockModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_block, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        header_txt = (TextView) findViewById(R.id.header_txt);
        progress = (ProgressBar) findViewById(R.id.progress);
        block_recycler = (RecyclerView) findViewById(R.id.block_recycler);
        blockModels = new ArrayList<>();
        block_recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        header_txt.setText(getIntent().getExtras().getString("PRO") + " Blocks");
        getAllBlocks();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getAllBlocks() {
        progress.setVisibility(View.VISIBLE);
        block_recycler.setVisibility(View.GONE);
        String URL_CHECK = GET_BLOCK_URL + getIntent().getExtras().getString("PR");
        System.out.println("Mahendra " + URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        block_recycler.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                blockModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    blockModels.add(new BlockModel(object.getString("property_block_name"), object.getString("totalPlots"), object.getString("pk_prm_block_id")));
                                }
                                block_recycler.setAdapter(new BlockAdapter());
                            } else {
                                Toast.makeText(getApplicationContext(), "No Blocks Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.setVisibility(View.GONE);
                        block_recycler.setVisibility(View.VISIBLE);
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

    class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockHolder> {

        @Override
        public int getItemCount() {
            return blockModels.size();
        }

        @Override
        public void onBindViewHolder(BlockAdapter.BlockHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.block_name.setText(blockModels.get(position).getProperty_block_name());
            holder.plot_numbers.setText("Block");
            // holder.plot_numbers.setText(blockModels.get(position).getTotalPlots() + " Plots");
            //holder.plot_numbers.setVisibility(View.INVISIBLE);
            holder.block_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), PlotsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("BID", blockModels.get(position).getProperty_block_name());
                    bundle.putString("PR", getIntent().getExtras().getString("PR"));
                    bundle.putString("PRN", getIntent().getExtras().getString("PRO"));
                    bundle.putString("BLOCK", blockModels.get(position).getPk_prm_block_id());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 29);
                }
            });
        }

        @Override
        public BlockAdapter.BlockHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.block_row, viewGroup, false);
            BlockAdapter.BlockHolder listHolder = new BlockAdapter.BlockHolder(mainGroup);
            return listHolder;
        }

        public class BlockHolder extends RecyclerView.ViewHolder {

            TextView block_name, plot_numbers;
            CardView block_card;

            public BlockHolder(View view) {
                super(view);
                plot_numbers = (TextView) view.findViewById(R.id.plot_numbers);
                block_name = (TextView) view.findViewById(R.id.block_name);
                block_card = (CardView) view.findViewById(R.id.block_card);
            }
        }
    }

}