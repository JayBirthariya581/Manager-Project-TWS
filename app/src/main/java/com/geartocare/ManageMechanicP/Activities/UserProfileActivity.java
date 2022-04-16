package com.geartocare.ManageMechanicP.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.geartocare.ManageMechanicP.R;
import com.geartocare.ManageMechanicP.SessionManager;

import org.jetbrains.annotations.NotNull;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SessionManager sessionManager;
    TextView profile_full_name,profile_user_key;
    TextInputLayout profile_phone,profile_email,profile_FullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getWindow().setStatusBarColor(ContextCompat.getColor(UserProfileActivity.this,R.color.black));
        /*update = findViewById(R.id.update_user);*/


        /*------------------------------Hooks start---------------------------------------*/
        profile_full_name = findViewById(R.id.profile_full_name);
        profile_user_key = findViewById(R.id.profile_user_key);
        profile_FullName = findViewById(R.id.profile_FullName);
        profile_email = findViewById(R.id.profile_email);
        profile_phone = findViewById(R.id.profile_phone);

        drawerLayout = findViewById(R.id.drawer_layout_user_profile);
        navigationView = findViewById(R.id.nav_view_user_profile);
        toolbar = findViewById(R.id.toolbar_user_profile);
        /*------------------------------Hooks end---------------------------------------*/



        /*------------------------------Variables---------------------------------------*/
        sessionManager = new SessionManager(UserProfileActivity.this);
        /*------------------------------Variables Ends---------------------------------------*/







        showAllUSerData();



        /*------------------------------Navigation Part Starts---------------------------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
       /* menu.findItem(R.id.nav_admin).setVisible(false);
        menu.findItem(R.id.nav_tutorial).setVisible(false);*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(UserProfileActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(UserProfileActivity.this);
        navigationView.setCheckedItem(R.id.nav_profile);
        navigationView.bringToFront();
        navigationView.requestLayout();
        /*------------------------------Navigation Part Ends---------------------------------------*/




    }

    public void showAllUSerData(){



        profile_full_name.setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME)+" "+sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_lASTNAME));
        profile_email.getEditText().setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_EMAIL));
        profile_phone.getEditText().setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_PHONENUMBER));

        profile_FullName.getEditText().setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME)+" "+sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_lASTNAME));



    }








    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        {
            startActivity(new Intent(UserProfileActivity.this, CustomerActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

            );
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_ninja:
                startActivity(new Intent(UserProfileActivity.this, CustomerActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                );
                finish();
                break;


                case R.id.nav_Mechanic:
                    startActivity(new Intent(UserProfileActivity.this,MechanicActivity.class));
                    finish();


                    break;

            case R.id.nav_Manage:
                startActivity(new Intent(UserProfileActivity.this,ManageActivity.class));
                finish();

                break;



            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                new SessionManager(UserProfileActivity.this).logoutSession();

                startActivity(new Intent(UserProfileActivity.this,LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                );
                finish();

                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

}








}