package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.databinding.ActivityEditTwsBinding;

import java.util.HashMap;

public class EditTwsActivity extends AppCompatActivity {
    ActivityEditTwsBinding binding;
    DatabaseReference DBref;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTwsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DBref = FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService");
        progressDialog = new CustomProgressDialog(EditTwsActivity.this);

        fillDetails();


        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processUpdate();


            }


        });


    }


    private void processUpdate() {
        progressDialog.show();
        String serviceName = binding.serviceName.getEditText().getText().toString();
        String servicePrice = binding.servicePrice.getEditText().getText().toString();
        String serviceDescription = binding.serviceDescription.getEditText().getText().toString();


        if (serviceName.isEmpty() || servicePrice.isEmpty() || serviceDescription.isEmpty()) {
            Toast.makeText(EditTwsActivity.this, "Please Fill all details", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        HashMap<String, Object> serviceDetails = new HashMap<>();


        serviceDetails.put("serviceName", serviceName);
        serviceDetails.put("serviceDescription", serviceDescription);
        serviceDetails.put("servicePrice", servicePrice);

        DBref.updateChildren(serviceDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if(task.isSuccessful()){
                    binding.serviceName.getEditText().setText(serviceName);
                    binding.serviceDescription.getEditText().setText(servicePrice);
                    binding.servicePrice.getEditText().setText(serviceDescription);
                    progressDialog.dismiss();


                }
            }
        });


    }

    private void fillDetails() {
        progressDialog.show();

        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot tws_snap) {

                if (tws_snap.exists()) {
                    String serviceName = tws_snap.child("serviceName").getValue(String.class);
                    String servicePrice = tws_snap.child("servicePrice").getValue(String.class);
                    String serviceDescription = tws_snap.child("serviceDescription").getValue(String.class);
                    binding.serviceName.getEditText().setText(serviceName);
                    binding.serviceDescription.getEditText().setText(servicePrice);
                    binding.servicePrice.getEditText().setText(serviceDescription);


                }


                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}