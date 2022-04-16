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
import com.geartocare.ManageMechanicP.Models.ModelCoupon;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.CardCouponBinding;
import com.geartocare.ManageMechanicP.databinding.CardDeleteBinding;

import java.util.ArrayList;

public class ManageCouponAdapter extends RecyclerView.Adapter<ManageCouponAdapter.MyViewHolder> {
    ArrayList<ModelCoupon> mList;
    Context context;
    CustomProgressDialog progressDialog;
    CardDeleteBinding db;
    Dialog dialog;

    public ManageCouponAdapter(ArrayList<ModelCoupon> mList, Context context) {
        this.mList = mList;
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
    public ManageCouponAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManageCouponAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCouponAdapter.MyViewHolder holder, int position) {
        ModelCoupon coupon = mList.get(position);

        holder.binding.coupon.setText(coupon.getCoupon());
        holder.binding.value.setText(coupon.getValue());


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                db.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        progressDialog.show();

                        FirebaseDatabase.getInstance().getReference("AppManager").child("CouponsAndOffers").child("Codes").child(coupon.getCoupon()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    mList.remove(position);
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
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardCouponBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = CardCouponBinding.bind(itemView);
        }
    }
}
