package com.geartocare.ManageMechanicP.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.TwBrandCardBinding;


import java.util.ArrayList;

public class TwBrandListAdapter extends RecyclerView.Adapter<TwBrandListAdapter.MyViewHoldeer> {
    ArrayList<String> brandList;
    Context context;
    EditText brandTv,modelTv;
    AlertDialog alertDialog;
    TwModelListAdapter modelListAdapter;
    DataSnapshot companyList;

    public void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    public TwBrandListAdapter(Context context,ArrayList<String> brandList , EditText brandTv, EditText modelTv) {
        this.brandList = brandList;
        this.context = context;
        this.brandTv = brandTv;
        this.modelTv = modelTv;
    }

    public DataSnapshot getCompanyList() {
        return companyList;
    }

    public void setCompanyList(DataSnapshot companyList) {
        this.companyList = companyList;
    }

    public TwModelListAdapter getModelListAdapter() {
        return modelListAdapter;
    }

    public void setModelListAdapter(TwModelListAdapter modelListAdapter) {
        this.modelListAdapter = modelListAdapter;
    }

    @NonNull
    @Override
    public MyViewHoldeer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.tw_brand_card,parent,false);
        return new MyViewHoldeer(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoldeer holder, int position) {

        holder.binding.brandName.setText(brandList.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brandTv.setText(brandList.get(position));
                modelListAdapter.getModelList().clear();
                for(DataSnapshot model : companyList.child(brandList.get(position)).child("ModelList").getChildren()){

                    modelListAdapter.getModelList().add(model.getValue(String.class));


                }

                modelTv.setText(modelListAdapter.getModelList().get(0));







                alertDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    class MyViewHoldeer extends RecyclerView.ViewHolder{
        TwBrandCardBinding binding;
        public MyViewHoldeer(@NonNull View itemView) {
            super(itemView);
            binding = TwBrandCardBinding.bind(itemView);
        }
    }

}
