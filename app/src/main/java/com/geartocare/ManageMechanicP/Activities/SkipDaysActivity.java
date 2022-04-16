package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.SkipDayAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.ActivitySkipDaysBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SkipDaysActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ActivitySkipDaysBinding binding;
    ArrayList<String> skipDays;
    DatabaseReference DBref;
    SkipDayAdapter adapter;
    CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySkipDaysBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(SkipDaysActivity.this, R.color.black));
        DBref = FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("offDays");
        progressDialog = new CustomProgressDialog(SkipDaysActivity.this);
        skipDays = new ArrayList<>();
        adapter = new SkipDayAdapter(skipDays,SkipDaysActivity.this);

        binding.rvDaysOff.setHasFixedSize(true);
        binding.rvDaysOff.setAdapter(adapter);
        binding.rvDaysOff.setLayoutManager(new LinearLayoutManager(SkipDaysActivity.this));

        makeList();

        binding.add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });



    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void makeList() {
        progressDialog.show();
        skipDays.clear();

        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot skipList) {

                if(skipList.exists()){

                    for(DataSnapshot sd : skipList.getChildren()){

                        skipDays.add(sd.getValue(String.class));


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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        progressDialog.show();
        Calendar combinedCal = Calendar.getInstance();
        combinedCal.set(year, month, dayOfMonth);
        long timeStamp = combinedCal.getTime().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");

        DBref.child(String.valueOf(sdf.format(timeStamp))).setValue(sdf.format(timeStamp)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    skipDays.add(sdf.format(timeStamp));
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }

            }
        });

    }
}