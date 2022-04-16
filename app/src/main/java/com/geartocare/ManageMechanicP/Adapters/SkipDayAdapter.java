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
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.CardDeleteBinding;
import com.geartocare.ManageMechanicP.databinding.CardGeneralDateBinding;


import java.util.ArrayList;

public class SkipDayAdapter extends RecyclerView.Adapter<SkipDayAdapter.MyViewHolder> {
    ArrayList<String> mlist;
    Context context;
    CustomProgressDialog progressDialog;
    CardDeleteBinding db;
    Dialog dialog;

    public SkipDayAdapter(ArrayList<String> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;


        progressDialog = new CustomProgressDialog(context);


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
        return new SkipDayAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_general_date,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String date = mlist.get(position);

        holder.binding.date.setText(date);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                db.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        progressDialog.show();
                        FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("offDays").child(date)
                                .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){


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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardGeneralDateBinding binding;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardGeneralDateBinding.bind(itemView);

        }
    }
}
