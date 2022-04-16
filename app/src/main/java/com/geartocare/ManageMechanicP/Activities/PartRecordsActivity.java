package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.PartRecordAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelPartRecord;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityPartRecordsBinding;

import java.util.ArrayList;

public class PartRecordsActivity extends AppCompatActivity {
    ActivityPartRecordsBinding binding;
    ArrayList<String> tempRecords;
    ArrayList<ModelPartRecord> records;
    PartRecordAdapter adapter;
    DatabaseReference DBref, InventoryRef;
    String mechanicID;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(PartRecordsActivity.this, R.color.black));


        progressDialog = new CustomProgressDialog(PartRecordsActivity.this);
        mechanicID = getIntent().getStringExtra("mechanicID");
        tempRecords = new ArrayList<>();
        records = new ArrayList<>();
        adapter = new PartRecordAdapter(PartRecordsActivity.this, records);


        binding.rvParts.setAdapter(adapter);
        binding.rvParts.setHasFixedSize(true);
        binding.rvParts.setLayoutManager(new LinearLayoutManager(PartRecordsActivity.this));


        DBref = FirebaseDatabase.getInstance().getReference("mechanics").child(mechanicID).child("PartList");
        InventoryRef = FirebaseDatabase.getInstance().getReference("AppManager").child("Inventory").child("PartList");


        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                DBref.updateChildren(adapter.getUpdated_parts()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            Toast.makeText(PartRecordsActivity.this, "Parts updated successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });


        makeList();

    }

    private void makeList() {
        progressDialog.show();
        tempRecords.clear();

        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot partListSnap) {

                if (partListSnap.exists()) {

                    for (DataSnapshot part_snap : partListSnap.getChildren()) {//getting mechanic's parts-list
                        records.add(part_snap.getValue(ModelPartRecord.class));
                        tempRecords.add(part_snap.getKey());
                    }
                }


                InventoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot InventorySnap) {
                        if (InventorySnap.exists()) {
                            for (DataSnapshot inventory_part_snap : InventorySnap.getChildren()) {
                                if (!tempRecords.contains(inventory_part_snap.getKey())) {
                                    ModelPartRecord r = new ModelPartRecord();

                                    r.setPartID(inventory_part_snap.getKey());
                                    r.setAvailable("0");
                                    r.setUsed("0");
                                    r.setOriginalCount("0");

                                    records.add(r);

                                }

                            }

                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}