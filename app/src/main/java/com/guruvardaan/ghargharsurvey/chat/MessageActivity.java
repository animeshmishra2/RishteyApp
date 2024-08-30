package com.guruvardaan.ghargharsurvey.chat;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_COMPLAINT_DATA;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_DEPARTMENT_USERS;
import static com.guruvardaan.ghargharsurvey.config.Config.RESOLVE_COMPLAINT;
import static com.guruvardaan.ghargharsurvey.config.Config.SHIFT_TIME;
import static com.guruvardaan.ghargharsurvey.config.Config.TRANSFER_COMPLAINT;
import static com.guruvardaan.ghargharsurvey.config.Config.UPLOAD_RESPONSE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.guruvardaan.ghargharsurvey.chat.ui.MessageSwipeController;
import com.guruvardaan.ghargharsurvey.chat.ui.SwipeControllerActions;
import com.guruvardaan.ghargharsurvey.utils.DataPart;
import com.guruvardaan.ghargharsurvey.utils.UriToBitmapConverter;
import com.guruvardaan.ghargharsurvey.utils.UriToPathConverter;
import com.guruvardaan.ghargharsurvey.utils.VolleyMultipartRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.ResModel;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;

public class MessageActivity extends AppCompatActivity {

    RecyclerView chatlist;
    ImageView goBack, sendbtn, change_time;
    Bitmap bms;
    LinearLayout tabChat;
    EditText msgedittext;
    ProgressBar progress_bar;
    TextView username;
    ArrayList<ResModel> resModels;
    ArrayList<String> ch_ids;
    ChatAdapter chatAdapter;
    String rep_id = "";
    UserDetails userDetails;
    ImageView transfer, resolve, uploadimagebtn;
    ProgressDialog pd;
    String user_status = "";
    String team_status = "";
    int height;
    int width;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    TextView reply_name, reply_message;
    LinearLayout reply_lay;
    ImageView reply_image, reply_close;
    int ss_rep = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        change_time = (ImageView) findViewById(R.id.change_time);
        ss_rep = -1;
        reply_name = (TextView) findViewById(R.id.reply_name);
        reply_message = (TextView) findViewById(R.id.reply_message);
        reply_lay = (LinearLayout) findViewById(R.id.reply_lay);
        reply_image = (ImageView) findViewById(R.id.reply_image);
        reply_close = (ImageView) findViewById(R.id.reply_close);
        height = displayMetrics.heightPixels / 2;
        width = displayMetrics.widthPixels / 2;
        userDetails = new UserDetails(getApplicationContext());
        chatlist = (RecyclerView) findViewById(R.id.chatlist);
        transfer = (ImageView) findViewById(R.id.transfer);
        resolve = (ImageView) findViewById(R.id.resolve);
        uploadimagebtn = (ImageView) findViewById(R.id.uploadimagebtn);
        user_status = "";
        team_status = "";
        resModels = new ArrayList<>();
        rep_id = "";
        ch_ids = new ArrayList<>();
        goBack = (ImageView) findViewById(R.id.goBack);
        sendbtn = (ImageView) findViewById(R.id.sendbtn);
        tabChat = (LinearLayout) findViewById(R.id.tabChat);
        msgedittext = (EditText) findViewById(R.id.msgedittext);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        username = (TextView) findViewById(R.id.username);
        username.setText(getIntent().getExtras().getString("UNAME"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chatlist.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter();
        chatlist.setAdapter(chatAdapter);
        getComplaintData(getIntent().getExtras().getString("CID"));
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        uploadimagebtn.setVisibility(View.VISIBLE);

        uploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (checkAndRequestPermissions()) {
                        System.out.println("Hiii");
                        pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
                    }
                } else {
                    if (checkAndRequestPermissionsStorage()) {
                        FilePickerBuilder.getInstance().setMaxCount(1) //optional
                                .setActivityTheme(R.style.LibAppTheme) //optional
                                .pickPhoto(MessageActivity.this, 205);
                    }
                }
            }
        });

        msgedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (msgedittext.getText().toString().trim().length() <= 0) {
                    sendbtn.setImageResource(R.drawable.white_microphone);
                } else {
                    sendbtn.setImageResource(R.drawable.send_her);
                }
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TransferActivity.class);
                startActivityForResult(intent, 222);
            }
        });

        resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResolveDialog();
            }
        });


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (msgedittext.getText().toString().trim().length() <= 0) {
                    startVoiceRecognitionActivity();
                } else {
                    uploadBitmap(null, getIntent().getExtras().getString("CID"), msgedittext.getText().toString().trim(), "");
                    //sendUserResponse(msgedittext.getText().toString().trim(), getIntent().getExtras().getString("CID"));
                    msgedittext.setText("");
                }
            }
        });

        change_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(MessageActivity.this, R.style.MySpinnerDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        shitTime(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                        //dob.setText(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
                dpd.show();
            }
        });

        MessageSwipeController messageSwipeController = new MessageSwipeController(this, new SwipeControllerActions() {
            @Override
            public void showReplyUI(int position) {
                ss_rep = position;
                setReplyThread(position);
                //Toast.makeText(getApplicationContext(), "Replying " + position, Toast.LENGTH_SHORT).show();
                //quotedMessagePos = position;
                //showQuotedMessage(messageList.get(position));
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(messageSwipeController);
        itemTouchHelper.attachToRecyclerView(chatlist);
        reply_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ss_rep = -1;
                setReplyThread(-1);
            }
        });
        setReplyThread(-1);
    }

    public void setReplyThread(int ss) {
        if (ss > -1) {
            reply_lay.setVisibility(View.VISIBLE);
            if (resModels.get(ss).getResponse_type().equalsIgnoreCase("2")) {
                reply_message.setText(resModels.get(ss).getResponse().trim());
                if (resModels.get(ss).getReplier_id().equalsIgnoreCase(userDetails.getuserid())) {
                    reply_name.setText("You");
                } else {
                    reply_name.setText(resModels.get(ss).getRep_name());
                }
                if (resModels.get(ss).getImage_url().length() > 4) {
                    reply_image.setVisibility(View.VISIBLE);
                    reply_message.setText("Image");
                    Glide.with(getApplicationContext()).load("https://network.ghargharbazar.com/rishtey/api/" + resModels.get(ss).getImage_url()).apply(RequestOptions.bitmapTransform(new RoundedCorners(3))).into(reply_image);
                } else {
                    reply_image.setVisibility(View.GONE);
                }


            } else {
                if (resModels.get(ss).getImage_url().length() > 4) {
                    reply_image.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load("https://network.ghargharbazar.com/rishtey/api/" + resModels.get(ss).getImage_url()).apply(RequestOptions.bitmapTransform(new RoundedCorners(3))).into(reply_image);
                } else {
                    reply_image.setVisibility(View.GONE);
                }
                reply_name.setText(resModels.get(ss).getU_name());
                reply_message.setText(resModels.get(ss).getResponse().trim());
            }
        } else {
            reply_lay.setVisibility(View.GONE);
        }
    }

    public void startVoiceRecognitionActivity() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
            startActivityForResult(intent, 1234);
        } catch (ActivityNotFoundException e) {
            String appPackageName = "com.google.android.googlequicksearchbox";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
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
                msgedittext.setText(matches.get(0).trim());
            }
        }
        if (requestCode == 222 & resultCode == RESULT_OK) {
            showTransferDialog(data.getExtras().getString("ID"), data.getExtras().getString("NAME"));
        }
        if (requestCode == 205 && resultCode == RESULT_OK) {
            try {
                String paths = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0));
                Glide.with(getApplicationContext()).asBitmap().load(paths).apply(new RequestOptions().override(width, height)).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        bms = resource;
                        if (bms != null) {
                            uploadBitmap(bms, getIntent().getExtras().getString("CID"), "", paths);
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showTransferDialog(String id, String name) {
        final Dialog dialog = new Dialog(MessageActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transfer_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView messages = dialog.findViewById(R.id.messages);
        messages.setText("Do you want to transfer this complaint to " + name + "? After clicking on Yes this complaint will be transferred and you will not be able to process this complaint.");
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                transferComplaint(id);
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.no_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void showResolveDialog() {
        final Dialog dialog = new Dialog(MessageActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.resolve_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                resolveComplaint();
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.no_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void getComplaintData(String comp_id) {
        progress_bar.setVisibility(View.VISIBLE);
        String URL_CHECK = GET_COMPLAINT_DATA + "cp_id=" + comp_id;
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                progress_bar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            if (!ch_ids.contains(object.getString("id"))) {
                                ch_ids.add(object.getString("id"));
                                int pss = -1;
                                if (!object.getString("reply_thread_id").equalsIgnoreCase("-1")) {
                                    pss = findRepPosition(object.getString("reply_thread_id"));
                                    System.out.println("Animesh PS " + pss);
                                }
                                resModels.add(new ResModel(object.getString("id"), object.getString("complaint_id"), object.getString("type"), object.getString("response_type"), object.getString("user_id"), object.getString("replier_id"), object.getString("transfer_to"), object.getString("image_url"), object.getString("response"), object.getString("user_status"), object.getString("team_status"), object.getString("res_date"), object.getString("status"), object.getString("rep_name"), object.getString("U_name"), object.getString("reply_thread_id"), pss));
                            }
                            if (object.getString("team_status").equalsIgnoreCase("1")) {
                                team_status = "1";
                            }
                            if (object.getString("user_status").equalsIgnoreCase("1")) {
                                user_status = "1";
                            }
                            if (i == data.length() - 1) {
                                if (object.getString("transfer_to").equalsIgnoreCase("0")) {
                                    rep_id = object.getString("replier_id");
                                } else {
                                    rep_id = object.getString("transfer_to");
                                }
                            }
                        }
                        if (rep_id.equalsIgnoreCase(userDetails.getuserid())) {
                            tabChat.setVisibility(View.VISIBLE);
                            transfer.setVisibility(View.VISIBLE);
                            resolve.setVisibility(View.VISIBLE);
                        } else {
                            tabChat.setVisibility(View.GONE);
                            transfer.setVisibility(View.GONE);
                            resolve.setVisibility(View.GONE);
                        }

                        if (user_status.equalsIgnoreCase("1")) {
                            tabChat.setVisibility(View.GONE);
                            resolve.setVisibility(View.GONE);
                            transfer.setVisibility(View.GONE);
                        }
                        if (team_status.equalsIgnoreCase("1")) {
                            resolve.setVisibility(View.GONE);
                            transfer.setVisibility(View.GONE);
                        }
                        chatAdapter.notifyDataSetChanged();
                        chatlist.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView sender_name, sender_message, sender_time, your_name, your_message, your_time, transfer_chat, your_reply_name, your_reply_message, other_reply_name, other_reply_message;
            RecyclerView header_recycler;
            ImageView chat_img, chat_img_other, your_reply_image, other_reply_image;
            LinearLayout chat_other, chat_my, your_reply_lay, other_reply_lay;

            public MyViewHolder(View view) {
                super(view);
                sender_name = (TextView) view.findViewById(R.id.sender_name);
                your_reply_name = (TextView) view.findViewById(R.id.your_reply_name);
                other_reply_name = (TextView) view.findViewById(R.id.other_reply_name);
                your_reply_message = (TextView) view.findViewById(R.id.your_reply_message);
                other_reply_message = (TextView) view.findViewById(R.id.other_reply_message);
                chat_img_other = (ImageView) view.findViewById(R.id.chat_img_other);
                your_reply_image = (ImageView) view.findViewById(R.id.your_reply_image);
                other_reply_image = (ImageView) view.findViewById(R.id.other_reply_image);
                transfer_chat = (TextView) view.findViewById(R.id.transfer_chat);
                sender_message = (TextView) view.findViewById(R.id.sender_message);
                sender_time = (TextView) view.findViewById(R.id.sender_time);
                chat_img = (ImageView) view.findViewById(R.id.chat_img);
                header_recycler = (RecyclerView) view.findViewById(R.id.header_recycler);
                chat_other = (LinearLayout) view.findViewById(R.id.chat_other);
                chat_my = (LinearLayout) view.findViewById(R.id.chat_my);
                your_reply_lay = (LinearLayout) view.findViewById(R.id.your_reply_lay);
                other_reply_lay = (LinearLayout) view.findViewById(R.id.other_reply_lay);
                your_name = (TextView) view.findViewById(R.id.your_name);
                your_message = (TextView) view.findViewById(R.id.your_message);
                your_time = (TextView) view.findViewById(R.id.your_time);
            }
        }

        @Override
        public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row_other, parent, false);
            return new ChatAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ChatAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            if (resModels.get(position).getType().equalsIgnoreCase("1")) {
                holder.transfer_chat.setVisibility(View.VISIBLE);
                holder.transfer_chat.setText(resModels.get(position).getResponse());
                holder.chat_my.setVisibility(View.GONE);
                holder.chat_other.setVisibility(View.GONE);
            } else {
                holder.transfer_chat.setVisibility(View.GONE);
                if (resModels.get(position).getResponse_type().equalsIgnoreCase("2")) {
                    holder.chat_other.setVisibility(View.GONE);
                    holder.chat_my.setVisibility(View.VISIBLE);
                    if (resModels.get(position).getReplier_id().equalsIgnoreCase(userDetails.getuserid())) {
                        holder.your_name.setText("You");
                    } else {
                        holder.your_name.setText(resModels.get(position).getRep_name());
                    }
                    if (resModels.get(position).getImage_url().length() > 4) {
                        holder.chat_img.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load("https://network.ghargharbazar.com/rishtey/api/" + resModels.get(position).getImage_url()).apply(RequestOptions.bitmapTransform(new RoundedCorners(3))).into(holder.chat_img);
                    } else {
                        holder.chat_img.setVisibility(View.GONE);
                    }

                    holder.your_message.setText(resModels.get(position).getResponse().trim());
                    holder.your_time.setText(convertMillisToDateString(getMills(resModels.get(position).getRes_date())));

                    if (resModels.get(position).getRep_pos() > -1) {
                        if (resModels.get(resModels.get(position).getRep_pos()).getImage_url().length() > 4) {
                            holder.your_reply_image.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load("https://network.ghargharbazar.com/rishtey/api/" + resModels.get(resModels.get(position).getRep_pos()).getImage_url()).apply(RequestOptions.bitmapTransform(new RoundedCorners(3))).into(holder.your_reply_image);
                        } else {
                            holder.your_reply_image.setVisibility(View.GONE);
                        }
                        holder.your_reply_lay.setVisibility(View.VISIBLE);
                        holder.your_reply_message.setText(resModels.get(resModels.get(position).getRep_pos()).getResponse());
                        if (resModels.get(resModels.get(position).getRep_pos()).getResponse_type().equalsIgnoreCase("2")) {
                            if (resModels.get(resModels.get(position).getRep_pos()).getReplier_id().equalsIgnoreCase(userDetails.getuserid())) {
                                holder.your_reply_name.setText("You");
                            } else {
                                holder.your_reply_name.setText(resModels.get(resModels.get(position).getRep_pos()).getRep_name());
                            }
                        } else {
                            holder.your_reply_name.setText(resModels.get(resModels.get(position).getRep_pos()).getU_name());
                        }
                    } else {
                        holder.your_reply_lay.setVisibility(View.GONE);
                    }
                } else {
                    holder.chat_other.setVisibility(View.VISIBLE);
                    holder.chat_my.setVisibility(View.GONE);
                    if (resModels.get(position).getImage_url().length() > 4) {
                        holder.chat_img_other.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load("https://network.ghargharbazar.com/rishtey/api/" + resModels.get(position).getImage_url()).apply(RequestOptions.bitmapTransform(new RoundedCorners(3))).into(holder.chat_img_other);
                    } else {
                        holder.chat_img_other.setVisibility(View.GONE);
                    }
                    holder.sender_name.setText(resModels.get(position).getU_name());
                    holder.sender_message.setText(resModels.get(position).getResponse().trim());
                    holder.sender_time.setText(convertMillisToDateString(getMills(resModels.get(position).getRes_date())));


                    if (resModels.get(position).getRep_pos() > -1) {
                        if (resModels.get(resModels.get(position).getRep_pos()).getImage_url().length() > 4) {
                            holder.other_reply_image.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load("https://network.ghargharbazar.com/rishtey/api/" + resModels.get(resModels.get(position).getRep_pos()).getImage_url()).apply(RequestOptions.bitmapTransform(new RoundedCorners(3))).into(holder.other_reply_image);
                        } else {
                            holder.other_reply_image.setVisibility(View.GONE);
                        }
                        holder.other_reply_lay.setVisibility(View.VISIBLE);
                        holder.other_reply_message.setText(resModels.get(resModels.get(position).getRep_pos()).getResponse());
                        if (resModels.get(resModels.get(position).getRep_pos()).getResponse_type().equalsIgnoreCase("2")) {
                            if (resModels.get(resModels.get(position).getRep_pos()).getReplier_id().equalsIgnoreCase(userDetails.getuserid())) {
                                holder.other_reply_name.setText("You");
                            } else {
                                holder.other_reply_name.setText(resModels.get(resModels.get(position).getRep_pos()).getRep_name());
                            }
                        } else {
                            holder.other_reply_name.setText(resModels.get(resModels.get(position).getRep_pos()).getU_name());
                        }
                    } else {
                        holder.other_reply_lay.setVisibility(View.GONE);
                    }

                }


                holder.chat_img_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ViewChatImageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("IMG", "https://network.ghargharbazar.com/rishtey/api/" + resModels.get(position).getImage_url());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                holder.chat_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ViewChatImageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("IMG", "https://network.ghargharbazar.com/rishtey/api/" + resModels.get(position).getImage_url());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            holder.other_reply_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatlist.smoothScrollToPosition(resModels.get(position).getRep_pos());
                }
            });
            holder.your_reply_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatlist.smoothScrollToPosition(resModels.get(position).getRep_pos());
                }
            });
        }

        @Override
        public int getItemCount() {
            return resModels.size();
        }
    }

    public long getMills(String s) {
        try {
            long l = Long.parseLong(s);
            return l;
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    private String convertMillisToDateString(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy hh:mm a", Locale.getDefault());
        Date date = new Date(millis);
        return dateFormat.format(date);
    }

    private void uploadBitmap(final Bitmap bitmap1, String comp_ids, String msg, String image_path) {
        pd = ProgressDialog.show(MessageActivity.this, "Please Wait...");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPLOAD_RESPONSE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                pd.dismiss();
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    System.out.println("Hello " + json);
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                            getComplaintData(comp_ids);
                            ss_rep = -1;
                            setReplyThread(-1);
                        } else {
                            Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("complaint_id", comp_ids);
                params.put("response_type", "2");
                params.put("replier_id", rep_id);
                params.put("chat_time", "" + System.currentTimeMillis());
                if (ss_rep > -1) {
                    params.put("reply_thread_id", "" + resModels.get(ss_rep).getId());
                } else {
                    params.put("reply_thread_id", "" + ss_rep);
                }
                try {
                    params.put("response", URLEncoder.encode(msg, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    params.put("response", msg);
                }
                System.out.println("What is MSG " + msg);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (bitmap1 != null) {
                    params.put("image", new DataPart(image_path, getFileDataFromDrawable(bitmap1)));
                }
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


    private void transferComplaint(String tr_id) {
        pd = ProgressDialog.show(MessageActivity.this, "Please Wait...");
        String URL_CHECK = TRANSFER_COMPLAINT + getIntent().getExtras().getString("CID") + "&replier_id=" + userDetails.getuserid() + "&transfer_to=" + tr_id + "&chat_time=" + System.currentTimeMillis();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), "Complaint Transferred", Toast.LENGTH_SHORT).show();
                        getComplaintData(getIntent().getExtras().getString("CID"));
                    } else {
                        Toast.makeText(getApplicationContext(), "Transfer Can Not Be Completed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Transfer Can Not Be Completed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void shitTime(long shift_mil) {
        pd = ProgressDialog.show(MessageActivity.this, "Please Wait...");
        String URL_CHECK = SHIFT_TIME + getIntent().getExtras().getString("CID") + "&userid=" + userDetails.getuserid() + "&chat_time=" + shift_mil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), "Complaint Shifted", Toast.LENGTH_SHORT).show();
                        getComplaintData(getIntent().getExtras().getString("CID"));
                    } else {
                        Toast.makeText(getApplicationContext(), "Shifting Can Not Be Completed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Shifting Can Not Be Completed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void resolveComplaint() {
        pd = ProgressDialog.show(MessageActivity.this, "Please Wait...");
        String URL_CHECK = RESOLVE_COMPLAINT + getIntent().getExtras().getString("CID") + "&replier_id=" + userDetails.getuserid() + "&resolve_by=2&chat_time=" + System.currentTimeMillis();

        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), "Complaint Resolved By You", Toast.LENGTH_SHORT).show();
                        getComplaintData(getIntent().getExtras().getString("CID"));
                    } else {
                        Toast.makeText(getApplicationContext(), "Complaint Can Not Be Resolved", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Complaint Can Not Be Resolved", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            System.out.println("Animesh " + UriToBitmapConverter.uriToBitmap(getApplicationContext(), uri));
            uploadBitmap(UriToBitmapConverter.uriToBitmap(getApplicationContext(), uri), getIntent().getExtras().getString("CID"), "", UriToPathConverter.getPathFromUri(getApplicationContext(), uri));
            //profile_image.setImageBitmap(UriToBitmapConverter.uriToBitmap(getApplicationContext(), uri));
        } else {
            Toast.makeText(getApplicationContext(), "No Valid Image Selected", Toast.LENGTH_SHORT).show();
        }
    });

    private boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (listPermissionsNeeded.size() > 0) {
            System.out.println("Animesh " + listPermissionsNeeded.get(0));
            ActivityCompat.requestPermissions(MessageActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2011);
            return false;
        } else {
            return true;
        }
    }


    private boolean checkAndRequestPermissionsStorage() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (listPermissionsNeeded.size() > 0) {
            System.out.println("Animesh " + listPermissionsNeeded.get(0));
            ActivityCompat.requestPermissions(MessageActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2011);
            return false;
        } else {
            return true;
        }
    }


    public long getForDate(String tt) {
        try {
            tt = tt + " 00:00:01";
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = format1.parse(tt);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public int findRepPosition(String r_id) {
        int pss = -1;
        for (int i = 0; i < resModels.size(); i++) {
            if (resModels.get(i).getId().equalsIgnoreCase(r_id)) {
                pss = i;
                break;
            }
        }
        return pss;
    }

}