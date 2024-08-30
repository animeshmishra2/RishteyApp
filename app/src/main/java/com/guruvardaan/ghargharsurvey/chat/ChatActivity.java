package com.guruvardaan.ghargharsurvey.chat;

import static com.guruvardaan.ghargharsurvey.config.Config.CHAT_HEADERS;
import static com.guruvardaan.ghargharsurvey.config.Config.CREATE_COMPLAINT;
import static com.guruvardaan.ghargharsurvey.config.Config.DATA_COLLECTION;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_COMPLAINT_DATA;
import static com.guruvardaan.ghargharsurvey.config.Config.RESOLVE_COMPLAINT;
import static com.guruvardaan.ghargharsurvey.config.Config.SELF_CUST_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.SUB_HEADERS;
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
import com.google.mlkit.vision.text.Text;
import com.guruvardaan.ghargharsurvey.chat.ui.MessageSwipeController;
import com.guruvardaan.ghargharsurvey.chat.ui.SwipeControllerActions;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.DataModel;
import com.guruvardaan.ghargharsurvey.model.HeaderModel;
import com.guruvardaan.ghargharsurvey.model.NewChatModel;
import com.guruvardaan.ghargharsurvey.model.PlotCustomer;
import com.guruvardaan.ghargharsurvey.model.ResModel;
import com.guruvardaan.ghargharsurvey.model.SubHeaderModel;
import com.guruvardaan.ghargharsurvey.utils.DataPart;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.utils.UriToBitmapConverter;
import com.guruvardaan.ghargharsurvey.utils.UriToPathConverter;
import com.guruvardaan.ghargharsurvey.utils.VolleyMultipartRequest;
import com.guruvardaan.ghargharsurvey.visits.VisitMapActivity;
import com.guruvardaan.ghargharsurvey.welcome.ProfileActivity;
import com.guruvardaan.ghargharsurvey.welcome.TeamList;
import com.rishtey.agentapp.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;

public class ChatActivity extends AppCompatActivity {

    LinearLayout tabChat;
    ImageView sendbtn, goBack, uploadimagebtn;
    RecyclerView chatlist, new_chat;
    ArrayList<NewChatModel> newChatModels;
    UserDetails userDetails;
    String chat_name = "";
    ArrayList<HeaderModel> headerModels;
    ArrayList<SubHeaderModel> subHeaderModels;
    ArrayList<DataModel> dataModels;
    ProgressDialog pd;
    ProgressBar progress_bar;
    NewChatAdapter newChatAdapter;
    ChatAdapter chatAdapter;
    Bitmap bms;
    EditText msgedittext;
    String selected_header, selected_sub_header;
    String cc_created;
    ArrayList<ResModel> resModels;
    ArrayList<String> ch_ids;
    String cc_id = "";
    String rep_id = "";
    String team_status = "";
    String user_status = "";
    ImageView resolve;

    int height;
    int width;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    TextView reply_name, reply_message;
    LinearLayout reply_lay;
    ImageView reply_image, reply_close;
    int ss_rep = -1;
    String priority = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ss_rep = -1;
        reply_name = (TextView) findViewById(R.id.reply_name);
        reply_message = (TextView) findViewById(R.id.reply_message);
        reply_lay = (LinearLayout) findViewById(R.id.reply_lay);
        reply_image = (ImageView) findViewById(R.id.reply_image);
        reply_close = (ImageView) findViewById(R.id.reply_close);
        chat_name = "Rishtey AI";
        resolve = (ImageView) findViewById(R.id.resolve);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels / 2;
        width = displayMetrics.widthPixels / 2;
        ch_ids = new ArrayList<>();
        dataModels = new ArrayList<>();
        cc_id = "";
        team_status = "0";
        user_status = "0";
        cc_created = "0";
        uploadimagebtn = (ImageView) findViewById(R.id.uploadimagebtn);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        msgedittext = (EditText) findViewById(R.id.msgedittext);
        resModels = new ArrayList<>();
        goBack = (ImageView) findViewById(R.id.goBack);
        userDetails = new UserDetails(getApplicationContext());
        newChatModels = new ArrayList<>();
        headerModels = new ArrayList<>();
        subHeaderModels = new ArrayList<>();
        chatlist = (RecyclerView) findViewById(R.id.chatlist);
        new_chat = (RecyclerView) findViewById(R.id.new_chat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        new_chat.setLayoutManager(linearLayoutManager);
        tabChat = (LinearLayout) findViewById(R.id.tabChat);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setStackFromEnd(true);
        chatlist.setLayoutManager(linearLayoutManager2);
        sendbtn = (ImageView) findViewById(R.id.sendbtn);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        newChatAdapter = new NewChatAdapter();
        new_chat.setAdapter(newChatAdapter);
        chatAdapter = new ChatAdapter();
        chatlist.setAdapter(chatAdapter);
        if (getIntent().getExtras().getString("NEW").equalsIgnoreCase("1")) {
            new_chat.setVisibility(View.VISIBLE);
            chatlist.setVisibility(View.INVISIBLE);
            progress_bar.setVisibility(View.GONE);
            tabChat.setVisibility(View.GONE);
            setData();
        } else {
            new_chat.setVisibility(View.INVISIBLE);
            chatlist.setVisibility(View.VISIBLE);
            cc_id = getIntent().getExtras().getString("CID");
            getComplaintData(getIntent().getExtras().getString("CID"));
        }
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
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new_chat.getVisibility() == View.VISIBLE) {
                    if (msgedittext.getText().toString().trim().length() <= 0) {
                        startVoiceRecognitionActivity();
                    } else {
                        newChatModels.add(new NewChatModel("1", chat_name, userDetails.getName(), "2", msgedittext.getText().toString().trim(), "1", System.currentTimeMillis(), new ArrayList<>(), new ArrayList<>()));
                        newChatAdapter.notifyDataSetChanged();
                        new_chat.smoothScrollToPosition(newChatAdapter.getItemCount() - 1);
                        createComplaint(msgedittext.getText().toString().trim());
                        msgedittext.setText("");
                        new_chat.setVisibility(View.GONE);
                        chatlist.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (msgedittext.getText().toString().trim().length() <= 0) {
                        startVoiceRecognitionActivity();
                    } else {
                        uploadBitmap(null, cc_id, msgedittext.getText().toString().trim(), "");
                        //sendUserResponse(msgedittext.getText().toString().trim(), cc_id);
                        msgedittext.setText("");
                    }
                }
            }
        });

        resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResolveDialog();
            }
        });

        uploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (checkAndRequestPermissions()) {
                        System.out.println("Hiii");
                        pickMedia.launch(new PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                .build());
                    }
                } else {
                    if (checkAndRequestPermissionsStorage()) {
                        FilePickerBuilder.getInstance()
                                .setMaxCount(1) //optional
                                .setActivityTheme(R.style.LibAppTheme) //optional
                                .pickPhoto(ChatActivity.this, 205);
                    }
                }
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


    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    System.out.println("Animesh " + UriToBitmapConverter.uriToBitmap(getApplicationContext(), uri));
                    uploadBitmap(UriToBitmapConverter.uriToBitmap(getApplicationContext(), uri), cc_id, "", UriToPathConverter.getPathFromUri(getApplicationContext(), uri));
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
            ActivityCompat.requestPermissions(ChatActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2011);
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
            ActivityCompat.requestPermissions(ChatActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2011);
            return false;
        } else {
            return true;
        }
    }

    public void setData() {
        newChatModels.clear();
        newChatModels.add(new NewChatModel("1", chat_name, userDetails.getName(), "1", "Hi, Welcome to Rishtey AI Chat Support", "1", System.currentTimeMillis(), new ArrayList<>(), new ArrayList<>()));
        newChatAdapter.notifyDataSetChanged();
        getAllHeaders();
    }

    private void getAllHeaders() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CHAT_HEADERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        headerModels.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    headerModels.add(new HeaderModel(object.getString("id"), object.getString("header_name"), object.getString("header_description"), object.getString("status"), object.getString("department_id"), object.getString("DepartmentName"), "-1"));
                                }
                                newChatModels.add(new NewChatModel("1", chat_name, userDetails.getName(), "1", "कृपया वो प्रकार चुने जिसके सम्बंधित आपको सहायता चाहिए", "2", System.currentTimeMillis(), headerModels, new ArrayList<>()));
                                newChatAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    class NewChatAdapter extends RecyclerView.Adapter<NewChatAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView sender_name, sender_message, sender_time, your_name, your_message, your_time, transfer_chat;
            RecyclerView header_recycler, sub_header_recycler;
            ImageView chat_img, chat_img_other;
            LinearLayout chat_other, chat_my, other_reply_lay, your_reply_lay;

            public MyViewHolder(View view) {
                super(view);
                sender_name = (TextView) view.findViewById(R.id.sender_name);
                transfer_chat = (TextView) view.findViewById(R.id.transfer_chat);
                sender_message = (TextView) view.findViewById(R.id.sender_message);
                sender_time = (TextView) view.findViewById(R.id.sender_time);
                your_reply_lay = (LinearLayout) view.findViewById(R.id.your_reply_lay);
                other_reply_lay = (LinearLayout) view.findViewById(R.id.other_reply_lay);
                header_recycler = (RecyclerView) view.findViewById(R.id.header_recycler);
                sub_header_recycler = (RecyclerView) view.findViewById(R.id.sub_header_recycler);
                chat_other = (LinearLayout) view.findViewById(R.id.chat_other);
                chat_img = (ImageView) view.findViewById(R.id.chat_img);
                chat_img_other = (ImageView) view.findViewById(R.id.chat_img_other);
                chat_my = (LinearLayout) view.findViewById(R.id.chat_my);
                your_name = (TextView) view.findViewById(R.id.your_name);
                your_message = (TextView) view.findViewById(R.id.your_message);
                your_time = (TextView) view.findViewById(R.id.your_time);
            }
        }

        @Override
        public NewChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row_other, parent, false);
            return new NewChatAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NewChatAdapter.MyViewHolder holder, final int position) {
            holder.chat_img.setVisibility(View.GONE);
            holder.chat_img_other.setVisibility(View.GONE);
            holder.your_reply_lay.setVisibility(View.GONE);
            holder.other_reply_lay.setVisibility(View.GONE);
            if (newChatModels.get(position).getType().equalsIgnoreCase("1")) {
                holder.transfer_chat.setVisibility(View.GONE);
                holder.chat_other.setVisibility(View.VISIBLE);
                holder.chat_my.setVisibility(View.GONE);
                holder.sender_name.setText(newChatModels.get(position).getFirst_name());
                holder.sender_message.setText(newChatModels.get(position).getMessage().trim());
                holder.sender_time.setText(convertMillisToDateString(newChatModels.get(position).getTimes()));
                if (newChatModels.get(position).getShow().equalsIgnoreCase("1")) {
                    holder.header_recycler.setVisibility(View.GONE);
                    holder.sub_header_recycler.setVisibility(View.GONE);
                } else if (newChatModels.get(position).getShow().equalsIgnoreCase("3")) {
                    holder.header_recycler.setVisibility(View.GONE);
                    holder.sub_header_recycler.setVisibility(View.VISIBLE);
                    holder.sub_header_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    holder.sub_header_recycler.setAdapter(new SubHeaderAdapter());
                } else {
                    holder.header_recycler.setVisibility(View.VISIBLE);
                    holder.header_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    holder.header_recycler.setAdapter(new HeaderAdapter());
                }
            } else {
                holder.transfer_chat.setVisibility(View.GONE);
                holder.chat_other.setVisibility(View.GONE);
                holder.chat_my.setVisibility(View.VISIBLE);
                holder.your_name.setText(newChatModels.get(position).getSecond_name());
                holder.your_message.setText(newChatModels.get(position).getMessage().trim());
                holder.your_time.setText(convertMillisToDateString(newChatModels.get(position).getTimes()));
            }
        }

        @Override
        public int getItemCount() {
            return newChatModels.size();
        }
    }

    private String convertMillisToDateString(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy hh:mm a", Locale.getDefault());
        Date date = new Date(millis);
        return dateFormat.format(date);
    }

    class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView header_name, header_description, department_name;
            LinearLayout header_lay;

            public MyViewHolder(View view) {
                super(view);
                header_name = (TextView) view.findViewById(R.id.header_name);
                header_description = (TextView) view.findViewById(R.id.header_description);
                department_name = (TextView) view.findViewById(R.id.department_name);
                header_lay = (LinearLayout) view.findViewById(R.id.header_lay);
            }
        }

        @Override
        public HeaderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_row, parent, false);
            return new HeaderAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(HeaderAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.header_name.setText(headerModels.get(position).getHeader_name());
            holder.header_description.setText(headerModels.get(position).getHeader_description());
            holder.department_name.setText(headerModels.get(position).getDepartment_name());
            holder.header_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected_header == null) {
                        selected_header = headerModels.get(position).getId();
                        newChatModels.add(new NewChatModel("1", chat_name, userDetails.getName(), "2", headerModels.get(position).getHeader_name() + "\n" + headerModels.get(position).getHeader_description(), "1", System.currentTimeMillis(), new ArrayList<>(), new ArrayList<>()));
                        newChatModels.add(new NewChatModel("1", chat_name, userDetails.getName(), "1", "अब किस हैडर से सम्बंधित समस्या है , उसे सेलेक्ट करें", "1", System.currentTimeMillis(), new ArrayList<>(), new ArrayList<>()));
                        getAllSubHeaders(headerModels.get(position).getId());

                    } else {
                        Toast.makeText(getApplicationContext(), "You Already Selected Header", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return headerModels.size();
        }
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
        if (requestCode == 205 && resultCode == RESULT_OK) {
            try {
                String paths = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), (Uri) data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0));
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(paths)
                        .apply(new RequestOptions().override(width, height))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                bms = resource;
                                if (bms != null) {
                                    uploadBitmap(bms, cc_id, "", paths);
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


    private void createComplaint(String msg) {
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String URL_CHECK = CREATE_COMPLAINT + "name=" + userDetails.getName() + "&userid=" + userDetails.getuserid() + "&firebase_token=" + userDetails.getFcm_msg() + "&msg=" + msg + "&description=Send From User&header_id=" + selected_header + "&chat_time=" + System.currentTimeMillis() + "&priority=" + priority;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        headerModels.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                                getComplaintData(jsonObject.getString("comp_id"));
                                cc_id = jsonObject.getString("comp_id");
                                uploadimagebtn.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void getComplaintData(String comp_id) {
        progress_bar.setVisibility(View.VISIBLE);
        String URL_CHECK = GET_COMPLAINT_DATA + "cp_id=" + comp_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
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
                                chatAdapter.notifyDataSetChanged();
                                chatlist.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                                if (user_status.equalsIgnoreCase("1")) {
                                    tabChat.setVisibility(View.GONE);
                                }
                                if (team_status.equalsIgnoreCase("1")) {
                                    if (user_status.equalsIgnoreCase("1")) {
                                        resolve.setVisibility(View.GONE);
                                    } else {
                                        resolve.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    resolve.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
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
            if (resModels.get(position).getType().equalsIgnoreCase("1")) {
                holder.transfer_chat.setVisibility(View.VISIBLE);
                holder.transfer_chat.setText(resModels.get(position).getResponse());
                holder.chat_my.setVisibility(View.GONE);
                holder.chat_other.setVisibility(View.GONE);
            } else {
                holder.transfer_chat.setVisibility(View.GONE);
                if (resModels.get(position).getResponse_type().equalsIgnoreCase("1")) {
                    holder.chat_other.setVisibility(View.GONE);
                    holder.chat_my.setVisibility(View.VISIBLE);
                    holder.your_name.setText("You");
                    holder.your_message.setText(resModels.get(position).getResponse().trim());
                    holder.your_time.setText(convertMillisToDateString(getMills(resModels.get(position).getRes_date())));
                    if (resModels.get(position).getImage_url().length() > 4) {
                        holder.chat_img.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext())
                                .load("https://network.ghargharbazar.com/rishtey/api/" + resModels.get(position).getImage_url())
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(3)))
                                .into(holder.chat_img);
                    } else {
                        holder.chat_img.setVisibility(View.GONE);
                    }

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
                        Glide.with(getApplicationContext())
                                .load("https://network.ghargharbazar.com/rishtey/api/" + resModels.get(position).getImage_url())
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(3)))
                                .into(holder.chat_img_other);
                    } else {
                        holder.chat_img_other.setVisibility(View.GONE);
                    }
                    holder.chat_other.setVisibility(View.VISIBLE);
                    holder.chat_my.setVisibility(View.GONE);
                    holder.sender_name.setText(resModels.get(position).getRep_name());
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

/*
    private void sendUserResponse(String msg, String comp_ids) {
        String URL_CHECK = UPLOAD_RESPONSE + "complaint_id=" + comp_ids + "&response_type=1&replier_id=" + rep_id + "&chat_time=" + System.currentTimeMillis() + "&response=" + msg;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                                getComplaintData(comp_ids);
                            } else {
                                Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
*/

    public void showResolveDialog() {
        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.resolve_dialog);
        TextView tv = dialog.findViewById(R.id.messages);
        tv.setText("This complaint will be marked resolved by you.Now you can not send messages to this chat as it has been marked as resolved.");
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

    private void resolveComplaint() {
        pd = ProgressDialog.show(ChatActivity.this, "Please Wait...");
        String URL_CHECK = RESOLVE_COMPLAINT + cc_id + "&replier_id=" + rep_id + "&resolve_by=1&chat_time=" + System.currentTimeMillis();
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


    private void uploadBitmap(final Bitmap bitmap1, String comp_ids, String msg, String image_path) {
        pd = ProgressDialog.show(ChatActivity.this, "Please Wait...");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPLOAD_RESPONSE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pd.dismiss();
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                                    ss_rep = -1;
                                    setReplyThread(-1);
                                    getComplaintData(comp_ids);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Message Failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("complaint_id", comp_ids);
                params.put("response_type", "1");
                params.put("replier_id", rep_id);
                if (ss_rep > -1) {
                    params.put("reply_thread_id", "" + resModels.get(ss_rep).getId());
                } else {
                    params.put("reply_thread_id", "" + ss_rep);
                }
                params.put("chat_time", "" + System.currentTimeMillis());
                try {
                    params.put("response", URLEncoder.encode(msg, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    params.put("response", msg);
                }
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
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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

    private void getAllSubHeaders(String hid) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SUB_HEADERS + hid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                subHeaderModels.clear();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    subHeaderModels.add(new SubHeaderModel(object.getString("id"), object.getString("header_id"), object.getString("sub_head_name"), object.getString("priority"), object.getString("status")));
                                }
                                subHeaderModels.add(new SubHeaderModel("-1", selected_header, "Other Issue", "0", ""));
                                newChatModels.add(new NewChatModel("1", chat_name, userDetails.getName(), "1", "कृपया वो प्रकार चुने जिसके सम्बंधित आपको सहायता चाहिए", "3", System.currentTimeMillis(), new ArrayList<>(), subHeaderModels));
                                newChatAdapter.notifyDataSetChanged();
                                new_chat.smoothScrollToPosition(newChatAdapter.getItemCount() - 1);

                                /*tabChat.setVisibility(View.VISIBLE);
                                uploadimagebtn.setVisibility(View.GONE);
                                selected_header = headerModels.get(position).getId();
                                newChatAdapter.notifyDataSetChanged();
                                new_chat.smoothScrollToPosition(newChatAdapter.getItemCount() - 1);*/
                            }
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    class SubHeaderAdapter extends RecyclerView.Adapter<SubHeaderAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView header_name, header_description, department_name;
            LinearLayout header_lay;

            public MyViewHolder(View view) {
                super(view);
                header_name = (TextView) view.findViewById(R.id.header_name);
                header_description = (TextView) view.findViewById(R.id.header_description);
                department_name = (TextView) view.findViewById(R.id.department_name);
                header_lay = (LinearLayout) view.findViewById(R.id.header_lay);
            }
        }

        @Override
        public SubHeaderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_row, parent, false);
            return new SubHeaderAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SubHeaderAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.header_name.setText(subHeaderModels.get(position).getSub_head_name());
            if (subHeaderModels.get(position).getPriority().equalsIgnoreCase("1")) {
                holder.header_description.setText("High Priority");
            } else {
                holder.header_description.setText("Medium Priority");
            }
            if (subHeaderModels.get(position).getStatus().equalsIgnoreCase("1")) {
                holder.department_name.setText("Active");
            } else {
                holder.department_name.setText("Inactive");
            }
            holder.header_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tabChat.getVisibility() != View.VISIBLE) {
                        selected_sub_header = subHeaderModels.get(position).getId();
                        priority = subHeaderModels.get(position).getPriority();
                        if (!subHeaderModels.get(position).getId().equalsIgnoreCase("-1")) {
                            getAllData(subHeaderModels.get(position).getId(), subHeaderModels.get(position).getSub_head_name());
                        } else {
                            tabChat.setVisibility(View.VISIBLE);
                            uploadimagebtn.setVisibility(View.GONE);
                            newChatModels.add(new NewChatModel("1", chat_name, userDetails.getName(), "1", "अब आपको जो समस्या है वो समस्या लिखें तथा भेजें", "1", System.currentTimeMillis(), new ArrayList<>(), new ArrayList<>()));
                            tabChat.setVisibility(View.VISIBLE);
                            uploadimagebtn.setVisibility(View.GONE);
                            newChatAdapter.notifyDataSetChanged();
                            new_chat.smoothScrollToPosition(newChatAdapter.getItemCount() - 1);
                        }
                        /*newChatModels.add(new NewChatModel("1", chat_name, userDetails.getName(), "2", headerModels.get(position).getHeader_name() + "\n" + headerModels.get(position).getHeader_description(), "1", System.currentTimeMillis(), new ArrayList<>(), new ArrayList<>()));
                        newChatModels.add(new NewChatModel("1", chat_name, userDetails.getName(), "1", "अब आपको जो समस्या है वो समस्या लिखें तथा भेजें", "1", System.currentTimeMillis(), new ArrayList<>(), new ArrayList<>()));
                        tabChat.setVisibility(View.VISIBLE);
                        uploadimagebtn.setVisibility(View.GONE);
                        selected_header = headerModels.get(position).getId();
                        newChatAdapter.notifyDataSetChanged();
                        new_chat.smoothScrollToPosition(newChatAdapter.getItemCount() - 1);*/
                    } else {
                        Toast.makeText(getApplicationContext(), "You Already Selected Sub Header", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return subHeaderModels.size();
        }
    }

    private void getAllData(String sub_id, String s_name) {
        pd = ProgressDialog.show(ChatActivity.this, "Please Wait...");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DATA_COLLECTION + sub_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                dataModels.clear();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    dataModels.add(new DataModel(object.getString("id"), object.getString("name"), object.getString("sub_head_id"), object.getString("status"), ""));
                                }
                                if (dataModels.size() > 0) {
                                    showDataDialog(s_name);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void showDataDialog(String sub_h_name) {
        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.data_value_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView value_txt_1 = dialog.findViewById(R.id.value_txt_1);
        TextView value_txt_2 = dialog.findViewById(R.id.value_txt_2);
        TextView value_txt_3 = dialog.findViewById(R.id.value_txt_3);
        TextView value_txt_4 = dialog.findViewById(R.id.value_txt_4);
        EditText value_value_1 = dialog.findViewById(R.id.value_value_1);
        EditText value_value_2 = dialog.findViewById(R.id.value_value_2);
        EditText value_value_3 = dialog.findViewById(R.id.value_value_3);
        EditText value_value_4 = dialog.findViewById(R.id.value_value_4);
        for (int i = 0; i < dataModels.size(); i++) {
            if (i == 0) {
                value_value_1.setVisibility(View.VISIBLE);
                value_txt_1.setVisibility(View.VISIBLE);
                value_value_1.setHint("Enter " + dataModels.get(i).getName());
                value_txt_1.setText(dataModels.get(i).getName());
            }
            if (i == 1) {
                value_value_2.setVisibility(View.VISIBLE);
                value_txt_2.setVisibility(View.VISIBLE);
                value_value_2.setHint("Enter " + dataModels.get(i).getName());
                value_txt_2.setText(dataModels.get(i).getName());
            }
            if (i == 2) {
                value_value_3.setVisibility(View.VISIBLE);
                value_txt_3.setVisibility(View.VISIBLE);
                value_value_3.setHint("Enter " + dataModels.get(i).getName());
                value_txt_3.setText(dataModels.get(i).getName());
            }
            if (i == 3) {
                value_value_4.setVisibility(View.VISIBLE);
                value_txt_4.setVisibility(View.VISIBLE);
                value_value_4.setHint("Enter " + dataModels.get(i).getName());
                value_txt_4.setText(dataModels.get(i).getName());
            }
        }
        if (dataModels.size() == 1) {
            value_txt_1.setVisibility(View.VISIBLE);
            value_value_1.setVisibility(View.VISIBLE);
            value_txt_2.setVisibility(View.GONE);
            value_value_2.setVisibility(View.GONE);
            value_txt_3.setVisibility(View.GONE);
            value_value_3.setVisibility(View.GONE);
            value_txt_4.setVisibility(View.GONE);
            value_value_4.setVisibility(View.GONE);
        }
        if (dataModels.size() == 2) {
            value_txt_1.setVisibility(View.VISIBLE);
            value_value_1.setVisibility(View.VISIBLE);
            value_txt_2.setVisibility(View.VISIBLE);
            value_value_2.setVisibility(View.VISIBLE);
            value_txt_3.setVisibility(View.GONE);
            value_value_3.setVisibility(View.GONE);
            value_txt_4.setVisibility(View.GONE);
            value_value_4.setVisibility(View.GONE);
        } else if (dataModels.size() == 3) {
            value_txt_1.setVisibility(View.VISIBLE);
            value_value_1.setVisibility(View.VISIBLE);
            value_txt_2.setVisibility(View.VISIBLE);
            value_value_2.setVisibility(View.VISIBLE);
            value_txt_3.setVisibility(View.VISIBLE);
            value_value_3.setVisibility(View.VISIBLE);
            value_txt_4.setVisibility(View.GONE);
            value_value_4.setVisibility(View.GONE);
        } else if (dataModels.size() == 4) {
            value_txt_1.setVisibility(View.VISIBLE);
            value_value_1.setVisibility(View.VISIBLE);
            value_txt_2.setVisibility(View.VISIBLE);
            value_value_2.setVisibility(View.VISIBLE);
            value_txt_3.setVisibility(View.VISIBLE);
            value_value_3.setVisibility(View.VISIBLE);
            value_txt_4.setVisibility(View.VISIBLE);
            value_value_4.setVisibility(View.VISIBLE);
        }
        dialog.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sub_h_name + "\n\n";
                for (int i = 0; i < dataModels.size(); i++) {
                    String ss = "";
                    if (i == 0) {
                        ss = dataModels.get(i).getName() + " - " + value_value_1.getText().toString().trim();
                    }
                    if (i == 1) {
                        ss = dataModels.get(i).getName() + " - " + value_value_2.getText().toString().trim();
                    }
                    if (i == 2) {
                        ss = dataModels.get(i).getName() + " - " + value_value_3.getText().toString().trim();
                    }
                    if (i == 3) {
                        ss = dataModels.get(i).getName() + " - " + value_value_4.getText().toString().trim();
                    }
                    msg = msg + ss + "\n";
                }
                tabChat.setVisibility(View.VISIBLE);
                uploadimagebtn.setVisibility(View.GONE);
                new_chat.setVisibility(View.INVISIBLE);
                chatlist.setVisibility(View.VISIBLE);
                createComplaint(msg);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.send_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}