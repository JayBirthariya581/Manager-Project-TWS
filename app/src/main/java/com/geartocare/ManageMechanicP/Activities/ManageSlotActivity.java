package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.ManageSlotAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityManageSlotBinding;
import com.geartocare.ManageMechanicP.databinding.AddCouponBinding;
import com.geartocare.ManageMechanicP.databinding.AddSlotBinding;
import com.geartocare.ManageMechanicP.databinding.CardAddBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ManageSlotActivity extends AppCompatActivity {
    ActivityManageSlotBinding binding;
    ManageSlotAdapter adapter;
    ArrayList<String> slots;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageSlotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ManageSlotActivity.this, R.color.black));
        progressDialog = new CustomProgressDialog(ManageSlotActivity.this);

        slots = new ArrayList<>();
        adapter = new ManageSlotAdapter(slots, ManageSlotActivity.this);


        binding.rvSlots.setLayoutManager(new LinearLayoutManager(ManageSlotActivity.this));
        binding.rvSlots.setHasFixedSize(true);
        binding.rvSlots.setAdapter(adapter);


        makeList();

        CardAddBinding ca = CardAddBinding.inflate(getLayoutInflater());
        Dialog dialog1 = new Dialog(ManageSlotActivity.this);
        dialog1.setContentView(ca.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog1.getWindow().setAttributes(lp);


        View.OnClickListener mc_click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = ca.value.getText().toString();
                if (value.isEmpty()) {
                    Toast.makeText(ManageSlotActivity.this, "Please enter proper value", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog1.dismiss();
                progressDialog.show();
                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("mechanicCount").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            binding.mcVal.setText(value);
                            ca.value.setText("");
                            progressDialog.dismiss();
                        }
                    }
                });


            }
        };


        View.OnClickListener dl_click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = ca.value.getText().toString();
                if (value.isEmpty()) {
                    Toast.makeText(ManageSlotActivity.this, "Please enter proper value", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog1.dismiss();
                progressDialog.show();
                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("dayDisplayLimit").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            binding.dlVal.setText(value);
                            ca.value.setText("");
                            progressDialog.dismiss();
                        }
                    }
                });


            }
        };


        binding.editMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ca.add.setOnClickListener(mc_click);
                dialog1.show();

            }
        });

        binding.editDaysLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ca.add.setOnClickListener(dl_click);
                dialog1.show();
            }
        });

        binding.editSd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ManageSlotActivity.this,SkipDaysActivity.class));

            }
        });


        AddSlotBinding asb = AddSlotBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(ManageSlotActivity.this);
        dialog.setContentView(asb.getRoot());

        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        lp1.copyFrom(dialog.getWindow().getAttributes());
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
        asb.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot = asb.slotTime.getText().toString();

                if (slot.isEmpty()) {
                    Toast.makeText(ManageSlotActivity.this, "Please enter valid slot", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                progressDialog.show();
                slots.add(slot);

                Integer[] slotArray = new Integer[slots.size()];

                for (int i = 0; i < slots.size(); i++) {
                    slotArray[i] = Integer.valueOf(slots.get(i));
                }

                Arrays.sort(slotArray);

                HashMap<String, Object> slot_map = new HashMap<>();
                for (int i = 0; i < slots.size(); i++) {
                    slot_map.put(String.valueOf(i + 1), String.valueOf(slotArray[i]));
                }

                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("timeSlots").updateChildren(slot_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            makeList();


                        }


                    }
                });


            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();


            }
        });


    }

    public void makeList() {
        progressDialog.show();
        slots.clear();


        FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("timeSlots").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot timeSlots) {

                if (timeSlots.exists()) {


                    for (DataSnapshot timeSlot : timeSlots.getChildren()) {

                        slots.add(timeSlot.getValue(String.class));


                    }
                    adapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}