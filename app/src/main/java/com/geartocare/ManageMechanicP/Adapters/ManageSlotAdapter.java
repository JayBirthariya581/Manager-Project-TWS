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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.CardCouponBinding;
import com.geartocare.ManageMechanicP.databinding.CardDeleteBinding;
import com.geartocare.ManageMechanicP.databinding.CardSlotBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ManageSlotAdapter extends RecyclerView.Adapter<ManageSlotAdapter.MyViewHolder> {
    ArrayList<String> mList;
    Context context;
    CustomProgressDialog progressDialog;
    CardDeleteBinding db;
    Dialog dialog;


    public ManageSlotAdapter(ArrayList<String> mList, Context context) {
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
    public ManageSlotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManageSlotAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_slot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageSlotAdapter.MyViewHolder holder, int position) {
        String slot = mList.get(position);

        holder.binding.slot.setText(slot);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog.show();

                db.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        progressDialog.show();


                        FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("timeSlots").child(String.valueOf(position + 1)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {


                                    mList.clear();


                                    FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("timeSlots").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot timeSlots) {

                                            if (timeSlots.exists()) {


                                                for (DataSnapshot timeSlot : timeSlots.getChildren()) {

                                                    mList.add(timeSlot.getValue(String.class));


                                                }

                                                Integer[] slotArray = new Integer[mList.size()];

                                                for (int i = 0; i < mList.size(); i++) {
                                                    slotArray[i] = Integer.valueOf(mList.get(i));
                                                }

                                                Arrays.sort(slotArray);

                                                HashMap<String, Object> slot_map = new HashMap<>();
                                                for (int i = 0; i < mList.size(); i++) {
                                                    slot_map.put(String.valueOf(i + 1), String.valueOf(slotArray[i]));
                                                }


                                                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("timeSlots").updateChildren(slot_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {


                                                            notifyDataSetChanged();
                                                            progressDialog.dismiss();


                                                        }

                                                    }
                                                });


                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


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
        CardSlotBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = CardSlotBinding.bind(itemView);
        }
    }
}
