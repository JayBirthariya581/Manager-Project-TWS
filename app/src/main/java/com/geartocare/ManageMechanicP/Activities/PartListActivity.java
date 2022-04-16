package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
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
import com.geartocare.ManageMechanicP.Adapters.PartListAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelPart;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityPartListBinding;
import com.geartocare.ManageMechanicP.databinding.AddPartBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PartListActivity extends AppCompatActivity {
    ActivityPartListBinding binding;
    PartListAdapter adapter;
    ArrayList<ModelPart> parts;
    DatabaseReference partRef;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(PartListActivity.this, R.color.black));
        parts = new ArrayList<>();
        progressDialog = new CustomProgressDialog(PartListActivity.this);
        adapter = new PartListAdapter(PartListActivity.this, parts);


        partRef = FirebaseDatabase.getInstance().getReference("AppManager").child("Inventory").child("PartList");
        binding.rvParts.setLayoutManager(new LinearLayoutManager(PartListActivity.this));
        binding.rvParts.setAdapter(adapter);
        binding.rvParts.setHasFixedSize(true);


        AddPartBinding apb = AddPartBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(PartListActivity.this);
        dialog.setContentView(apb.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);


        makeList();


        apb.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = apb.name.getText().toString();
                String company = apb.company.getText().toString();
                String op = apb.op.getText().toString();
                String sp = apb.sp.getText().toString();
                String dis = apb.partDis.getText().toString();
                if (name.isEmpty() || sp.isEmpty() || op.isEmpty() || company.isEmpty()) {
                    Toast.makeText(PartListActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();


                progressDialog.show();

                ModelPart modelPart = new ModelPart();
                Double op_in = Double.valueOf(op);
                Double sp_in = Double.valueOf(sp);
                Double dis_per;


                    Double dis_in = sp_in-op_in;

                    dis_per = (dis_in * 100) / op_in;







                modelPart.setName(name);
                modelPart.setCompany(company);
                DecimalFormat numberFormat = new DecimalFormat("#.00");
                modelPart.setDiscount(numberFormat.format(dis_per));
                modelPart.setOriginalPrice(op);
                modelPart.setSellingPrice(sp);

                partRef.child(name + "_" + company).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            progressDialog.dismiss();
                            Toast.makeText(PartListActivity.this, "Part already exists", Toast.LENGTH_SHORT).show();

                        } else {
                            partRef.child(name + "_" + company).setValue(modelPart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        parts.add(modelPart);
                                        adapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                        Toast.makeText(PartListActivity.this, "Part added successfully", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(PartListActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                    apb.name.setText("");
                                    apb.company.setText("");
                                    apb.sp.setText("");
                                    apb.op.setText("");
                                    apb.partDis.setText("-");


                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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

    private void makeList() {
        progressDialog.show();


        partRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot partList) {
                if (partList.exists()) {


                    for (DataSnapshot part_snap : partList.getChildren()) {

                        parts.add(part_snap.getValue(ModelPart.class));

                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();


                } else {
                    progressDialog.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}