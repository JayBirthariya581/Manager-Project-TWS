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
import com.geartocare.ManageMechanicP.Adapters.TwsChecklistAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivityTwsCheckListBinding;
import com.geartocare.ManageMechanicP.databinding.AddSlotBinding;
import com.geartocare.ManageMechanicP.databinding.AddTwsCheckBinding;

import java.util.ArrayList;

public class TwsCheckListActivity extends AppCompatActivity {
    ActivityTwsCheckListBinding binding;
    TwsChecklistAdapter adapter;
    ArrayList<String> checkList;
    DatabaseReference DBref;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTwsCheckListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(TwsCheckListActivity.this, R.color.black));
        progressDialog = new CustomProgressDialog(TwsCheckListActivity.this);
        checkList = new ArrayList<>();
        adapter = new TwsChecklistAdapter(TwsCheckListActivity.this, checkList);
        DBref = FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService").child("serviceCheckList");
        binding.rvServiceCl.setHasFixedSize(true);
        binding.rvServiceCl.setAdapter(adapter);
        binding.rvServiceCl.setLayoutManager(new LinearLayoutManager(TwsCheckListActivity.this));


        makeList();
        AddTwsCheckBinding atcb = AddTwsCheckBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(TwsCheckListActivity.this);
        dialog.setContentView(atcb.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);



        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
            }
        });


        atcb.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String checkItem = atcb.checkItem.getText().toString();

                if(checkItem.isEmpty()){
                    Toast.makeText(TwsCheckListActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                atcb.checkItem.setText("");
                progressDialog.show();
                DBref.child(String.valueOf(checkList.size())).setValue(checkItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            checkList.add(checkItem);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }


                    }
                });


            }
        });






    }

    private void makeList() {
        progressDialog.show();
        checkList.clear();
        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot check_snap) {

                if (check_snap.exists()) {

                    for (DataSnapshot checkItem : check_snap.getChildren()) {

                        checkList.add(checkItem.getValue(String.class));

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