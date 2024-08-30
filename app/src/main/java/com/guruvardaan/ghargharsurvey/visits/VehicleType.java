package com.guruvardaan.ghargharsurvey.visits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.model.VehicleModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import java.util.ArrayList;

public class VehicleType extends BaseActivity {

    RecyclerView type_recyclerview;
    ArrayList<VehicleModel> vehicleModels;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_vehicle_type, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        type_recyclerview = (RecyclerView) findViewById(R.id.type_recyclerview);
        type_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        vehicleModels = new ArrayList<>();
        vehicleModels.add(new VehicleModel("1", "Rented Car", "0", R.drawable.rents));
        vehicleModels.add(new VehicleModel("2", "Company Car", "0", R.drawable.cars));
        vehicleModels.add(new VehicleModel("3", "Bus", "0", R.drawable.bus));
        vehicleModels.add(new VehicleModel("4", "Train", "0", R.drawable.trains));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        type_recyclerview.setAdapter(new VehicleAdapter());
    }

    class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleHolder> {

        @Override
        public int getItemCount() {
            return vehicleModels.size();

        }

        @Override
        public void onBindViewHolder(VehicleAdapter.VehicleHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(vehicleModels.get(position).getVehicle_name());
            holder.pr_image.setImageResource(vehicleModels.get(position).getImg());
            holder.prod_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("TYPE", vehicleModels.get(position).getVehicle_name());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public VehicleAdapter.VehicleHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.vehicle_row, viewGroup, false);
            VehicleAdapter.VehicleHolder listHolder = new VehicleAdapter.VehicleHolder(mainGroup);
            return listHolder;
        }

        public class VehicleHolder extends RecyclerView.ViewHolder {

            TextView prod_name;
            ImageView pr_image;
            LinearLayout prod_layout;

            public VehicleHolder(View view) {
                super(view);
                prod_layout = (LinearLayout) view.findViewById(R.id.prod_layout);
                prod_name = (TextView) view.findViewById(R.id.prod_name);
                pr_image = (ImageView) view.findViewById(R.id.pr_image);
            }
        }
    }

}