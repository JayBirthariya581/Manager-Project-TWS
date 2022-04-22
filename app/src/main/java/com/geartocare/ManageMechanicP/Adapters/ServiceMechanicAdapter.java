package com.geartocare.ManageMechanicP.Adapters;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.card.MaterialCardView;
import com.geartocare.ManageMechanicP.Activities.BookingPartsActivity;
import com.geartocare.ManageMechanicP.Activities.CustomerActivity;
import com.geartocare.ManageMechanicP.Activities.GenerateImageActivity;
import com.geartocare.ManageMechanicP.Activities.ListAssignMechanicActivity;
import com.geartocare.ManageMechanicP.Activities.ServiceImagesActivity;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelBooking;
import com.geartocare.ManageMechanicP.Models.ModelService;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.AssignedServiceEditBinding;
import com.geartocare.ManageMechanicP.databinding.DeleteBookingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceMechanicAdapter extends RecyclerView.Adapter<ServiceMechanicAdapter.MyViewHolder> {

    ArrayList<ModelService> mlist;
    Context context;
    String phone;

    String vhNo;
    ModelBooking bookingDetails;
    ActivityResultLauncher launcher, launcher2;
    CustomProgressDialog progressDialog;

    public String getVhNo() {
        return vhNo;
    }

    public void setVhNo(String vhNo) {
        this.vhNo = vhNo;
    }

    public ActivityResultLauncher getLauncher2() {
        return launcher2;
    }

    public void setLauncher2(ActivityResultLauncher launcher2) {
        this.launcher2 = launcher2;
    }

    public ModelBooking getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(ModelBooking bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public void setLauncher(ActivityResultLauncher launcher) {
        this.launcher = launcher;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ServiceMechanicAdapter(ArrayList<ModelService> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        progressDialog = new CustomProgressDialog(context);
    }


    @NotNull
    @Override
    public ServiceMechanicAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_service_mechanic, parent, false);

        return new ServiceMechanicAdapter.MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NotNull ServiceMechanicAdapter.MyViewHolder holder, int position) {
        ModelService m = mlist.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
        String date_txt = sdf.format(Long.valueOf(m.getDate()));
        String[] time_arr = m.getTime().split(" ")[0].split(":");
        String time_ = time_arr[0] + "_" + time_arr[1];
        holder.date.setText(date_txt);
        holder.time.setText(m.getTime());
        holder.vehicleNo.setText(vhNo);
        holder.ServiceStatus.setText(m.getStatus());
        holder.address.setText(m.getLocation().getTxt());


        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("LatLng", m.getLocation().getLat() + "," + m.getLocation().getLng());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "LatLng copied to clip board", Toast.LENGTH_SHORT).show();


            }
        });


       /* holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View vi = LayoutInflater.from(context).inflate(R.layout.delete_booking, null);
                DeleteBookingBinding binding = DeleteBookingBinding.bind(vi);
                AlertDialog builder = new AlertDialog.Builder(context).create();
                builder.setIcon(R.drawable.ic_mechanic);
                builder.setTitle("Manage | GearToCare");
                builder.setView(binding.getRoot());

                builder.show();


                binding.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                        progressDialog.show();
                        FirebaseDatabase.getInstance().getReference("Users").child(bookingDetails.getUid()).child("vehicles").child(bookingDetails.getVehicleID()).child("services")
                                .child(bookingDetails.getServiceID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_txt).child(time_)
                                            .child(bookingDetails.getServiceID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mlist.remove(position);
                                                notifyDataSetChanged();
                                                progressDialog.dismiss();
                                                context.startActivity(new Intent(context, BookingListActivity.class));

                                            } else {
                                                Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }


                                        }
                                    });

                                        *//*FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID()).
                                                child("Service_List").child(m.getServiceID()).setValue(null);*//*


                                } else {
                                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });


                    }
                });



                binding.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launcher2.launch(new Intent(context,ServiceEditActivity.class).putExtra("serviceDetails",bookingDetails));
                    }
                });


                return false;
            }
        });*/


        if (m.getStatus().equals("Done")) {
            holder.assignMechanic.setVisibility(View.GONE);
            holder.images.setVisibility(View.VISIBLE);
            holder.approve.setVisibility(View.VISIBLE);
            holder.generatePdf.setVisibility(View.VISIBLE);
            holder.pt_box.setVisibility(View.VISIBLE);
            holder.p_box.setVisibility(View.VISIBLE);
            holder.paymentType.setVisibility(View.VISIBLE);
            holder.paymentPrice.setVisibility(View.VISIBLE);
            holder.paymentType.setText("Payment type : " + m.getPayment().getPaymentType());
            holder.paymentPrice.setText("Cost : " + m.getPayment().getPrice());
            holder.parts.setVisibility(View.VISIBLE);


            holder.parts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, BookingPartsActivity.class);

                    i.putExtra("serviceDetails", bookingDetails);


                    context.startActivity(i);

                }
            });


            holder.generatePdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, GenerateImageActivity.class);

                    i.putExtra("serviceDetails", bookingDetails);


                    context.startActivity(i);

                }
            });


            holder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.show();


                    FirebaseDatabase.getInstance().getReference("Users").child(bookingDetails.getUid()).child("vehicles").child(bookingDetails.getVehicleID()).child("services")
                            .child(bookingDetails.getServiceID())
                            .child("status").setValue("Completed").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {


                                FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID())
                                        .child("service_counter").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Integer service_counter = Integer.parseInt(snapshot.getValue(String.class));

                                            service_counter = service_counter + 1;
                                            FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID())
                                                    .child("service_counter").setValue(String.valueOf(service_counter)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {


                                                        FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_txt).child(time_)
                                                                .child(bookingDetails.getServiceID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                if (task.isSuccessful()) {
                                                                    FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID()).child("Service_List").child(m.getServiceID())
                                                                            .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                mlist.remove(position);
                                                                                notifyDataSetChanged();
                                                                                progressDialog.dismiss();

                                                                                Intent i = new Intent(context, CustomerActivity.class);
                                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                context.startActivity(i);
                                                                                Toast.makeText(context, "Service marked as completed", Toast.LENGTH_LONG).show();


                                                                            } else {
                                                                                Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                                                progressDialog.dismiss();
                                                                            }
                                                                        }
                                                                    });
                                                                } else {
                                                                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                                    progressDialog.dismiss();
                                                                }
                                                            }
                                                        });


                                                    } else {
                                                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
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


            holder.images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ServiceImagesActivity.class);

                    intent.putExtra("serviceDetails", bookingDetails);


                    context.startActivity(intent);
                }
            });

        } else if (m.getStatus().equals("On_Hold")) {

            holder.assignMechanic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    HashMap<String, String> sd = new HashMap();

                    sd.put("date", m.getDate());
                    sd.put("time", m.getTime());
                    sd.put("serviceID", m.getServiceID());
                    sd.put("uid", bookingDetails.getUid());
                    sd.put("vehicleID", bookingDetails.getVehicleID());


                    Intent intent = new Intent(context, ListAssignMechanicActivity.class);
                    intent.putExtra("serviceDetails", sd);


                    launcher.launch(intent);


                }
            });

        } else {
            holder.assignMechanic.setText("UnAssign");
            holder.assignMechanic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                View vi = LayoutInflater.from(context).inflate(R.layout.assigned_service_edit, null);
                                AssignedServiceEditBinding binding = AssignedServiceEditBinding.bind(vi);
                                AlertDialog builder = new AlertDialog.Builder(context).create();
                                builder.setIcon(R.drawable.ic_mechanic);
                                builder.setTitle("Manage | GearToCare");
                                builder.setView(binding.getRoot());


                                binding.mechName.setText(snapshot.child("firstName").getValue(String.class) + " " + snapshot.child("lastName").getValue(String.class));

                                binding.unAssign.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        builder.dismiss();
                                        progressDialog.show();
                                        FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID()).child("Service_List").child(m.getServiceID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    HashMap<String, Object> map = new HashMap<>();
                                                    map.put("status", "On_Hold");
                                                    map.put("mechanicID", "No_Mechanic");
                                                    FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_txt).child(time_)
                                                            .child(bookingDetails.getServiceID()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {
                                                                HashMap<String, Object> updated = new HashMap<>();
                                                                updated.put("status", "On_Hold");
                                                                updated.put("mechanicID", "No_Mechanic");
                                                                FirebaseDatabase.getInstance().getReference("Users").child(bookingDetails.getUid()).child("vehicles").child(bookingDetails.getVehicleID()).child("services")
                                                                        .child(bookingDetails.getServiceID()).updateChildren(updated).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            mlist.get(position).setStatus("On_Hold");
                                                                            m.setStatus("On_Hold");
                                                                            mlist.get(position).setMechanicID("No_Mechanic");

                                                                            m.setMechanicID("No_Mechanic");
                                                                            holder.ServiceStatus.setText(m.getStatus());
                                                                            holder.assignMechanic.setText("Assign Mechanic");
                                                                            notifyDataSetChanged();

                                                                            progressDialog.dismiss();

                                                                        } else {
                                                                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                                            progressDialog.dismiss();
                                                                        }
                                                                    }
                                                                });


                                                            } else {
                                                                Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }


                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }


                                            }
                                        });


                                    }
                                });


                                builder.show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }
            });


        }


    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, time, vehicleNo, address, ServiceStatus, paymentType,paymentPrice;
        Button assignMechanic, images, approve, generatePdf;
        RelativeLayout pt_box,p_box;
        MaterialCardView parts;


        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks
            ServiceStatus = itemView.findViewById(R.id.serviceCard_Status);
            date = itemView.findViewById(R.id.serviceCard_date);
            time = itemView.findViewById(R.id.serviceCard_time);
            vehicleNo = itemView.findViewById(R.id.serviceCard_vehicleNo);
            address = itemView.findViewById(R.id.serviceCard_Address);
            assignMechanic = itemView.findViewById(R.id.cardMechanic_assign_btn);
            images = itemView.findViewById(R.id.cardMechanic_seeImages_btn);
            approve = itemView.findViewById(R.id.cardMechanic_approve_btn);
            generatePdf = itemView.findViewById(R.id.cardMechanic_gen_pdf);
            pt_box = itemView.findViewById(R.id.pt_box);
            p_box = itemView.findViewById(R.id.p_box);
            paymentPrice = itemView.findViewById(R.id.paymentPrice);
            paymentType = itemView.findViewById(R.id.payment_type);
            parts = itemView.findViewById(R.id.parts);
        }
    }


}
