package com.example.ilya.lorekeep.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilya.lorekeep.R;


public class SignUpFragment extends Fragment {

    private static final String TITLE = "TITLE";
    private String title;
    private static final String TAG = "signup fragment";
    private EditText emailText;
    private EditText passwordText;
    private EditText passwordTextRepeat;
    private Button signupButton;
    private AuthProgressDialog progressDialog;
    private ViewPager viewPager;

    public static SignUpFragment newInstance(String title) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        signupButton = (Button) root.findViewById(R.id.button_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        emailText = (EditText) root.findViewById(R.id.input_email_signup);
        passwordText = (EditText) root.findViewById(R.id.input_password_signup);
        passwordTextRepeat = (EditText) root.findViewById(R.id.input_password_signup_repeat);
        viewPager = (ViewPager) getActivity().findViewById(R.id.vpPager);
        return root;
    }

    public void signUp() {
        Log.d(TAG, "SignUp");

        if (!validate()) {
            onSignUpFailed();
            return;
        }

        signupButton.setEnabled(false);

        progressDialog = new AuthProgressDialog(getContext(), "SignUp");
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        if (signUpExecute()){
                            onSignUpSuccess();
                        } else {
                            onSignUpFailed();
                        }

                    }
                }, 2000);

    }

    public void onSignUpFailed() {
        Toast.makeText( getContext(),"SignUp failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);

    }

    public void onSignUpSuccess() {

        progressDialog = null;
        signupButton.setEnabled(true);
        viewPager.setCurrentItem(0);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordRepeat = passwordTextRepeat.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty()|| password.length() < 4 || password.length() > 10) {
            passwordText.setError("password empty or < 4 or > 10");
            passwordTextRepeat.setError("password empty or < 4 or > 10");
            valid = false;
        } else {
            passwordText.setError(null);
            passwordTextRepeat.setError(null);
        }
        if (!passwordRepeat.equals(password)) {
            passwordTextRepeat.setError("please repeat right");
            valid = false;
        } else {
            passwordTextRepeat.setError(null);
        }

        return valid;
    }

    public boolean signUpExecute() {
        //TODO logic
        return true;
    }
}
