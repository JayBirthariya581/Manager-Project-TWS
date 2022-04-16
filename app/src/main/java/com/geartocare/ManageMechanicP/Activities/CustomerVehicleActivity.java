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
import com.geartocare.ManageMechanicP.Adapters.CustomerVehicleAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelVehicle;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityCustomerVehicleBinding;

import java.util.ArrayList;

public class CustomerVehicleActivity extends AppCompatActivity {
    ActivityCustomerVehicleBinding binding;

    CustomerVehicleAdapter adapter;
    ArrayList<ModelVehicle> bookingList;
    CustomProgressDialog dialog;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerVehicleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(CustomerVehicleActivity.this, R.color.black));
        uid = getIntent().getStringExtra("uid");

        dialog = new CustomProgressDialog(CustomerVehicleActivity.this);

        bookingList = new ArrayList<>();
        adapter = new CustomerVehicleAdapter(CustomerVehicleActivity.this,bookingList,uid);



        binding.rvBookingList.setLayoutManager(new LinearLayoutManager(CustomerVehicleActivity.this));
        binding.rvBookingList.setHasFixedSize(true);
        binding.rvBookingList.setAdapter(adapter);




        makeList();




    }






    private void makeList() {
        dialog.show();
        bookingList.clear();

        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("vehicles")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot bookings) {


                        if(bookings.exists()){

                            for(DataSnapshot booking : bookings.getChildren()){

                                ModelVehicle bookingFromDB = booking.getValue(ModelVehicle.class);

                                bookingList.add(bookingFromDB);




                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();


                        }else{
                            dialog.dismiss();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }








}