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
import com.geartocare.ManageMechanicP.Adapters.TwsPricingAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelPrice;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityTwsPricingBinding;
import com.geartocare.ManageMechanicP.databinding.AddPriceBinding;
import com.geartocare.ManageMechanicP.databinding.AddTwsCheckBinding;

import java.util.ArrayList;

public class TwsPricingActivity extends AppCompatActivity {
    ActivityTwsPricingBinding binding;
    ArrayList<ModelPrice> priceList;
    TwsPricingAdapter adapter;
    DatabaseReference DBref;
    CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTwsPricingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(TwsPricingActivity.this, R.color.black));
        progressDialog = new CustomProgressDialog(TwsPricingActivity.this);
        priceList = new ArrayList<>();
        adapter = new TwsPricingAdapter(priceList,TwsPricingActivity.this);
        DBref = FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService").child("Pricing");
        binding.rvTwsPricing.setAdapter(adapter);
        binding.rvTwsPricing.setLayoutManager(new LinearLayoutManager(TwsPricingActivity.this));
        binding.rvTwsPricing.setHasFixedSize(true);



        AddPriceBinding apb = AddPriceBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(TwsPricingActivity.this);
        dialog.setContentView(apb.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);


        makeList();


        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

            }
        });



        apb.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = apb.type.getText().toString();
                String value = apb.value.getText().toString();
                if(type.isEmpty() || value.isEmpty()){
                    Toast.makeText(TwsPricingActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                progressDialog.show();

                DBref.child(type).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){


                            ModelPrice mp = new ModelPrice();
                            mp.setType(type);
                            mp.setPrice(value);
                            priceList.add(mp);
                            adapter.notifyDataSetChanged();
                            apb.type.setText("");
                            apb.value.setText("");
                            progressDialog.dismiss();







                        }


                    }
                });





            }
        });


    }

    private void makeList() {
        progressDialog.show();
        priceList.clear();
        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot check_snap) {

                if (check_snap.exists()) {

                    for (DataSnapshot checkItem : check_snap.getChildren()) {
                        ModelPrice p  = new ModelPrice();
                        p.setPrice(checkItem.getValue(String.class));
                        p.setType(checkItem.getKey());
                        priceList.add(p);

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