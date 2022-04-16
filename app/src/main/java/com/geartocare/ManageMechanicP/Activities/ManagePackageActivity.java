package com.geartocare.ManageMechanicP.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.ManagePackageAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelPackage;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityManagePackageBinding;
import com.geartocare.ManageMechanicP.databinding.AddCouponBinding;

import java.util.ArrayList;

public class ManagePackageActivity extends AppCompatActivity {
    ActivityManagePackageBinding binding;
    ManagePackageAdapter adapter;
    CustomProgressDialog progressDialog;
    ArrayList<ModelPackage> packages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagePackageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ManagePackageActivity.this, R.color.black));
        progressDialog = new CustomProgressDialog(ManagePackageActivity.this);

        packages = new ArrayList<>();
        adapter = new ManagePackageAdapter(packages, ManagePackageActivity.this);

        binding.rvPackage.setHasFixedSize(true);
        binding.rvPackage.setAdapter(adapter);
        binding.rvPackage.setLayoutManager(new LinearLayoutManager(ManagePackageActivity.this));







        makeList();


        ActivityResultLauncher<Intent> lau = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if(result.getResultCode()== Activity.RESULT_OK){



                    makeList();

                }


            }
        });


        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lau.launch(new Intent(ManagePackageActivity.this,CreatePackageActivity.class).putExtra("type","create"));
            }
        });


    }

    private void makeList() {
        progressDialog.show();
        packages.clear();

        FirebaseDatabase.getInstance().getReference("AppManager").child("PackageManager").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot packageList) {

                if (packageList.exists()) {

                    for (DataSnapshot pack : packageList.getChildren()) {
                        ModelPackage m = pack.getValue(ModelPackage.class);


                        packages.add(m);


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