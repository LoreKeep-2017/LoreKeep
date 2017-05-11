package com.example.ilya.lorekeep.topic.topicApi;

import com.example.ilya.lorekeep.topic.topicApi.models.TopicAnswer;
import com.example.ilya.lorekeep.topic.topicApi.models.TopicModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TopicApi {

    @POST("/api/topic")
    Call<TopicAnswer> createNewTopic(@Body TopicModel newTopic);

    @GET("/api/topic/{userId}")
    Call<List<TopicModel>> getAllTopics(@Path("userId") int userId);

    @DELETE("/api/delete/{id}")
    Call<Response> deleteTopic(@Path("id") int topicId);

}
