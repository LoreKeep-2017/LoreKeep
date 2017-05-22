package com.example.ilya.lorekeep.note.noteApi;

import com.example.ilya.lorekeep.note.noteApi.noteModels.NoteAnswer;
import com.example.ilya.lorekeep.note.noteApi.noteModels.NoteModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NoteApi {

    @POST("/api/note")
    Call<NoteAnswer> createNote(@Body NoteModel newNote, @Header("Cookie") String sessionId);

    @GET("/api/note/{noteId}")
    Call<List<NoteModel>> getAllNoteByTopic(@Path("noteId") int noteId);

    @GET("/api/note/all/{userId}")
    Call<List<NoteModel>> getAllNoteByUserId(@Path("userId") int userId);

    @DELETE("/api/note/{noteId}")
    Call<String> deleteNote(@Path("noteId") int noteId);

    @GET("/api/changes/note/{topicId}")
    Call<List<NoteModel>> getChanges(@Path("topicId") int topicId, @Header("Cookie") String sessionId);

//    @GET("/api/changes/delete/")

//    @PUT("/api/note")
//    Call<> updateNote()

}
