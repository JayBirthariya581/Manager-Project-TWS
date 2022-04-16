package com.geartocare.ManageMechanicP.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelMechanic;
import com.geartocare.ManageMechanicP.R;


import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class AssingMechanicAdapter extends RecyclerView.Adapter<AssingMechanicAdapter.MyViewHolder> {

    ArrayList<ModelMechanic> mlist;
    Context context;
    HashMap<String, String> serviceDetails;
    CustomProgressDialog progressDialog;


    public HashMap<String, String> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(HashMap<String, String> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public AssingMechanicAdapter(ArrayList<ModelMechanic> mlist, Context context, HashMap<String, String> serviceDetails) {
        this.mlist = mlist;
        this.context = context;
        this.serviceDetails = serviceDetails;
        progressDialog = new CustomProgressDialog(context);
    }


    @NotNull
    @Override
    public AssingMechanicAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_assign_mechanic, parent, false);

        return new AssingMechanicAdapter.MyViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NotNull AssingMechanicAdapter.MyViewHolder holder, int position) {
        ModelMechanic m = mlist.get(position);
        holder.name.setText(m.getFirstName() + " " + m.getLastName());
        holder.mechanicID.setText(m.getMechanicID());
        holder.phone.setText(m.getPhone());

        holder.mechStatus.setText(m.getMechStatus());


        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // DatabaseReference Cref = FirebaseDatabase.getInstance().getReference("Users").child(phone);

                progressDialog.show();
                String[] time_arr = serviceDetails.get("time").split(" ")[0].split(":");
                String time_ = time_arr[0]+"_"+time_arr[1];
                serviceDetails.put("time",time_);
                FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID()).child("Service_List").child(serviceDetails.get("serviceID")).setValue(serviceDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("mechanicID",m.getMechanicID());
                            map.put("status","Assigned");
                            FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(new SimpleDateFormat("dd_MM_yyyy").format(Long.valueOf(serviceDetails.get("date")))).child(time_).child(serviceDetails.get("serviceID"))
                                    .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        HashMap<String,Object> updated = new HashMap<>();
                                        updated.put("status","Assigned");
                                        updated.put("mechanicID",m.getMechanicID());
                                        FirebaseDatabase.getInstance().getReference("Users").child(serviceDetails.get("uid")).child("vehicles").child(serviceDetails.get("vehicleID")).child("services")
                                                .child(serviceDetails.get("serviceID"))
                                                .updateChildren(updated).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    ((Activity) context).setResult(Activity.RESULT_OK);
                                                    ((Activity) context).finish();

                                                }else{
                                                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });


                        }else{
                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });

                /*Cref.child("services").child(serviceID).child("mechanicID").setValue(m.getMechanicID());
                Cref.child("services").child(serviceID).child("serviceStatus").setValue("Assigned").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {

                    if(task.isSuccessful()){
                        DatabaseReference Mref = FirebaseDatabase.getInstance().getReference("mechanics").child(m.getPhone());
                        Mref.child("mechStatus").setValue("On Service");

                        HashMap<String,String> sv = new HashMap<>();

                        sv.put("phone",phone);
                        sv.put("Service_ID",serviceID);

                        Mref.child("Service_List").child(serviceID).setValue(sv);

                        DatabaseReference BoH = FirebaseDatabase.getInstance().getReference("Bookings_on_hold").child(phone).child(serviceID);

                        BoH.child("mechanic").setValue(m.getMechanicID());
                        BoH.child("status").setValue("Assigned");


                       // context.startActivity(new Intent(context,BookingDetailActivity.class).putExtra("phone",phone));
                        ((Activity) context).onBackPressed();

                    }

                }
            });*/


            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, phone, mechanicID, mechStatus;
        Button assign;

        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks
            mechStatus = itemView.findViewById(R.id.assignCard_Status);
            assign = itemView.findViewById(R.id.mech_assign_btn);
            name = itemView.findViewById(R.id.mech_name);
            phone = itemView.findViewById(R.id.mech_phone);

            mechanicID = itemView.findViewById(R.id.mech_id);

        }
    }
}
