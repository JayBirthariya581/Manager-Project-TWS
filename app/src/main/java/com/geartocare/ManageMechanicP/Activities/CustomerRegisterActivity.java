package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.R;

import java.util.HashMap;

public class CustomerRegisterActivity extends AppCompatActivity {
    TextInputLayout fullNameL, phoneL, emailL;
    Button addCustomer;
    DatabaseReference DBref;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        getWindow().setStatusBarColor(ContextCompat.getColor(CustomerRegisterActivity.this, R.color.black));

        /*------------------------------Hooks start---------------------------------------*/

        fullNameL = findViewById(R.id.FullNameCustomerRegister);
        emailL = findViewById(R.id.emailCustomerRegister);
        phoneL = findViewById(R.id.phoneCustomerRegister);
        addCustomer = findViewById(R.id.addCustomerRegister);
        progressDialog = new CustomProgressDialog(CustomerRegisterActivity.this);
        /*------------------------------Hooks end---------------------------------------*/


        /*------------------------------Variables---------------------------------------*/
        DBref = FirebaseDatabase.getInstance().getReference("Users");


        /*------------------------------Variables end---------------------------------------*/


        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fullNameL.getEditText().getText().toString().equals("") || phoneL.getEditText().getText().toString().equals("") || emailL.getEditText().getText().toString().equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(CustomerRegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phoneL.getEditText().getText().toString().length() != 10) {
                    progressDialog.dismiss();
                    Toast.makeText(CustomerRegisterActivity.this, "Phone no. should of 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressDialog.show();

                    if (fullNameL.getEditText().getText().toString().contains("Mr.") || fullNameL.getEditText().getText().toString().contains("Ms.")) {
                        String Fname = fullNameL.getEditText().getText().toString();
                        String em = emailL.getEditText().getText().toString();
                        String ph = phoneL.getEditText().getText().toString();

                        DatabaseReference DBr = FirebaseDatabase.getInstance().getReference("Users");
                        String uid = DBr.push().getKey();
                        HashMap<String, String> hmap = new HashMap<>();

                        hmap.put("fullName", Fname);

                        hmap.put("email", em);
                        hmap.put("phone", ph);
                        hmap.put("uid", uid);
                        // hmap.put("bookingStatus", "false");
                        /*hmap.put("serviceCount","0");*/


                        DBr.child(uid).setValue(hmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(CustomerRegisterActivity.this, "Customer Added", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CustomerRegisterActivity.this, CustomerDisplayActivity.class).putExtra("uid", uid));

                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(CustomerRegisterActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(CustomerRegisterActivity.this, "Include Mr./Ms. in full name", Toast.LENGTH_SHORT).show();
                        return;
                    }


                }


            }
        });


    }
}