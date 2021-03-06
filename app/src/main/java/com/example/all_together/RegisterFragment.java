package com.example.all_together;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    public static int MIN_CHARACTERS_PASSWORD = 6;
    public static int PHONE_NUMBER_CHARACTERS = 10;

    interface OnRegisterFragmentListener {
        void onRegister(String email, String password);
        void onPhoneRegister(String phoneNumber);
    }

    OnRegisterFragmentListener callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (OnRegisterFragmentListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement OnRegisterFragmentListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.register_fragment, container, false);

        final Button layout1 = rootView.findViewById(R.id.layout1);
        final Button layout2 = rootView.findViewById(R.id.layout2);

        final CardView cardView = rootView.findViewById(R.id.cardView);
        final CardView cardView1 = rootView.findViewById(R.id.cardView1);
        final CardView cardView2 = rootView.findViewById(R.id.cardView2);

        layout1.setOnClickListener(new View.OnClickListener() { //layout for volunteer
            @Override
            public void onClick(View v) {
                cardView2.setVisibility(View.GONE);
                cardView1.setVisibility(View.VISIBLE);
                layout1.setBackground(getResources().getDrawable(R.drawable.color_back4_circle));
                layout2.setBackground(getResources().getDrawable(R.drawable.background_button_light));
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() { //layout for need help
            @Override
            public void onClick(View v) {
                cardView1.setVisibility(View.GONE);
                cardView2.setVisibility(View.VISIBLE);
                layout1.setBackground(getResources().getDrawable(R.drawable.background_button_light));
                layout2.setBackground(getResources().getDrawable(R.drawable.color_back4_circle));

            }
        });


        final EditText passwordEditText = rootView.findViewById(R.id.password_register);
        final EditText emailEditText = rootView.findViewById(R.id.email_register);

        //final EditText passwordOldEditText = rootView.findViewById(R.id.password_old_register);
        final EditText phoneEditText = rootView.findViewById(R.id.phone_register);

        Button submitOldBtn = rootView.findViewById(R.id.submit_register_help);

        submitOldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNum = phoneEditText.getText().toString();
                String phoneNumber;
                if (phoneNum.equals(""))
                    phoneNumber = "";
                else
                    phoneNumber = phoneNum.substring(1);

                if(TextUtils.isEmpty(phoneNumber)){
                    phoneEditText.setError(getResources().getString(R.string.phone_required));
                    return;
                }

                if (phoneNumber.length()< (PHONE_NUMBER_CHARACTERS-1) ) {
                    passwordEditText.setError(getResources().getString(R.string.phone_required) + " "
                            + PHONE_NUMBER_CHARACTERS + " " + getResources().getString(R.string.numbers));
                    return;
                }

                callback.onPhoneRegister(phoneNumber);

            }
        });

        Button submitBtn = rootView.findViewById(R.id.submit_register);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String password = passwordEditText.getText().toString();
                final String email = emailEditText.getText().toString();

                if(TextUtils.isEmpty(email)){
                     emailEditText.setError(getResources().getString(R.string.user_email_is_required));
                     return;
                }

                if(TextUtils.isEmpty(password)){
                    passwordEditText.setError(getResources().getString(R.string.password_is_required));
                    return;
                }

                if (password.length() < MIN_CHARACTERS_PASSWORD ) {
                    passwordEditText.setError(getResources().getString(R.string.password_is_required) + " " + MIN_CHARACTERS_PASSWORD
                            + " " + getResources().getString(R.string.characters));
                    return;
                }

                callback.onRegister(email, password);

            }
        });

        return rootView;
      }
}
