package com.guruvardaan.ghargharsurvey.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public class SingleFileUploader {

    public FileUploaderCallback fileUploaderCallback;
    private File files;
    public int uploadIndex = -1;
    private long totalFileLength = 0;
    private long totalFileUploaded = 0;
    private UploadInterface uploadInterface;
    private String auth_token = "";
    private String responses;


    private interface UploadInterface {

        @Multipart
        @POST
        Call<JsonElement> uploadFile(@Url String url, @Part MultipartBody.Part file, @Header("Authorization") String authorization);

        @Multipart
        @POST
        Call<JsonElement> uploadFile(@Url String url, @Part MultipartBody.Part file);
    }

    public interface FileUploaderCallback {
        void onError();

        void onFinish(String responses);

        void onProgressUpdate(int currentpercent, int totalpercent, int filenumber);
    }

    public class PRRequestBody extends RequestBody {
        private File mFile;

        private static final int DEFAULT_BUFFER_SIZE = 2048;

        public PRRequestBody(final File file) {
            mFile = file;
        }

        @Override
        public MediaType contentType() {
            // i want to upload only images
            return MediaType.parse("*/*");
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLength));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
            }
        }
    }

    public SingleFileUploader() {
        uploadInterface = ApiClient2.getClient().create(UploadInterface.class);
    }

    public void uploadFiles(String urls, String rc_key, File rc_file, FileUploaderCallback fileUploaderCallbacks) {
        totalFileLength = rc_file.length();
        fileUploaderCallback = fileUploaderCallbacks;
        uploadSingleFile(urls, rc_key, rc_file, fileUploaderCallbacks);
    }


    private void uploadSingleFile(String urls, String rc_key, File rc_file, FileUploaderCallback fileUploaderCallback) {
        PRRequestBody fileBody = new PRRequestBody(rc_file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(rc_key, rc_file.getName(), fileBody);
        Call<JsonElement> call;
        if (auth_token.isEmpty()) {
            call = uploadInterface.uploadFile(urls, filePart);
        } else {
            call = uploadInterface.uploadFile(urls, filePart, auth_token);
        }
        System.out.println("Vikas " + call.toString());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    responses = jsonElement.toString();
                    System.out.println("Vikas " + responses);
                    fileUploaderCallback.onFinish(responses);
                } else {
                    responses = "";
                    System.out.println("Vikas Error " + response + " -- " + response.message() + " --- " + response.errorBody() + " -- " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                fileUploaderCallback.onError();
                t.printStackTrace();
            }
        });
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            int current_percent = (int) (100 * mUploaded / mTotal);
            int total_percent = (int) (100 * (totalFileUploaded + mUploaded) / totalFileLength);
            fileUploaderCallback.onProgressUpdate(current_percent, total_percent, uploadIndex + 1);
        }
    }
}