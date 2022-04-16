package com.geartocare.ManageMechanicP.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.SessionManager;
import com.geartocare.ManageMechanicP.databinding.ActivityMainBinding;
import com.geartocare.ManageMechanicP.databinding.ActivityManageBinding;

import org.jetbrains.annotations.NotNull;

public class ManageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ActivityManageBinding binding;
    SessionManager sessionManager;
    
   
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ManageActivity.this,R.color.black));
        sessionManager = new SessionManager(ManageActivity.this);


        /*------------------------------Navigation Part Starts---------------------------------------*/
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Manage");
        Menu menu = binding.navView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        /*menu.findItem(R.id.nav_admin).setVisible(false);
        menu.findItem(R.id.nav_tutorial).setVisible(false);*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ManageActivity.this,binding.drawerLayout,binding.toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(ManageActivity.this);
        binding.navView.setCheckedItem(R.id.nav_Manage);
        binding.navView.bringToFront();
        binding.navView.requestLayout();

        /*------------------------------Navigation Part Ends---------------------------------------*/
        


       /* binding.ManageCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ManageActivity.this, ManageCouponActivity.class));

            }
        });


        binding.ManageServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ManageActivity.this, ManageServiceActivity.class));

            }
        });


        binding.manageSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ManageActivity.this, ManageSlotActivity.class));

            }
        });



        binding.ManagePackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ManageActivity.this, ManagePackageActivity.class));

            }
        });
*/

        binding.ManageInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ManageActivity.this,PartListActivity.class));
            }
        });


    }







    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){



            case R.id.nav_ninja:
                startActivity(new Intent(ManageActivity.this, CustomerActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                );
                finish();
                break;

            case R.id.nav_Mechanic:
                startActivity(new Intent(ManageActivity.this, MechanicActivity.class));

                break;

            case R.id.nav_Manage:


                break;




            case R.id.nav_profile:
                startActivity(new Intent(ManageActivity.this, UserProfileActivity.class));


                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                sessionManager.logoutSession();
                startActivity(new Intent(ManageActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                );
                finish();
                break;


        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
    
    
    
    
    
    
    
    
}