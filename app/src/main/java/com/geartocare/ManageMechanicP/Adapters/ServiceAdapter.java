package com.geartocare.ManageMechanicP.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.geartocare.ManageMechanicP.Activities.BookingPartsActivity;
import com.geartocare.ManageMechanicP.Activities.GenerateImageActivity;
import com.geartocare.ManageMechanicP.Activities.ServiceImagesActivity;
import com.geartocare.ManageMechanicP.Models.ModelBooking;
import com.geartocare.ManageMechanicP.Models.ModelService;
import com.geartocare.ManageMechanicP.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {


    ArrayList<ModelService> mlist;
    Context context;
    SimpleDateFormat sdf;

    HashMap<String,String> userDetails;
    public ServiceAdapter(ArrayList<ModelService> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }


    public HashMap<String, String> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(HashMap<String, String> userDetails) {
        this.userDetails = userDetails;
    }



    @NotNull
    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_service, parent, false);

        return new ServiceAdapter.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NotNull ServiceAdapter.MyViewHolder holder, int position) {
        ModelService m = mlist.get(position);
        holder.date.setText(sdf.format(Long.valueOf(m.getDate())));
        holder.time.setText(m.getTime());
        holder.vehicleNo.setText(userDetails.get("VhNo"));
        holder.address.setText(m.getLocation().getTxt());

        holder.getPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, GenerateImageActivity.class);
                ModelBooking mb = new ModelBooking();


                mb.setUid(userDetails.get("uid"));
                mb.setMechanicID(m.getMechanicID());
                mb.setStatus(m.getStatus());
                mb.setVehicleID(userDetails.get("VehicleID"));
                mb.setServiceID(m.getServiceID());

                i.putExtra("serviceDetails", mb);

                context.startActivity(i);

            }
        });


        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("LatLng", m.getLocation().getLat() + "," + m.getLocation().getLng());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "LatLng copied to clip board", Toast.LENGTH_SHORT).show();


            }
        });

        holder.seeImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ServiceImagesActivity.class);

                ModelBooking mb = new ModelBooking();
                mb.setServiceID(m.getServiceID());
                mb.setVehicleID(userDetails.get("VehicleID"));
                mb.setUid(userDetails.get("uid"));
                mb.setStatus(m.getStatus());
                mb.setMechanicID(m.getMechanicID());
                intent.putExtra("serviceDetails", mb);

                context.startActivity(intent);
            }
        });


        holder.parts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookingPartsActivity.class);

                ModelBooking mb = new ModelBooking();
                mb.setServiceID(m.getServiceID());
                mb.setVehicleID(userDetails.get("VehicleID"));
                mb.setUid(userDetails.get("uid"));
                mb.setStatus(m.getStatus());
                mb.setMechanicID(m.getMechanicID());
                intent.putExtra("serviceDetails", mb);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, time, vehicleNo, address;
        Button seeImages, getPdf;
        MaterialCardView parts;


        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks

            date = itemView.findViewById(R.id.serviceCard_date);
            time = itemView.findViewById(R.id.serviceCard_time);
            vehicleNo = itemView.findViewById(R.id.serviceCard_vehicleNo);
            address = itemView.findViewById(R.id.serviceCard_Address);
            seeImages = itemView.findViewById(R.id.cardMechanic_seeImages_btn);
            getPdf = itemView.findViewById(R.id.getPdf);
            parts = itemView.findViewById(R.id.parts);

        }
    }


}
