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
import com.geartocare.ManageMechanicP.databinding.CardTwsChecklistBinding;

import java.util.ArrayList;

public class TwsChecklistAdapter extends RecyclerView.Adapter<TwsChecklistAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> mList;
    CustomProgressDialog progressDialog;
    CardDeleteBinding db;
    Dialog dialog;


    public TwsChecklistAdapter(Context context, ArrayList<String> mList) {
        this.context = context;
        this.mList = mList;
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
        return new TwsChecklistAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_tws_checklist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String checkItem = mList.get(position);

        holder.binding.checkItem.setText(checkItem);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog.show();

                db.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        progressDialog.show();
                        FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService").child("serviceCheckList")
                                .child(String.valueOf(position)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                    progressDialog.dismiss();
                                }


                            }
                        });


                    }
                });


                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardTwsChecklistBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = CardTwsChecklistBinding.bind(itemView);

        }
    }
}
