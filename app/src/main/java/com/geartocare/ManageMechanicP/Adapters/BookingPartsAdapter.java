package com.geartocare.ManageMechanicP.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geartocare.ManageMechanicP.Models.ModelBookingPart;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.CardPartBookingBinding;

import java.util.ArrayList;

public class BookingPartsAdapter extends RecyclerView.Adapter<BookingPartsAdapter.MyViewHolder> {
    Context context;
    ArrayList<ModelBookingPart> parts;


    public BookingPartsAdapter(Context context, ArrayList<ModelBookingPart> parts) {
        this.context = context;
        this.parts = parts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingPartsAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_part_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelBookingPart mbp = parts.get(position);
        holder.binding.partName.setText(mbp.getPartName());
        holder.binding.count.setText(mbp.getCount());
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardPartBookingBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = CardPartBookingBinding.bind(itemView);
        }
    }
}
