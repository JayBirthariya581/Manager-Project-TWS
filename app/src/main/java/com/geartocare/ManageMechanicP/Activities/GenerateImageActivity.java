package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.geartocare.ManageMechanicP.Adapters.GenerateImageAdapter;
import com.geartocare.ManageMechanicP.Models.ModelBooking;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.Models.SingleImage;
import com.geartocare.ManageMechanicP.databinding.ActivityGenerateImageBinding;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GenerateImageActivity extends AppCompatActivity {
    ArrayList<String> links;
    DatabaseReference DBref;
    ActivityGenerateImageBinding binding;

    GenerateImageAdapter adapter;
    String name;
    ModelBooking sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenerateImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(GenerateImageActivity.this, R.color.black));


        sd = (ModelBooking) getIntent().getSerializableExtra("serviceDetails");

        DBref = FirebaseDatabase.getInstance().getReference("Users").child(sd.getUid());


        links = new ArrayList<>();


        adapter = new GenerateImageAdapter(links,GenerateImageActivity.this);


        binding.imageList.setAdapter(adapter);
        binding.imageList.setLayoutManager(new LinearLayoutManager(GenerateImageActivity.this));
        binding.imageList.setHasFixedSize(true);






        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot customer) {

                if(customer.exists()){
                    links.clear();
                    name = customer.child("fullName").getValue(String.class);



                    for(DataSnapshot imageLink : customer.child("vehicles").child(sd.getVehicleID())
                            .child("services").child(sd.getServiceID()).child("images").getChildren()){

                        SingleImage single =  imageLink.getValue(SingleImage.class);

                        links.add(single.getImageUrl());



                    }

                    adapter.notifyDataSetChanged();



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        binding.generatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PdfGenerator.getBuilder()
                        .setContext(GenerateImageActivity.this)
                        .fromViewSource()

                        .fromView(binding.imageList)
                        .setFileName(name+" "+sd.getServiceID())

                        .setFolderName("PDF of services")

                        .openPDFafterGeneration(true)

                        .build(new PdfGeneratorListener() {
                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                            }

                            @Override
                            public void showLog(String log) {
                                super.showLog(log);
                            }

                            @Override
                            public void onStartPDFGeneration() {
                                /*When PDF generation begins to start*/
                            }

                            @Override
                            public void onFinishPDFGeneration() {
                                /*When PDF generation is finished*/
                            }

                            @Override
                            public void onSuccess(SuccessResponse response) {
                                super.onSuccess(response);
                            }
                        });
            }
        });







    }


}