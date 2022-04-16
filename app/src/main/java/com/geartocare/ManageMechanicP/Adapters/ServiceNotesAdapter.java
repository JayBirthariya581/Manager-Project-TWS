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
import com.google.firebase.database.DatabaseReference;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.databinding.AddNoteBinding;
import com.geartocare.ManageMechanicP.databinding.CardDeleteBinding;
import com.geartocare.ManageMechanicP.databinding.CardNotesBinding;

import java.util.ArrayList;

public class ServiceNotesAdapter extends RecyclerView.Adapter<ServiceNotesAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> mlist;
    CustomProgressDialog progressDialog;
    DatabaseReference Svref;
    CardDeleteBinding acb;
    Dialog dialog;


    public DatabaseReference getSvref() {
        return Svref;
    }

    public void setSvref(DatabaseReference svref) {
        Svref = svref;
    }

    public ServiceNotesAdapter(Context context, ArrayList<String> mlist) {
        this.context = context;
        this.mlist = mlist;
        progressDialog = new CustomProgressDialog(context);

        acb = CardDeleteBinding.inflate(((Activity) context).getLayoutInflater());
        dialog = new Dialog(context);
        dialog.setContentView(acb.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);




    }

    @NonNull
    @Override
    public ServiceNotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceNotesAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_notes,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceNotesAdapter.MyViewHolder holder, int position) {

        holder.binding.note.setText(mlist.get(position));


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                dialog.show();

                acb.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Svref.child("Notes").child(String.valueOf(position+1)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mlist.remove(position);
                                    notifyDataSetChanged();
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
        return mlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CardNotesBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardNotesBinding.bind(itemView);
        }
    }

}
