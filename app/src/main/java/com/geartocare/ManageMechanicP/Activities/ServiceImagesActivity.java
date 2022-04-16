package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.geartocare.ManageMechanicP.Models.ModelBooking;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.Adapters.ServiceImageAdapter;
import com.geartocare.ManageMechanicP.Models.SingleImage;
import com.geartocare.ManageMechanicP.databinding.ActivityServiceImagesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceImagesActivity extends AppCompatActivity {
    ActivityServiceImagesBinding binding;
    DatabaseReference imgRef;
    ArrayList<String> links;
    ServiceImageAdapter adapter;

    ModelBooking modelBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ServiceImagesActivity.this, R.color.black));


        modelBooking = (ModelBooking) getIntent().getSerializableExtra("serviceDetails");
        links = new ArrayList<>();
        adapter = new ServiceImageAdapter(links, ServiceImagesActivity.this);


        imgRef = FirebaseDatabase.getInstance().getReference("Users").child(modelBooking.getUid()).child("vehicles").child(modelBooking.getVehicleID())
                .child("services").child(modelBooking.getServiceID()).child("images");

        binding.imageList.setLayoutManager(new LinearLayoutManager(ServiceImagesActivity.this));
        binding.imageList.setHasFixedSize(true);
        binding.imageList.setAdapter(adapter);


        makeList();


    }

    private void makeList() {
        links.clear();
        imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot images) {

                if (images.exists()) {

                    for (DataSnapshot singleImage : images.getChildren()) {
                        SingleImage single = singleImage.getValue(SingleImage.class);

                        links.add(single.getImageUrl());
                        adapter.notifyDataSetChanged();

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}