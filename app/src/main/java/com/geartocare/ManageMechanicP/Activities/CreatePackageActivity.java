package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Helpers.PackageHelper;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityCreatePackageBinding;

import java.util.HashMap;

public class CreatePackageActivity extends AppCompatActivity {
    ActivityCreatePackageBinding binding;
    CustomProgressDialog dialog;
    DatabaseReference DBref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePackageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(CreatePackageActivity.this, R.color.black));

        DBref = FirebaseDatabase.getInstance().getReference("AppManager").child("PackageManager");
        dialog = new CustomProgressDialog(CreatePackageActivity.this);


        if(getIntent().getStringExtra("type").equals("edit")){


            HashMap<String,String> packageDetails = (HashMap<String, String>) getIntent().getSerializableExtra("packageDetails");

            binding.packageName.getEditText().setText(packageDetails.get("name"));
            binding.packageCost.getEditText().setText(packageDetails.get("packageCost"));
            binding.packageDescription.getEditText().setText(packageDetails.get("description"));
            binding.packageVehicle.getEditText().setText(packageDetails.get("vehicleCount"));
            binding.packageSvCost.getEditText().setText(packageDetails.get("serviceCost"));
            binding.packageValidity.getEditText().setText(packageDetails.get("validity"));

            binding.ycl.setText("Update");




        }else{
            binding.ycl.setText("Create");
        }



        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processUpdate();
            }
        });


    }

    private void processUpdate() {
        dialog.show();
        String packageName = binding.packageName.getEditText().getText().toString();
        String packageDescription = binding.packageDescription.getEditText().getText().toString();
        String packageCost = binding.packageCost.getEditText().getText().toString();
        String packageSvCost = binding.packageSvCost.getEditText().getText().toString();
        String packageValidity = binding.packageValidity.getEditText().getText().toString();
        String packageVehicle = binding.packageVehicle.getEditText().getText().toString();


        if(packageName.isEmpty() || packageDescription.isEmpty() || packageCost.isEmpty() || packageSvCost.isEmpty() || packageValidity.isEmpty() || packageVehicle.isEmpty()){
            Toast.makeText(CreatePackageActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        PackageHelper packageHelper = new PackageHelper(packageName,packageName,packageCost,packageDescription,packageSvCost,packageValidity,packageVehicle);


        DBref.child(packageName).setValue(packageHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){


                    Toast.makeText(CreatePackageActivity.this, "Package Created Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    setResult(Activity.RESULT_OK);
                    finish();

                }else{

                    Toast.makeText(CreatePackageActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }

            }
        });





    }


}