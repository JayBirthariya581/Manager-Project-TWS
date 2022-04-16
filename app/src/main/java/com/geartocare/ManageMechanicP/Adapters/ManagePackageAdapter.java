package com.geartocare.ManageMechanicP.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.geartocare.ManageMechanicP.Activities.CreatePackageActivity;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelPackage;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.AddCouponBinding;
import com.geartocare.ManageMechanicP.databinding.AddEditCardBinding;
import com.geartocare.ManageMechanicP.databinding.CardDeleteBinding;
import com.geartocare.ManageMechanicP.databinding.CardManagePackageBinding;
import com.geartocare.ManageMechanicP.databinding.CardManageServiceBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ManagePackageAdapter extends RecyclerView.Adapter<ManagePackageAdapter.MyViewHolder> {
    ArrayList<ModelPackage> mList;
    Context context;
    CustomProgressDialog progressDialog;
    AddEditCardBinding db;
    Dialog dialog;


    public ManagePackageAdapter(ArrayList<ModelPackage> mList, Context context) {
        this.mList = mList;
        this.context = context;

        progressDialog = new CustomProgressDialog(context);
        db = AddEditCardBinding.inflate(((Activity) context).getLayoutInflater());
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
    public ManagePackageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManagePackageAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_manage_package,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePackageAdapter.MyViewHolder holder, int position) {
        ModelPackage mp = mList.get(position);

        holder.binding.packageName.setText(mp.getName());
        holder.binding.packageDescription.setText(mp.getDescription());
        holder.binding.packagePrice.setText(mp.getCost());
        holder.binding.packageServicePrice.setText(mp.getServiceCost());
        holder.binding.packageValidity.setText(mp.getValidity());
        holder.binding.packageVehicleCount.setText(mp.getVehicleCount());



        db.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> packageDetails = new HashMap<>();
                packageDetails.put("name",mp.getName());
                packageDetails.put("description",mp.getDescription());
                packageDetails.put("packageCost",mp.getCost());
                packageDetails.put("serviceCost",mp.getServiceCost());
                packageDetails.put("vehicleCount",mp.getVehicleCount());
                packageDetails.put("validity",mp.getValidity());

                Intent i = new Intent(context,CreatePackageActivity.class);

                i.putExtra("type","edit");
                i.putExtra("packageDetails",packageDetails);

                context.startActivity(i);
                dialog.dismiss();



            }
        });


        db.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressDialog.show();
                FirebaseDatabase.getInstance().getReference("AppManager").child("PackageManager").child(mp.getName()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
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


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {



                dialog.show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CardManagePackageBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = CardManagePackageBinding.bind(itemView);
        }
    }
}
