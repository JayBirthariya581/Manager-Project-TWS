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
import com.geartocare.ManageMechanicP.Adapters.TwsModelAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityTwsModelsBinding;
import com.geartocare.ManageMechanicP.databinding.AddCompanyBinding;

import java.util.ArrayList;

public class TwsModelsActivity extends AppCompatActivity {
    ActivityTwsModelsBinding binding;
    ArrayList<String> modelList;
    TwsModelAdapter adapter;
    DatabaseReference DBref;
    CustomProgressDialog progressDialog;
    String companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTwsModelsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getWindow().setStatusBarColor(ContextCompat.getColor(TwsModelsActivity.this, R.color.black));
        companyName = getIntent().getStringExtra("companyName");
        binding.companyHead.setText(companyName);
        progressDialog = new CustomProgressDialog(TwsModelsActivity.this);
        modelList = new ArrayList<>();
        adapter = new TwsModelAdapter(modelList, TwsModelsActivity.this);

        DBref = FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService").child("CompanyList").child(companyName).child("ModelList");
        binding.rvTwsModels.setAdapter(adapter);
        binding.rvTwsModels.setLayoutManager(new LinearLayoutManager(TwsModelsActivity.this));
        binding.rvTwsModels.setHasFixedSize(true);



        AddCompanyBinding acb = AddCompanyBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(TwsModelsActivity.this);
        dialog.setContentView(acb.getRoot());

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




        acb.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = acb.name.getText().toString();

                if(name.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(TwsModelsActivity.this, "Please enter valid name", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();

                DBref.child(String.valueOf((modelList.size()+1))).setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){


                            modelList.add(name);
                            adapter.notifyDataSetChanged();

                            acb.name.setText("");

                            progressDialog.dismiss();

                        }


                    }
                });

            }
        });




    }



    private void makeList() {
        progressDialog.show();
        modelList.clear();
        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot check_snap) {

                if (check_snap.exists()) {

                    for (DataSnapshot checkItem : check_snap.getChildren()) {

                        modelList.add(checkItem.getValue(String.class));

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