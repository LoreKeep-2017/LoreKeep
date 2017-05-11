package com.example.ilya.lorekeep.user.userApi;

import com.example.ilya.lorekeep.user.userApi.userModels.UserAnswerModel;
import com.example.ilya.lorekeep.user.userApi.userModels.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @POST("/api/signup")
    Call<UserAnswerModel> signUp(@Body UserModel newUser);

    @POST("/api/signin")
    Call<UserAnswerModel> signIn(@Body UserModel user);

    @GET("api/auth")
    Call<String> isAuth();

    @GET("api/session")
    Call<UserAnswerModel> getSession();

    @DELETE("api/session")
    Call<UserAnswerModel> deleteSession();

    @GET("/api/user/{id}")
    Call<UserAnswerModel> getUserById(@Path("id") int id);

//    @DELETE("/api/user/{id}")
//    Call<> deleteUser(@Path("id") int id);

}
