package com.guruvardaan.ghargharsurvey.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.rishtey.agentapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationUtils {

    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl, String priority) {
        if (TextUtils.isEmpty(message))
            return;
        final int icon = R.mipmap.ic_launcher;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_MUTABLE
                );
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "notiiii");
        if (!TextUtils.isEmpty(imageUrl)) {
            System.out.println("Big Image Url1");

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                System.out.println("Big Image Url2");
                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    System.out.println("Big Image Url3");
                    //showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, Settings.System.DEFAULT_NOTIFICATION_URI);

                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, Settings.System.DEFAULT_NOTIFICATION_URI, priority);
                } else {
                    System.out.println("Big Image Url4");
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, Settings.System.DEFAULT_NOTIFICATION_URI, priority);
                }
            } else {
                showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, Settings.System.DEFAULT_NOTIFICATION_URI, priority);
            }
        } else {
            System.out.println("Big Image Url5");
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, Settings.System.DEFAULT_NOTIFICATION_URI, priority);
            //playNotificationSound();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "GOPINATH";
            String description = "App GOPI";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.default_notification_channel_id), name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound, String priority) {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.noti_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message)).setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setNumber(1)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(100, builder.build());
        playNotificationSound(priority);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound, String priority) {
        System.out.println(title + " --- " + message);
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(mContext, mContext.getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.noti_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setNumber(1).setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null))
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(100, notification);
        playNotificationSound(priority);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound(String priority) {
        try {
            String nt;
            if (priority.equalsIgnoreCase("0")) {
                nt = "notification";
            } else {
                nt = "emergency";
            }
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/" + nt);
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
*/
}