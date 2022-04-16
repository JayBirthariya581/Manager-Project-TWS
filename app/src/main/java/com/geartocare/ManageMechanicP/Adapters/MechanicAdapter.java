package com.geartocare.ManageMechanicP.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geartocare.ManageMechanicP.Activities.MechanicDisplayActivity;
import com.geartocare.ManageMechanicP.Models.ModelMechanic;
import com.geartocare.ManageMechanicP.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MechanicAdapter extends RecyclerView.Adapter<MechanicAdapter.MyViewHolder>{


    ArrayList<ModelMechanic> mlist;
    Context context;

    public MechanicAdapter(ArrayList<ModelMechanic> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }


    @NotNull
    @Override
    public MechanicAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_mechanic,parent,false);

        return new MechanicAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull MechanicAdapter.MyViewHolder holder, int position) {
        ModelMechanic m = mlist.get(position);
        holder.name.setText(m.getFirstName()+" "+m.getLastName());
        holder.mechanicID.setText(m.getMechanicID());
        holder.phone.setText(m.getPhone());
        holder.email.setText(m.getEmail());
        holder.address.setText(m.getAddress());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MechanicDisplayActivity.class)
                        .putExtra("mechanicID",m.getMechanicID())
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone,email,address,mechanicID;
        Button viewDetails,parts;
        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks
           name = itemView.findViewById(R.id.mech_name);
           phone = itemView.findViewById(R.id.mech_phone);
           email = itemView.findViewById(R.id.mech_email);
           mechanicID = itemView.findViewById(R.id.mech_id);
           address = itemView.findViewById(R.id.mech_address);
           parts = itemView.findViewById(R.id.parts);

        }
    }
}
