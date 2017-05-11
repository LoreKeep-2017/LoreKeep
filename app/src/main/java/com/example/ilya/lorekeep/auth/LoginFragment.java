package com.example.ilya.lorekeep.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilya.lorekeep.LoginActivity;
import com.example.ilya.lorekeep.MainActivity;
import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.NetworkThread;
import com.example.ilya.lorekeep.config.RetrofitFactory;
import com.example.ilya.lorekeep.user.userApi.UserApi;
import com.example.ilya.lorekeep.user.userApi.userModels.UserAnswerModel;
import com.example.ilya.lorekeep.user.userApi.userModels.UserModel;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    private static final String TITLE = "TITLE";
    private static final String TAG = "login fragment";
    private String title;
    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private AuthProgressDialog progressDialog;

    public static LoginFragment newInstance(String title) {
        LoginFragment fragmentFirst = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (Button) root.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        emailText = (EditText) root.findViewById(R.id.input_email_login);
        passwordText = (EditText) root.findViewById(R.id.input_password_login);
        return root;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        progressDialog = new AuthProgressDialog(getContext(), "LogIn");

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        if (loginExecute()){
                            onLoginSuccess();
                        } else {
                            onLoginFailed();
                        }
                    }
                }, 2000);
    }

    public void onLoginFailed() {
        Toast.makeText( getContext(),"Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);

    }

    public void onLoginSuccess() {

        progressDialog = null;
        loginButton.setEnabled(true);

        UserModel newUser = new UserModel();
        newUser.setLogin("ilya");
        newUser.setPassword("ilya");
        final UserApi user = RetrofitFactory.retrofitLore().create(UserApi.class);
        final Call<UserAnswerModel> call = user.signIn(newUser);
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

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(Exception ex){
                Log.d("onError", "Error " + ex.toString());

            }
        });


    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("password empty");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    public boolean loginExecute() {
        //TODO logic
        progressDialog.dismiss();
        return true;
    }


}
