package com.geartocare.ManageMechanicP.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference DBref;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    SessionManager sessionManager;
    HashMap<String, String> userData;

    String token;
    TextInputLayout phone;
    TextView fullName;
    Button searchCustomer;
    MaterialCardView todaysBookings, unAppBooking, assBooking;
    MaterialCardView addCustomer, listCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        getWindow().setStatusBarColor(ContextCompat.getColor(CustomerActivity.this, R.color.black));


        processToken();



        /*------------------------------Hooks start---------------------------------------*/
        drawerLayout = findViewById(R.id.drawer_layout_ninja);
        navigationView = findViewById(R.id.nav_view_ninja);
        toolbar = findViewById(R.id.toolbar_ninja);

        phone = findViewById(R.id.phoneCustomer);
        todaysBookings = findViewById(R.id.todaysBookings);
        unAppBooking = findViewById(R.id.unAppBooking);
        assBooking = findViewById(R.id.assBooking);
        addCustomer = findViewById(R.id.addCustomer);
        fullName = findViewById(R.id.profile_full_name);
        listCustomer = findViewById(R.id.listCustomer);
        searchCustomer = findViewById(R.id.searchCustomer);





        /*------------------------------Hooks end---------------------------------------*/





        /*------------------------------Variables---------------------------------------*/
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        sessionManager = new SessionManager(CustomerActivity.this);
        userData = sessionManager.getUsersDetailsFromSessions();

        /*------------------------------Variables end---------------------------------------*/





        /*------------------------------Navigation Part Starts---------------------------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
     /*   menu.findItem(R.id.nav_admin).setVisible(false);
        menu.findItem(R.id.nav_tutorial).setVisible(false);*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(CustomerActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(CustomerActivity.this);
        navigationView.setCheckedItem(R.id.nav_ninja);
        navigationView.bringToFront();
        navigationView.requestLayout();

        /*------------------------------Navigation Part Ends---------------------------------------*/


        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerActivity.this, CustomerRegisterActivity.class));
            }
        });

        searchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getEditText().getText().toString().equals("")) {
                    Toast.makeText(CustomerActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else {


                    Query checkUser = DBref.orderByChild("phone").equalTo(phone.getEditText().getText().toString());

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {


                                for(DataSnapshot snap : snapshot.getChildren()){
                                    startActivity(new Intent(CustomerActivity.this, CustomerDisplayActivity.class).putExtra("uid", snap.getKey()));

                                    phone.getEditText().setText("");
                                    break;

                                }




                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerActivity.this);
                                builder.setIcon(R.drawable.ic_mechanic);
                                builder.setTitle("Manage | GearToCare");

                                builder.setMessage("No such User exist. Add new");

                                builder.show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }

            }
        });


        listCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CustomerActivity.this, ListCustomerActivity.class));


            }
        });


        todaysBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerActivity.this, BookingListActivity.class).putExtra("type","On_Hold"));
            }
        });

        unAppBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerActivity.this, BookingListActivity.class).putExtra("type","Done"));
            }
        });

        assBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerActivity.this, BookingListActivity.class).putExtra("type","Assigned"));
            }
        });


    }






    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.nav_ninja:
                break;

            case R.id.nav_Mechanic:
                startActivity(new Intent(CustomerActivity.this, MechanicActivity.class));

                break;


            case R.id.nav_Manage:
                startActivity(new Intent(CustomerActivity.this, ManageActivity.class));

                break;

            /* case R.id.nav_admin:
             *//* startActivity(new Intent(CustomerActivity.this,AdminSearchActivity.class));*//*
                finish();
                break;*/


            case R.id.nav_profile:
                startActivity(new Intent(CustomerActivity.this, UserProfileActivity.class));


                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                sessionManager.logoutSession();
                startActivity(new Intent(CustomerActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                );
                finish();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    private void processToken() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (task.isSuccessful()) {

                    token = task.getResult();


                    FirebaseDatabase.getInstance().getReference("managers").child(sessionManager.getUsersDetailsFromSessions().get(SessionManager.KEY_PHONENUMBER)).child("token").setValue(token)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        SharedPreferences.Editor editor = new SessionManager(CustomerActivity.this).getEditor();


                                        editor.putString(SessionManager.KEY_TOKEN, token);
                                        editor.commit();


                                    }

                                }
                            });


                }

            }
        });


    }
}