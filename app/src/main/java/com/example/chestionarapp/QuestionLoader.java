package com.example.chestionarapp;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QuestionLoader {
    public static List<Question> loadQuestions(Context context, String filename) {
        List<Question> list = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Question>>(){}.getType();
            list = gson.fromJson(json, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
