package com.geartocare.ManageMechanicP.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.icu.util.Calendar;
import android.os.Build;
import android.widget.DatePicker;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.Adapters.TwBrandListAdapter;
import com.geartocare.ManageMechanicP.Adapters.TwModelListAdapter;
import com.geartocare.ManageMechanicP.Helpers.CustomProgressDialog;
import com.geartocare.ManageMechanicP.Helpers.LocationHelper;
import com.geartocare.ManageMechanicP.Helpers.PaymentHelper;
import com.geartocare.ManageMechanicP.Helpers.VehicleHelper;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.Models.Service;
import com.geartocare.ManageMechanicP.SessionManager;
import com.geartocare.ManageMechanicP.databinding.SelectBrandDialogBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerServiceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    TextInputLayout serviceDateL, serviceTimeL, serviceVehicleNoL, serviceAddressL, serviceModel, serviceCompany, serviceCost;
    String uid;
    Button addService;
    ArrayList<String> CompanyList, ModelList;
    String longitude, latitude, location_txt;
    TwBrandListAdapter adapter;
    TwModelListAdapter ModelAdapter;
    String serviceId, VehicleId;
    String timeStamp;
    RecyclerView cL;
    CustomProgressDialog progressDialog;
    String date_, dateSlash;
    String time_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        getWindow().setStatusBarColor(ContextCompat.getColor(CustomerServiceActivity.this, R.color.black));


        /*------------------------------Hooks start---------------------------------------*/
        serviceDateL = findViewById(R.id.serviceDateL);
        serviceTimeL = findViewById(R.id.serviceTimeL);
        addService = findViewById(R.id.addService);
        serviceVehicleNoL = findViewById(R.id.serviceVehicleNoL);
        serviceAddressL = findViewById(R.id.serviceAddressL);
        serviceCompany = findViewById(R.id.serviceCompany);
        serviceModel = findViewById(R.id.serviceModel);
        serviceCost = findViewById(R.id.serviceCost);


        progressDialog = new CustomProgressDialog(CustomerServiceActivity.this);


        /*------------------------------Hooks end---------------------------------------*/

        /*------------------------------Variables---------------------------------------*/
        uid = getIntent().getStringExtra("uid");

        /*------------------------------Variables end---------------------------------------*/

        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("services");


        ActivityResultLauncher<Intent> lau = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    latitude = data.getStringExtra("Lat");
                    longitude = data.getStringExtra("Lng");
                    location_txt = data.getStringExtra("address");
                    serviceAddressL.getEditText().setText(location_txt);

                }
            }
        });


        serviceAddressL.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CustomerServiceActivity.this, PlacePickerActivity.class);
                lau.launch(i);

            }
        });


        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBooking();
            }
        });


        serviceDateL.getEditText().setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        serviceTimeL.getEditText().setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

//        manageVehicleBox();


    }

    /*private void manageVehicleBox() {
        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService").child("CompanyList");

        ModelList = new ArrayList<>();
        ModelAdapter = new TwModelListAdapter(CustomerServiceActivity.this, ModelList, serviceModel.getEditText());


        CompanyList = new ArrayList<>();
        adapter = new TwBrandListAdapter(CustomerServiceActivity.this, CompanyList, serviceCompany.getEditText(), serviceModel.getEditText());


        adapter.setModelListAdapter(ModelAdapter);


        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot companyList) {


                if (companyList.exists()) {
                    CompanyList.clear();


                    for (DataSnapshot company : companyList.getChildren()) {

                        CompanyList.add(company.getKey());
                    }


                    adapter.setCompanyList(companyList);

                    serviceCompany.getEditText().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            SelectBrandDialogBinding b = SelectBrandDialogBinding.inflate(getLayoutInflater());
                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerServiceActivity.this);
                            builder.setView(b.getRoot());


                            CustomerServiceActivity.this.cL = b.brandList;

                            b.heading.setText("Select Brand");
                            b.brandList.setAdapter(adapter);
                            b.brandList.setLayoutManager(new LinearLayoutManager(CustomerServiceActivity.this));
                            b.brandList.setHasFixedSize(true);


                            //builder.create().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            adapter.notifyDataSetChanged();
                            AlertDialog a = builder.show();
                            a.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            a.getWindow().setLayout(800, 1000);
                            adapter.setAlertDialog(a);


                        }
                    });

                    serviceModel.getEditText().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            SelectBrandDialogBinding mb = SelectBrandDialogBinding.inflate(getLayoutInflater());
                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerServiceActivity.this);
                            builder.setView(mb.getRoot());


                            mb.heading.setText("Select Model");
                            mb.brandList.setAdapter(ModelAdapter);
                            mb.brandList.setLayoutManager(new LinearLayoutManager(CustomerServiceActivity.this));
                            mb.brandList.setHasFixedSize(true);


                            //builder.create().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            ModelAdapter.notifyDataSetChanged();
                            AlertDialog am = builder.show();
                            am.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            am.getWindow().setLayout(800, 1000);
                            ModelAdapter.setAlertDialog(am);


                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }*/


    private void createBooking() {
        progressDialog.show();
        String date = serviceDateL.getEditText().getText().toString();
        String time = serviceTimeL.getEditText().getText().toString();
        String vehicleNo = serviceVehicleNoL.getEditText().getText().toString();
        String address = serviceAddressL.getEditText().getText().toString();
        String Company = serviceCompany.getEditText().getText().toString();
        String Model = serviceModel.getEditText().getText().toString();
        String Cost = serviceCost.getEditText().getText().toString();


        if (date.isEmpty() || time.isEmpty() || vehicleNo.isEmpty() || address.isEmpty() || Company.isEmpty() || Model.isEmpty() || Cost.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(CustomerServiceActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);


        PaymentHelper paymentHelper = new PaymentHelper(Cost, "payAfterService", "On_Hold");
        LocationHelper locationHelper = new LocationHelper(latitude, longitude, location_txt);

        serviceId = UserRef.child("services").push().getKey();
        Service service = new Service(serviceId, new SessionManager(CustomerServiceActivity.this).getUsersDetailsFromSessions().get(SessionManager.KEY_PHONENUMBER), timeStamp, time, locationHelper, paymentHelper, "On_Hold", "No_Mechanic");
        VehicleHelper vehicleHelper = new VehicleHelper(Company, Model, vehicleNo);

        VehicleId = Company + "_" + Model + "_" + vehicleNo;
        HashMap<String, Object> vehicleMap = new HashMap<>();
        vehicleMap.put("company", Company);
        vehicleMap.put("model", Model);
        vehicleMap.put("vehicleNo", vehicleNo);


        Query CheckVehicle = UserRef.child("vehicles").orderByKey().equalTo(VehicleId);


        CheckVehicle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot vehicle_snap) {


                if (vehicle_snap.exists()) {

                    UserRef.child("vehicles").child(VehicleId).child("services").child(serviceId).setValue(service).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override

                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {


                                HashMap<String, Object> sv_det = new HashMap<>();
                                sv_det.put("uid", uid);
                                sv_det.put("serviceID", serviceId);
                                sv_det.put("vehicleID", VehicleId);
                                sv_det.put("mechanicID", "No_Mechanic");
                                sv_det.put("status", "On_Hold");


                                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_).child(time_).child(serviceId).setValue(sv_det).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(CustomerServiceActivity.this, "Service Added Successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(CustomerServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });





                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(CustomerServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    UserRef.child("vehicles").child(VehicleId).setValue(vehicleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                UserRef.child("vehicles").child(VehicleId).child("services").child(serviceId).setValue(service).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override

                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


                                            HashMap<String, Object> sv_det = new HashMap<>();
                                            sv_det.put("uid", uid);
                                            sv_det.put("serviceID", serviceId);
                                            sv_det.put("vehicleID", VehicleId);
                                            sv_det.put("mechanicID", "No_Mechanic");
                                            sv_det.put("status", "On_Hold");


                                            FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_).child(time_).child(serviceId).setValue(sv_det).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        progressDialog.dismiss();
                                                        Toast.makeText(CustomerServiceActivity.this, "Service Added Successfully", Toast.LENGTH_SHORT).show();
                                                        finish();

                                                    }
                                                }
                                            });





                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(CustomerServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(CustomerServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        Calendar combinedCal = Calendar.getInstance();
        combinedCal.set(year, month, dayOfMonth);
        timeStamp = String.valueOf(combinedCal.getTime().getTime());
        date_ = new SimpleDateFormat("dd_MM_yyyy").format(Long.valueOf(timeStamp));
        dateSlash = new SimpleDateFormat("dd/MM/yyyy").format(Long.valueOf(timeStamp));
        serviceDateL.getEditText().setText(dateSlash);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, mHour, mMinute, false);
        timePickerDialog.show();

    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hr, int min) {

        if ((hr - 12) > 0) {
            serviceTimeL.getEditText().setText((hr - 12) + ":" + min + " pm");
            time_ = (hr - 12) + "_" + min;
        } else {
            serviceTimeL.getEditText().setText(hr + ":" + min + " am");
            time_ = hr + "_" + min;
        }


    }
}