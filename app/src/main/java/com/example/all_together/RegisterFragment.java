package com.example.all_together;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {

    interface OnRegisterFragmentListener{
        void onRegister(String username, String password, String email);
    }

    OnRegisterFragmentListener callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (OnRegisterFragmentListener) context;
        } catch (ClassCastException ex){
            throw new ClassCastException("The activity must implement OnRegisterFragmentListener inteface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.register_fragment, container, false);

        final EditText usernameEditText = rootView.findViewById(R.id.username_register);
        final EditText passwordEitText = rootView.findViewById(R.id.password_register);
        final EditText emailEditText = rootView.findViewById(R.id.email_register);

        Button submitBtn = rootView.findViewById(R.id.submit_register);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onRegister(usernameEditText.getText().toString(),
                        passwordEitText.getText().toString(),
                        emailEditText.getText().toString());
            }
        });

        return rootView;
    }
}
