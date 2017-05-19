package com.example.ilya.lorekeep;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ilya.lorekeep.auth.FragmentAdapter;
import com.example.ilya.lorekeep.config.NetworkThread;
import com.example.ilya.lorekeep.config.RetrofitFactory;
import com.example.ilya.lorekeep.user.userApi.UserApi;
import com.example.ilya.lorekeep.user.userApi.userModels.UserAnswerModel;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isAuth();

        viewPager = (ViewPager) findViewById(R.id.vpPager);
        adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


    }

    private boolean isAuth() {
        String sessionId;
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.sharedTitle), Context.MODE_PRIVATE);
        sessionId = sharedPref.getString(getString(R.string.sessionId), "");
        Log.e("mysessionId", "isAuth: " + sessionId);
        if ( sessionId.equals("") ) {
            Log.e("ddd", "isAuth: "+"sessionId is empty in sharedpref" );
            return false;
        } else {
            return sessionRequest(sessionId);
        }
    }

    private boolean sessionRequest(String sessionId) {
        final UserApi user = RetrofitFactory.retrofitLore().create(UserApi.class);
        final Call<String> call = user.isAuth("sessionId="+sessionId);
        NetworkThread.getInstance().execute(call, new NetworkThread.ExecuteCallback<String>(){

            @Override
            public void onSuccess(String result, Response<String> response) {
                Headers headers = response.headers();
                Log.e("onSuccess", "Response " + headers.toString());
                Log.e("onSuccess", "Success " + result.toString());

            }

            @Override
            public void onError(Exception ex) {
                Log.e("onErorr", "error " + ex.getMessage());
            }
        });
        return false;

    }

    public void setPage(int position){
        viewPager.setCurrentItem(position);
    }

}
