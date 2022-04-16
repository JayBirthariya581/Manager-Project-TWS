package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.R;

import org.jetbrains.annotations.NotNull;

public class CustomerDisplayActivity extends AppCompatActivity {
    TextInputLayout fullNameD,phoneD,emailD;
    DatabaseReference DBref;
    String uid;
    Button newBooking,pastBooking,edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_display);
        getWindow().setStatusBarColor(ContextCompat.getColor(CustomerDisplayActivity.this,R.color.black));



        /*------------------------------Hooks start---------------------------------------*/
        fullNameD = findViewById(R.id.FullNameCustomerDisplay);

        emailD = findViewById(R.id.emailCustomerDisplay);
        phoneD = findViewById(R.id.phoneCustomerDisplay);
        newBooking = findViewById(R.id.newBooking);
        pastBooking = findViewById(R.id.pastBooking);
        edit = findViewById(R.id.edit);
        /*addCustomeD = findViewById(R.id.addCustomerRegister);*/



        /*------------------------------Hooks end---------------------------------------*/


        /*------------------------------Variables---------------------------------------*/
         uid = getIntent().getStringExtra("uid");

        /*------------------------------Variables end---------------------------------------*/


        FirebaseDatabase.getInstance().getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    fullNameD.getEditText().setText(snapshot.child("fullName").getValue(String.class));
                    phoneD.getEditText().setText(snapshot.child("phone").getValue(String.class));

                    emailD.getEditText().setText(snapshot.child("email").getValue(String.class));






                }else{

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerDisplayActivity.this, EditCustomerDetails.class)
                .putExtra("uid",uid)
                );
            }
        });


        newBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CustomerDisplayActivity.this,CustomerServiceActivity.class).putExtra("uid",uid));

            }
        });


        pastBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(CustomerDisplayActivity.this,CustomerVehicleActivity.class).putExtra("uid",uid));
            }
        });







    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(CustomerDisplayActivity.this, CustomerActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

        );
        finish();
    }
}