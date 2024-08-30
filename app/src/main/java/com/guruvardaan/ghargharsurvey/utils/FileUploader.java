package com.guruvardaan.ghargharsurvey.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import java.util.Vector;

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

public class FileUploader {

    public FileUploaderCallback fileUploaderCallback;
    public int uploadIndex = -1;
    private long totalFileLength = 0;
    private long totalFileUploaded = 0;
    private UploadInterface uploadInterface;
    private String auth_token = "";
    private String responses;


    private interface UploadInterface {

        @Multipart
        @POST
        Call<JsonElement> uploadFile(@Url String url, @Part MultipartBody.Part file, @Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Header("Authorization") String authorization);

        @Multipart
        @POST
        Call<JsonElement> uploadFile(@Url String url, @Part MultipartBody.Part file, @Part MultipartBody.Part file1, @Part MultipartBody.Part file2);
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

    public FileUploader() {
        uploadInterface = ApiClient.getClient().create(UploadInterface.class);
    }

    public void uploadFiles(String urls, String rc_key, File rc_file, String loan_1_key, File loan1, String loan_2_key, File loan2, FileUploaderCallback fileUploaderCallbacks) {
        totalFileLength = rc_file.length() + loan1.length() + loan2.length();
        fileUploaderCallback = fileUploaderCallbacks;
        uploadSingleFile(urls, rc_key, rc_file, loan_1_key, loan1, loan_2_key, loan2, fileUploaderCallbacks);
    }


    private void uploadSingleFile(String urls, String rc_key, File rc_file, String loan_1_key, File loan1, String loan_2_key, File loan2, FileUploaderCallback fileUploaderCallback) {
        PRRequestBody fileBody = new PRRequestBody(rc_file);
        PRRequestBody fileBody1 = new PRRequestBody(loan1);
        PRRequestBody fileBody2 = new PRRequestBody(loan2);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(rc_key, rc_file.getName(), fileBody);
        MultipartBody.Part filePart1 = MultipartBody.Part.createFormData(loan_1_key, loan1.getName(), fileBody1);
        MultipartBody.Part filePart2 = MultipartBody.Part.createFormData(loan_2_key, loan2.getName(), fileBody2);
        Call<JsonElement> call;
        if (auth_token.isEmpty()) {
            call = uploadInterface.uploadFile(urls, filePart, filePart1, filePart2);
        } else {
            call = uploadInterface.uploadFile(urls, filePart, filePart1, filePart2, auth_token);
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