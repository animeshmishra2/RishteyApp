package com.guruvardaan.ghargharsurvey.customers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

public class FindTextActivity extends BaseActivity {

    Button scan_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_find_text, frameLayout);
        scan_code = (Button) findViewById(R.id.scan_code);
        scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(FindTextActivity.this).cameraOnly().compress(400).start(234);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 234 && resultCode == RESULT_OK) {
            String imagePath = data.getStringExtra(ImagePicker.EXTRA_FILE_PATH);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            recognizeText(bitmap);
        }
    }

    private void recognizeText(Bitmap bitmap) {
        // Create an InputImage from the Bitmap
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        // Create a TextRecognizer instance
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // Process the image
        Task<Text> result = recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        // Text recognition successful
                        // Handle the recognized text
                        for (Text.TextBlock block : visionText.getTextBlocks()) {
                            System.out.println("Animesh "+block.getText().toLowerCase());
                            if(block.getText().toLowerCase().contains("km"))
                            {
                                Toast.makeText(getApplicationContext(),"Found "+block.getText(),Toast.LENGTH_SHORT).show();
                            }

                            // Process the text blocks, lines, elements, and symbols
                            // ...
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Text recognition failed
                        // Handle the error
                        e.printStackTrace();
                    }
                });
    }

    private TextRecognizer getTextRecognizer() {
        // [START mlkit_local_doc_recognizer]
        TextRecognizer detector = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        // [END mlkit_local_doc_recognizer]

        return detector;
    }
}