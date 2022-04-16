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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.SessionManager;

import org.jetbrains.annotations.NotNull;

public class MechanicActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button addMechanic,listMechanic,searchMechanic;
    DatabaseReference DBref;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView fullName;
    TextInputLayout phoneMechanic;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic);
        getWindow().setStatusBarColor(ContextCompat.getColor(MechanicActivity.this,R.color.black));



        /*------------------------------Hooks start---------------------------------------*/
        drawerLayout = findViewById(R.id.drawer_layout_mechanic);
        navigationView = findViewById(R.id.nav_view_mechanic);
        toolbar = findViewById(R.id.toolbar_mechanic);
        addMechanic = findViewById(R.id.addMechanic);
        listMechanic = findViewById(R.id.listMechanic);
       /* fullName = findViewById(R.id.profile_full_name);*/
        searchMechanic = findViewById(R.id.searchMechanic);
        phoneMechanic = findViewById(R.id.phoneMechanic);
        /*------------------------------Hooks end---------------------------------------*/



        /*------------------------------Variables---------------------------------------*/
        DBref= FirebaseDatabase.getInstance().getReference("mechanics");
        sessionManager = new SessionManager(MechanicActivity.this);



        /*------------------------------Variables end---------------------------------------*/



        /*------------------------------Navigation Part Starts---------------------------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage");
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        /*menu.findItem(R.id.nav_admin).setVisible(false);
        menu.findItem(R.id.nav_tutorial).setVisible(false);*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MechanicActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(MechanicActivity.this);
        navigationView.setCheckedItem(R.id.nav_Mechanic);
        navigationView.bringToFront();
        navigationView.requestLayout();

        /*------------------------------Navigation Part Ends---------------------------------------*/



        String phone = phoneMechanic.getEditText().getText().toString();


        Query checkUser = DBref.orderByChild("phone").equalTo(phone);

        searchMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneMechanic.getEditText().getText().toString().equals("")){
                    Toast.makeText(MechanicActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }else {


                    Query checkUser = DBref.orderByChild("phone").equalTo(phoneMechanic.getEditText().getText().toString());

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {

                                startActivity(new Intent(MechanicActivity.this, MechanicDisplayActivity.class).putExtra("phone", phoneMechanic.getEditText().getText().toString()));



                                phoneMechanic.getEditText().setText("");


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MechanicActivity.this);
                                builder.setIcon(R.drawable.ic_mechanic);
                                builder.setTitle("Manage | GearToCare");

                                builder.setMessage("No such mechanic exist. Add new");

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



        addMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MechanicActivity.this,MechanicRegisterActivity.class));
            }
        });


        listMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MechanicActivity.this,ListMechanicActivity.class));
            }
        });






    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(MechanicActivity.this, CustomerActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

        );
        finish();
    }






    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){



            case R.id.nav_ninja:

                startActivity(new Intent(MechanicActivity.this, CustomerActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                );
                finish();
                break;

            case R.id.nav_Mechanic:

                break;

           /* case R.id.nav_admin:
                *//* startActivity(new Intent(CustomerActivity.this,AdminSearchActivity.class));*//*
                finish();
                break;*/


            case R.id.nav_Manage:
                startActivity(new Intent(MechanicActivity.this,ManageActivity.class));
                finish();

                break;


            case R.id.nav_profile:
                startActivity(new Intent(MechanicActivity.this,UserProfileActivity.class));
                finish();

                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                sessionManager.logoutSession();
                startActivity(new Intent(MechanicActivity.this,LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                );
                finish();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}