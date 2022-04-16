package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityEditCustomerDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EditCustomerDetails extends AppCompatActivity {
    ActivityEditCustomerDetailsBinding binding;
    DatabaseReference DBref;
    String uid;
    CustomProgressDialog dialog;
    String[] name_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCustomerDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(EditCustomerDetails.this, R.color.black));

        uid = getIntent().getStringExtra("uid");
        dialog = new CustomProgressDialog(EditCustomerDetails.this);


        DBref = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        showCustomerDetails();


        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                HashMap<String,Object> m = new HashMap<>();

                m.put("fullName",binding.FullNameRegister.getText().toString());
                m.put("phone",binding.phoneRegister.getText().toString());
                m.put("email",binding.emailRegsiter.getText().toString());

                DBref.updateChildren(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            Intent i = new Intent(EditCustomerDetails.this, CustomerDisplayActivity.class);
                            i.putExtra("uid",uid);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();

                        }


                    }
                });

            }
        });




    }

    private void showCustomerDetails() {

        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    binding.FullNameCustomerDisplay.getEditText().setText(snapshot.child("fullName").getValue(String.class));
                    binding.phoneCustomerDisplay.getEditText().setText(snapshot.child("phone").getValue(String.class));

                    binding.emailCustomerDisplay.getEditText().setText(snapshot.child("email").getValue(String.class));






                }else{

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }


}