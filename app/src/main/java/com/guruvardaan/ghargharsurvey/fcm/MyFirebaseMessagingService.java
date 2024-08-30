package com.guruvardaan.ghargharsurvey.fcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.guruvardaan.ghargharsurvey.chat.ChatActivity;
import com.guruvardaan.ghargharsurvey.chat.MessageActivity;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.welcome.SplashActivity;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    UserDetails userDetails;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        userDetails = new UserDetails(getApplicationContext());
        //System.out.println("Animesh 1 " + remoteMessage.getNotification().getBody());
        System.out.println("Animesh Mishra " + remoteMessage.getData());
        if (remoteMessage == null)
            return;
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData());
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", jsonObject.getString("body"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                //notificationUtils.playNotificationSound();
                Intent nint = null;
                if (jsonObject.getString("type").equalsIgnoreCase("1")) {
                    if (!userDetails.getUser_type().equalsIgnoreCase("3")) {
                        nint = new Intent(getApplicationContext(), ChatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("NEW", "0");
                        bundle.putString("CID", jsonObject.getString("complaint_id"));
                        nint.putExtras(bundle);
                    } else {
                        nint = new Intent(getApplicationContext(), SplashActivity.class);
                    }
                } else if (jsonObject.getString("type").equalsIgnoreCase("2")) {
                    if (userDetails.getUser_type().equalsIgnoreCase("3")) {
                        nint = new Intent(getApplicationContext(), MessageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("UNAME", jsonObject.getString("sender_name"));
                        bundle.putString("CID", jsonObject.getString("complaint_id"));
                        nint.putExtras(bundle);
                    } else {
                        nint = new Intent(getApplicationContext(), SplashActivity.class);
                    }
                } else {
                    nint = new Intent(getApplicationContext(), SplashActivity.class);
                }

                showNotifications(getApplicationContext(), jsonObject.getString("title"), jsonObject.getString("body"), jsonObject.getString("type"), nint, "", jsonObject.getString("priority"));
                //showNotifications(getApplicationContext(), jsonObject.getString("title"), jsonObject.getString("body"), jsonObject.getString("timestamp"), nint, jsonObject.getString("image"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Hello Baba");
        }
    }


    private void showNotifications(Context context, String title, String message, String timeStamp, Intent intent, String img, String priority) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, img, priority);
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        userDetails = new UserDetails(getApplicationContext());
        userDetails.setFcm_msg(token);
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");
        Date date = new Date();
        return dateFormat.format(date);
    }
}