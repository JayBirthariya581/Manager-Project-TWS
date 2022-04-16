package com.geartocare.ManageMechanicP.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Activities.BookingDetailActivity;
import com.geartocare.ManageMechanicP.Activities.EditServiceActivity;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelBookingUser;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.AddEditCardBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingServicesAdapter extends RecyclerView.Adapter<BookingServicesAdapter.MyViewHolder> {

    ArrayList<ModelBookingUser> mlist;
    Context context;
    String type;
    CustomProgressDialog progressDialog;
    AddEditCardBinding db;
    Dialog dialog;
    String date, time;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BookingServicesAdapter(ArrayList<ModelBookingUser> mlist, Context context) {
        this.mlist = mlist;
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


    @NotNull
    @Override
    public BookingServicesAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.booking_card, parent, false);
        return new BookingServicesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull BookingServicesAdapter.MyViewHolder holder, int position) {


        ModelBookingUser m = mlist.get(position);
        holder.firstName.setText(m.getFullName());
        holder.phone.setText(m.getPhone());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> bookingDetails = new HashMap<>();
                bookingDetails.put("uid", m.getUid());
                bookingDetails.put("fullName", m.getFullName());
                bookingDetails.put("serviceID", m.getServiceID());
                bookingDetails.put("vehicleID", m.getVehicleID());
                bookingDetails.put("phone", m.getPhone());
                context.startActivity(new Intent(context, BookingDetailActivity.class).putExtra("bookingDetails", bookingDetails));
            }
        });


        db.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> bookingDetails = new HashMap<>();
                bookingDetails.put("uid", m.getUid());
                bookingDetails.put("fullName", m.getFullName());
                bookingDetails.put("serviceID", m.getServiceID());
                bookingDetails.put("vehicleID", m.getVehicleID());
                bookingDetails.put("phone", m.getPhone());
                dialog.dismiss();
                context.startActivity(new Intent(context, EditServiceActivity.class).putExtra("bookingDetails", bookingDetails));

            }
        });


        db.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressDialog.show();

                FirebaseDatabase.getInstance().getReference("Users").child(m.getUid()).child("vehicles").child(m.getVehicleID()).child("services")
                        .child(m.getServiceID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String[] time_arr = time.split(" ")[0].split(":");


                            FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date).child(time)
                                    .child(m.getServiceID()).child("mechanicID").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot mechID_snap) {
                                    if (mechID_snap.exists()) {

                                        String mechID = mechID_snap.getValue(String.class);

                                        if (mechID.equals("No_Mechanic")) {
                                            FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date).child(time)
                                                    .child(m.getServiceID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        mlist.remove(position);
                                                        Toast.makeText(context, "Service Deleted", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();

                                                    } else {
                                                        Toast.makeText(context, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            });
                                        } else {
                                            FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date).child(time)
                                                    .child(m.getServiceID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        FirebaseDatabase.getInstance().getReference("mechanics").child(mechID).child("Service_List")
                                                                .child(m.getServiceID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    mlist.remove(position);
                                                                    Toast.makeText(context, "Service Deleted", Toast.LENGTH_SHORT).show();
                                                                    progressDialog.dismiss();
                                                                } else {
                                                                    Toast.makeText(context, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                    progressDialog.dismiss();
                                                                }
                                                            }
                                                        });
                                                    }

                                                }
                                            });
                                        }


                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } else {
                            Toast.makeText(context, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog.show();

                return true;
            }
        });


    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView firstName, phone;

        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks
            firstName = itemView.findViewById(R.id.bookingCard_name);
            phone = itemView.findViewById(R.id.bookingCard_Phone);


        }


    }


}
