package com.guruvardaan.ghargharsurvey.visits;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.model.PlotRequestModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import java.util.ArrayList;

public class CreateCustomersActivity extends BaseActivity {

    ArrayList<PlotRequestModel> customerModels;
    ImageView back_button;
    RecyclerView customer_recyclerview;
    TextView total_customer, add_customer;
    LinearLayout request_new_trip;
    CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_create_customers, frameLayout);
        customerModels = new ArrayList<>();
        back_button = (ImageView) findViewById(R.id.back_button);
        customer_recyclerview = (RecyclerView) findViewById(R.id.customer_recyclerview);
        total_customer = (TextView) findViewById(R.id.total_customer);
        add_customer = (TextView) findViewById(R.id.add_customer);
        request_new_trip = (LinearLayout) findViewById(R.id.request_new_trip);
        customer_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CustomerAdapter();
        customer_recyclerview.setAdapter(adapter);
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateCustomersActivity.this, SelectCustomers.class);
                startActivityForResult(intent, 22);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("SM", customerModels);
                bundle.putString("VID", getIntent().getExtras().getString("VID"));
                returnIntent.putExtras(bundle);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        request_new_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customerModels.size() > 0) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SM", customerModels);
                    bundle.putString("VID", getIntent().getExtras().getString("VID"));
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Select Customers", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("SM", customerModels);
        bundle.putString("VID", getIntent().getExtras().getString("VID"));
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK) {
            customerModels.add((PlotRequestModel) data.getExtras().getSerializable("CM"));
            adapter.notifyDataSetChanged();
            total_customer.setText(customerModels.size() + " Customers");
        }
    }

    class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {

        @Override
        public int getItemCount() {
            return customerModels.size();
        }

        @Override
        public void onBindViewHolder(CustomerAdapter.CustomerHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.name.setText(customerModels.get(position).getCustomer_name());
            holder.edit.setVisibility(View.GONE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateCustomersActivity.this, AddCustomerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("POS", position);
                    bundle.putString("TYPE", "E");
                    bundle.putSerializable("CM", customerModels.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 22);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customerModels.remove(position);
                    total_customer.setText(customerModels.size() + " Customers");
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public CustomerAdapter.CustomerHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.customer_row, viewGroup, false);
            CustomerAdapter.CustomerHolder listHolder = new CustomerAdapter.CustomerHolder(mainGroup);
            return listHolder;
        }

        public class CustomerHolder extends RecyclerView.ViewHolder {

            TextView name;
            ImageView edit, delete;

            public CustomerHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                edit = (ImageView) view.findViewById(R.id.edit);
                delete = (ImageView) view.findViewById(R.id.delete);
            }
        }
    }

}