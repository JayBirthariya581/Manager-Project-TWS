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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.ManageCouponAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelCoupon;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityManageCouponBinding;
import com.geartocare.ManageMechanicP.databinding.AddCouponBinding;

import java.util.ArrayList;

public class ManageCouponActivity extends AppCompatActivity {
    ActivityManageCouponBinding binding;
    ManageCouponAdapter adapter;
    ArrayList<ModelCoupon> coupons;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageCouponBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ManageCouponActivity.this, R.color.black));
        progressDialog = new CustomProgressDialog(ManageCouponActivity.this);
        coupons = new ArrayList<>();
        adapter = new ManageCouponAdapter(coupons, ManageCouponActivity.this);


        binding.rvCoupons.setLayoutManager(new LinearLayoutManager(ManageCouponActivity.this));
        binding.rvCoupons.setAdapter(adapter);
        binding.rvCoupons.setHasFixedSize(true);


        makeList();


        AddCouponBinding acb = AddCouponBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(ManageCouponActivity.this);
        dialog.setContentView(acb.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);


        acb.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = acb.code.getText().toString();
                String v = acb.value.getText().toString();
                if (c.isEmpty() || v.isEmpty()) {
                    Toast.makeText(ManageCouponActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                progressDialog.show();
                FirebaseDatabase.getInstance().getReference("AppManager").child("CouponsAndOffers").child("Codes").child(c).setValue(v).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            ModelCoupon m = new ModelCoupon();
                            m.setValue("-" + v.trim());
                            m.setCoupon(c);
                            coupons.add(m);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();


                        }


                    }
                });

                Toast.makeText(ManageCouponActivity.this, c + "  " + v, Toast.LENGTH_SHORT).show();
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
        coupons.clear();

        FirebaseDatabase.getInstance().getReference("AppManager").child("CouponsAndOffers").child("Codes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot codes) {

                if (codes.exists()) {


                    for (DataSnapshot code : codes.getChildren()) {

                        ModelCoupon m = new ModelCoupon();

                        m.setCoupon(code.getKey());
                        m.setValue(code.getValue(String.class));


                        coupons.add(m);
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