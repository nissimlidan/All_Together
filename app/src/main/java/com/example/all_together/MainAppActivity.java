package com.example.all_together;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.all_together.ui.BottomNavigationViewHelper;
import com.example.all_together.ui.add.AddFragment;
import com.example.all_together.ui.chat.ChatFragment;
import com.example.all_together.ui.dashboard.DashboardFragment;
import com.example.all_together.ui.home.HomeFragment;
import com.example.all_together.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainAppActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        coordinatorLayout = findViewById(R.id.container);
        navigationView = findViewById(R.id.navigation_view_activitymainapp);
        drawerLayout = findViewById(R.id.drawerLayout_activitymainapp);

        Toolbar toolbar = findViewById(R.id.toolbar_activitymainapp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icons_menu_w);

        View headerView = navigationView.getHeaderView(0);
        TextView userTv = headerView.findViewById(R.id.navigation_header_container);
        userTv.setText(firebaseUser.getEmail());
        if (firebaseUser != null) { //sign in or sign up
            navigationView.getMenu().findItem(R.id.sign_in).setVisible(false);
            navigationView.getMenu().findItem(R.id.sign_up).setVisible(false);
            navigationView.getMenu().findItem(R.id.sign_out).setVisible(true);
        } else { // sign out
            navigationView.getMenu().findItem(R.id.sign_in).setVisible(true);
            navigationView.getMenu().findItem(R.id.sign_up).setVisible(true);
            navigationView.getMenu().findItem(R.id.sign_out).setVisible(false);
        }

        // No need for action bar because we have a toolbar as an action bar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.icons_menu_w);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.sign_up:
                        Toast.makeText(MainAppActivity.this, "Sign Up", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sign_in:
                        Toast.makeText(MainAppActivity.this, "Sign In", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sign_out:
                        Toast.makeText(MainAppActivity.this, "Sign Out", Toast.LENGTH_SHORT).show();
                        logout(new View(MainAppActivity.this));
                        break;
                }
                return false;
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                View headerView = navigationView.getHeaderView(0);
                TextView userTv = headerView.findViewById(R.id.navigation_header_container);

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) { //sign in or sign up

                    userTv.setText(user.getEmail());

                    navigationView.getMenu().findItem(R.id.sign_in).setVisible(false);
                    navigationView.getMenu().findItem(R.id.sign_up).setVisible(false);
                    navigationView.getMenu().findItem(R.id.sign_out).setVisible(true);
                } else { // sign out
                    navigationView.getMenu().findItem(R.id.sign_in).setVisible(true);
                    navigationView.getMenu().findItem(R.id.sign_up).setVisible(true);
                    navigationView.getMenu().findItem(R.id.sign_out).setVisible(false);
                }
            }
        };


        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.nav_view);
        setBottomNavigationViewEx();
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    private BottomNavigationViewEx.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_dashboard:
                            selectedFragment = new DashboardFragment();
                            break;
                        case R.id.navigation_add:
                            selectedFragment = new AddFragment();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.navigation_chat:
                            selectedFragment = new ChatFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };

    private void setBottomNavigationViewEx() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.nav_view);
        BottomNavigationViewHelper.setBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(this, bottomNavigationViewEx);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}