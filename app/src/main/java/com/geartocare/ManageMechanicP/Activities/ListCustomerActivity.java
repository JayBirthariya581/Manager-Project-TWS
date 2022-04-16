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
import com.geartocare.ManageMechanicP.Models.ModelCustomer;
import com.geartocare.ManageMechanicP.Adapters.MyAdapter;
import com.geartocare.ManageMechanicP.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListCustomerActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<ModelCustomer> modelCustomerList;
    DatabaseReference DBr = FirebaseDatabase.getInstance().getReference("Users");
    SwipeRefreshLayout srl;
    CustomProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);
        getWindow().setStatusBarColor(ContextCompat.getColor(ListCustomerActivity.this,R.color.black));
        srl=findViewById(R.id.swiperefresh);
        progressBar = new CustomProgressDialog(ListCustomerActivity.this);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListCustomerActivity.this));


        modelCustomerList = new ArrayList<>();
        adapter = new MyAdapter(modelCustomerList,ListCustomerActivity.this);


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
        modelCustomerList.clear();

        DBr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ModelCustomer modelCustomer = dataSnapshot.getValue(ModelCustomer.class);
                        modelCustomerList.add(modelCustomer);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.dismiss();

                }
                else{
                    progressBar.dismiss();
                }



            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }


}