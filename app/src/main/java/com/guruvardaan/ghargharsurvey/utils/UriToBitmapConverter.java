package com.guruvardaan.ghargharsurvey.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class UriToBitmapConverter {

    public static Bitmap uriToBitmap(Context context, Uri uri) {
        try {
            // Use the ContentResolver to open an InputStream to the URI
            // and decode the stream into a Bitmap
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}