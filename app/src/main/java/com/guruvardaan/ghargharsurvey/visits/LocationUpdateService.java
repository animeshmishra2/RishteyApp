package com.guruvardaan.ghargharsurvey.visits;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.guruvardaan.ghargharsurvey.utils.DBHelper;
import com.rishtey.agentapp.R;

public class LocationUpdateService extends Service /*implements View.OnTouchListener*/ {

    //region data
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    DBHelper dbHelper;
    /*private WindowManager windowManager;
    private View overlayView;
    private WindowManager.LayoutParams layoutParams;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;*/
    //endregion

    //onCreate
    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(getApplicationContext());
        initData();
        /*windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Inflate your custom layout for the overlay
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);

        // Set the initial visibility of the overlay view
        overlayView.setVisibility(View.VISIBLE);

        // Define the layout parameters for the overlay view
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        // Set the position of the overlay view (e.g., align it to the left side)
        layoutParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        layoutParams.x = 0;
        layoutParams.y = 0;

        overlayView.setOnTouchListener(this);
        // Add the overlay view to the window manager
        windowManager.addView(overlayView, layoutParams);*/
    }

/*
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = layoutParams.x;
                initialY = layoutParams.y;
                initialTouchX = motionEvent.getRawX();
                initialTouchY = motionEvent.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                // Handle dragging logic
                int xDiff = (int) (motionEvent.getRawX() - initialTouchX);
                int yDiff = (int) (motionEvent.getRawY() - initialTouchY);
                layoutParams.x = initialX + xDiff;
                layoutParams.y = initialY + yDiff;
                windowManager.updateViewLayout(overlayView, layoutParams);
                return true;
            case MotionEvent.ACTION_UP:
                // Handle click event
                if (Math.abs(motionEvent.getRawX() - initialTouchX) < 10 && Math.abs(motionEvent.getRawY() - initialTouchY) < 10) {
                    openActivity();
                } else if (motionEvent.getRawY() - initialTouchY > (screenHeight * 0.5)) {
                    closeOverlay();
                }
                return true;
        }
        return false;
    }
*/

   /* private void closeOverlay() {
        stopSelf(); // Stop the OverlayService
    }


    private void openActivity() {
        Intent intent = new Intent(this, MyVisitsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/

    //Location Callback
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location currentLocation = locationResult.getLastLocation();
            dbHelper.insertTrip(VisitOpenActivity.trip_id, "" + currentLocation.getLatitude(), "" + currentLocation.getLongitude(), VisitOpenActivity.status, "0");
            //Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "," + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            Log.d("Locations", currentLocation.getLatitude() + "," + currentLocation.getLongitude());
            Intent intent = new Intent("LocationBroadcast");
            Bundle bundle = new Bundle();
            bundle.putDouble("LT", currentLocation.getLatitude());
            bundle.putDouble("LN", currentLocation.getLongitude());
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(LocationUpdateService.this).sendBroadcast(intent);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        prepareForegroundNotification();
        startLocationUpdates();

        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(this.locationRequest,
                this.locationCallback, Looper.myLooper());
    }

    private void prepareForegroundNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "RISHTEYID",
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
        Intent notificationIntent = new Intent(this, MyVisitsActivity.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {

            pendingIntent = PendingIntent.getActivity
                    (this, 24, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity
                    (this, 24, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        }

        Notification notification = new NotificationCompat.Builder(this, "RISHTEYID")
                .setContentTitle(getString(R.string.app_name))
                .setContentTitle("Location Used by Rishtey Township Pvt Ltd")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initData() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setSmallestDisplacement(2);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        removeLocationUpdates();
       /* if (overlayView != null) {
            windowManager.removeView(overlayView);
        }*/
        super.onDestroy();
    }

    public void removeLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }
}