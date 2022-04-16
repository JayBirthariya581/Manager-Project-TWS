package com.geartocare.ManageMechanicP.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geartocare.ManageMechanicP.Activities.CustomerDisplayActivity;
import com.geartocare.ManageMechanicP.Models.ModelCustomer;
import com.geartocare.ManageMechanicP.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    ArrayList<ModelCustomer> mlist;
    Context context;

    public MyAdapter(ArrayList<ModelCustomer> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;

    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.customer_card,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {

        ModelCustomer m = mlist.get(position);
        holder.name.setText(m.getFullName());
        holder.email.setText(m.getEmail());
        holder.phone.setText(m.getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CustomerDisplayActivity.class)
                                    .putExtra("uid",m.getUid())
                );
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,email,phone;

        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks
            name = itemView.findViewById(R.id.cust_name);
            email= itemView.findViewById(R.id.cust_email);
            phone = itemView.findViewById(R.id.cust_phone);



        }
    }
}
