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
import com.geartocare.ManageMechanicP.Adapters.MechanicAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelMechanic;
import com.geartocare.ManageMechanicP.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListMechanicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MechanicAdapter adapter;
    private ArrayList<ModelMechanic> modelMechanicList;
    DatabaseReference DBr ;
    SwipeRefreshLayout srl;
    CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mechanic);
        srl=findViewById(R.id.swiperefresh);
        getWindow().setStatusBarColor(ContextCompat.getColor(ListMechanicActivity.this,R.color.black));

        progressDialog = new CustomProgressDialog(ListMechanicActivity.this);
        /*------------------------------Variables---------------------------------------*/
        String phone = getIntent().getStringExtra("phone");
        DBr = FirebaseDatabase.getInstance().getReference("mechanics");

        /*------------------------------Variables end---------------------------------------*/

        recyclerView=findViewById(R.id.rv_mechanic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListMechanicActivity.this));


        modelMechanicList = new ArrayList<>();
        adapter = new MechanicAdapter(modelMechanicList,ListMechanicActivity.this);

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
        progressDialog.show();
        modelMechanicList.clear();

        DBr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    modelMechanicList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ModelMechanic modelCustomer =dataSnapshot.getValue(ModelMechanic.class);
                        modelMechanicList.add(modelCustomer);
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                }



            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });


    }
}