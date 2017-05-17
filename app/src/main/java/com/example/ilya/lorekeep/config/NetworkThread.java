package com.example.ilya.lorekeep.config;

import android.os.Handler;
import android.os.Looper;

import com.example.ilya.lorekeep.user.userApi.userModels.UserAnswerModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class NetworkThread {

    private final static NetworkThread INSTANCE = new NetworkThread();

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final String MESSAGE = "message";
    private final String CAUSE = "cause";

    private NetworkThread() {
    }

    public static NetworkThread getInstance() {
        return INSTANCE;
    }

    public <T> void execute(final Call<T> call, final ExecuteCallback<T> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response<T> response = call.execute();
                    if (response.isSuccessful()) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(response.body(), response);
                            }
                        });
                    } else {
                        throw new IOException(getCauseError(response));
                    }
                } catch (final IOException e) {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface ExecuteCallback<T> {
        void onSuccess(T result, Response<T> response);

        void onError(Exception ex);
    }

    private String getCauseError(Response response) throws IOException, JSONException {
        final String error = response.errorBody().string();
        JSONObject jObjError = new JSONObject(error);
        return jObjError.getJSONObject(MESSAGE).getString(CAUSE);

    }
}

