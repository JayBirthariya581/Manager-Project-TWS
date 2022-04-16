package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.BookingsAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelBooking;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityServiceAssignedBinding;

import java.util.ArrayList;

public class ServiceAssignedActivity extends AppCompatActivity {
    ActivityServiceAssignedBinding binding;
    CustomProgressDialog progressDialog;
    DatabaseReference DBref;
    ArrayList<String> dates;
    BookingsAdapter adapter;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceAssignedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ServiceAssignedActivity.this, R.color.black));
        type = getIntent().getStringExtra("type");
        progressDialog = new CustomProgressDialog(ServiceAssignedActivity.this);

        DBref = FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots");
        dates = new ArrayList<>();
        adapter = new BookingsAdapter(dates,ServiceAssignedActivity.this);


        binding.rvBookings.setLayoutManager(new LinearLayoutManager(ServiceAssignedActivity.this));
        binding.rvBookings.setAdapter(adapter);
        binding.rvBookings.setHasFixedSize(true);




        makeList();

    }

    private void makeList() {

        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Slots) {
                if(Slots.exists()){
                    for(DataSnapshot date_slot : Slots.getChildren()){


                        for(DataSnapshot time_slot : date_slot.getChildren()){

                            for(DataSnapshot sv : time_slot.getChildren() ){
                                ModelBooking b = sv.getValue(ModelBooking.class);

                                FirebaseDatabase.getInstance().getReference("Users").child(b.getUid()).child("vehicles")
                                        .child(b.getVehicleID()).child("services").child(b.getServiceID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot service) {
                                        if(service.exists()){

                                            if(service.child("status").getValue(String.class).equals(type)){
                                                dates.add(date_slot.getKey());
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

                    }



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ServiceAssignedActivity.this, CustomerActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

        );
        finish();
    }
}