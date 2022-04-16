package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Models.ModelBooking;
import com.geartocare.ManageMechanicP.Models.ModelConfirmationSms;
import com.geartocare.ManageMechanicP.Models.ModelService;
import com.geartocare.ManageMechanicP.Models.ModelUser;
import com.geartocare.ManageMechanicP.Models.ModelVehicle;
import com.geartocare.ManageMechanicP.databinding.ActivitySmsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmsActivity extends AppCompatActivity {
    ActivitySmsBinding binding;
    RequestQueue requestQueue;
    String postUrl = "https://api.msg91.com/api/v5/flow/";
    String authKey = "371903ABidvYyDKby61e12476P1";
    ArrayList<ModelBooking> bookings;
    ArrayList<ModelUser> bookings_user;
    ArrayList<ModelConfirmationSms> confirmSmsList;
    CustomProgressDialog progressDialog;
    JSONArray userArray;
    String date;

    //SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySmsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new CustomProgressDialog(SmsActivity.this);
        bookings = new ArrayList<>();
        bookings_user = new ArrayList<>();
        confirmSmsList = new ArrayList<>();
        date = getIntent().getStringExtra("date");
        requestQueue = Volley.newRequestQueue(SmsActivity.this);



        binding.sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                bookings.clear();
                bookings_user.clear();
                confirmSmsList.clear();
                userArray = new JSONArray();
                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot Slots) {
                        if (Slots.exists()) {

                            for (DataSnapshot slot : Slots.getChildren()) {

                                for (DataSnapshot booking : slot.getChildren()) {

                                    bookings.add(booking.getValue(ModelBooking.class));

                                }
                                //bookings received from slot manager
//                                Toast.makeText(SmsActivity.this, bookings.size()., Toast.LENGTH_SHORT).show();


                                //received all the details from user node


                            }

                            FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot Users) {

                                    if(Users.exists()){
                                        confirmSmsList.clear();
                                        for (ModelBooking b : bookings) {
                                            ModelConfirmationSms mcs = new ModelConfirmationSms();

                                            mcs.setFullName(Users.child(b.getUid()).child("fullName").getValue(String.class));
                                            mcs.setPhone(Users.child(b.getUid()).child("phone").getValue(String.class));
                                            mcs.setVehicle(Users.child(b.getUid()).child("vehicles").child(b.getVehicleID()).getValue(ModelVehicle.class));
                                            mcs.setService(Users.child(b.getUid()).child("vehicles").child(b.getVehicleID()).child("services").child(b.getServiceID()).getValue(ModelService.class));


                                            confirmSmsList.add(mcs);
                                        }
                                        Toast.makeText(SmsActivity.this, String.valueOf(confirmSmsList.size()), Toast.LENGTH_SHORT).show();

                                        try {
                                            JSONObject body = new JSONObject();

                                            body.put("flow_id", "624437a4912ca715b82488bb");
                                            body.put("sender", "GTCTWS");




                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                                            for (ModelConfirmationSms smsDetail : confirmSmsList) {

                                                JSONObject userObj = new JSONObject();

                                                userObj.put("mobiles", "91" + smsDetail.getPhone());
                                                userObj.put("name", smsDetail.getFullName());
                                                long ts = Long.valueOf(smsDetail.getService().getDate());
                                                userObj.put("date", sdf.format(ts));
                                                userObj.put("time", smsDetail.getService().getTime());

                                                userArray.put(userObj);

                                            }

                                            //received recipients array


                                            body.put("recipients", userArray);


                                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, body, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                    // code run is got response
                                                    Toast.makeText(SmsActivity.this, "Sms sent", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();

                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // code run is got error

                                                }
                                            }) {
                                                @Override
                                                public Map<String, String> getHeaders() throws AuthFailureError {


                                                    Map<String, String> header = new HashMap<>();
                                                    header.put("content-type", "application/json");
                                                    header.put("authkey", authKey);
                                                    return header;


                                                }
                                            };
                                            requestQueue.add(request);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } else {
                            Toast.makeText(SmsActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


    }
}