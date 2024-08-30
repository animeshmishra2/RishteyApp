package com.guruvardaan.ghargharsurvey.config;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MarshMallowPermission {
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 1;
    public static final int READ_AND_WRITE_PERMISSION_REQUEST_CODE = 3;
    public static final int GET_ACCOUNT_PERMISSION_REQUEST_CODE = 3;
    public static final int GET_FINE_LOCATION_PERMISSION_REQUEST_CODE = 4;


    Context context;

    public MarshMallowPermission(Context context) {
        this.context = context;
    }

    public boolean checkPermissionForReadPhoneState() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public boolean checkPermissionForReadExternalStorage() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForWriteExternalStorage() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForReadandWriteExternalStorage() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "Read and Write external storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_AND_WRITE_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public boolean checkPermissionForGetAccount() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForGetAccount() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.GET_ACCOUNTS)) {
                Toast.makeText(activity, "Location permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.GET_ACCOUNTS}, GET_ACCOUNT_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public boolean checkPermissionForAccessFineLocation() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForAccessFineLocation() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(activity, "Location permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GET_FINE_LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

}