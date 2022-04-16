package com.geartocare.ManageMechanicP.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.ServiceNotesAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelBooking;
import com.geartocare.ManageMechanicP.Models.ModelService;
import com.geartocare.ManageMechanicP.Models.ModelUser;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.Adapters.ServiceMechanicAdapter;
import com.geartocare.ManageMechanicP.databinding.ActivityBookingDetailBinding;
import com.geartocare.ManageMechanicP.databinding.AddNoteBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingDetailActivity extends AppCompatActivity {

    private ServiceMechanicAdapter adapter;
    private ArrayList<ModelService> serviceList;
    DatabaseReference DB_User, SvRef;
    ActivityBookingDetailBinding binding;
    HashMap<String, String> bookingDetails;
    String uid, serviceID, vehicleID;
    ArrayList<String> notes;
    CustomProgressDialog progressDialog;
    ServiceNotesAdapter notesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(BookingDetailActivity.this, R.color.black));

        progressDialog = new CustomProgressDialog(BookingDetailActivity.this);


        bookingDetails = (HashMap<String, String>) getIntent().getSerializableExtra("bookingDetails");
        uid = bookingDetails.get("uid");
        serviceID = bookingDetails.get("serviceID");
        vehicleID = bookingDetails.get("vehicleID");





        DB_User = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        SvRef = DB_User.child("vehicles").child(vehicleID).child("services").child(serviceID);

        notes = new ArrayList<>();
        serviceList = new ArrayList<>();
        notesAdapter = new ServiceNotesAdapter(BookingDetailActivity.this, notes);
        notesAdapter.setSvref(SvRef);
        binding.rvServiceNotes.setAdapter(notesAdapter);
        binding.rvServiceNotes.setLayoutManager(new LinearLayoutManager(BookingDetailActivity.this));
        binding.rvServiceNotes.setHasFixedSize(true);


        adapter = new ServiceMechanicAdapter(serviceList, BookingDetailActivity.this);
        adapter.setPhone(bookingDetails.get("phone"));
        adapter.setVhNo(vehicleID);


        ActivityResultLauncher<Intent> lau2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==Activity.RESULT_OK){



                }
            }
        });





        ActivityResultLauncher<Intent> lau = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {


                    makeList();


                }

            }
        });

        adapter.setLauncher(lau);


        ModelBooking mb = new ModelBooking();
        mb.setUid(uid);
        mb.setServiceID(serviceID);
        mb.setVehicleID(vehicleID);
        adapter.setBookingDetails(mb);


        binding.rvServiceDetail.setHasFixedSize(true);

        binding.rvServiceDetail.setLayoutManager(new LinearLayoutManager(BookingDetailActivity.this));

        binding.rvServiceDetail.setAdapter(adapter);


        DB_User.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot user) {

                if (user.exists()) {

                    ModelUser us = user.getValue(ModelUser.class);

                    binding.firstNameBD.getEditText().setText(us.getFullName());
                    binding.phoneBD.getEditText().setText(us.getPhone());


                    binding.phoneBD.getEditText().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + binding.phoneBD.getEditText().getText().toString()));
                            startActivity(intent);
                        }
                    });

                    binding.emailBD.getEditText().setText(us.getEmail());
                    makeList();
                } else {
                    Toast.makeText(BookingDetailActivity.this, "No such user exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        binding.swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        makeList();
                        binding.swiperefresh.setRefreshing(false);
                    }
                }
        );



        AddNoteBinding acb = AddNoteBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(BookingDetailActivity.this);
        dialog.setContentView(acb.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        acb.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressDialog.show();
                SvRef.child("Notes").child(String.valueOf(notes.size()+1)).setValue(acb.note.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            notes.add(acb.note.getText().toString());
                            notesAdapter.notifyDataSetChanged();
                            acb.note.setText("");
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(BookingDetailActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });


            }
        });


        binding.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog.show();


            }
        });








    }


    private void makeList() {
        progressDialog.show();
        serviceList.clear();
        notes.clear();
        DB_User.child("vehicles").child(vehicleID).child("services").child(serviceID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot specific_Service) {
                if (specific_Service.exists()) {
                    serviceList.add(specific_Service.getValue(ModelService.class));

                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();



                    if(specific_Service.child("Notes").exists()){


                        for(DataSnapshot note : specific_Service.child("Notes").getChildren()){

                            notes.add(note.getValue(String.class));

                        }
                        notesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }




                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {


            }
        });


    }

}