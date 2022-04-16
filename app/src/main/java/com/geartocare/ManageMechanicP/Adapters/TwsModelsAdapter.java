package com.geartocare.ManageMechanicP.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.CardCompanyBinding;

import java.util.ArrayList;

public class TwsModelsAdapter extends RecyclerView.Adapter<TwsModelsAdapter.MyViewHolder>{
    ArrayList<String> mlist;
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TwsModelsAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_company,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.name.setText(mlist.get(position));



    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CardCompanyBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = CardCompanyBinding.bind(itemView);
        }
    }
}
