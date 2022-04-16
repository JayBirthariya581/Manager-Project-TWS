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
import com.geartocare.ManageMechanicP.databinding.ActivityBookingListBinding;

import java.util.ArrayList;

public class BookingListActivity extends AppCompatActivity {
    ActivityBookingListBinding binding;
    CustomProgressDialog progressDialog;
    DatabaseReference DBref;
    ArrayList<String> dates;
    BookingsAdapter adapter;
    String type;
    int check_ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(BookingListActivity.this, R.color.black));
        type = getIntent().getStringExtra("type");
        progressDialog = new CustomProgressDialog(BookingListActivity.this);

        DBref = FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots");
        dates = new ArrayList<>();
        adapter = new BookingsAdapter(dates, BookingListActivity.this);
        adapter.setType(type);

        binding.rvBookings.setLayoutManager(new LinearLayoutManager(BookingListActivity.this));
        binding.rvBookings.setAdapter(adapter);
        binding.rvBookings.setHasFixedSize(true);


        makeList();

    }



    private void makeList() {
        progressDialog.show();
        dates.clear();
        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Slots) {
                if (Slots.exists()) {
                    for (DataSnapshot date_slot : Slots.getChildren()) {
                        check_ts = 0;

                        for (DataSnapshot time_slot : date_slot.getChildren()) {

                            for (DataSnapshot sv : time_slot.getChildren()) {
                                ModelBooking b = sv.getValue(ModelBooking.class);

                                if (b.getStatus().equals(type)) {
                                    check_ts = 1;
                                    break;
                                }


                            }
                            if (check_ts == 1) {
                                dates.add(date_slot.getKey());
                                break;
                            }

                        }

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BookingListActivity.this, CustomerActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

        );
        finish();
    }
}