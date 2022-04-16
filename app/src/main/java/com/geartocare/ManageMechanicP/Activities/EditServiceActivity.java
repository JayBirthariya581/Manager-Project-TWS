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
import com.geartocare.ManageMechanicP.Models.ModelService;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.Models.Service;
import com.geartocare.ManageMechanicP.SessionManager;
import com.geartocare.ManageMechanicP.databinding.SelectBrandDialogBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class EditServiceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    TextInputLayout serviceDateL, serviceTimeL, serviceVehicleNoL, serviceAddressL, serviceModel, serviceCompany, serviceCost;
    String uid;
    Button update;
    ArrayList<String> CompanyList, ModelList;
    String longitude, latitude, location_txt;
    TwBrandListAdapter adapter;
    TwModelListAdapter ModelAdapter;
    String serviceId, originalVehicleID;
    String timeStamp;
    RecyclerView cL;
    HashMap<String, String> bookingDetails;
    CustomProgressDialog progressDialog;
    ModelService originalService;
    String time_new, time_original;
    String dateSlashOriginal, dateSlashNew, date_original, date_new;
    HashMap<String, Object> newBookingDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);
        getWindow().setStatusBarColor(ContextCompat.getColor(EditServiceActivity.this, R.color.black));
        bookingDetails = (HashMap<String, String>) getIntent().getSerializableExtra("bookingDetails");
        progressDialog = new CustomProgressDialog(EditServiceActivity.this);
        /*------------------------------Hooks start---------------------------------------*/
        serviceDateL = findViewById(R.id.serviceDateL);
        serviceTimeL = findViewById(R.id.serviceTimeL);
        update = findViewById(R.id.update);
        serviceVehicleNoL = findViewById(R.id.serviceVehicleNoL);
        serviceAddressL = findViewById(R.id.serviceAddressL);
        serviceCompany = findViewById(R.id.serviceCompany);
        serviceModel = findViewById(R.id.serviceModel);
        serviceCost = findViewById(R.id.serviceCost);


        serviceId = bookingDetails.get("serviceID");
        originalVehicleID = bookingDetails.get("vehicleID");
        uid = bookingDetails.get("uid");

        /*------------------------------Hooks end---------------------------------------*/

        /*------------------------------Variables---------------------------------------*/


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


                Intent i = new Intent(EditServiceActivity.this, PlacePickerActivity.class);
                lau.launch(i);

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


        fillDetails();
//        manageVehicleBox();


    }

    /*private void manageVehicleBox() {
        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService").child("CompanyList");

        ModelList = new ArrayList<>();
        ModelAdapter = new TwModelListAdapter(EditServiceActivity.this, ModelList, serviceModel.getEditText());


        CompanyList = new ArrayList<>();
        adapter = new TwBrandListAdapter(EditServiceActivity.this, CompanyList, serviceCompany.getEditText(), serviceModel.getEditText());


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
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditServiceActivity.this);
                            builder.setView(b.getRoot());


                            EditServiceActivity.this.cL = b.brandList;

                            b.heading.setText("Select Brand");
                            b.brandList.setAdapter(adapter);
                            b.brandList.setLayoutManager(new LinearLayoutManager(EditServiceActivity.this));
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditServiceActivity.this);
                            builder.setView(mb.getRoot());


                            mb.heading.setText("Select Model");
                            mb.brandList.setAdapter(ModelAdapter);
                            mb.brandList.setLayoutManager(new LinearLayoutManager(EditServiceActivity.this));
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


    public void fillDetails() {
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("vehicles").child(originalVehicleID).child("services").child(serviceId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sv_snap) {

                if (sv_snap.exists()) {

                    originalService = sv_snap.getValue(ModelService.class);


                    long d = Long.valueOf(originalService.getDate());
                    String[] vehicleDetails = originalVehicleID.split("_");

                    timeStamp = originalService.getDate();
                    dateSlashOriginal = new SimpleDateFormat("dd/MM/yyyy").format(d);
                    dateSlashNew = dateSlashOriginal;
                    date_original = new SimpleDateFormat("dd_MM_yyyy").format(d);
                    date_new = date_original;


                    String[] timefr = (originalService.getTime().split(" ")[0]).split(":");
                    time_original = timefr[0] + "_" + timefr[1];
                    time_new = time_original;

                    serviceDateL.getEditText().setText(dateSlashOriginal);
                    serviceTimeL.getEditText().setText(originalService.getTime());
                    serviceVehicleNoL.getEditText().setText(vehicleDetails[2]);
                    serviceAddressL.getEditText().setText(originalService.getLocation().getTxt());
                    serviceCompany.getEditText().setText(vehicleDetails[0]);
                    serviceModel.getEditText().setText(vehicleDetails[1]);
                    serviceCost.getEditText().setText(originalService.getPayment().getPrice());


                    latitude = originalService.getLocation().getLat();
                    longitude = originalService.getLocation().getLng();
                    location_txt = originalService.getLocation().getTxt();

                    progressDialog.dismiss();

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
                                Toast.makeText(EditServiceActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);


                            PaymentHelper paymentHelper = new PaymentHelper(Cost, originalService.getPayment().getPaymentType(), originalService.getPayment().getPaymentStatus());
                            LocationHelper locationHelper = new LocationHelper(latitude, longitude, location_txt);


                            Service service = new Service(serviceId, new SessionManager(EditServiceActivity.this).getUsersDetailsFromSessions().get(SessionManager.KEY_PHONENUMBER), timeStamp, time, locationHelper, paymentHelper, originalService.getStatus(), originalService.getMechanicID());


                            String newVehicleID = Company + "_" + Model + "_" + vehicleNo;
                            HashMap<String, Object> vehicleMap = new HashMap<>();
                            vehicleMap.put("company", Company);
                            vehicleMap.put("model", Model);
                            vehicleMap.put("vehicleNo", vehicleNo);

                            HashMap<String, Object> mechMap = new HashMap<>();
                            mechMap.put("uid", uid);
                            mechMap.put("serviceID", serviceId);
                            mechMap.put("vehicleID", newVehicleID);
                            mechMap.put("time", time);
                            mechMap.put("date", timeStamp);

                            newBookingDetails = new HashMap<>();
                            newBookingDetails.put("uid", uid);
                            newBookingDetails.put("serviceID", serviceId);
                            newBookingDetails.put("vehicleID", newVehicleID);
                            newBookingDetails.put("mechanicID", service.getMechanicID());
                            newBookingDetails.put("status", service.getStatus());

                            //first checking if the vehicle is changed
                            if (newVehicleID.equals(originalVehicleID)) {
                                UserRef.child("vehicles").child(originalVehicleID).child("services").child(serviceId).setValue(service).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override

                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            if (date_original.equals(date_new)) {

                                                if (time_original.equals(time_new)) {//check if time is changed
                                                    FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_new).child(time_new).child(serviceId).child("vehicleID").setValue(newVehicleID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                if (service.getMechanicID().equals("No_Mechanic")) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                    startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                    );
                                                                    finish();
                                                                } else {
                                                                    FirebaseDatabase.getInstance().getReference("mechanics").child(service.getMechanicID()).child("Service_List")
                                                                            .child(serviceId).setValue(mechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                );
                                                                                finish();
                                                                            } else {
                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }


                                                            }

                                                        }
                                                    });
                                                } else {//time is different


                                                    //delete old time slot
                                                    FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_original).child(time_original).child(serviceId).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                //assign service on new time slot
                                                                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_new).child(time_new).child(serviceId).setValue(newBookingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            if (service.getMechanicID().equals("No_Mechanic")) {
                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                );
                                                                                finish();
                                                                            } else {
                                                                                FirebaseDatabase.getInstance().getReference("mechanics").child(service.getMechanicID()).child("Service_List")
                                                                                        .child(serviceId).setValue(mechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            progressDialog.dismiss();
                                                                                            Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                            startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                            );
                                                                                            finish();
                                                                                        } else {
                                                                                            progressDialog.dismiss();
                                                                                            Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }

                                                                    }
                                                                });
                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                                }


                                            } else {//if date is different


                                                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_original).child(time_original).child(serviceId).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {


                                                        if (task.isSuccessful()) {
                                                            FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_new).child(time_new).child(serviceId).setValue(newBookingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if (task.isSuccessful()) {
                                                                        if (service.getMechanicID().equals("No_Mechanic")) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                            startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                            );
                                                                            finish();
                                                                        } else {
                                                                            FirebaseDatabase.getInstance().getReference("mechanics").child(service.getMechanicID()).child("Service_List")
                                                                                    .child(serviceId).setValue(mechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        progressDialog.dismiss();
                                                                                        Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                        startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                        );
                                                                                        finish();
                                                                                    } else {
                                                                                        progressDialog.dismiss();
                                                                                        Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }

                                                                }
                                                            });
                                                        } else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                        }


                                                    }
                                                });


                                            }


                                        } else {
                                            Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {//vehicle is changed


                                //delete service from old vehicle
                                UserRef.child("vehicles").child(originalVehicleID).child("services").child(serviceId).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override

                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Query checkVehicle = UserRef.child("vehicles").orderByKey().equalTo(newVehicleID);

                                            //check if the vehicle with new serviceID exists or not
                                            checkVehicle.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                                    if (snapshot.exists()) {//if exists assign service to it normally
                                                        UserRef.child("vehicles").child(newVehicleID).child("services").child(serviceId).setValue(service).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override

                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    //check if date is same
                                                                    if (date_original.equals(date_new)) {

                                                                        if (time_original.equals(time_new)) {//check if time is changed
                                                                            FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_new).child(time_new).child(serviceId).child("vehicleID").setValue(newVehicleID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        if (service.getMechanicID().equals("No_Mechanic")) {
                                                                                            progressDialog.dismiss();
                                                                                            Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                            startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                            );
                                                                                            finish();
                                                                                        } else {
                                                                                            FirebaseDatabase.getInstance().getReference("mechanics").child(service.getMechanicID()).child("Service_List")
                                                                                                    .child(serviceId).setValue(mechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        progressDialog.dismiss();
                                                                                                        Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                        startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                        );
                                                                                                        finish();
                                                                                                    } else {
                                                                                                        progressDialog.dismiss();
                                                                                                        Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }

                                                                                }
                                                                            });
                                                                        } else {//time is different


                                                                            //delete old time slot
                                                                            FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_original).child(time_original).child(serviceId).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {

                                                                                        //assign service on new time slot
                                                                                        FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_new).child(time_new).child(serviceId).setValue(newBookingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    if (service.getMechanicID().equals("No_Mechanic")) {
                                                                                                        progressDialog.dismiss();
                                                                                                        Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                        startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                        );
                                                                                                        finish();
                                                                                                    } else {
                                                                                                        FirebaseDatabase.getInstance().getReference("mechanics").child(service.getMechanicID()).child("Service_List")
                                                                                                                .child(serviceId).setValue(mechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    progressDialog.dismiss();
                                                                                                                    Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                                    startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                                    );
                                                                                                                    finish();
                                                                                                                } else {
                                                                                                                    progressDialog.dismiss();
                                                                                                                    Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }

                                                                                            }
                                                                                        });
                                                                                    } else {
                                                                                        progressDialog.dismiss();
                                                                                        Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });


                                                                        }


                                                                    } else {//if date is different


                                                                        FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_original).child(time_original).child(serviceId).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                                if (task.isSuccessful()) {
                                                                                    FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_new).child(time_new).child(serviceId).setValue(newBookingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            if (task.isSuccessful()) {
                                                                                                if (service.getMechanicID().equals("No_Mechanic")) {
                                                                                                    progressDialog.dismiss();
                                                                                                    Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                    startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                    );
                                                                                                    finish();
                                                                                                } else {
                                                                                                    FirebaseDatabase.getInstance().getReference("mechanics").child(service.getMechanicID()).child("Service_List")
                                                                                                            .child(serviceId).setValue(mechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                progressDialog.dismiss();
                                                                                                                Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                                startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                                );
                                                                                                                finish();
                                                                                                            } else {
                                                                                                                progressDialog.dismiss();
                                                                                                                Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            }

                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    progressDialog.dismiss();
                                                                                    Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                }


                                                                            }
                                                                        });


                                                                    }


                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    } else {//if the vehicle doesn't exist

                                                        //create it
                                                        VehicleHelper vehicleHelper = new VehicleHelper(Company, Model, vehicleNo);


                                                        UserRef.child("vehicles").child(newVehicleID).setValue(vehicleHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {


                                                                    //after creation attach service to it
                                                                    UserRef.child("vehicles").child(newVehicleID).child("services").child(serviceId).setValue(service).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override

                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {


                                                                                if (date_original.equals(date_new)) {

                                                                                    if (time_original.equals(time_new)) {//check if time is changed
                                                                                        FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_new).child(time_new).child(serviceId).setValue(newBookingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    if (service.getMechanicID().equals("No_Mechanic")) {
                                                                                                        progressDialog.dismiss();
                                                                                                        Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                        startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                        );
                                                                                                        finish();
                                                                                                    } else {
                                                                                                        FirebaseDatabase.getInstance().getReference("mechanics").child(service.getMechanicID()).child("Service_List")
                                                                                                                .child(serviceId).setValue(mechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    progressDialog.dismiss();
                                                                                                                    Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                                    startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                                    );
                                                                                                                    finish();
                                                                                                                } else {
                                                                                                                    progressDialog.dismiss();
                                                                                                                    Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }

                                                                                            }
                                                                                        });
                                                                                    } else {//time is different


                                                                                        //delete old time slot
                                                                                        FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_original).child(time_original).child(serviceId).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {

                                                                                                    //assign service on new time slot
                                                                                                    FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_new).child(time_new).child(serviceId).setValue(newBookingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                if (service.getMechanicID().equals("No_Mechanic")) {
                                                                                                                    progressDialog.dismiss();
                                                                                                                    Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                                    startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                                    );
                                                                                                                    finish();
                                                                                                                } else {
                                                                                                                    FirebaseDatabase.getInstance().getReference("mechanics").child(service.getMechanicID()).child("Service_List")
                                                                                                                            .child(serviceId).setValue(mechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                progressDialog.dismiss();
                                                                                                                                Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                                                startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                                                );
                                                                                                                                finish();
                                                                                                                            } else {
                                                                                                                                progressDialog.dismiss();
                                                                                                                                Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                                }
                                                                                                            }

                                                                                                        }
                                                                                                    });
                                                                                                } else {
                                                                                                    progressDialog.dismiss();
                                                                                                    Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });


                                                                                    }


                                                                                } else {//if date is different


                                                                                    FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_original).child(time_original).child(serviceId).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {


                                                                                            if (task.isSuccessful()) {
                                                                                                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("Slots").child(date_new).child(time_new).child(serviceId).setValue(newBookingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                                        if (task.isSuccessful()) {
                                                                                                            if (service.getMechanicID().equals("No_Mechanic")) {
                                                                                                                progressDialog.dismiss();
                                                                                                                Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();
                                                                                                                startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                                );
                                                                                                                startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                                );
                                                                                                                finish();

                                                                                                            } else {
                                                                                                                FirebaseDatabase.getInstance().getReference("mechanics").child(service.getMechanicID()).child("Service_List")
                                                                                                                        .child(serviceId).setValue(mechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                                        if (task.isSuccessful()) {
                                                                                                                            progressDialog.dismiss();
                                                                                                                            Toast.makeText(EditServiceActivity.this, "Service updated Successfully", Toast.LENGTH_SHORT).show();

                                                                                                                            startActivity(new Intent(EditServiceActivity.this, CustomerActivity.class)
                                                                                                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                                                                                                            );
                                                                                                                            finish();
                                                                                                                        } else {
                                                                                                                            progressDialog.dismiss();
                                                                                                                            Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        }

                                                                                                    }
                                                                                                });
                                                                                            } else {
                                                                                                progressDialog.dismiss();
                                                                                                Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                                            }


                                                                                        }
                                                                                    });


                                                                                }


                                                                            } else {
                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });

                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditServiceActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
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
        dateSlashNew = new SimpleDateFormat("dd/MM/yyyy").format(Long.valueOf(timeStamp));
        date_new = new SimpleDateFormat("dd_MM_yyyy").format(Long.valueOf(timeStamp));
        serviceDateL.getEditText().setText(dateSlashNew);

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
            time_new = (hr - 12) + "_" + min;
        } else {
            serviceTimeL.getEditText().setText(hr + ":" + min + " am");
            time_new = (hr - 12) + "_" + min;
        }


    }
}