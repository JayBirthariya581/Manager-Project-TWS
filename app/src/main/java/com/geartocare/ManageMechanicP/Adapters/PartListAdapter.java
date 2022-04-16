package com.geartocare.ManageMechanicP.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelPart;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.AddPartBinding;
import com.geartocare.ManageMechanicP.databinding.CardDeleteBinding;
import com.geartocare.ManageMechanicP.databinding.CardDeleteEditBinding;
import com.geartocare.ManageMechanicP.databinding.CardPartBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PartListAdapter extends RecyclerView.Adapter<PartListAdapter.MyViewHolder> {
    private final DatabaseReference partRef;
    Context context;
    ArrayList<ModelPart> parts;
    CustomProgressDialog progressDialog;
    CardDeleteEditBinding db;
    Dialog dialog;
    Dialog dia;
    AddPartBinding apb;

    public PartListAdapter(Context context, ArrayList<ModelPart> parts) {
        this.context = context;
        this.parts = parts;

        progressDialog = new CustomProgressDialog(context);
        db = CardDeleteEditBinding.inflate(((Activity) context).getLayoutInflater());
        dialog = new Dialog(context);
        dialog.setContentView(db.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);



        apb = AddPartBinding.inflate(((Activity) context).getLayoutInflater());
        apb.add.setText("Update");
        dia = new Dialog(context);
        dia.setContentView(apb.getRoot());

        WindowManager.LayoutParams p = new WindowManager.LayoutParams();
        p.copyFrom(dia.getWindow().getAttributes());
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dia.getWindow().setAttributes(p);

        partRef = FirebaseDatabase.getInstance().getReference("AppManager").child("Inventory").child("PartList");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PartListAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_part, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelPart modelPart = parts.get(position);


        holder.binding.partName.setText(modelPart.getName());
        holder.binding.partCompany.setText(modelPart.getCompany());
        holder.binding.partSP.setText(modelPart.getSellingPrice());
        holder.binding.partOP.setText(modelPart.getOriginalPrice());
        holder.binding.partDis.setText(modelPart.getDiscount());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                dialog.show();


                db.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        dialog.dismiss();

                        apb.name.setText(modelPart.getName());
                        apb.company.setText(modelPart.getCompany());
                        apb.op.setText(modelPart.getOriginalPrice());
                        apb.sp.setText(modelPart.getSellingPrice());



                        apb.add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String name=apb.name.getText().toString();
                                String company=apb.company.getText().toString();
                                String op=apb.op.getText().toString();
                                String sp=apb.sp.getText().toString();
                                String dis =apb.partDis.getText().toString();
                                if(name.isEmpty() || sp.isEmpty() || op.isEmpty() || company.isEmpty()){
                                    Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                dia.dismiss();


                                progressDialog.show();

                                ModelPart mp = new ModelPart();
                                Double op_in = Double.valueOf(op);
                                Double sp_in = Double.valueOf(sp);
                                Double dis_per;


                                Double dis_in = sp_in-op_in;

                                dis_per = (dis_in * 100) / op_in;





                                mp.setName(name);
                                mp.setCompany(company);
                                DecimalFormat numberFormat = new DecimalFormat("#.00");
                                mp.setDiscount(numberFormat.format(dis_per));
                                mp.setOriginalPrice(op);
                                mp.setSellingPrice(sp);
                                partRef.child(name+"_"+company).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            parts.get(position).setName(mp.getName());
                                            parts.get(position).setCompany(mp.getCompany());
                                            parts.get(position).setDiscount(mp.getDiscount());
                                            parts.get(position).setSellingPrice(mp.getSellingPrice());
                                            parts.get(position).setOriginalPrice(mp.getOriginalPrice());

                                            notifyDataSetChanged();
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Part edited successfully", Toast.LENGTH_SHORT).show();


                                        }else{
                                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                        apb.name.setText("");
                                        apb.company.setText("");
                                        apb.sp.setText("");
                                        apb.op.setText("");
                                        apb.partDis.setText("-");

                                    }
                                });





                            }
                        });

                        dia.show();


                    }
                });


                db.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        progressDialog.show();
                        FirebaseDatabase.getInstance().getReference("AppManager").child("Inventory").child("PartList")
                                .child(modelPart.getName()+"_"+modelPart.getCompany()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    parts.remove(position);
                                    notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }else{
                                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
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
        return parts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardPartBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = CardPartBinding.bind(itemView);

        }
    }
}
