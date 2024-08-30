package com.guruvardaan.ghargharsurvey.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.rishtey.agentapp.R;

public class FeedbackActivity extends BaseActivity {

    ImageView back_bt;
    EditText title, feedback;
    CardView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_feedback, frameLayout);
        back_bt = (ImageView) findViewById(R.id.back_bt);
        title = (EditText) findViewById(R.id.title);
        feedback = (EditText) findViewById(R.id.feedback);
        submit = (CardView) findViewById(R.id.submit);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Any Title", Toast.LENGTH_SHORT).show();
                } else if (feedback.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Feedback", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Feedback Submitted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}