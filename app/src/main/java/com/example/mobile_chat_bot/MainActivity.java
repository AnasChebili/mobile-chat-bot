package com.example.mobile_chat_bot;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MistralTest";
    private static final String API_KEY = "kVvcTttXC8hLXkYY3ijrBsaE0U5RX2gY";
    private static final String API_URL = "https://api.mistral.ai/v1/chat/completions";
    private static final String MODEL = "mistral-tiny"; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Create JSON request body
                JSONObject body = new JSONObject();
                body.put("model", MODEL);

                JSONArray messages = new JSONArray();
                JSONObject userMessage = new JSONObject();
                userMessage.put("role", "user");
                userMessage.put("content", "What is the best French cheese?");
                messages.put(userMessage);

                body.put("messages", messages);

                // Send the request
                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(body.toString().getBytes());
                os.flush();
                os.close();

                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // Log the raw response
                Log.d(TAG, "Response: " + response.toString());

                // Extract and log the message content
                JSONObject jsonResponse = new JSONObject(response.toString());
                String content = jsonResponse
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");

                Log.i(TAG, "AI Response: " + content);

            } catch (Exception e) {
                Log.e(TAG, "Error: ", e);
            }
        }).start();

    }
}