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
import com.geartocare.ManageMechanicP.Adapters.AssingMechanicAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelMechanic;
import com.geartocare.ManageMechanicP.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAssignMechanicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AssingMechanicAdapter adapter;
    private ArrayList<ModelMechanic> assignMechanicList;
    DatabaseReference DBr;
    SwipeRefreshLayout srl;
    HashMap<String,String> serviceDetails;
    CustomProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assign_mechanic);
        srl = findViewById(R.id.swiperefresh);
        progressBar = new CustomProgressDialog(ListAssignMechanicActivity.this);
        getWindow().setStatusBarColor(ContextCompat.getColor(ListAssignMechanicActivity.this, R.color.black));



        /*------------------------------Variables---------------------------------------*/
        serviceDetails = (HashMap<String, String>) getIntent().getSerializableExtra("serviceDetails");


        DBr = FirebaseDatabase.getInstance().getReference("mechanics");

        /*------------------------------Variables end---------------------------------------*/

        recyclerView = findViewById(R.id.rv_assign_mechanic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListAssignMechanicActivity.this));


        assignMechanicList = new ArrayList<>();
        adapter = new AssingMechanicAdapter(assignMechanicList, ListAssignMechanicActivity.this,serviceDetails);

        adapter.setServiceDetails(serviceDetails);

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

  /*  @Override
    public void onBackPressed() {
        Intent i = new Intent(ListAssignMechanicActivity.this, BookingDetailActivity.class);
        i.putExtra("phone", phone);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
        finish();
    }*/

    private void makelist() {
        progressBar.show();
        assignMechanicList.clear();

        DBr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ModelMechanic modelCustomer = dataSnapshot.getValue(ModelMechanic.class);
                        assignMechanicList.add(modelCustomer);
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