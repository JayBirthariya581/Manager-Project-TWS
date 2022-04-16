package com.geartocare.ManageMechanicP.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geartocare.ManageMechanicP.Models.ModelPartRecord;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.CardPartRecordBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class PartRecordAdapter extends RecyclerView.Adapter<PartRecordAdapter.MyViewHolder> {
    Context context;
    ArrayList<ModelPartRecord> mlist;
    HashMap<String,Object> updated_parts;

    public HashMap<String, Object> getUpdated_parts() {
        return updated_parts;
    }

    public void setUpdated_parts(HashMap<String, Object> updated_parts) {
        this.updated_parts = updated_parts;
    }

    public PartRecordAdapter(Context context, ArrayList<ModelPartRecord> mlist) {
        this.context = context;
        this.mlist = mlist;
        updated_parts = new HashMap<>();
    }

    public ArrayList<ModelPartRecord> getMlist() {
        return mlist;
    }

    public void setMlist(ArrayList<ModelPartRecord> mlist) {
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PartRecordAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_part_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelPartRecord record = mlist.get(position);


        holder.binding.partName.setText(record.getPartID());
        holder.binding.available.setText(record.getAvailable());
        holder.binding.used.setText(record.getUsed());
        holder.binding.originalCount.setText(record.getOriginalCount());


        holder.binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.available.setText("0");
                holder.binding.used.setText("0");
                holder.binding.originalCount.setText("0");

                updated_parts.put(record.getPartID(),null);
            }
        });

        holder.binding.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer v = Integer.valueOf(holder.binding.originalCount.getText().toString()) + 1;
                String a =String.valueOf(v - Integer.valueOf(record.getUsed()));
                mlist.get(position).setAvailable(a);
                mlist.get(position).setOriginalCount(v.toString());
                holder.binding.available.setText(a);
                holder.binding.originalCount.setText(String.valueOf(v));
                updated_parts.put(record.getPartID(),mlist.get(position));
            }
        });

        holder.binding.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Integer oc = Integer.valueOf(holder.binding.originalCount.getText().toString());

                if (oc > Integer.valueOf(record.getUsed())) {
                    Integer v = oc - 1;
                    if (v >= 0) {
                        String a =String.valueOf(v - Integer.valueOf(record.getUsed()));
                        mlist.get(position).setAvailable(a);
                        mlist.get(position).setOriginalCount(v.toString());
                        holder.binding.available.setText(String.valueOf(v - Integer.valueOf(record.getUsed())));
                        holder.binding.originalCount.setText(String.valueOf(v));
                        updated_parts.put(record.getPartID(),mlist.get(position));
                    }
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardPartRecordBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardPartRecordBinding.bind(itemView);
        }
    }
}
