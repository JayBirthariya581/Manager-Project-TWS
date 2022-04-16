package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.Mechanic;
import com.geartocare.ManageMechanicP.R;

import org.jetbrains.annotations.NotNull;

public class MechanicRegisterActivity extends AppCompatActivity {

    TextInputLayout firstNameL,lastNameL,phoneL,addressL,emailL,passwordL;
    DatabaseReference DBr;
    Button addMechanicRegister;
    CustomProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_register);
        getWindow().setStatusBarColor(ContextCompat.getColor(MechanicRegisterActivity.this,R.color.black));
        dialog = new CustomProgressDialog(MechanicRegisterActivity.this);


        /*------------------------------Hooks start---------------------------------------*/
        firstNameL = findViewById(R.id.FirstNameMechanicRegister);
        lastNameL = findViewById(R.id.LastNameMechanicRegister);
        phoneL = findViewById(R.id.phoneMechanicRegister);
        emailL = findViewById(R.id.emailMechanicRegister);
        addressL = findViewById(R.id.addressMechanicRegister);
        addMechanicRegister = findViewById(R.id.addMechanicRegister);
        passwordL = findViewById(R.id.passwordMechanicRegisterL);



        /*------------------------------Hooks end---------------------------------------*/


        /*------------------------------Variables---------------------------------------*/

            DBr= FirebaseDatabase.getInstance().getReference("mechanics");



        /*------------------------------Variables end---------------------------------------*/






        addMechanicRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerMechanic();

            }
        });













    }


    public void registerMechanic(){
        dialog.show();






        String firstName = firstNameL.getEditText().getText().toString();
        String lastname = lastNameL.getEditText().getText().toString();
        String phone = phoneL.getEditText().getText().toString();
        String email = emailL.getEditText().getText().toString();
        String address = addressL.getEditText().getText().toString();
        String password = passwordL.getEditText().getText().toString();

        if(firstName.isEmpty() || lastname.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty() || password.isEmpty()){
            Toast.makeText(MechanicRegisterActivity.this, "Fill all the details", Toast.LENGTH_SHORT).show();
            return;
        }

        Query checkMechanic = DBr.orderByChild("phone").equalTo(phone);

        checkMechanic.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MechanicRegisterActivity.this);
                    builder.setMessage("Mechanic with this phone already exist");
                    builder.setIcon(R.drawable.ic_mechanic);
                    builder.show();


                }else{
                    dialog.show();


                    String mechanicID = DBr.push().getKey();
                    Mechanic mechanic = new Mechanic(firstName,lastname,phone,email,address,mechanicID,"Available",password,"0");

                    DBr.child(mechanicID).setValue(mechanic).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(MechanicRegisterActivity.this, "Mechanic added successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MechanicRegisterActivity.this,MechanicDisplayActivity.class)
                                        .putExtra("mechanicID",mechanicID)
                                );
                                finish();
                            }

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



}