package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.BookingServicesAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelBooking;
import com.geartocare.ManageMechanicP.Models.ModelBookingUser;
import com.geartocare.ManageMechanicP.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BookingServicesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingServicesAdapter adapter;
    private ArrayList<ModelBooking> BookingList;
    private ArrayList<ModelBookingUser> BookingUserList;
    DatabaseReference DB_Cust, BoH;
    CustomProgressDialog progressBar;
    SwipeRefreshLayout mySwipeRefreshLayout;
    String date, time,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_booking);
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        progressBar = new CustomProgressDialog(BookingServicesActivity.this);

        getWindow().setStatusBarColor(ContextCompat.getColor(BookingServicesActivity.this, R.color.black));

        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        type = getIntent().getStringExtra("type");
        BookingList = new ArrayList<>();
        BookingUserList = new ArrayList<>();
        adapter = new BookingServicesAdapter(BookingUserList, BookingServicesActivity.this);
        adapter.setType(type);
        adapter.setTime(time);
        adapter.setDate(date);
        recyclerView = findViewById(R.id.rv_todayBooking);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookingServicesActivity.this));

        recyclerView.setAdapter(adapter);


        DB_Cust = FirebaseDatabase.getInstance().getReference("Users");
        BoH = FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date).child(time);


        makeList();


        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        makeList();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


    }

    private void makeList() {
        progressBar.show();
        BookingList.clear();
        BookingUserList.clear();
        adapter.notifyDataSetChanged();
        BoH.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot Service_List_DB) {
                if (Service_List_DB.exists()) {
                    //progressBar.dismiss();
                    //findViewById(R.id.emp).setVisibility(View.VISIBLE);

                    for (DataSnapshot Booking : Service_List_DB.getChildren()) {


                        if(Booking.child("status").getValue(String.class).equals(type)){
                            BookingList.add(Booking.getValue(ModelBooking.class));
                        }


                    }


                    for (ModelBooking b : BookingList) {

                        DB_Cust.child(b.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot userDetail) {


                                if (userDetail.exists()) {

                                    ModelBookingUser u = new ModelBookingUser();
                                    u.setUid(b.getUid());
                                    u.setServiceID(b.getServiceID());
                                    u.setVehicleID(b.getVehicleID());
                                    u.setPhone(userDetail.child("phone").getValue(String.class));
                                    u.setFullName(userDetail.child("fullName").getValue(String.class));
                                    BookingUserList.add(u);
                                    adapter.notifyDataSetChanged();

                                }
                                progressBar.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                    }


                } else {
                    Toast.makeText(BookingServicesActivity.this, date + time, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}