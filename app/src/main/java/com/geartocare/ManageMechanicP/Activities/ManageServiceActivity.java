package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.ManageServiceAdapter;
import com.geartocare.ManageMechanicP.Models.ModelTwoWheelerService;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityManageServiceBinding;

import java.util.ArrayList;

public class ManageServiceActivity extends AppCompatActivity {
    ActivityManageServiceBinding binding;
    ManageServiceAdapter adapter;
    ArrayList<ModelTwoWheelerService> services;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ManageServiceActivity.this, R.color.black));


        services = new ArrayList<>();
        adapter = new ManageServiceAdapter(services,ManageServiceActivity.this);


        
        binding.rvService.setLayoutManager(new LinearLayoutManager(ManageServiceActivity.this));
        binding.rvService.setAdapter(adapter);
        binding.rvService.setHasFixedSize(true);


        makeList();



    }

    private void makeList() {

        services.clear();


        FirebaseDatabase.getInstance().getReference("Services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot serviceList) {


                if(serviceList.exists()){

                    for(DataSnapshot sv : serviceList.getChildren()){


                        ModelTwoWheelerService m = sv.getValue(ModelTwoWheelerService.class);


                        services.add(m);



                    }


                    adapter.notifyDataSetChanged();



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}