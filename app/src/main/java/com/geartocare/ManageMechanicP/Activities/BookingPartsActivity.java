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
import com.geartocare.ManageMechanicP.Adapters.BookingPartsAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelBooking;
import com.geartocare.ManageMechanicP.Models.ModelBookingPart;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityBookingPartsBinding;

import java.util.ArrayList;

public class BookingPartsActivity extends AppCompatActivity {
    ActivityBookingPartsBinding binding;
    ArrayList<ModelBookingPart> parts;
    BookingPartsAdapter adapter;
    ModelBooking bookingDetails;
    CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingPartsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(BookingPartsActivity.this, R.color.black));
        parts = new ArrayList<>();
        adapter = new BookingPartsAdapter(BookingPartsActivity.this,parts);
        bookingDetails = (ModelBooking) getIntent().getSerializableExtra("serviceDetails");
        binding.rvParts.setHasFixedSize(true);
        binding.rvParts.setAdapter(adapter);
        binding.rvParts.setLayoutManager(new LinearLayoutManager(BookingPartsActivity.this));
        progressDialog = new CustomProgressDialog(BookingPartsActivity.this);

        makeList();

    }

    private void makeList() {
        progressDialog.show();
        parts.clear();
        FirebaseDatabase.getInstance().getReference("Users").child(bookingDetails.getUid()).child("vehicles").child(bookingDetails.getVehicleID())
                .child("services").child(bookingDetails.getServiceID()).child("PartList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot partListSnap) {
                if(partListSnap.exists()){
                    for(DataSnapshot part_snap : partListSnap.getChildren()){
                        ModelBookingPart mbp = new ModelBookingPart();

                        mbp.setPartName(part_snap.getKey());
                        mbp.setCount(part_snap.getValue(String.class));
                        parts.add(mbp);
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}