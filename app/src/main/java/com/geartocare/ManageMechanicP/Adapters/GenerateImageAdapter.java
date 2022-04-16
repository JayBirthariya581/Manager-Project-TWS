package com.geartocare.ManageMechanicP.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.geartocare.ManageMechanicP.Activities.ImageViewerActivity;
import com.geartocare.ManageMechanicP.R;

import java.util.ArrayList;

public class GenerateImageAdapter extends RecyclerView.Adapter<GenerateImageAdapter.MyViewHolder> {
    ArrayList<String> links;
    Context context;


    public GenerateImageAdapter(ArrayList<String> links, Context context) {
        this.links = links;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.gen_image_card,parent,false);

        return new GenerateImageAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(links.get(position)).placeholder(R.drawable.manage_icon).into(holder.svIamge);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ImageViewerActivity.class).putExtra("imageUrl",links.get(position)));

            }
        });
    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView svIamge;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            svIamge = itemView.findViewById(R.id.sv_image);
        }
    }

}
