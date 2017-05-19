package com.example.ilya.lorekeep.auth;

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

import com.example.ilya.lorekeep.MainActivity;
import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.config.NetworkThread;
import com.example.ilya.lorekeep.config.RetrofitFactory;
import com.example.ilya.lorekeep.topic.TopicActivity;
import com.example.ilya.lorekeep.topic.dao.Topic;
import com.example.ilya.lorekeep.topic.topicApi.TopicApi;
import com.example.ilya.lorekeep.topic.topicApi.models.TopicModel;
import com.example.ilya.lorekeep.user.userApi.UserApi;
import com.example.ilya.lorekeep.user.userApi.userModels.UserAnswerModel;
import com.example.ilya.lorekeep.user.userApi.userModels.UserModel;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    private static final String TITLE = "TITLE";
    private static final String TAG = "login fragment";
    private String title;
    private EditText loginText;
    private EditText passwordText;
    private Button loginButton;
    private AuthProgressDialog progressDialog;

    private String login;
    private String password;

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
        loginText = (EditText) root.findViewById(R.id.input_login_login);
        passwordText = (EditText) root.findViewById(R.id.input_password_login);
        return root;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("incorrect inputs");
            return;
        }

        loginButton.setEnabled(false);

        progressDialog = new AuthProgressDialog(getContext(), "LogIn");
        loginExecute();
    }

    public void onLoginFailed(String cause) {
        Toast.makeText(getContext(), "Login failed: " + cause, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void onLoginSuccess() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
        loginButton.setEnabled(true);
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);

    }

    public boolean validate() {
        boolean valid = true;

        login = loginText.getText().toString();
        password = passwordText.getText().toString();

        if (login.isEmpty()) {
            this.loginText.setError("login");
            valid = false;
        } else {
            loginText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("password empty");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    public void loginExecute() {
        UserModel newUser = new UserModel();
        newUser.setLogin(login);
        newUser.setPassword(password);
        final UserApi user = RetrofitFactory.retrofitLore().create(UserApi.class);
        final Call<UserAnswerModel> call = user.signIn(newUser);
        NetworkThread.getInstance().execute(call, new NetworkThread.ExecuteCallback<UserAnswerModel>() {

            @Override
            public void onSuccess(UserAnswerModel result, Response<UserAnswerModel> response) {
                Headers headers = response.headers();
                Log.d("onSuccess", "Response " + headers.toString());
                Log.d("onSuccess", "Success " + result.getSessionId());
                Log.d("onSuccess", "Success " + result.getUserId());

                SharedPreferences sharedPref = getContext().getSharedPreferences(
                        getString(R.string.sharedTitle), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sessionId), result.getSessionId());
                editor.putInt(getString(R.string.userId), result.getUserId());
                editor.putBoolean(getString(R.string.firstTime), true);
                editor.apply();

                fetchAllTopics(result.getUserId());

                onLoginSuccess();
            }

            @Override
            public void onError(Exception ex) {
                Log.d("onError", "Error " + ex.getMessage());
                onLoginFailed(ex.getMessage());

            }
        });
    }


    public void fetchAllTopics(final Integer userId){
        final TopicApi topic = RetrofitFactory.retrofitLore().create(TopicApi.class);
        final Call<List<TopicModel>> callPullAll = topic.getAllTopics(userId);
        NetworkThread.getInstance().execute(callPullAll, new NetworkThread.ExecuteCallback<List<TopicModel>>(){
            @Override
            public void onSuccess(List<TopicModel> result, Response<List<TopicModel>> response){
                try {
                    Log.d("on get all topics", "result size " + result.size());
                    for(int i = 0; i < result.size(); ++i) {
                        Topic mTopic = new Topic();
                        mTopic.setTopicUserId(userId);
                        mTopic.setTopicTitle(result.get(i).getTitle());
                        mTopic.setServerTopicId(result.get(i).getTopicId());
                        mTopic.setTopicChanged(false);
                        mTopic.setTopicCreated(false);
                        mTopic.setTopicDeleted(false);
                        mTopic.setTopicCreationDate(new Date());
                        HelperFactory.getHelper().getTopicDAO().setTopic(mTopic);
                    }
                } catch (SQLException e) {
                    Log.e("on create", "fail to get query");
                }
            }


            @Override
            public void onError(Exception ex) {
                Log.d("onError", "Error " + ex.toString());
            }
        });
    }


}
