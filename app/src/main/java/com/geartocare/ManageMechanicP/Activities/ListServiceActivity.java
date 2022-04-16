package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelService;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.Adapters.ServiceAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ListServiceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private ArrayList<ModelService> modelServiceList;
    DatabaseReference DBr ;

    HashMap<String,String> userDetails;
    SwipeRefreshLayout srl;
    CustomProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_service);
        getWindow().setStatusBarColor(ContextCompat.getColor(ListServiceActivity.this,R.color.black));
        srl=findViewById(R.id.swiperefresh);
        progressBar = new CustomProgressDialog(ListServiceActivity.this);

        userDetails = (HashMap<String,String>) getIntent().getSerializableExtra("vehicleDetails");

        /*------------------------------Variables---------------------------------------*/

        DBr = FirebaseDatabase.getInstance().getReference("Users").child(userDetails.get("uid")).child("vehicles").child(userDetails.get("VehicleID")).child("services");

        /*------------------------------Variables end---------------------------------------*/

        recyclerView=findViewById(R.id.recyclerViewListService);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListServiceActivity.this));


        modelServiceList = new ArrayList<>();
        adapter = new ServiceAdapter(modelServiceList,ListServiceActivity.this);
        adapter.setUserDetails(userDetails);


        recyclerView.setAdapter(adapter);




        makelist();

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makelist();
                srl.setRefreshing(false);
            }
        });



    }

    private void makelist() {
        progressBar.show();
        modelServiceList.clear();
        DBr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    modelServiceList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ModelService modelCustomer =dataSnapshot.getValue(ModelService.class);
                        modelServiceList.add(modelCustomer);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.dismiss();
                }else{
                    progressBar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });
    }
}