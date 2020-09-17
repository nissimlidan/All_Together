package com.example.all_together;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.all_together.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RegisterFragment.OnRegisterFragmentListener {

    final String FRAGMENT_REGISTER_TAG = "fragment_register";
    final int RC_SIGN_IN = 1;
    private Toolbar toolbar;
    private CardView cardView;

    private ArrayList<User> Users = new ArrayList<>();

    private DatabaseReference mDatabase;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CoordinatorLayout coordinatorLayout;

    private CollapsingToolbarLayout collapsingToolbarLayout;


    private SignInButton googleSignInButton;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth.AuthStateListener listener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Go to Main App Activity
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //check if user is null
        if (firebaseUser != null){

            Toast.makeText(this, "Auto Login From FireBase", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(this, MainAppActivity.class);
            startActivity(intent);
            finish();
        }

        googleSignInButton = findViewById(R.id.googleSignIn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icons_menu_w);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setIcon(R.drawable.icons_menu_w);

        cardView = findViewById(R.id.cardView);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        collapsingToolbarLayout = findViewById(R.id.collapsingLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        navigationView = findViewById(R.id.navigation_view);

        final EditText passwordEt = findViewById(R.id.passwordInput);
        final EditText emailEt = findViewById(R.id.emailInput);

        TextView register_btn = (TextView) findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.setVisibility(View.GONE);

                FragmentManager registerFragment = getSupportFragmentManager();
                FragmentTransaction transaction = registerFragment.beginTransaction();
                transaction.add(R.id.coordinatorLayout,new RegisterFragment(), FRAGMENT_REGISTER_TAG);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button login = findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = passwordEt.getText().toString();
                String email = emailEt.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Add why a user was unable to log in
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Sign In in Successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(),MyHomeActivity.class);
//                            //intent.putExtra(userName,"userName");
//                            startActivity(intent);

                            Intent intent = new Intent(getApplicationContext(),MainAppActivity.class);
                            startActivity(intent);

                            finish();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Sign In Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                    }
                });


        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                 FirebaseUser user = firebaseAuth.getCurrentUser();

                 if (user != null) { //sign in or sign up

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


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//                item.setChecked(true);

                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.sign_up:
                        Toast.makeText(MainActivity.this, "Sign Up", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sign_in:
                        Toast.makeText(MainActivity.this, "Sign In", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sign_out:
                        Toast.makeText(MainActivity.this, "Sign Out", Toast.LENGTH_SHORT).show();
                        //firebaseAuth.signOut();
                        break;
                }

                return false;
            }
        });

    }

    public void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){

            drawerLayout.openDrawer(GravityCompat.START);
        }
//        switch (item.getItemId()) {
//            case R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                Toast.makeText(this, "Home is selected", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.SignIn:
//                Toast.makeText(this, "SignIn is selected", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.SignUp:
//                Toast.makeText(this, "SignUp is selected", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.SignOut:
//                mAuth.signOut();
//                Toast.makeText(this, "SignOut is selected", Toast.LENGTH_SHORT).show();
//                break;
//        }

        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu, menu);
//        return true;
//    }

    @Override
    public void onRegister(final String userName, String password, String email) {

//        cardView.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sign Up in Successful" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
                    startActivity(intent);
//                    String user_id = mAuth.getCurrentUser().getUid();
//                    DatabaseReference CurrentUser_db =  mDatabase.child(user_id);
//                    CurrentUser_db.child("Email").setValue(email);
//                    CurrentUser_db.child("Password").setValue(password);
                }

                //Add why a user was unable to log in
                else
                    Toast.makeText(MainActivity.this, "Sign Up Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_REGISTER_TAG);
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode==RC_SIGN_IN & resultCode == RESULT_OK){
           Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
           handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount>completedTask){
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(MainActivity.this, "Signed In With Google Successfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Sign In With Google Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }

    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser fUser){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            Toast.makeText(MainActivity.this, personName + personEmail, Toast.LENGTH_SHORT).show();




        }
    }
}
