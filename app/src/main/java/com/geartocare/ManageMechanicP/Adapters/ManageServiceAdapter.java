package com.geartocare.ManageMechanicP.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geartocare.ManageMechanicP.Activities.EditTwsActivity;
import com.geartocare.ManageMechanicP.Activities.TwsCheckListActivity;
import com.geartocare.ManageMechanicP.Activities.TwsCompanyActivity;
import com.geartocare.ManageMechanicP.Activities.TwsPricingActivity;
import com.geartocare.ManageMechanicP.Models.ModelTwoWheelerService;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.CardCouponBinding;
import com.geartocare.ManageMechanicP.databinding.CardManageServiceBinding;

import java.util.ArrayList;

public class ManageServiceAdapter extends RecyclerView.Adapter<ManageServiceAdapter.MyViewHolder> {
    ArrayList<ModelTwoWheelerService> mList;
    Context context;


    public ManageServiceAdapter(ArrayList<ModelTwoWheelerService> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ManageServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManageServiceAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_manage_service,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageServiceAdapter.MyViewHolder holder, int position) {
        ModelTwoWheelerService tws = mList.get(position);

        holder.binding.serviceName.setText(tws.getServiceName());
        holder.binding.serviceDescription.setText(tws.getServiceDescription());
        holder.binding.servicePrice.setText(tws.getServicePrice());

        holder.binding.checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TwsCheckListActivity.class));

            }
        });

        holder.binding.companyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, TwsCompanyActivity.class));
            }
        });


        holder.binding.pricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, TwsPricingActivity.class));
            }
        });



        holder.binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, EditTwsActivity.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CardManageServiceBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = CardManageServiceBinding.bind(itemView);
        }
    }
}
