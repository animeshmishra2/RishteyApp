package com.guruvardaan.ghargharsurvey.agent;

import static com.guruvardaan.ghargharsurvey.config.Config.AGENT_RECEIPT_URL;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.guruvardaan.ghargharsurvey.model.AgentReceiptModel;
import com.guruvardaan.ghargharsurvey.model.MyPlotRequestModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class AgentReceiptActivity extends BaseActivity {

    RecyclerView recyclerView;
    ArrayList<AgentReceiptModel> agentReceiptModels;
    ImageView back_button;
    ProgressBar progress;
    LinearLayout no_transaction;
    UserDetails userDetails;
    ReceiptAdapter adapter;
    String file_p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_agent_receipt, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        no_transaction = (LinearLayout) findViewById(R.id.no_transaction);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progress = (ProgressBar) findViewById(R.id.progress);
        back_button = (ImageView) findViewById(R.id.back_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        agentReceiptModels = new ArrayList<>();
        adapter = new ReceiptAdapter();
        recyclerView.setAdapter(adapter);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getReceipt(getPastDate(), getCurrentDate());
    }

    class ReceiptAdapter extends RecyclerView.Adapter {

        class Row1Holder extends RecyclerView.ViewHolder {

            EditText start_date, end_date;
            TextView submit_date;

            public Row1Holder(View itemView) {
                super(itemView);
                start_date = (EditText) itemView.findViewById(R.id.start_date);
                end_date = (EditText) itemView.findViewById(R.id.end_date);
                submit_date = (TextView) itemView.findViewById(R.id.submit_date);
            }
        }

        class Row2Holder extends RecyclerView.ViewHolder {

            TextView customer_name, receipt_type, plot_name, amount, date_txt, download, share;

            public Row2Holder(View view) {
                super(view);
                share = (TextView) view.findViewById(R.id.share);
                download = (TextView) view.findViewById(R.id.download);
                customer_name = (TextView) view.findViewById(R.id.customer_name);
                receipt_type = (TextView) view.findViewById(R.id.receipt_type);
                plot_name = (TextView) view.findViewById(R.id.plot_name);
                amount = (TextView) view.findViewById(R.id.amount);
                date_txt = (TextView) view.findViewById(R.id.date_txt);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;
            switch (viewType) {
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_row_1, parent, false);
                    return new ReceiptAdapter.Row1Holder(view);
                case 2:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_row, parent, false);
                    return new ReceiptAdapter.Row2Holder(view);
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {

            switch (agentReceiptModels.get(position).getType()) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                default:
                    return -1;
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int listPosition) {
            final AgentReceiptModel object = agentReceiptModels.get(listPosition);
            if (object != null) {
                switch (object.getType()) {
                    case 1:


                        break;
                    case 2:
                        ((Row2Holder) holder).customer_name.setText(object.getCustomerFirstName());
                        ((Row2Holder) holder).receipt_type.setText(object.getPdfType());
                        ((Row2Holder) holder).amount.setText("Paid Amount : " + object.getTransaction_amount());
                        ((Row2Holder) holder).plot_name.setText("Plot Name : " + object.getPlotNumber());
                        ((Row2Holder) holder).date_txt.setText(object.getIns_dt());
                        ((Row2Holder) holder).download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                downloadPDF(object.getUrl(), object.getPdfType(), "1");
                            }
                        });
                        ((Row2Holder) holder).share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                shareLink(object.getUrl(), object.getPdfType());
                                downloadPDF(object.getUrl(), object.getPdfType(), "2");
                            }
                        });
                        break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return agentReceiptModels.size();
        }
    }

    private void getReceipt(String start_date, String end_date) {
        no_transaction.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = AGENT_RECEIPT_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid();
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        System.out.println("Animeshwa " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            agentReceiptModels.clear();
                            //agentReceiptModels.add(new AgentReceiptModel(1, start_date, end_date, "", "", "", "", "", "", "", ""));
                            for (int i = 0; i < Table.length(); i++) {
                                JSONObject object = Table.getJSONObject(i);
                                agentReceiptModels.add(new AgentReceiptModel(2, start_date, end_date, object.getString("id"), object.getString("pdfType"), object.getString("showText"), object.getString("ins_dt"), object.getString("url"), object.getString("customerFirstName"), object.getString("plotNumber"), object.getString("transaction_amount")));
                            }
                            Collections.sort(agentReceiptModels, new Comparator<AgentReceiptModel>() {
                                @Override
                                public int compare(AgentReceiptModel p1, AgentReceiptModel p2) {
                                    return getIds(p1.getId()) - getIds(p2.getId()); // Ascending
                                }

                            });
                            Collections.reverse(agentReceiptModels);
                            if (agentReceiptModels.size() > 1) {
                                recyclerView.setVisibility(View.VISIBLE);
                                no_transaction.setVisibility(View.GONE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                no_transaction.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public String getCurrentDate() {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy");
        String newDateStr = postFormater.format(new Date());
        return newDateStr;
    }

    public String getPastDate() {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy");
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.add(Calendar.MONTH, -1);
        aCalendar.set(Calendar.DATE, 1);
        Date firstDateOfPreviousMonth = aCalendar.getTime();
        String newDateStr = postFormater.format(firstDateOfPreviousMonth);
        return newDateStr;
    }

    public void downloadPDF(String url, String title, String type) {
        file_p = "";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Downloading File, Please Wait...");
        request.setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        File f = new File(Environment.DIRECTORY_DOWNLOADS, fileName);
        file_p = f.getAbsolutePath();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        if (!f.exists()) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }

        if (type.equalsIgnoreCase("2")) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                    new IntentFilter("SharePDF"));
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            File outputFile = new File(file_p);
            Uri uri = FileProvider.getUriForFile(
                    AgentReceiptActivity.this,
                    "com.rishtey.agentapp.provider", //(use your app signature + ".provider" )
                    outputFile);
            System.out.println("Mishur " + uri);
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("*/*");
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(share, "share.."));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void shareLink(String rec, String type) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Rishtey");
            String shareMessage = "\nReceipt Generated By Application \n\n " + type + "\n\n";
            shareMessage = shareMessage + rec;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public int getIds(String s) {
        try {
            int n = Integer.parseInt(s);
            return n;
        } catch (Exception e) {
            return 0;
        }
    }
}