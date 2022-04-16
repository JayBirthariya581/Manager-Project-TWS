package com.geartocare.ManageMechanicP.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelPrice;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.CardDeleteBinding;
import com.geartocare.ManageMechanicP.databinding.CardPriceBinding;

import java.util.ArrayList;

public class TwsPricingAdapter extends RecyclerView.Adapter<TwsPricingAdapter.MyViewHolder> {
    ArrayList<ModelPrice> mlist;
    Context context;
    CustomProgressDialog progressDialog;
    CardDeleteBinding db;
    Dialog dialog;

    public TwsPricingAdapter(ArrayList<ModelPrice> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;

        db = CardDeleteBinding.inflate(((Activity) context).getLayoutInflater());
        dialog = new Dialog(context);
        dialog.setContentView(db.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TwsPricingAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_price,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelPrice price = mlist.get(position);


        holder.binding.type.setText(price.getType());
        holder.binding.value.setText(price.getPrice());



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                db.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        progressDialog.show();

                        FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService").child("Pricing").child(price.getType()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    mlist.remove(position);
                                    notifyDataSetChanged();
                                    progressDialog.dismiss();


                                }


                            }
                        });

                    }
                });


                dialog.show();


                return false;
            }
        });




    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CardPriceBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardPriceBinding.bind(itemView);

        }
    }
}
