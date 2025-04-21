package com.example.mobile_chat_bot.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.mobile_chat_bot.BuildConfig;


public class MistralApiClient {

    private static final String TAG = "MistralApiClient";
    private static final String API_KEY = BuildConfig.MISTRAL_API_KEY;
    private static final String MODEL = "mistral-tiny";
    private static final String API_URL = "https://api.mistral.ai/v1/chat/completions";

    private final OkHttpClient client;
    private final Gson gson;

    public MistralApiClient() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public void sendMessage(String userMessage, MistralResponseCallback callback) {
        JsonObject json = new JsonObject();
        json.addProperty("model", MODEL);

        JsonArray messages = new JsonArray();
        JsonObject msg = new JsonObject();
        msg.addProperty("role", "user");
        msg.addProperty("content", userMessage);
        messages.add(msg);

        json.add("messages", messages);

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed", e);
                callback.onResponse("Failed to connect.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onResponse("Error,"+response.code());
                    return;
                }

                String resp = response.body().string();

                JsonObject result = gson.fromJson(resp, JsonObject.class);
                String reply = result
                        .getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();

                callback.onResponse(reply);
            }
        });
    }
}
