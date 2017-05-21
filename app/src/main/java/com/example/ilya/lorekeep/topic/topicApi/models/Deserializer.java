package com.example.ilya.lorekeep.topic.topicApi.models;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

//public class Deserializer implements JsonDeserializer<TopicModel> {
//
//    private final Gson gson = new GsonBuilder().create();
//
//    @Override
//    public TopicModel deserialize(JsonElement json, Type typeOf, JsonDeserializationContext context) throws JsonParseException {
//        TopicModel topicModel = gson.fromJson(json, typeOf);
//
//        JsonObject jsonObject = json.getAsJsonObject();
//
//        return topicModel;
//    }
//}
