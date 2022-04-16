package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.geartocare.ManageMechanicP.Helpers.ManagerHelperClass;
import com.geartocare.ManageMechanicP.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyPhoneNo extends AppCompatActivity {
    TextView codeEnteredByTheUser;
    ProgressBar progressBar;
    Button verify_btn;
    String verificationCodeBySystem;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    RequestQueue requestQueue;
    String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        /*------------------------------Hooks start---------------------------------------*/
        verify_btn = findViewById(R.id.verify_btn);
        codeEnteredByTheUser = findViewById(R.id.verification_code_entered_by_user);
        progressBar = findViewById(R.id.progress_bar);
        /*------------------------------Hooks end---------------------------------------*/

        requestQueue = Volley.newRequestQueue(VerifyPhoneNo.this);

        phoneNo = getIntent().getStringExtra("phone");

        sendVerificationCodeToUser(phoneNo);


        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (codeEnteredByTheUser.getText().toString().isEmpty()) {
                    Toast.makeText(VerifyPhoneNo.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                } else {

                    verifyCode(codeEnteredByTheUser.getText().toString());
                }

            }
        });


    }

    private void sendVerificationCodeToUser(String phoneNo) {

        try {
            JSONObject body = new JSONObject();


            body.put("template_id", "623b4c8fdc29a74e866082b5");
            body.put("mobile", "91" + "8668967239");
            body.put("authkey", "371903ABidvYyDKby61e12476P1");
            body.put("otp_length", "6");


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://api.msg91.com/api/v5/otp?template_id=623b4c8fdc29a74e866082b5&mobile=91"+phoneNo+"&authkey=371903ABidvYyDKby61e12476P1&otp_length=6", body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // code run is got response
                    try {
                        if (response.get("type").equals("success")) {
                            Toast.makeText(VerifyPhoneNo.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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
                    header.put("Content-Type", "application/json");
                    return header;


                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void verifyCode(String codeByUser) {


        try {


            JSONObject body = new JSONObject();


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://api.msg91.com/api/v5/otp/verify?otp=" + codeByUser + "&authkey=371903ABidvYyDKby61e12476P1&mobile=91" + phoneNo, body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // code run is got response
                    try {
                        if (response.get("type").equals("success")) {
                            enterDataToDataBase();

                        } else {
                            Toast.makeText(VerifyPhoneNo.this, "Invalid OTP", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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
                    header.put("Content-Type", "application/json");
                    return header;


                }
            };
            requestQueue.add(request);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    public void enterDataToDataBase() {

        reference = FirebaseDatabase.getInstance().getReference("managers");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Intent carrier = getIntent();

        String firstName = carrier.getStringExtra("firstName");
        String lastname = carrier.getStringExtra("lastName");
        String email = carrier.getStringExtra("email");
        String phone = carrier.getStringExtra("phone");
        String password = carrier.getStringExtra("password");


        ManagerHelperClass managerHelper = new ManagerHelperClass(firstName, lastname, phone, email, password);

        reference.child(phone).setValue(managerHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(VerifyPhoneNo.this, "Your Account has been created successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(VerifyPhoneNo.this, LoginActivity.class));
                    finish();
                }
            }
        });

    }

}