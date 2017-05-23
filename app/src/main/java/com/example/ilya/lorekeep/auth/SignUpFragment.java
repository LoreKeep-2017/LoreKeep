package com.example.ilya.lorekeep.auth;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.NetworkThread;
import com.example.ilya.lorekeep.config.RetrofitFactory;
import com.example.ilya.lorekeep.user.userApi.UserApi;
import com.example.ilya.lorekeep.user.userApi.userModels.UserAnswerModel;
import com.example.ilya.lorekeep.user.userApi.userModels.UserModel;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;


public class SignUpFragment extends Fragment {

    private static final String TITLE = "TITLE";
    private String title;
    private static final String TAG = "signup fragment";
    private final static int MY_PERMISSION_READ_PHONE = 1;


    private EditText loginText;
    private EditText phoneText;
    private EditText emailText;
    private EditText passwordText;
    private EditText passwordTextRepeat;
    private Button signupButton;


    private String login;
    private String phone;
    private String email;
    private String password;

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
    @TargetApi(23)
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

        loginText = (EditText) root.findViewById(R.id.input_login_signup);
        phoneText = (EditText) root.findViewById(R.id.input_phone_signup);
        emailText = (EditText) root.findViewById(R.id.input_email_signup);
        passwordText = (EditText) root.findViewById(R.id.input_password_signup);
        passwordTextRepeat = (EditText) root.findViewById(R.id.input_password_signup_repeat);

        viewPager = (ViewPager) getActivity().findViewById(R.id.vpPager);
        return root;
    }

    public void signUp() {
        Log.d(TAG, "SignUp");

        if (!validate()) {
            onSignUpFailed("incorrect inputs");
            return;
        }

        signupButton.setEnabled(false);

        progressDialog = new AuthProgressDialog(getContext(), "SignUp");
        signUpExecute();
    }

    public void onSignUpFailed(String cause) {
        Toast.makeText( getContext(),"Login failed: " + cause, Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
        if (progressDialog != null){
            progressDialog.dismiss();
        }

    }

    public void onSignUpSuccess() {

        if (progressDialog != null){
            progressDialog.dismiss();
        }
        progressDialog = null;
        signupButton.setEnabled(true);
        clearFragment();
        viewPager.setCurrentItem(0);
    }

    private void clearFragment(){
        loginText.setText("");
        emailText.setText("");
        phoneText.setText("");
        emailText.setText("");
        passwordText.setText("");
        passwordTextRepeat.setText("");
    }

    public boolean validate() {
        boolean valid = true;

        login = loginText.getText().toString();
        phone = phoneText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        String passwordRepeat = passwordTextRepeat.getText().toString();

        if (login.isEmpty() || login.length() < 4 ){
            this.loginText.setError("enter a valid login > 4");
            valid = false;
        } else {
            loginText.setError(null);
        }

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()){
            this.phoneText.setError("enter valid phone");
            valid = false;
        } else {
            phoneText.setError(null);
        }

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

    public void signUpExecute() {

        UserModel newUser = new UserModel();
        newUser.setLogin(login);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setPhonenumber(phone);

        final UserApi user = RetrofitFactory.retrofitLore().create(UserApi.class);
        final Call<UserAnswerModel> call = user.signUp(newUser);
        NetworkThread.getInstance().execute(call, new NetworkThread.ExecuteCallback<UserAnswerModel>(){

            @Override
            public void onSuccess(UserAnswerModel result, Response<UserAnswerModel> response){
                Headers headers = response.headers();
                Log.d("onSuccess", "Response " + headers.toString());
                Log.d("onSuccess", "Success " + result.getSessionId());
                Log.d("onSuccess", "Success " + result.getUserId());

                SharedPreferences sharedPref = getContext().getSharedPreferences(
                        getString(R.string.sharedTitle), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sessionId), result.getSessionId());
                editor.putInt(getString(R.string.userId), result.getUserId());
                editor.apply();

                onSignUpSuccess();

            }

            @Override
            public void onError(Exception ex){
                Log.d("onError", "Error " + ex.getMessage());
                onSignUpFailed(ex.getMessage());

            }
        });
    }
}
