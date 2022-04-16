package com.geartocare.ManageMechanicP.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.geartocare.ManageMechanicP.databinding.ActivityServiceEditBinding;

public class ServiceEditActivity extends AppCompatActivity {
    ActivityServiceEditBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityServiceEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());








    }
}