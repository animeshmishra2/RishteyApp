package com.guruvardaan.ghargharsurvey.customers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.MyPlotRequestModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

public class ViewRequestActivity extends BaseActivity {

    TextView agent_name, customer_name, property_type, requested_property, requested_block, requested_plot;
    TextView your_remark, team_remark, status, alloted_property, alloted_block, alloted_plot, alloted_price;
    MyPlotRequestModel myPlotRequestModel;
    UserDetails userDetails;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_view_request, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        myPlotRequestModel = (MyPlotRequestModel) getIntent().getExtras().getSerializable("PM");
        agent_name = (TextView) findViewById(R.id.agent_name);
        customer_name = (TextView) findViewById(R.id.customer_name);
        property_type = (TextView) findViewById(R.id.property_type);
        requested_property = (TextView) findViewById(R.id.requested_property);
        requested_block = (TextView) findViewById(R.id.requested_block);
        requested_plot = (TextView) findViewById(R.id.requested_plot);
        your_remark = (TextView) findViewById(R.id.your_remark);
        team_remark = (TextView) findViewById(R.id.team_remark);
        status = (TextView) findViewById(R.id.status);
        alloted_property = (TextView) findViewById(R.id.alloted_property);
        alloted_block = (TextView) findViewById(R.id.alloted_block);
        alloted_plot = (TextView) findViewById(R.id.alloted_plot);
        alloted_price = (TextView) findViewById(R.id.alloted_price);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (myPlotRequestModel.getAdvisor_name().equalsIgnoreCase("") || myPlotRequestModel.getAdvisor_name().equalsIgnoreCase("null")) {
            agent_name.setText("Agent Not Found");
        } else {
            agent_name.setText(myPlotRequestModel.getAdvisor_name());
        }
        if (userDetails.getUser_type().equalsIgnoreCase("2")) {
            customer_name.setText(userDetails.getName());
        } else {
            customer_name.setText(myPlotRequestModel.getRequestedcustomerName());
        }

        if (myPlotRequestModel.getProperty_type_name().equalsIgnoreCase("") || myPlotRequestModel.getProperty_type_name().equalsIgnoreCase("null")) {
            property_type.setText("Property Not Decided");
        } else {
            property_type.setText(myPlotRequestModel.getProperty_type_name());
        }
        if (myPlotRequestModel.getRequested_property().equalsIgnoreCase("") || myPlotRequestModel.getRequested_property().equalsIgnoreCase("null")) {
            requested_property.setText("Requested Property Not Found");
        } else {
            requested_property.setText(myPlotRequestModel.getRequested_property());
        }
        if (myPlotRequestModel.getRequested_block().equalsIgnoreCase("") || myPlotRequestModel.getRequested_block().equalsIgnoreCase("null")) {
            requested_block.setText("Requested Block Not Found");
        } else {
            requested_block.setText(myPlotRequestModel.getRequested_block());
        }
        if (myPlotRequestModel.getRequested_plot().equalsIgnoreCase("") || myPlotRequestModel.getRequested_plot().equalsIgnoreCase("null")) {
            requested_plot.setText("Requested Plot Not Found");
        } else {
            requested_plot.setText(myPlotRequestModel.getRequested_plot());
        }
        if (myPlotRequestModel.getUserDetails().equalsIgnoreCase("") || myPlotRequestModel.getUserDetails().equalsIgnoreCase("null")) {
            your_remark.setText("Remarks Not Sent");
        } else {
            your_remark.setText(myPlotRequestModel.getUserDetails());
        }
        if (myPlotRequestModel.getEmpComment().equalsIgnoreCase("") || myPlotRequestModel.getEmpComment().equalsIgnoreCase("null")) {
            team_remark.setText("No Comments Till Now");
        } else {
            team_remark.setText(myPlotRequestModel.getEmpComment());
        }
        if (myPlotRequestModel.getStatus().equalsIgnoreCase("") || myPlotRequestModel.getStatus().equalsIgnoreCase("null")) {
            status.setText("Status Not Updated");
        } else {
            if (myPlotRequestModel.getStatus().equalsIgnoreCase("Avilable")) {
                status.setText("Available");
            } else {
                status.setText(myPlotRequestModel.getStatus());
            }
        }
        if (myPlotRequestModel.getAllotedPropertyName().equalsIgnoreCase("") || myPlotRequestModel.getAllotedPropertyName().equalsIgnoreCase("null")) {
            alloted_property.setText("Property Not Allotted");
        } else {
            alloted_property.setText(myPlotRequestModel.getAllotedPropertyName());
        }
        if (myPlotRequestModel.getAllotedBlockName().equalsIgnoreCase("") || myPlotRequestModel.getAllotedBlockName().equalsIgnoreCase("null")) {
            alloted_block.setText("Block Not Allotted");
        } else {
            alloted_block.setText(myPlotRequestModel.getAllotedBlockName());
        }
        if (myPlotRequestModel.getAllotedPlotName().equalsIgnoreCase("") || myPlotRequestModel.getAllotedPlotName().equalsIgnoreCase("null")) {
            alloted_plot.setText("Plot Not Allotted");
        } else {
            alloted_plot.setText(myPlotRequestModel.getAllotedPlotName());
        }
        if (myPlotRequestModel.getAmount().equalsIgnoreCase("") || myPlotRequestModel.getAmount().equalsIgnoreCase("null")) {
            alloted_price.setText("Amount Not Provided");
        } else {
            alloted_price.setText("\u20b9" + myPlotRequestModel.getAmount());
        }


    }
}